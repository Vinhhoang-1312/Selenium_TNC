package helpers;

import config.TNCStoreConfig;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

public class BaseTest {
    protected WebDriver driver;
    private static final Logger log = LoggerFactory.getLogger(BaseTest.class);

    @BeforeMethod
    public void setUp() {
        try {
            log.info("🔄 Starting simplified BaseTest setup...");

            // Directly initialize Chrome driver without DriverFactory
            log.info("📦 Setting up ChromeDriver...");
            WebDriverManager.chromedriver().setup();

            ChromeOptions options = new ChromeOptions();
            options.addArguments("--no-sandbox");
            options.addArguments("--disable-dev-shm-usage");
            options.addArguments("--remote-allow-origins=*");
            options.addArguments("--disable-extensions");

            log.info("🚀 Creating ChromeDriver instance...");
            driver = new ChromeDriver(options);

            if (driver == null) {
                throw new RuntimeException("ChromeDriver creation failed - driver is null");
            }

            log.info("✅ Driver created successfully: {}", driver.getClass().getSimpleName());

            // Set basic timeouts
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
            driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));

            // Navigate to website
            log.info("🌐 Navigating to: {}", TNCStoreConfig.BASE_URL);
            driver.get(TNCStoreConfig.BASE_URL);

            // Maximize window
            try {
                driver.manage().window().maximize();
                log.info("✅ Window maximized");
            } catch (Exception e) {
                log.warn("⚠️ Could not maximize window: {}", e.getMessage());
            }

            // Wait for page load
            Thread.sleep(2000);

            log.info("✅ Setup completed successfully");
            log.info("📍 Current URL: {}", driver.getCurrentUrl());
            log.info("📝 Page title: {}", driver.getTitle());

        } catch (Exception e) {
            log.error("❌ BaseTest setup failed: {}", e.getMessage(), e);

            // Cleanup on failure
            if (driver != null) {
                try {
                    driver.quit();
                } catch (Exception cleanupError) {
                    log.error("Cleanup failed: {}", cleanupError.getMessage());
                }
            }

            throw new RuntimeException("BaseTest setup failed: " + e.getMessage(), e);
        }
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        if (driver != null) {
            try {
                log.info("🧹 Cleaning up WebDriver...");
                driver.quit();
                log.info("✅ Driver cleanup completed");
            } catch (Exception e) {
                log.warn("⚠️ Error during cleanup: {}", e.getMessage());
            }
        }
    }

    // ADDED BACK: Utility methods that tests may need
    protected String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    protected String getPageTitle() {
        return driver.getTitle();
    }

    protected void refreshPage() {
        driver.navigate().refresh();
    }

    protected void sleep(int seconds) {
        try {
            Thread.sleep(seconds * 1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    protected void takeScreenshot(String testName) {
        // Simple implementation - can be enhanced later
        try {
            log.info("Taking screenshot for: {}", testName);
        } catch (Exception e) {
            log.warn("Could not take screenshot: {}", e.getMessage());
        }
    }
}
