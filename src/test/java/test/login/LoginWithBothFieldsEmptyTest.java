package test.login;

import pages.AuthenticationPage;
import test.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoginWithBothFieldsEmptyTest extends BaseTest {
    private static final Logger log = LoggerFactory.getLogger(LoginWithBothFieldsEmptyTest.class);

    private AuthenticationPage getAuthPage() {
        if (driver == null) {
            throw new RuntimeException("Driver is null - BaseTest setup may have failed");
        }
        return new AuthenticationPage(driver);
    }

    @Test(groups = {"authentication", "negative", "login"},
            description = "AUTH-LI-07: Login with both fields empty")
    public void testLoginWithBothFieldsEmpty() {
        try {
            AuthenticationPage authPage = getAuthPage();
            authPage.performLogin("", "");
            boolean loginFailed = !authPage.isLoginSuccessful();
            Assert.assertTrue(loginFailed, "Login should fail with both fields empty");
        } catch (Exception e) {
            log.error("❌ Both fields empty test failed: ", e);
            throw e;
        }
    }
}
