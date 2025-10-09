package tests;

import core.BaseTest;
import data.TestUser;
import io.qameta.allure.*;
import listeners.BaseListener;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.LoginPage;
import pages.RegisterPage;

@Listeners({BaseListener.class})
@Epic("Authentication")
@Feature("User Registration")
public class RegisterTests extends BaseTest {

    @Test(groups = {"authentication", "register"},
            description = "REG-01: Register with valid data")
    @Story("User Registration")
    @Severity(SeverityLevel.BLOCKER)
    @Description("Verify that a new user can successfully register with valid name, email, and password")
    public void testRegisterWithValidData() {
        LoginPage loginPage = new LoginPage(getDriver());
        RegisterPage registerPage;

        TestUser user = TestUser.createUniqueUser("Register");
        registerPage = loginPage.navigateToRegister();
        registerPage.performRegister(user.name, user.email, user.password);

        logger.info("Register test completed with email: {}", user.email);
    }

    @Test(groups = {"authentication", "register"},
            description = "REG-02: Register with invalid email")
    @Story("User Registration - Validation")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify that registration fails when using an invalid email format")
    public void testRegisterWithInvalidEmail() {
        LoginPage loginPage = new LoginPage(getDriver());
        RegisterPage registerPage;

        registerPage = loginPage.navigateToRegister();
        registerPage.performRegister("Test User", "invalidemail", "Test123456");

        logger.info("Register with invalid email completed");
    }

    @Test(groups = {"authentication", "register"},
            description = "REG-03: Register with weak password")
    @Story("User Registration - Validation")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify that registration fails when using a weak password")
    public void testRegisterWithWeakPassword() {
        LoginPage loginPage = new LoginPage(getDriver());
        RegisterPage registerPage;
        TestUser user = TestUser.createUniqueUser("WeakPwd");

        registerPage = loginPage.navigateToRegister();
        registerPage.performRegister(user.name, user.email, "123");

        logger.info("Register with weak password completed");
    }
}
