package helpers;

import com.aventstack.extentreports.ExtentTest;
import commons.DriverFactory;
import helpers.*;
import org.openqa.selenium.By;
import config.TNCStoreConfig;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.JavascriptExecutor;
import org.testng.annotations.*;
import config.TNCStoreConfig;
import org.openqa.selenium.TimeoutException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestResult;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.List;

public class BaseTest {
    protected WebDriver driver;
    private static final Logger log = LoggerFactory.getLogger(BaseTest.class);
    private boolean driverInitializationAttempted = false;
    private static final int MAX_INIT_RETRIES = 2;

    /**
     * Decide if headless mode should be enabled based on system properties or environment variables.
     */
    private boolean isHeadlessRequested() {
        String sysProp = System.getProperty("headless", "false");
        String envVar = System.getenv("HEADLESS");
        if ("true".equalsIgnoreCase(sysProp) || "1".equals(envVar) || "true".equalsIgnoreCase(envVar)) {
            return true;
        }
        return false;
    }

    private ChromeOptions buildChromeOptions() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--disable-extensions");
        options.addArguments("--disable-gpu");
        options.addArguments("--disable-popup-blocking");
        options.addArguments("--disable-notifications");
        options.addArguments("--start-maximized");
        options.addArguments("--disable-web-security");
        options.addArguments("--disable-features=VizDisplayCompositor");
        options.addArguments("--disable-background-timer-throttling");
        options.addArguments("--disable-backgrounding-occluded-windows");
        options.addArguments("--disable-renderer-backgrounding");
        options.addArguments("--disable-field-trial-config");
        options.addArguments("--disable-ipc-flooding-protection");
        options.setPageLoadStrategy(PageLoadStrategy.EAGER);

