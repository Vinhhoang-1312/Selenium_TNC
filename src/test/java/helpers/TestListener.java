package helpers;

import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

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

        // Capture screenshot on failure
        try {
            // Get driver from test result (assuming driver is accessible)
            Object testInstance = result.getInstance();
            WebDriver driver = getDriverFromTestInstance(testInstance);

            if (driver != null) {
                String screenshotPath = captureScreenshot(driver, testName + "_FAILED");
                if (screenshotPath != null) {
                    ReportManager.logFail("Test failed: " + testName + "<br>Error: " + errorMessage);
                    // Add screenshot to report if possible
                } else {
                    ReportManager.logFail("Test failed: " + testName + "<br>Error: " + errorMessage);
                }
            } else {
                ReportManager.logFail("Test failed: " + testName + "<br>Error: " + errorMessage);
            }
        } catch (Exception e) {
            ReportManager.logFail("Test failed: " + testName + "<br>Error: " + errorMessage);
        }

        ReportManager.logTestResult(testName, "FAIL", "N/A", errorMessage);
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        String skipReason = result.getThrowable() != null ? result.getThrowable().getMessage() : "Test skipped";

        ReportManager.logInfo("Test skipped: " + testName + " - Reason: " + skipReason);
        ReportManager.logTestResult(testName, "SKIP", "N/A", skipReason);
    }

    @Override
    public void onFinish(ITestContext context) {
        // Flush reports when test suite finishes
        ReportManager.flushReports();
        System.out.println("📊 Test execution completed. Reports generated.");
    }

    // Helper method to get driver from test instance
    private WebDriver getDriverFromTestInstance(Object testInstance) {
        try {
            // Try to get driver field using reflection
            java.lang.reflect.Field driverField = testInstance.getClass().getDeclaredField("driver");
            driverField.setAccessible(true);
            return (WebDriver) driverField.get(testInstance);
        } catch (Exception e) {
            // Try parent class
            try {
                java.lang.reflect.Field driverField = testInstance.getClass().getSuperclass().getDeclaredField("driver");
                driverField.setAccessible(true);
                return (WebDriver) driverField.get(testInstance);
            } catch (Exception ex) {
                System.out.println("⚠️ Could not access driver field for screenshot");
                return null;
            }
        }
    }

    // Helper method to capture screenshot
    private String captureScreenshot(WebDriver driver, String testName) {
        try {
            TakesScreenshot screenshot = (TakesScreenshot) driver;
            File sourceFile = screenshot.getScreenshotAs(OutputType.FILE);

            String timestamp = java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            String fileName = testName + "_" + timestamp + ".png";
            String filePath = "target/screenshots/" + fileName;

            File destFile = new File(filePath);
            destFile.getParentFile().mkdirs(); // Create directory if not exists
            Files.copy(sourceFile.toPath(), destFile.toPath());

            System.out.println("📸 Screenshot captured: " + filePath);
            return filePath;

        } catch (Exception e) {
            System.err.println("❌ Failed to capture screenshot: " + e.getMessage());
            return null;
        }
    }
}
