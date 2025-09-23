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
import utils.ReportManager;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class TestListener implements ITestListener {

    @Override
    public void onStart(ITestContext context) {
        // Auto-detect module from test class name when suite starts
        String suiteName = context.getSuite().getName();
        String[] testClasses = context.getAllTestMethods()[0].getTestClass().getName().split("\\.");
        String testClassName = testClasses[testClasses.length - 1];

        // Set module based on test class name
        ReportManager.setModuleFromTestClass(testClassName);
        System.out.println("🎯 Detected module: " + testClassName + " -> Starting report generation");
    }

    @Override
    public void onTestStart(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        ReportManager.startTest(testName);
        ReportManager.logInfo("Test started: " + testName);
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        ReportManager.logPass("Test passed: " + testName);
        ReportManager.logTestResult(testName, "PASS", "N/A", null);
    }

    @Override
    public void onTestFailure(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        String errorMessage = result.getThrowable().getMessage();

        ReportManager.logFail("Test failed: " + testName);
        ReportManager.logFail("Error: " + errorMessage);
        ReportManager.logTestResult(testName, "FAIL", "N/A", errorMessage);

        // Take screenshot on failure
        WebDriver driver = DriverFactory.getDriver();
        if (driver != null) {
            try {
                String screenshotDir = "target/screenshots/";
                Files.createDirectories(Paths.get(screenshotDir));

                String screenshotPath = screenshotDir + testName + "_" + System.currentTimeMillis() + ".png";

                File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
                Files.copy(src.toPath(), Paths.get(screenshotPath));

                ReportManager.getTest().fail("Test Failed: " + result.getThrowable(),
                        MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath).build());

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        String skipReason = result.getThrowable() != null ? result.getThrowable().getMessage() : "No reason provided";

        ReportManager.logSkip("Test skipped: " + testName);
        ReportManager.logSkip("Reason: " + skipReason);
        ReportManager.logTestResult(testName, "SKIP", "N/A", skipReason);
    }

    @Override
    public void onFinish(ITestContext context) {
        ReportManager.endReporting();
        System.out.println("✅ Module report generation completed!");
    }
}
