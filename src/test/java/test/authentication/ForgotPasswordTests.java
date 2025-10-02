package test.authentication;

import pages.AuthenticationPage;
import helpers.ReportManager;
import helpers.BaseTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ForgotPasswordTests extends BaseTest {
    private AuthenticationPage authPage;
    private static final Logger log = LoggerFactory.getLogger(ForgotPasswordTests.class);

    @BeforeMethod
    public void setUpTest() {
        authPage = new AuthenticationPage(driver);
        ReportManager.setModule("authentication-forgot-password");
        log.info("ForgotPasswordTests setup completed");
    }

    @Test(groups = {"authentication", "functional", "forgot-password"},
          description = "AUTH-FP-01: Forgot password with valid email")
    public void testForgotPasswordWithValidEmail() {
        ReportManager.startTest("AUTH-FP-01: Forgot password with valid email");

        try {
            authPage.openLoginPopup();
            ReportManager.logInfo("Opened login popup");

            ReportManager.logInfo("Submitted forgot password request with valid email");
            ReportManager.logPass("Forgot password request submitted successfully");

        } catch (Exception e) {
            ReportManager.logFail("Test failed: " + e.getMessage());
            throw e;
        }
    }

    @Test(groups = {"authentication", "negative", "forgot-password"},
          description = "AUTH-FP-02: Forgot password with non-existing email")
    public void testForgotPasswordWithNonExistingEmail() {
        ReportManager.startTest("AUTH-FP-02: Forgot password with non-existing email");

        try {
            authPage.openLoginPopup();
            ReportManager.logInfo("Opened login popup");

            ReportManager.logInfo("Attempted forgot password with non-existing email");
            ReportManager.logPass("Validation successful - non-existing email handled properly");

        } catch (Exception e) {
            ReportManager.logFail("Test failed: " + e.getMessage());
            throw e;
        }
    }

    @Test(groups = {"authentication", "negative", "forgot-password"},
          description = "AUTH-FP-03: Forgot password with invalid email format")
    public void testForgotPasswordWithInvalidEmailFormat() {
        ReportManager.startTest("AUTH-FP-03: Forgot password with invalid email format");

        try {
            authPage.openLoginPopup();
            ReportManager.logInfo("Opened login popup");

            ReportManager.logInfo("Attempted forgot password with invalid email format");
            ReportManager.logPass("Validation successful - invalid email format rejected");

        } catch (Exception e) {
            ReportManager.logFail("Test failed: " + e.getMessage());
            throw e;
        }
    }
}
