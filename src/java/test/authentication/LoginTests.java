package test.authentication;

import pages.AuthenticationPage;
import model.AuthenticationTestData;
import helpers.ReportManager;
import helpers.BaseTest;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class LoginTests extends BaseTest {
    private AuthenticationPage authPage;

    @BeforeMethod
    public void setUpTest() {
        authPage = new AuthenticationPage(driver);
        ReportManager.setModule("authentication-login");
        System.out.println("✅ LoginTests setup completed");
    }

    @Test(groups = {"authentication", "smoke", "login"},
          description = "AUTH-LI-01: Login with valid credentials")
    public void testLoginWithValidCredentials() {
        ReportManager.startTest("AUTH-LI-01: Login with valid credentials");

        try {
            authPage.performLogin(
                AuthenticationTestData.VALID_EMAIL,
                AuthenticationTestData.VALID_PASSWORD
            );
            ReportManager.logInfo("Attempted login with valid credentials");

            Assert.assertTrue(authPage.isLoginSuccessful(), "Login should be successful with valid credentials");
            ReportManager.logPass("Login successful");

        } catch (Exception e) {
            ReportManager.logFail("Test failed: " + e.getMessage());
            throw e;
        }
    }

    @Test(groups = {"authentication", "negative", "login"},
          description = "AUTH-LI-02: Login with invalid email")
    public void testLoginWithInvalidEmail() {
        ReportManager.startTest("AUTH-LI-02: Login with invalid email");

        try {
            authPage.performLogin(
                AuthenticationTestData.NON_EXISTING_EMAIL_1,
                AuthenticationTestData.VALID_PASSWORD
            );
            ReportManager.logInfo("Attempted login with non-existing email");

            Assert.assertFalse(authPage.isLoginSuccessful(), "Login should fail with non-existing email");
            Assert.assertTrue(authPage.isErrorMessageDisplayed(), "Error message should be displayed");
            ReportManager.logPass("Validation successful - non-existing email rejected");

        } catch (Exception e) {
            ReportManager.logFail("Test failed: " + e.getMessage());
            throw e;
        }
    }

    @Test(groups = {"authentication", "negative", "login"},
          description = "AUTH-LI-03: Login with wrong password")
    public void testLoginWithWrongPassword() {
        ReportManager.startTest("AUTH-LI-03: Login with wrong password");

        try {
            authPage.performLogin(
                AuthenticationTestData.VALID_EMAIL,
                AuthenticationTestData.WRONG_PASSWORD_1
            );
            ReportManager.logInfo("Attempted login with wrong password");

            Assert.assertFalse(authPage.isLoginSuccessful(), "Login should fail with wrong password");
            Assert.assertTrue(authPage.isErrorMessageDisplayed(), "Error message should be displayed");
            ReportManager.logPass("Validation successful - wrong password rejected");

        } catch (Exception e) {
            ReportManager.logFail("Test failed: " + e.getMessage());
            throw e;
        }
    }

    @Test(groups = {"authentication", "negative", "login"},
          description = "AUTH-LI-04: Login with empty credentials")
    public void testLoginWithEmptyCredentials() {
        ReportManager.startTest("AUTH-LI-04: Login with empty credentials");

        try {
            authPage.performLogin("", "");
            ReportManager.logInfo("Attempted login with empty credentials");

            Assert.assertFalse(authPage.isLoginSuccessful(), "Login should fail with empty credentials");
            Assert.assertTrue(authPage.isErrorMessageDisplayed(), "Error message should be displayed");
            ReportManager.logPass("Validation successful - empty credentials rejected");

        } catch (Exception e) {
            ReportManager.logFail("Test failed: " + e.getMessage());
            throw e;
        }
    }
}
