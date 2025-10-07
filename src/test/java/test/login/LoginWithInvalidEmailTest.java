package test.login;

import pages.AuthenticationPage;
import data.AuthenticationTestData;
import test.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoginWithInvalidEmailTest extends BaseTest {
    private static final Logger log = LoggerFactory.getLogger(LoginWithInvalidEmailTest.class);

    private AuthenticationPage getAuthPage() {
        if (driver == null) {
            throw new RuntimeException("Driver is null - BaseTest setup may have failed");
        }
        return new AuthenticationPage(driver);
    }

    @Test(groups = {"authentication", "negative", "login"},
            description = "AUTH-LI-02: Login with invalid email")
    public void testLoginWithInvalidEmail() {
        try {
            AuthenticationPage authPage = getAuthPage();
            authPage.performLogin(
                    AuthenticationTestData.INVALID_EMAIL_1,
                    AuthenticationTestData.VALID_PASSWORD
            );
            boolean loginFailed = !authPage.isLoginSuccessful();
            Assert.assertTrue(loginFailed, "Login should fail for invalid email (should still show 'Tài khoản')");
        } catch (Exception e) {
            log.error(" Invalid email test failed: ", e);
            throw e;
        }
    }
}

