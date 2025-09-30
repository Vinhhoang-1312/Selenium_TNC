package test.authentication;

import pages.AuthenticationPage;
import model.AuthenticationTestData;
import helpers.ReportManager;
import helpers.BaseTest;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RegisterTests extends BaseTest {
    private AuthenticationPage authPage;
    private static final Logger log = LoggerFactory.getLogger(RegisterTests.class);

    @BeforeMethod
    public void setUpTest() {
        authPage = new AuthenticationPage(driver);
        ReportManager.setModule("authentication-register");
        log.info("✅ RegisterTests setup completed");
    }

    @Test(groups = {"authentication", "smoke", "signup"},
          description = "AUTH-SU-01: Register with valid name, email, and password")
    public void testRegisterWithValidData() {
        ReportManager.startTest("AUTH-SU-01: Register with valid data");

        try {
            authPage.goToRegisterPage();
            ReportManager.logInfo("Navigated to register page");

            authPage.performRegistration(
                AuthenticationTestData.VALID_NAME,
                AuthenticationTestData.VALID_EMAIL,
                AuthenticationTestData.VALID_PASSWORD
            );
            ReportManager.logInfo("Filled registration form with valid data");

            Assert.assertTrue(authPage.isLoginSuccessful(), "User should be logged in after successful registration");
            ReportManager.logPass("Registration successful");

        } catch (Exception e) {
            ReportManager.logFail("Test failed: " + e.getMessage());
            throw e;
        }
    }

    @Test(groups = {"authentication", "negative", "signup"},
          description = "AUTH-SU-02: Register with existing email")
    public void testRegisterWithExistingEmail() {
        ReportManager.startTest("AUTH-SU-02: Register with existing email");

        try {
            authPage.goToRegisterPage();
            ReportManager.logInfo("Navigated to register page");

            authPage.performRegistration(
                AuthenticationTestData.VALID_NAME_2,
                AuthenticationTestData.EXISTING_EMAIL,
                AuthenticationTestData.VALID_PASSWORD_2
            );
            ReportManager.logInfo("Attempted registration with existing email");

            Assert.assertTrue(authPage.isErrorMessageDisplayed(), "Error message should be displayed");
            String errorMessage = authPage.getErrorMessage();
            Assert.assertTrue(errorMessage.contains("Email đã được sử dụng"),
                "Error message should indicate email already exists");
            ReportManager.logPass("Validation successful - existing email rejected");

        } catch (Exception e) {
            ReportManager.logFail("Test failed: " + e.getMessage());
            throw e;
        }
    }

    @Test(groups = {"authentication", "negative", "signup"},
          description = "AUTH-SU-03: Register with invalid email format")
    public void testRegisterWithInvalidEmail() {
        ReportManager.startTest("AUTH-SU-03: Register with invalid email format");

        try {
            authPage.goToRegisterPage();
            ReportManager.logInfo("Navigated to register page");

            authPage.performRegistration(
                AuthenticationTestData.VALID_NAME_3,
                AuthenticationTestData.INVALID_EMAIL_1,
                AuthenticationTestData.VALID_PASSWORD_3
            );
            ReportManager.logInfo("Attempted registration with invalid email format");

            Assert.assertTrue(authPage.isErrorMessageDisplayed(), "Error message should be displayed");
            ReportManager.logPass("Validation successful - invalid email format rejected");

        } catch (Exception e) {
            ReportManager.logFail("Test failed: " + e.getMessage());
            throw e;
        }
    }

    @Test(groups = {"authentication", "negative", "signup"},
          description = "AUTH-SU-04: Register with weak password")
    public void testRegisterWithWeakPassword() {
        ReportManager.startTest("AUTH-SU-04: Register with weak password");

        try {
            authPage.goToRegisterPage();
            ReportManager.logInfo("Navigated to register page");

            authPage.performRegistration(
                AuthenticationTestData.VALID_NAME,
                AuthenticationTestData.VALID_EMAIL_2,
                AuthenticationTestData.WEAK_PASSWORD_1
            );
            ReportManager.logInfo("Attempted registration with weak password");

            Assert.assertTrue(authPage.isErrorMessageDisplayed(), "Error message should be displayed");
            ReportManager.logPass("Validation successful - weak password rejected");

        } catch (Exception e) {
            ReportManager.logFail("Test failed: " + e.getMessage());
            throw e;
        }
    }
}
