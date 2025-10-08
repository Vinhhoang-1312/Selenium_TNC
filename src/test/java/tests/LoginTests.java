package tests;

import core.BaseTest;
import data.AuthenticationTestData;
import data.TestUser;
import io.qameta.allure.*;
import listeners.BaseListener;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.HomePage;
import pages.LoginPage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Listeners({BaseListener.class})
@Epic("Authentication")
@Feature("Login Functionality")
public class LoginTests extends BaseTest {
    private static final Logger log = LoggerFactory.getLogger(LoginTests.class);

    @Test(groups = {"authentication", "smoke", "login"},
            description = "AUTH-LI-01: Login with valid credentials")
    @Story("User Login")
    @Severity(SeverityLevel.BLOCKER)
    @Description("Verify that a user can successfully login with valid email and password credentials")
    public void testLoginWithValidCredentials() {
        try {
            String email = AuthenticationTestData.VALID_EMAIL;
            String password = AuthenticationTestData.VALID_PASSWORD;

            LoginPage loginPage = new LoginPage(getDriver());
            log.info("Attempting login with user: {}", email);
            Allure.step("Open login popup and enter credentials");
            loginPage.performLogin(email, password);

            Allure.step("Verify login is successful");
            Assert.assertTrue(loginPage.isLoginSuccessful(), "Login should be successful with valid credentials");
            log.info("Login test completed successfully with user: {}", email);
        } catch (Exception e) {
            log.error("Login test failed: ", e);
            throw e;
        }
    }

    @Test(groups = {"authentication", "negative", "login"},
            description = "AUTH-LI-02: Login with invalid email")
    @Story("User Login - Negative Scenarios")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify that login fails when using an invalid email format")
    public void testLoginWithInvalidEmail() {
        try {
            LoginPage loginPage = new LoginPage(getDriver());
            log.info("Attempting login with invalid email");

            Allure.step("Attempt login with invalid email format");
            loginPage.performLogin(
                    AuthenticationTestData.INVALID_EMAIL_1,
                    AuthenticationTestData.VALID_PASSWORD
            );

            Allure.step("Verify login fails for invalid email");
            boolean loginFailed = !loginPage.isLoginSuccessful();
            Assert.assertTrue(loginFailed, "Login should fail for invalid email");
            log.info("Login correctly failed with invalid email");
        } catch (Exception e) {
            log.error("Invalid email test failed: ", e);
            throw e;
        }
    }

    @Test(groups = {"authentication", "negative", "login"},
            description = "AUTH-LI-04: Login with non-existent account")
    @Story("User Login - Negative Scenarios")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify that login fails when using credentials that don't exist in the system")
    public void testLoginWithNonExistentAccount() {
        try {
            LoginPage loginPage = new LoginPage(getDriver());
            TestUser nonExistentUser = TestUser.createUniqueUser("NonExistent");
            log.info("Attempting login with non-existent account: {}", nonExistentUser.email);

            Allure.step("Attempt login with non-existent account: " + nonExistentUser.email);
            loginPage.performLogin(nonExistentUser.email, nonExistentUser.password);

            Allure.step("Verify login fails for non-existent account");
            boolean loginFailed = !loginPage.isLoginSuccessful();
            Assert.assertTrue(loginFailed, "Login should fail for non-existent account");
            log.info("Login correctly failed with non-existent account");
        } catch (Exception e) {
            log.error("Non-existent account test failed: ", e);
            throw e;
        }
    }

    @Test(groups = {"authentication", "negative", "login"},
            description = "AUTH-LI-05: Login with empty email")
    @Story("User Login - Validation")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify that login fails when email field is left empty")
    public void testLoginWithEmptyEmail() {
        try {
            LoginPage loginPage = new LoginPage(getDriver());
            log.info("Attempting login with empty email");

            Allure.step("Attempt login with empty email field");
            loginPage.performLogin("", AuthenticationTestData.VALID_PASSWORD);

            Allure.step("Verify login fails with empty email");
            boolean loginFailed = !loginPage.isLoginSuccessful();
            Assert.assertTrue(loginFailed, "Login should fail with empty email");
            log.info("Login correctly failed with empty email");
        } catch (Exception e) {
            log.error("Empty email test failed: ", e);
            throw e;
        }
    }

    @Test(groups = {"authentication", "negative", "login"},
            description = "AUTH-LI-06: Login with empty password")
    @Story("User Login - Validation")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify that login fails when password field is left empty")
    public void testLoginWithEmptyPassword() {
        try {
            LoginPage loginPage = new LoginPage(getDriver());
            log.info("Attempting login with empty password");

            Allure.step("Attempt login with empty password field");
            loginPage.performLogin(AuthenticationTestData.VALID_EMAIL, "");

            Allure.step("Verify login fails with empty password");
            boolean loginFailed = !loginPage.isLoginSuccessful();
            Assert.assertTrue(loginFailed, "Login should fail with empty password");
            log.info("Login correctly failed with empty password");
        } catch (Exception e) {
            log.error("Empty password test failed: ", e);
            throw e;
        }
    }

    @Test(groups = {"authentication", "negative", "login"},
            description = "AUTH-LI-07: Login with both fields empty")
    @Story("User Login - Validation")
    @Severity(SeverityLevel.MINOR)
    @Description("Verify that login fails when both email and password fields are empty")
    public void testLoginWithBothFieldsEmpty() {
        try {
            LoginPage loginPage = new LoginPage(getDriver());
            log.info("Attempting login with both fields empty");

            Allure.step("Attempt login with both fields empty");
            loginPage.performLogin("", "");

            Allure.step("Verify login fails with both fields empty");
            boolean loginFailed = !loginPage.isLoginSuccessful();
            Assert.assertTrue(loginFailed, "Login should fail with both fields empty");
            log.info("Login correctly failed with both fields empty");
        } catch (Exception e) {
            log.error("Both fields empty test failed: ", e);
            throw e;
        }
    }
}
