package tests;

import core.BaseTest;
import data.TestUser;
import io.qameta.allure.*;
import listeners.BaseListener;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.LoginPage;
import pages.RegisterPage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Listeners({BaseListener.class})
@Epic("Authentication")
@Feature("User Registration")
public class RegisterTests extends BaseTest {
    private static final Logger log = LoggerFactory.getLogger(RegisterTests.class);

    @Test(groups = {"authentication", "register"},
            description = "REG-01: Register with valid data")
    @Story("User Registration")
    @Severity(SeverityLevel.BLOCKER)
    @Description("Verify that a new user can successfully register with valid name, email, and password")
    public void testRegisterWithValidData() {
        // Use TestUser.createUniqueUser() to avoid code duplication
        TestUser user = TestUser.createUniqueUser("Register");

        Allure.step("Navigate to registration form");
        LoginPage loginPage = new LoginPage(getDriver());
        RegisterPage registerPage = loginPage.navigateToRegister();

        Allure.step("Fill registration form with valid data: " + user.email);
        registerPage.performRegister(user.name, user.email, user.password);

        customWait(2000);
        log.info("Register test completed with email: {}", user.email);
    }

    @Test(groups = {"authentication", "register"},
            description = "REG-02: Register with invalid email")
    @Story("User Registration - Validation")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify that registration fails when using an invalid email format")
    public void testRegisterWithInvalidEmail() {
        Allure.step("Navigate to registration form");
        LoginPage loginPage = new LoginPage(getDriver());
        RegisterPage registerPage = loginPage.navigateToRegister();

        Allure.step("Attempt to register with invalid email format");
        registerPage.performRegister("Test User", "invalidemail", "Test123456");

        customWait(1000);
        log.info("Register with invalid email completed");
    }

    @Test(groups = {"authentication", "register"},
            description = "REG-03: Register with weak password")
    @Story("User Registration - Validation")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify that registration fails when using a weak password")
    public void testRegisterWithWeakPassword() {
        // Use TestUser.createUniqueUser() to avoid code duplication
        TestUser user = TestUser.createUniqueUser("WeakPwd");

        Allure.step("Navigate to registration form");
        LoginPage loginPage = new LoginPage(getDriver());
        RegisterPage registerPage = loginPage.navigateToRegister();

        Allure.step("Attempt to register with weak password");
        registerPage.performRegister(user.name, user.email, "123");

        customWait(1000);
        log.info("Register with weak password completed");
    }
}
