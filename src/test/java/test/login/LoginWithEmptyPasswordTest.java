package test.login;

import pages.AuthenticationPage;
import data.AuthenticationTestData;
import test.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoginWithEmptyPasswordTest extends BaseTest {
    private static final Logger log = LoggerFactory.getLogger(LoginWithEmptyPasswordTest.class);

    private AuthenticationPage getAuthPage() {
        if (driver == null) {
            throw new RuntimeException("Driver is null - BaseTest setup may have failed");
        }
        return new AuthenticationPage(driver);
    }

    @Test(groups = {"authentication", "negative", "login"},
            description = "AUTH-LI-06: Login with empty password")
    public void testLoginWithEmptyPassword() {
        try {
            AuthenticationPage authPage = getAuthPage();
            authPage.performLogin(AuthenticationTestData.VALID_EMAIL, "");
            boolean loginFailed = !authPage.isLoginSuccessful();
            Assert.assertTrue(loginFailed, "Login should fail with empty password");
        } catch (Exception e) {
            log.error("❌ Empty password test failed: ", e);
            throw e;
        }
    }
}

