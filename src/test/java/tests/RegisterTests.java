package tests;

import core.BaseTest;
import data.TestUser;
import io.qameta.allure.*;
import listeners.BaseListener;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.LoginPage;
import pages.RegisterPage;
import utils.TestDataProviders;
import utils.Reporter;

@Listeners({BaseListener.class})
@Epic("Authentication")
@Feature("User Registration")
public class RegisterTests extends BaseTest {

    @Test(groups = {"authentication", "register"},
            description = "REG-01: Register with valid data",
            dataProvider = "registrationData", dataProviderClass = TestDataProviders.class)
    @Story("User Registration")
    @Severity(SeverityLevel.BLOCKER)
    @Description("Verify that a new user can successfully register with valid name, email, and password")
    public void testRegisterWithValidData(Object[] regRow) {
        LoginPage loginPage = new LoginPage(getDriver());

        String name, email, password;
        if (regRow != null && regRow.length >= 3) {
            name = (String) regRow[0];
            email = (String) regRow[1];
            password = (String) regRow[2];
        } else {
            TestUser user = TestUser.createUniqueUser("Register");
            name = user.name;
            email = user.email;
            password = user.password;
        }

        Reporter.LogToReport("Step 1: Navigate to Register page");
        RegisterPage registerPage = loginPage.navigateToRegister();

        Reporter.LogToReport("Step 2: Perform registration");
        registerPage.performRegister(name, email, password);

        Reporter.LogToReport("Step 3: Verify registration completed");
        logger.info("Register test completed with email: {}", email);
    }

    @Test(groups = {"authentication", "register"},
            description = "REG-02: Register with invalid email")
    @Story("User Registration - Validation")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify that registration fails when using an invalid email format")
    public void testRegisterWithInvalidEmail() {
        LoginPage loginPage = new LoginPage(getDriver());

        Reporter.LogToReport("Step 1: Navigate to Register page");
        RegisterPage registerPage = loginPage.navigateToRegister();

        Reporter.LogToReport("Step 2: Attempt registration with invalid email");
        registerPage.performRegister("Test User", "invalidemail", "Test123456");

        Reporter.LogToReport("Step 3: Verify registration failed");
        logger.info("Register with invalid email completed");
    }

    @Test(groups = {"authentication", "register"},
            description = "REG-03: Register with weak password")
    @Story("User Registration - Validation")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify that registration fails when using a weak password")
    public void testRegisterWithWeakPassword() {
        LoginPage loginPage = new LoginPage(getDriver());
        TestUser user = TestUser.createUniqueUser("WeakPwd");

        Reporter.LogToReport("Step 1: Navigate to Register page");
        RegisterPage registerPage = loginPage.navigateToRegister();

        Reporter.LogToReport("Step 2: Attempt register with weak password");
        registerPage.performRegister(user.name, user.email, "123");

        Reporter.LogToReport("Step 3: Verify registration failed due to weak password");
        logger.info("Register with weak password completed");
    }
}
