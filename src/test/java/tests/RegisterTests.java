package tests;

import core.BaseTest;
import listeners.BaseListener;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.LoginPage;
import pages.RegisterPage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Listeners({BaseListener.class})
public class RegisterTests extends BaseTest {
    private static final Logger log = LoggerFactory.getLogger(RegisterTests.class);

    @Test(groups = {"authentication", "register"},
            description = "REG-01: Register with valid data")
    public void testRegisterWithValidData() {
        String timestamp = String.valueOf(System.currentTimeMillis());
        String uniqueEmail = "user" + timestamp + "@test.com";
        String name = "Test User " + timestamp;
        String password = "Test123456";

        LoginPage loginPage = new LoginPage(getDriver());
        RegisterPage registerPage = loginPage.navigateToRegister();

        registerPage.performRegister(name, uniqueEmail, password);

        customWait(2000);
        log.info("Register test completed with email: {}", uniqueEmail);
    }

    @Test(groups = {"authentication", "register"},
            description = "REG-02: Register with invalid email")
    public void testRegisterWithInvalidEmail() {
        LoginPage loginPage = new LoginPage(getDriver());
        RegisterPage registerPage = loginPage.navigateToRegister();

        registerPage.performRegister("Test User", "invalidemail", "Test123456");

        customWait(1000);
        log.info("Register with invalid email completed");
    }

    @Test(groups = {"authentication", "register"},
            description = "REG-03: Register with weak password")
    public void testRegisterWithWeakPassword() {
        String timestamp = String.valueOf(System.currentTimeMillis());
        String uniqueEmail = "user" + timestamp + "@test.com";

        LoginPage loginPage = new LoginPage(getDriver());
        RegisterPage registerPage = loginPage.navigateToRegister();

        registerPage.performRegister("Test User", uniqueEmail, "123");

        customWait(1000);
        log.info("Register with weak password completed");
    }
}
