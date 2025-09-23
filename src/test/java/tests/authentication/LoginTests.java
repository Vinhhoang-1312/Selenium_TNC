package tests.authentication;

import base.BaseTest;
import pages.AuthenticationPage;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import utils.ReportManager;
import data.AuthenticationTestData;

public class LoginTests extends BaseTest {
    private AuthenticationPage authPage;

    @BeforeMethod
    public void setUpTest() {
        super.setUp("chrome");
        authPage = new AuthenticationPage(driver);
        ReportManager.setModule("authentication-login");
    }

    @Test(groups = {"authentication", "smoke", "login"},
          description = "AUTH-SI-01: Login with valid email and password")
    public void testLoginWithValidCredentials() {
        test = ReportManager.startTest("AUTH-SI-01: Login with valid credentials");

        try {
            authPage.goToLoginPage();
            ReportManager.logInfo("Navigated to login page");

            authPage.performLogin(
                AuthenticationTestData.VALID_EMAIL,
                AuthenticationTestData.VALID_PASSWORD
            );
            ReportManager.logInfo("Entered valid login credentials");

            Assert.assertTrue(authPage.isLoginSuccessful(), "User should be logged in successfully");
            ReportManager.logPass("Login successful");

        } catch (Exception e) {
            ReportManager.logFail("Test failed: " + e.getMessage());
            takeScreenshot("AUTH-SI-01_Failed");
            throw e;
        }
    }

    @Test(groups = {"authentication", "regression", "login"},
          description = "AUTH-SI-02: Login with wrong password")
    public void testLoginWithWrongPassword() {
        test = ReportManager.startTest("AUTH-SI-02: Login with wrong password");

        try {
            authPage.goToLoginPage();
            ReportManager.logInfo("Navigated to login page");

            authPage.performLogin(
                AuthenticationTestData.VALID_EMAIL,
                AuthenticationTestData.WRONG_PASSWORD_1
            );
            ReportManager.logInfo("Entered wrong password");

            Assert.assertTrue(authPage.isErrorMessageDisplayed(), "Error message should be displayed");
            ReportManager.logPass("Wrong password error displayed correctly");

        } catch (Exception e) {
            ReportManager.logFail("Test failed: " + e.getMessage());
            takeScreenshot("AUTH-SI-02_Failed");
            throw e;
        }
    }

    @Test(groups = {"authentication", "regression", "login"},
          description = "AUTH-SI-03: Login with unregistered email")
    public void testLoginWithUnregisteredEmail() {
        test = ReportManager.startTest("AUTH-SI-03: Login with unregistered email");

        try {
            authPage.goToLoginPage();
            ReportManager.logInfo("Navigated to login page");

            authPage.performLogin(
                AuthenticationTestData.NON_EXISTING_EMAIL_1,
                AuthenticationTestData.VALID_PASSWORD
            );
            ReportManager.logInfo("Entered unregistered email");

            Assert.assertTrue(authPage.isErrorMessageDisplayed(), "Error message should be displayed");
            ReportManager.logPass("Unregistered email error displayed correctly");

        } catch (Exception e) {
            ReportManager.logFail("Test failed: " + e.getMessage());
            takeScreenshot("AUTH-SI-03_Failed");
            throw e;
        }
    }

    @Test(groups = {"authentication", "regression", "login"},
          description = "AUTH-SI-04: Login with invalid email format")
    public void testLoginWithInvalidEmailFormat() {
        test = ReportManager.startTest("AUTH-SI-04: Login with invalid email format");

        try {
            authPage.goToLoginPage();
            ReportManager.logInfo("Navigated to login page");

            authPage.performLogin(
                AuthenticationTestData.INVALID_EMAIL_1,
                AuthenticationTestData.VALID_PASSWORD
            );
            ReportManager.logInfo("Entered invalid email format");

            Assert.assertTrue(authPage.isErrorMessageDisplayed(), "Validation error should be displayed");
            ReportManager.logPass("Invalid email format error displayed correctly");

        } catch (Exception e) {
            ReportManager.logFail("Test failed: " + e.getMessage());
            takeScreenshot("AUTH-SI-04_Failed");
            throw e;
        }
    }
}
