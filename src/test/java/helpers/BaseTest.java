package helpers;

import com.aventstack.extentreports.ExtentTest;
import commons.DriverFactory;
import helpers.*;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.*;
import config.TNCStoreConfig;

import java.time.Duration;

public class BaseTest {
    protected WebDriver driver;
    protected ExtentTest test;

    @BeforeSuite
    public void setupSuite() {
        ExtentManager.initReports();
    }

    @AfterSuite
    public void tearDownSuite() {
        ExtentManager.flush();
    }

    @BeforeMethod
    public void setUp() {
        // Initialize driver without parameters dependency
        String browser = ConfigReader.getProperty("browser", "chrome");
        DriverFactory.initializeDriver(browser);
        driver = DriverFactory.getDriver();

        // Set timeouts using config
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(TNCStoreConfig.IMPLICIT_WAIT));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(TNCStoreConfig.PAGE_LOAD_TIMEOUT));

        // Navigate to base URL
        driver.get(TNCStoreConfig.BASE_URL);

        // Maximize window if configured
        if (TNCStoreConfig.MAXIMIZE_WINDOW) {
            driver.manage().window().maximize();
        }

        System.out.println("✅ WebDriver initialized successfully");
    }

    @AfterMethod
    public void tearDown() {
        DriverFactory.quitDriver();
    }

    // Screenshot utility method
    protected void takeScreenshot(String testName) {
        ScreenshotUtils.captureScreenshot(driver, testName);
    }

    // Wait utility methods
    protected void waitForPageLoad() {
        WaitUtils.waitForPageLoad(driver);
    }

    protected void sleep(int seconds) {
        try {
            Thread.sleep(seconds * 1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    // Browser utility methods
    protected void refreshPage() {
        driver.navigate().refresh();
    }

    protected void navigateBack() {
        driver.navigate().back();
    }

    protected void navigateForward() {
        driver.navigate().forward();
    }

    protected String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    protected String getPageTitle() {
        return driver.getTitle();
    }

    // Test data and reporting helpers
    protected void logInfo(String message) {
        if (test != null) {
            test.info(message);
        }
    }

    protected void logPass(String message) {
        if (test != null) {
            test.pass(message);
        }
    }

    protected void logFail(String message) {
        if (test != null) {
            test.fail(message);
        }
    }

    protected void logWarning(String message) {
        if (test != null) {
            test.warning(message);
        }
    }
}
