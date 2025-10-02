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
import org.testng.annotations.*;
import config.TNCStoreConfig;
import org.openqa.selenium.TimeoutException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
                driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(45));

                log.info("🌐 Navigating to base URL: {}", TNCStoreConfig.BASE_URL);
                driver.get(TNCStoreConfig.BASE_URL);

                try {
                    driver.manage().window().maximize();
                } catch (Exception e) {
                    log.warn("⚠️ Could not maximize window: {}", e.getMessage());
                }

                log.info("📍 Current URL after init: {}", safeGetCurrentUrl());
                log.info("📝 Page title: {}", safeGetTitle());

                return; // success
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

    @BeforeMethod(alwaysRun = true)
    @Parameters({"browser"})
    public void setUp(@Optional("chrome") String browser) {
        initializeDriver(browser);
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        cleanupDriverQuietly();
        driver = null;
        driverInitializationAttempted = false;
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
        try { Thread.sleep(millis); } catch (InterruptedException ie) { Thread.currentThread().interrupt(); }
    }

    private String safeGetCurrentUrl() {
        try { return driver != null ? driver.getCurrentUrl() : "(no driver)"; } catch (Exception e) { return "(unavailable)"; }
    }

    private String safeGetTitle() {
        try { return driver != null ? driver.getTitle() : "(no driver)"; } catch (Exception e) { return "(unavailable)"; }
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
    protected String getCurrentUrl() { return safeGetCurrentUrl(); }
    protected String getPageTitle() { return safeGetTitle(); }
    protected void refreshPage() { if (driver != null) driver.navigate().refresh(); }
}
