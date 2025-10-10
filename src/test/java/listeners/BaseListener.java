package listeners;

import core.BaseTest;
import io.qameta.allure.Allure;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reports.TestUtilities;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class BaseListener implements ITestListener {
    private static final Logger logger = LoggerFactory.getLogger(BaseListener.class);

    @Override
    public void onStart(ITestContext context) {
        logger.info("========================================");
        logger.info("Starting Test Suite: {}", context.getCurrentXmlTest().getName());
        logger.info("========================================");
        // Guard Allure calls: Allure lifecycle may not be initialized yet in some listener orderings
        try {
            Allure.suite(context.getCurrentXmlTest().getName());
        } catch (Exception e) {
            logger.warn("Allure not ready to accept suite name (listener ordering). Skipping Allure.suite call.", e);
        }
    }

    @Override
    public void onTestStart(ITestResult result) {
        logger.info("▶ Starting Test: {}", result.getMethod().getMethodName());
        String description = result.getMethod().getDescription();
        if (description != null && !description.isEmpty()) {
            try {
                Allure.description(description);
                logger.info("  Description: {}", description);
            } catch (Exception e) {
                logger.warn("Allure not ready to accept test description. Skipping Allure.description call.", e);
            }
        }
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        logger.info("✅ Test PASSED: {}", result.getMethod().getMethodName());
        long duration = result.getEndMillis() - result.getStartMillis();
        logger.info("  Duration: {} ms", duration);
    }

    @Override
    public void onTestFailure(ITestResult result) {
        logger.error("❌ Test FAILED: {}", result.getMethod().getMethodName());
        logger.error("  Reason: {}", result.getThrowable().getMessage());

        // Capture screenshot and attach to Allure (TestUtilities may call Allure API internally)
        try {
            TestUtilities.captureScreenshotOnFailure(result, result.getMethod().getMethodName());
        } catch (Exception e) {
            logger.warn("Failed to capture screenshot/attach via TestUtilities", e);
        }

        // Attach failure details to Allure - guard these calls
        try {
            Allure.addAttachment("Failure Reason", result.getThrowable().getMessage());
            Allure.addAttachment("Stack Trace", getStackTrace(result.getThrowable()));
        } catch (Exception e) {
            logger.warn("Allure not ready to accept attachments. Skipping attachments.", e);
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        logger.warn("⏭ Test SKIPPED: {}", result.getMethod().getMethodName());
        if (result.getThrowable() != null) {
            logger.warn("  Reason: {}", result.getThrowable().getMessage());
            try {
                Allure.addAttachment("Skip Reason", result.getThrowable().getMessage());
            } catch (Exception e) {
                logger.warn("Allure not ready to accept skip attachments. Skipping.", e);
            }
        }
    }

    @Override
    public void onFinish(ITestContext context) {
        logger.info("========================================");
        logger.info("Test Suite Finished: {}", context.getCurrentXmlTest().getName());
        logger.info("  Total Tests: {}", context.getAllTestMethods().length);
        logger.info("  Passed: {}", context.getPassedTests().size());
        logger.info("  Failed: {}", context.getFailedTests().size());
        logger.info("  Skipped: {}", context.getSkippedTests().size());
        logger.info("========================================");
    }

    private String getStackTrace(Throwable throwable) {
        if (throwable == null) return "";
        StringBuilder sb = new StringBuilder();
        sb.append(throwable.toString()).append("\n");
        for (StackTraceElement element : throwable.getStackTrace()) {
            sb.append("\tat ").append(element.toString()).append("\n");
        }
        return sb.toString();
    }
}
