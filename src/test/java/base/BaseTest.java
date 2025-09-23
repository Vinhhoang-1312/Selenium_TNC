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

import java.io.File;

public class BaseTest {
    protected static ExtentReports extent;
    protected static ExtentTest test;
    protected static WebDriver driver; // thêm driver để dùng chung

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
    public void setupDriver() {
        driver = DriverFactory.getDriver();
    }

    @AfterMethod
    public void tearDownDriver() {
        DriverFactory.quitDriver();
    }

    protected void takeScreenshot(String fileName) {
        try {
            File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            String destPath = "target/screenshots/" + fileName + ".png"; // nên để trong target
            File destFile = new File(destPath);
            destFile.getParentFile().mkdirs(); // tạo folder nếu chưa có
            FileHandler.copy(screenshot, destFile);
            test.addScreenCaptureFromPath(destPath);
        } catch (Exception e) {
            test.warning("Không thể chụp screenshot: " + e.getMessage());
        }
    }
}
