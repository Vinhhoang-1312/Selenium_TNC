package commons.listener;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.ISuite;
import org.testng.ISuiteListener;

/**
 * TestNG Listener for handling test execution events
 * This is a REUSABLE component that can be used across different projects
 * Simple implementation without dependencies on test-specific helper classes
 */
public class TestListener implements ITestListener, ISuiteListener {

    private static ExtentReports extent;
    private static ThreadLocal<ExtentTest> test = new ThreadLocal<>();

    @Override
    public void onStart(ISuite suite) {
        // Initialize ExtentReports once per suite
        if (extent == null) {
            String reportPath = "target/test-report.html";
            ExtentSparkReporter sparkReporter = new ExtentSparkReporter(reportPath);
            sparkReporter.config().setDocumentTitle("Automation Test Report");
            sparkReporter.config().setReportName("Test Execution Results");

            extent = new ExtentReports();
            extent.attachReporter(sparkReporter);
            extent.setSystemInfo("Environment", "QA");
            extent.setSystemInfo("Tester", "Automation Team");

            System.out.println("📊 ExtentReport initialized: " + reportPath);
        }
    }

    @Override
    public void onTestStart(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        String className = result.getTestClass().getName();

        ExtentTest extentTest = extent.createTest(testName);
        test.set(extentTest);

        extentTest.info("🚀 Starting test: " + testName);
        extentTest.info("Class: " + className);

        System.out.println("🚀 Starting test: " + testName);
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        String testName = result.getMethod().getMethodName();

        ExtentTest extentTest = test.get();
        if (extentTest != null) {
            extentTest.log(Status.PASS, "✅ Test passed: " + testName);
        }

        System.out.println("✅ Test passed: " + testName);
    }

    @Override
    public void onTestFailure(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        String errorMessage = result.getThrowable() != null ? result.getThrowable().getMessage() : "Unknown error";

        ExtentTest extentTest = test.get();
        if (extentTest != null) {
            extentTest.log(Status.FAIL, "❌ Test failed: " + testName);
            extentTest.log(Status.FAIL, "Error: " + errorMessage);

            // Add stack trace
            if (result.getThrowable() != null) {
                extentTest.log(Status.FAIL, result.getThrowable());
            }
        }

        System.out.println("❌ Test failed: " + testName);
        System.out.println("Error: " + errorMessage);
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        String skipReason = result.getThrowable() != null ? result.getThrowable().getMessage() : "Unknown reason";

        ExtentTest extentTest = test.get();
        if (extentTest != null) {
            extentTest.log(Status.SKIP, "⚠️ Test skipped: " + testName);
            extentTest.log(Status.SKIP, "Reason: " + skipReason);
        }

        System.out.println("⚠️ Test skipped: " + testName);
    }

    @Override
    public void onFinish(ISuite suite) {
        if (extent != null) {
            extent.flush();
            System.out.println("📊 Test report generated successfully!");
        }
    }

    // Clean up ThreadLocal to prevent memory leaks
    public static void cleanup() {
        test.remove();
    }
}
