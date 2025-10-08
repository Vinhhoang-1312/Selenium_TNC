package core;

import io.qameta.allure.Allure;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Optional;
import helpers.PopupHandler;
import reports.TestUtilities;
import utils.AllureSoftAssert;
import utils.DriverHelper;

import static utils.ConfigReader.getProperty;

public abstract class BaseTest {

    protected final String baseUrl = getProperty("domain.url");
    public static final Logger logger = LoggerFactory.getLogger(BaseTest.class);

    @BeforeMethod(alwaysRun = true)
    @Parameters(value = "browser")
    public void setUp(@Optional("chrome") String browser) {
        DriverHelper.setDriverThreadLocal(DriverFactory.initDriver(browser));
        navigateToBaseUrl();
        new PopupHandler(getDriver()).dismissAllPopups();
        DriverHelper.setSoftAssertThreadLocal(new AllureSoftAssert());
    }

    public WebDriver getDriver() {
        return DriverHelper.getDriver();
    }

    public AllureSoftAssert getSoftAssert() {
        return DriverHelper.getSoftAssert();
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown(ITestResult result) {
        try {
            if (getSoftAssert() != null) {
                getSoftAssert().assertAll();
            }
        } catch (AssertionError e) {
            logger.error("Soft assertion failures detected: {}", e.getMessage());
            if (result.getStatus() == ITestResult.SUCCESS) {
                result.setStatus(ITestResult.FAILURE);
                result.setThrowable(e);
            }
        } finally {
           if (result.getStatus() == ITestResult.FAILURE) {
                TestUtilities.captureScreenshotOnFailure(result, result.getMethod().getMethodName());
            }

            try {
                if (getDriver() != null) {
                    Allure.step("Closing browser");
                    getDriver().quit();
                }
            } finally {
                // Clean up thread-local variables
                DriverHelper.quitDriverThreadLocal();
                DriverHelper.quitSoftAssertThreadLocal();
            }
        }
    }

    private void navigateToBaseUrl() {
        if (baseUrl == null || baseUrl.isBlank()) {
            throw new IllegalStateException("Base URL must be provided via application.properties");
        }
        String targetUrl = baseUrl.endsWith("/") ? baseUrl : baseUrl + "/";

        Allure.step("Navigating to " + targetUrl);
        getDriver().get(targetUrl);
    }

    /**
     * DEPRECATED: Use explicit waits in page objects instead of Thread.sleep
     * This method is kept for backward compatibility only
     */
    @Deprecated
    protected void customWait(int milliseconds) {
        logger.warn("customWait() is deprecated - use explicit wait methods in page objects instead");
    }
}
