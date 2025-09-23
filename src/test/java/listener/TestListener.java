package listener;

import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import base.DriverFactory;
import utils.ExtentManager;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class TestListener implements ITestListener {

    @Override
    public void onTestStart(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        ExtentManager.startTest(testName);
        ExtentManager.getTest().log(Status.INFO, "Test started: " + testName);
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        ExtentManager.getTest().log(Status.PASS, "Test passed: " + result.getMethod().getMethodName());
    }

    @Override
    public void onTestFailure(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        ExtentManager.getTest().log(Status.FAIL, "Test failed: " + testName);
        ExtentManager.getTest().log(Status.FAIL, "Error: " + result.getThrowable().getMessage());

        // Take screenshot on failure
        WebDriver driver = DriverFactory.getDriver();
        if (driver != null) {
            try {
                String screenshotDir = "report/screenshots/";
                Files.createDirectories(Paths.get(screenshotDir));

                String screenshotPath = screenshotDir + testName + "_" + System.currentTimeMillis() + ".png";

                File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
                Files.copy(src.toPath(), Paths.get(screenshotPath));

                ExtentManager.getTest().fail("Test Failed: " + result.getThrowable(),
                        MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath).build());

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        ExtentManager.getTest().log(Status.SKIP, "Test skipped: " + result.getMethod().getMethodName());
        ExtentManager.getTest().log(Status.SKIP, "Reason: " + result.getThrowable().getMessage());
    }

    @Override
    public void onFinish(ITestContext context) {
        ExtentManager.flush();
    }
}
