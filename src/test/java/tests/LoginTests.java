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
import utils.Reporter;

@Listeners({BaseListener.class})
@Epic("Authentication")
@Feature("Login Functionality")
public class LoginTests extends BaseTest {

    @Test(groups = {"authentication", "smoke", "login"},
          description = "AUTH-LI-01: Login with valid credentials",
          dataProvider = "validLoginData", dataProviderClass = TestDataProviders.class)
    @Story("User Login")
    @Severity(SeverityLevel.BLOCKER)
    @Description("Verify that a user can successfully login with valid email and password credentials")
    public void testLoginWithValidCredentials(Object[] validRow) {
        LoginPage loginPage = new LoginPage(getDriver());

        try {
            String email = (String) validRow[1];
            String password = (String) validRow[2];

            logger.info("Attempting login with user: {}", email);

            Reporter.LogToReport("Step 1: Perform login");
            loginPage.performLogin(email, password);

            Reporter.LogToReport("Step 2: Verify login succeeded");
            Assert.assertTrue(loginPage.isLoginSuccessful(), "Login should be successful with valid credentials");

            logger.info("Login test completed successfully with user: {}", email);
        } catch (Exception e) {
            logger.error("Login test failed: ", e);
            throw e;
        }
    }

    @Test(groups = {"authentication", "negative", "login"},
          description = "AUTH-LI-02: Login with invalid email",
          dataProvider = "invalidLoginData", dataProviderClass = TestDataProviders.class)
    @Story("User Login - Negative Scenarios")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify that login fails when using an invalid email format")
    public void testLoginWithInvalidEmail(Object[] invalidRow) {
        LoginPage loginPage = new LoginPage(getDriver());

        try {
            String invalidEmail = (String) invalidRow[1];

            Object[] validRow = TestDataProviders.class.getDeclaredMethods()[0].getDeclaringClass() == TestDataProviders.class ?
                    new TestDataProviders().provideValidLoginData()[0] : new TestDataProviders().provideValidLoginData()[0];
            String validPassword = (String) validRow[2];

            logger.info("Attempting login with invalid email");

            Reporter.LogToReport("Step 1: Perform login with invalid email");
            loginPage.performLogin(invalidEmail, validPassword);

            Reporter.LogToReport("Step 2: Verify login failed");
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
        LoginPage loginPage = new LoginPage(getDriver());

        try {
            TestUser nonExistentUser = TestUser.createUniqueUser("NonExistent");
            logger.info("Attempting login with non-existent account: {}", nonExistentUser.email);

            Reporter.LogToReport("Step 1: Perform login with non-existent account");
            loginPage.performLogin(nonExistentUser.email, nonExistentUser.password);

            Reporter.LogToReport("Step 2: Verify login failed");
            boolean loginFailed = !loginPage.isLoginSuccessful();
            Assert.assertTrue(loginFailed, "Login should fail for non-existent account");

            logger.info("Login correctly failed with non-existent account");
        } catch (Exception e) {
            logger.error("Non-existent account test failed: ", e);
            throw e;
        }
    }

    @Test(groups = {"authentication", "negative", "login"},
            description = "AUTH-LI-05: Login with empty email",
            dataProvider = "validLoginData", dataProviderClass = TestDataProviders.class)
    @Story("User Login - Validation")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify that login fails when email field is left empty")
    public void testLoginWithEmptyEmail(Object[] validRow) {
        LoginPage loginPage = new LoginPage(getDriver());

        try {
            String validPassword = (String) validRow[2];

            logger.info("Attempting login with empty email");

            Reporter.LogToReport("Step 1: Perform login with empty email field");
            loginPage.performLogin("", validPassword);

            Reporter.LogToReport("Step 2: Verify login failed");
            boolean loginFailed = !loginPage.isLoginSuccessful();
            Assert.assertTrue(loginFailed, "Login should fail with empty email");

            logger.info("Login correctly failed with empty email");
        } catch (Exception e) {
            logger.error("Empty email test failed: ", e);
            throw e;
        }
    }

    @Test(groups = {"authentication", "negative", "login"},
            description = "AUTH-LI-06: Login with empty password",
            dataProvider = "validLoginData", dataProviderClass = TestDataProviders.class)
    @Story("User Login - Validation")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify that login fails when password field is left empty")
    public void testLoginWithEmptyPassword(Object[] validRow) {
        LoginPage loginPage = new LoginPage(getDriver());

        try {
            String validEmail = (String) validRow[1];

            logger.info("Attempting login with empty password");

            Reporter.LogToReport("Step 1: Perform login with empty password field");
            loginPage.performLogin(validEmail, "");

            Reporter.LogToReport("Step 2: Verify login failed");
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
        LoginPage loginPage = new LoginPage(getDriver());

        try {
            logger.info("Attempting login with both fields empty");

            Reporter.LogToReport("Step 1: Perform login with both fields empty");
            loginPage.performLogin("", "");

            Reporter.LogToReport("Step 2: Verify login failed");
            boolean loginFailed = !loginPage.isLoginSuccessful();
            Assert.assertTrue(loginFailed, "Login should fail with both fields empty");

            logger.info("Login correctly failed with both fields empty");
        } catch (Exception e) {
            logger.error("Both fields empty test failed: ", e);
            throw e;
        }
    }

}