package test.login;

import pages.AuthenticationPage;
import data.AuthenticationTestData;
import test.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoginWithNonExistentAccountTest extends BaseTest {
    private static final Logger log = LoggerFactory.getLogger(LoginWithNonExistentAccountTest.class);

    private AuthenticationPage getAuthPage() {
        if (driver == null) {
            throw new RuntimeException("Driver is null - BaseTest setup may have failed");
        }
        return new AuthenticationPage(driver);
    }

    @Test(groups = {"authentication", "negative", "login"},
            description = "AUTH-LI-04: Login with non-existent account")
    public void testLoginWithNonExistentAccount() {
        try {
            AuthenticationPage authPage = getAuthPage();
            AuthenticationTestData.TestUser nonExistentUser = AuthenticationTestData.createUniqueUser("NonExistent");
            authPage.performLogin(nonExistentUser.email, nonExistentUser.password);
            boolean loginFailed = !authPage.isLoginSuccessful();
            Assert.assertTrue(loginFailed, "Login should fail for non-existent account (should still show 'Tài khoản')");
        } catch (Exception e) {
            log.error("❌ Non-existent account test failed: ", e);
            throw e;
        }
    }
}

