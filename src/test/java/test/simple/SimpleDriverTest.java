package test.simple;

import commons.DriverFactory;
import config.TNCStoreConfig;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SimpleDriverTest {
    private static final Logger log = LoggerFactory.getLogger(SimpleDriverTest.class);
    private WebDriver driver;

    @BeforeMethod
    public void setUp() {
        try {
            log.info("=== SIMPLE TEST SETUP START ===");

            DriverFactory.initializeDriver("chrome");
            driver = DriverFactory.getDriver();

            if (driver == null) {
                throw new RuntimeException("Driver is null after DriverFactory.getDriver()");
            }

            log.info("Driver initialized: {}", driver.getClass().getSimpleName());

            driver.get(TNCStoreConfig.BASE_URL);
            log.info("Navigated to: {}", TNCStoreConfig.BASE_URL);
            log.info("Current URL: {}", driver.getCurrentUrl());

            log.info("=== SIMPLE TEST SETUP COMPLETED ===");

        } catch (Exception e) {
            log.error("Setup failed: {}", e.getMessage(), e);
            throw e;
        }
    }

    @Test
    public void testDriverWorks() {
        try {
            log.info("=== SIMPLE TEST EXECUTION ===");

            if (driver == null) {
                throw new RuntimeException("Driver is null in test method");
            }

            String title = driver.getTitle();
            log.info("Page title: {}", title);

            String url = driver.getCurrentUrl();
            log.info("Current URL: {}", url);

            log.info("=== SIMPLE TEST PASSED ===");

        } catch (Exception e) {
            log.error("Test failed: {}", e.getMessage(), e);
            throw e;
        }
    }

    @AfterMethod
    public void tearDown() {
        try {
            if (driver != null) {
                driver.quit();
                log.info("Driver quit successfully");
            }
        } catch (Exception e) {
            log.warn("Error during cleanup: {}", e.getMessage());
        }
    }
}
