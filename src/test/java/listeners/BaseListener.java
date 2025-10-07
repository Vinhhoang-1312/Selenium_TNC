package listeners;

import core.BaseTest;
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
        logger.info("Starting test {}", context.getCurrentXmlTest().getName());
    }

    @Override
    public void onTestStart(ITestResult result) {
        logger.info("onTestStart - {}", result.getMethod().getMethodName());
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        logger.info("onTestSuccess - {}", result.getMethod().getMethodName());
    }

    @Override
    public void onTestFailure(ITestResult result) {
        logger.error("onTestFailure - {}", result.getMethod().getMethodName());
        TestUtilities.captureScreenshotOnFailure(result, result.getMethod().getMethodName());
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        logger.warn("onTestSkipped - {}", result.getMethod().getMethodName());
    }

    @Override
    public void onFinish(ITestContext context) {
        logger.info("Finish test {}", context.getCurrentXmlTest().getName());
    }
}

