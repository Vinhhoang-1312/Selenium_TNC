package base;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.io.FileHandler;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import utils.ConfigReader;
import utils.ExtentManager;
import utils.ScreenshotUtils;
import utils.WaitUtils;

import java.io.File;
import java.time.Duration;

public class BaseTest {
    protected static ExtentReports extent;
    protected static ExtentTest test;
    protected WebDriver driver;

    @BeforeSuite
    public void setupReport() {
        ExtentSparkReporter spark = new ExtentSparkReporter("target/ExtentReport.html");
        extent = new ExtentReports();
        extent.attachReporter(spark);
    }

    @AfterSuite
    public void tearDownReport() {
        if (extent != null) {
            extent.flush();
        }
        if (driver != null) {
            driver.quit();
        }
    }

    @BeforeMethod
    @Parameters("browser")
    public void setUp(@Optional("chrome") String browser) {
        // Initialize driver
        DriverFactory.initializeDriver(browser);
        driver = DriverFactory.getDriver();

        // Set timeouts
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(
            Integer.parseInt(ConfigReader.getProperty("implicit.wait", "10"))));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(
            Integer.parseInt(ConfigReader.getProperty("page.load.timeout", "30"))));

        // Navigate to base URL
        driver.get(ConfigReader.getProperty("base.url"));

        // Maximize window
        driver.manage().window().maximize();
    }

    @AfterMethod
    public void tearDown() {
        DriverFactory.closeDriver();
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
