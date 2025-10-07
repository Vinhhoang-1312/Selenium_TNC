package test.login;

import pages.AuthenticationPage;
import data.AuthenticationTestData;
import test.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoginWithEmptyEmailTest extends BaseTest {
    private static final Logger log = LoggerFactory.getLogger(LoginWithEmptyEmailTest.class);

    private AuthenticationPage getAuthPage() {
        if (driver == null) {
            throw new RuntimeException("Driver is null - BaseTest setup may have failed");
        }
        return new AuthenticationPage(driver);
    }

    @Test(groups = {"authentication", "negative", "login"},
            description = "AUTH-LI-05: Login with empty email")
    public void testLoginWithEmptyEmail() {
        try {
            AuthenticationPage authPage = getAuthPage();
            authPage.performLogin("", AuthenticationTestData.VALID_PASSWORD);
            boolean loginFailed = !authPage.isLoginSuccessful();
            Assert.assertTrue(loginFailed, "Login should fail with empty email");
        } catch (Exception e) {
            log.error("❌ Empty email test failed: ", e);
            throw e;
        }
    }
}

