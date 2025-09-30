package test.simple;

import commons.DriverFactory;
import config.TNCStoreConfig;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class SimpleDriverTest {
    private WebDriver driver;

    @BeforeMethod
    public void setUp() {
        try {
            System.out.println("=== SIMPLE TEST SETUP START ===");

            DriverFactory.initializeDriver("chrome");
            driver = DriverFactory.getDriver();

            if (driver == null) {
                throw new RuntimeException("Driver is null after DriverFactory.getDriver()");
            }

            System.out.println("✅ Driver initialized: " + driver.getClass().getSimpleName());

            driver.get(TNCStoreConfig.BASE_URL);
            System.out.println("✅ Navigated to: " + TNCStoreConfig.BASE_URL);
            System.out.println("✅ Current URL: " + driver.getCurrentUrl());

            System.out.println("=== SIMPLE TEST SETUP COMPLETED ===");

        } catch (Exception e) {
            System.err.println("❌ Setup failed: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    @Test
    public void testDriverWorks() {
        try {
            System.out.println("=== SIMPLE TEST EXECUTION ===");

            if (driver == null) {
                throw new RuntimeException("Driver is null in test method");
            }

            String title = driver.getTitle();
            System.out.println("✅ Page title: " + title);

            String url = driver.getCurrentUrl();
            System.out.println("✅ Current URL: " + url);

            System.out.println("=== SIMPLE TEST PASSED ===");

        } catch (Exception e) {
            System.err.println("❌ Test failed: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    @AfterMethod
    public void tearDown() {
        try {
            if (driver != null) {
                driver.quit();
                System.out.println("✅ Driver quit successfully");
            }
        } catch (Exception e) {
            System.err.println("⚠️ Error during cleanup: " + e.getMessage());
        }
    }
}