        if (isHeadlessRequested()) {
            options.addArguments("--headless=new");
            options.addArguments("--window-size=1920,1080");
            log.info("▶ Headless mode enabled");
        }
        return options;
    }

    private EdgeOptions buildEdgeOptions() {
        EdgeOptions options = new EdgeOptions();
        if (isHeadlessRequested()) {
            options.addArguments("--headless=new");
            options.addArguments("--window-size=1920,1080");
            log.info("▶ Headless mode enabled (Edge)");
        } else {
            options.addArguments("--start-maximized");
        }
        options.addArguments("--disable-gpu");
        options.addArguments("--disable-popup-blocking");
        options.addArguments("--disable-notifications");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        return options;
    }

    /**
     * Core driver initialization with retry and detailed diagnostics.
     */
    protected synchronized void initializeDriver(String browserName) {
        if (driver != null) {
            return; // Already initialized
        }
        if (driverInitializationAttempted) {
            log.warn("Driver initialization was already attempted previously; retrying may indicate prior failure.");
        }

        driverInitializationAttempted = true;
        RuntimeException lastError = null;

        for (int attempt = 1; attempt <= MAX_INIT_RETRIES; attempt++) {
            log.info("🔄 Initializing WebDriver (attempt {}/{}), browser={}...", attempt, MAX_INIT_RETRIES, browserName);
            try {
                String normalized = browserName == null ? "chrome" : browserName.trim().toLowerCase();
                switch (normalized) {
                    case "edge":
                        WebDriverManager.edgedriver().setup();
                        driver = new EdgeDriver(buildEdgeOptions());
                        break;
                    case "chrome":
                    default:
                        WebDriverManager.chromedriver().setup();
                        driver = new ChromeDriver(buildChromeOptions());
                        break;
                }

                if (driver == null) {
                    throw new RuntimeException("Driver instance was not created (returned null)");
                }

                log.info("✅ Driver created: {}", driver.getClass().getSimpleName());

                driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
                driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(60));

                log.info("🌐 Navigating to base URL: {}", TNCStoreConfig.BASE_URL);

                // Enhanced website loading with fallback strategies
                boolean pageLoaded = false;
                try {
                    driver.get(TNCStoreConfig.BASE_URL);
                    pageLoaded = true;
                } catch (TimeoutException e) {
                    log.warn("⚠️ Page load timeout, trying JavaScript navigation...");
                    try {
                        ((JavascriptExecutor) driver).executeScript("window.stop();");
                        Thread.sleep(2000);
                        String currentUrl = driver.getCurrentUrl();
                        if (currentUrl.contains("tncstore.vn")) {
                            log.info("✅ Page partially loaded via timeout recovery");
                            pageLoaded = true;
                        } else {
                            log.warn("🔄 Retrying with direct navigation...");
                            ((JavascriptExecutor) driver).executeScript("window.location.href = arguments[0];", TNCStoreConfig.BASE_URL);
                            Thread.sleep(5000);
                            pageLoaded = true;
                        }
                    } catch (Exception jsError) {
                        log.error("❌ JavaScript navigation failed: {}", jsError.getMessage());
                        throw new RuntimeException("Could not load website even with fallback methods", e);
                    }
                }

                if (!pageLoaded) {
                    throw new RuntimeException("Website failed to load after all attempts");
                }

                try {
                    driver.manage().window().maximize();
                    // Remove browser zoom out (do not set zoom to 50%)
                } catch (Exception e) {
                    log.warn("Could not maximize browser window: {}", e.getMessage());
                }

                log.info("📍 Current URL after init: {}", safeGetCurrentUrl());
                log.info("📝 Page title: {}", safeGetTitle());

                return;
            } catch (Exception e) {
                lastError = new RuntimeException("WebDriver init failure on attempt " + attempt + ": " + e.getMessage(), e);
                log.error("❌ Driver initialization failed on attempt {}: {}", attempt, e.getMessage(), e);
                cleanupDriverQuietly();
                driver = null;
                pause(1500);
            }
        }

        // Exhausted retries
        if (lastError != null) {
            throw lastError;
        } else {
            throw new RuntimeException("Driver initialization failed for unknown reasons (no exception captured)");
        }
    }

    /**
     * Public fallback for tests to explicitly trigger driver setup if @BeforeMethod was skipped.
     */
    public void lazyInitDriver() {
        if (driver == null) {
            log.warn("🚧 Driver null detected in test context — attempting lazy initialization (default: chrome)...");
            try {
                initializeDriver("chrome");
            } catch (RuntimeException e) {
                log.error("Lazy initialization failed: {}", e.getMessage(), e);
                throw e;
            }
        }
    }

    public BaseTest() {
        log.info("[DEBUG] BaseTest constructor called");
        System.out.println("[DEBUG] BaseTest constructor called");
    }

    @BeforeMethod(alwaysRun = true)
    @Parameters({"browser"})
    public void setUp(@Optional("chrome") String browser) {
        log.info("[DEBUG] BaseTest setUp called");
        System.out.println("[DEBUG] BaseTest setUp called");
        initializeDriver(browser);
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        cleanupDriverQuietly();
        driver = null;
        driverInitializationAttempted = false;
    }

    @AfterMethod(alwaysRun = true)
    public void captureScreenshotOnFailure(ITestResult result) {
        if (result.getStatus() == ITestResult.FAILURE && driver != null) {
            try {
                File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
                String screenshotDir = "report/screenshots/";
                Files.createDirectories(Paths.get(screenshotDir));
                String filename = screenshotDir + result.getName() + "_fail_" + System.currentTimeMillis() + ".png";
                Files.copy(src.toPath(), Paths.get(filename));
                System.out.println("Screenshot saved: " + filename);
            } catch (Exception e) {
                System.err.println("Failed to capture screenshot: " + e.getMessage());
            }
        }
    }

    private void cleanupDriverQuietly() {
        if (driver != null) {
            try {
                log.info("🧹 Quitting WebDriver...");
                driver.quit();
                log.info("✅ WebDriver quit successfully");
            } catch (Exception e) {
                log.warn("⚠️ Error during driver quit: {}", e.getMessage());
            }
        }
    }

    private void pause(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
        }
    }

    private String safeGetCurrentUrl() {
        try {
            return driver != null ? driver.getCurrentUrl() : "(no driver)";
        } catch (Exception e) {
            return "(unavailable)";
        }
    }

    private String safeGetTitle() {
        try {
            return driver != null ? driver.getTitle() : "(no driver)";
        } catch (Exception e) {
            return "(unavailable)";
        }
    }

    protected void sleep(int seconds) {
        try {
            Thread.sleep(seconds * 1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    // Wait utility methods
    protected void waitForPageLoad() {
        WaitUtils.waitForPageLoad(driver);
    }

    protected void takeScreenshot(String testName) {
        // Simple implementation - can be enhanced later
        try {
            log.info("Taking screenshot for: {}", testName);
        } catch (Exception e) {
            log.warn("Could not take screenshot: {}", e.getMessage());
        }
    }

    public void clickIfPresent(By locator) {
        try {
            waitForPageLoad();
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
            WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(locator));

            if (element.isDisplayed() && element.isEnabled()) {
                element.click();
                System.out.println("Clicked on element: " + locator.toString());
            }
        } catch (TimeoutException e) {
            System.out.println("Element not found within timeout, skip clicking: " + locator.toString());
        }
    }


    // Utility methods
    protected String getCurrentUrl() {
        return safeGetCurrentUrl();
    }

    protected String getPageTitle() {
        return safeGetTitle();
    }

    protected void refreshPage() {
        if (driver != null) {
            driver.navigate().refresh();
        }
    }
}
