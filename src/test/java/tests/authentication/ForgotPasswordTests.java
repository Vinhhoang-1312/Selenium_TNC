package tests.authentication;

import base.BaseTest;
import pages.AuthenticationPage;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import utils.ReportManager;
import data.AuthenticationTestData;

public class ForgotPasswordTests extends BaseTest {
    private AuthenticationPage authPage;

    @BeforeMethod
    public void setUpTest() {
        super.setUp("chrome");
        authPage = new AuthenticationPage(driver);
        ReportManager.setModule("authentication-forgot-password");
    }

    @Test(groups = {"authentication", "regression", "forgot-password"},
          description = "AUTH-FP-01: Reset password with valid registered email")
    public void testForgotPasswordWithValidEmail() {
        test = ReportManager.startTest("AUTH-FP-01: Reset password with valid email");

        try {
            authPage.goToLoginPage();
            ReportManager.logInfo("Navigated to login page");

            authPage.performForgotPassword(AuthenticationTestData.VALID_EMAIL);
            ReportManager.logInfo("Submitted forgot password request");

            ReportManager.logPass("Forgot password request processed");

        } catch (Exception e) {
            ReportManager.logFail("Test failed: " + e.getMessage());
            takeScreenshot("AUTH-FP-01_Failed");
            throw e;
        }
    }

    @Test(groups = {"authentication", "regression", "forgot-password"},
          description = "AUTH-FP-02: Reset password with non-existing email")
    public void testForgotPasswordWithNonExistingEmail() {
        test = ReportManager.startTest("AUTH-FP-02: Reset password with non-existing email");

        try {
            authPage.goToLoginPage();
            ReportManager.logInfo("Navigated to login page");

            authPage.performForgotPassword(AuthenticationTestData.FORGOT_NON_EXISTING_EMAIL_1);
            ReportManager.logInfo("Submitted forgot password with non-existing email");

            Assert.assertTrue(authPage.isErrorMessageDisplayed(), "Error message should be displayed");
            ReportManager.logPass("Non-existing email error displayed correctly");

        } catch (Exception e) {
            ReportManager.logFail("Test failed: " + e.getMessage());
            takeScreenshot("AUTH-FP-02_Failed");
            throw e;
        }
    }

    @Test(groups = {"authentication", "regression", "forgot-password"},
          description = "AUTH-FP-03: Reset password with invalid email format")
    public void testForgotPasswordWithInvalidEmailFormat() {
        test = ReportManager.startTest("AUTH-FP-03: Reset password with invalid email format");

        try {
            authPage.goToLoginPage();
            ReportManager.logInfo("Navigated to login page");

            authPage.performForgotPassword(AuthenticationTestData.FORGOT_INVALID_EMAIL_1);
            ReportManager.logInfo("Submitted forgot password with invalid email");

            Assert.assertTrue(authPage.isErrorMessageDisplayed(), "Validation error should be displayed");
            ReportManager.logPass("Invalid email format error displayed correctly");

        } catch (Exception e) {
            ReportManager.logFail("Test failed: " + e.getMessage());
            takeScreenshot("AUTH-FP-03_Failed");
            throw e;
        }
    }
}
