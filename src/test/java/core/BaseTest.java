package core;

import io.qameta.allure.Allure;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Optional;
import org.testng.asserts.SoftAssert;
import helpers.PopupHandler;
import utils.DriverHelper;
import helpers.WaitUtils;

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
        DriverHelper.setSoftAssertThreadLocal(new SoftAssert());
    }

    public WebDriver getDriver() {
        return DriverHelper.getDriver();
    }

    public SoftAssert getSoftAssert() {
        return DriverHelper.getSoftAssert();
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        try {
            if (getDriver() != null) {
                getDriver().quit();
            }
            Allure.step("Tearing down");
            if (getSoftAssert() != null) {
                getSoftAssert().assertAll();
            }
        } finally {
            DriverHelper.quitDriverThreadLocal();
            DriverHelper.quitSoftAssertThreadLocal();
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
     * Utility method to pause execution for specified milliseconds
     *
     * @param milliseconds time to wait in milliseconds
     */
    protected void customWait(int milliseconds) {
        WaitUtils.sleep(milliseconds);
    }
}
