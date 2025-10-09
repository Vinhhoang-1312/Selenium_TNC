package tests;

import core.BaseTest;
import data.TestUser;
import io.qameta.allure.*;
import listeners.BaseListener;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.LoginPage;
import utils.TestDataProviders;

@Listeners({BaseListener.class})
@Epic("Authentication")
@Feature("Login Functionality")
public class LoginTests extends BaseTest {

    @Test(groups = {"authentication", "smoke", "login"},
            description = "AUTH-LI-01: Login with valid credentials")
    @Story("User Login")
    @Severity(SeverityLevel.BLOCKER)
    @Description("Verify that a user can successfully login with valid email and password credentials")
    public void testLoginWithValidCredentials() {
        // Initialize page objects
        LoginPage loginPage = new LoginPage(getDriver());

        try {
            // Get valid credentials from DataProvider (first row of Excel)
            Object[][] validData = new TestDataProviders().provideValidLoginData();
            String email = (String) validData[0][1]; // Email column
            String password = (String) validData[0][2]; // Password column

            logger.info("Attempting login with user: {}", email);
            loginPage.performLogin(email, password);

            Assert.assertTrue(loginPage.isLoginSuccessful(), "Login should be successful with valid credentials");
            logger.info("Login test completed successfully with user: {}", email);
        } catch (Exception e) {
            logger.error("Login test failed: ", e);
            throw e;
        }
    }

    @Test(groups = {"authentication", "negative", "login"},
            description = "AUTH-LI-02: Login with invalid email")
    @Story("User Login - Negative Scenarios")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify that login fails when using an invalid email format")
    public void testLoginWithInvalidEmail() {
        // Initialize page objects
        LoginPage loginPage = new LoginPage(getDriver());

        try {
            // Get invalid email from DataProvider (second row of Excel)
            Object[][] invalidData = new TestDataProviders().provideInvalidLoginData();
            String invalidEmail = (String) invalidData[0][1]; // First invalid email

            Object[][] validData = new TestDataProviders().provideValidLoginData();
            String validPassword = (String) validData[0][2]; // Valid password

            logger.info("Attempting login with invalid email");
            loginPage.performLogin(invalidEmail, validPassword);

            boolean loginFailed = !loginPage.isLoginSuccessful();
            Assert.assertTrue(loginFailed, "Login should fail for invalid email");
            logger.info("Login correctly failed with invalid email");
        } catch (Exception e) {
            logger.error("Invalid email test failed: ", e);
            throw e;
        }
    }

    @Test(groups = {"authentication", "negative", "login"},
            description = "AUTH-LI-04: Login with non-existent account")
    @Story("User Login - Negative Scenarios")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify that login fails when using credentials that don't exist in the system")
    public void testLoginWithNonExistentAccount() {
        // Initialize page objects
        LoginPage loginPage = new LoginPage(getDriver());

        try {
            TestUser nonExistentUser = TestUser.createUniqueUser("NonExistent");
            logger.info("Attempting login with non-existent account: {}", nonExistentUser.email);

            loginPage.performLogin(nonExistentUser.email, nonExistentUser.password);

            boolean loginFailed = !loginPage.isLoginSuccessful();
            Assert.assertTrue(loginFailed, "Login should fail for non-existent account");
            logger.info("Login correctly failed with non-existent account");
        } catch (Exception e) {
            logger.error("Non-existent account test failed: ", e);
            throw e;
        }
    }

    @Test(groups = {"authentication", "negative", "login"},
            description = "AUTH-LI-05: Login with empty email")
    @Story("User Login - Validation")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify that login fails when email field is left empty")
    public void testLoginWithEmptyEmail() {
        // Initialize page objects
        LoginPage loginPage = new LoginPage(getDriver());

        try {
            // Get valid password from DataProvider
            Object[][] validData = new TestDataProviders().provideValidLoginData();
            String validPassword = (String) validData[0][2];

            logger.info("Attempting login with empty email");

            loginPage.performLogin("", validPassword);

            boolean loginFailed = !loginPage.isLoginSuccessful();
            Assert.assertTrue(loginFailed, "Login should fail with empty email");
            logger.info("Login correctly failed with empty email");
        } catch (Exception e) {
            logger.error("Empty email test failed: ", e);
            throw e;
        }
    }

    @Test(groups = {"authentication", "negative", "login"},
            description = "AUTH-LI-06: Login with empty password")
    @Story("User Login - Validation")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify that login fails when password field is left empty")
    public void testLoginWithEmptyPassword() {
        // Initialize page objects
        LoginPage loginPage = new LoginPage(getDriver());

        try {
            // Get valid email from DataProvider
            Object[][] validData = new TestDataProviders().provideValidLoginData();
            String validEmail = (String) validData[0][1];

            logger.info("Attempting login with empty password");

            loginPage.performLogin(validEmail, "");

            boolean loginFailed = !loginPage.isLoginSuccessful();
            Assert.assertTrue(loginFailed, "Login should fail with empty password");
            logger.info("Login correctly failed with empty password");
        } catch (Exception e) {
            logger.error("Empty password test failed: ", e);
            throw e;
        }
    }

    @Test(groups = {"authentication", "negative", "login"},
            description = "AUTH-LI-07: Login with both fields empty")
    @Story("User Login - Validation")
    @Severity(SeverityLevel.MINOR)
    @Description("Verify that login fails when both email and password fields are empty")
    public void testLoginWithBothFieldsEmpty() {
        // Initialize page objects
        LoginPage loginPage = new LoginPage(getDriver());

        try {
            logger.info("Attempting login with both fields empty");

            loginPage.performLogin("", "");

            boolean loginFailed = !loginPage.isLoginSuccessful();
            Assert.assertTrue(loginFailed, "Login should fail with both fields empty");
            logger.info("Login correctly failed with both fields empty");
        } catch (Exception e) {
            logger.error("Both fields empty test failed: ", e);
            throw e;
        }
    }

}