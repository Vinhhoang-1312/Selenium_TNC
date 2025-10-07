package test.login;

import pages.AuthenticationPage;
import data.AuthenticationTestData;
import test.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoginWithValidCredentialsTest extends BaseTest {
    private static final Logger log = LoggerFactory.getLogger(LoginWithValidCredentialsTest.class);

    private AuthenticationPage getAuthPage() {
        if (driver == null) {
            throw new RuntimeException("Driver is null - BaseTest setup may have failed");
        }
        return new AuthenticationPage(driver);
    }

    @Test(groups = {"authentication", "smoke", "login"},
            description = "AUTH-LI-01: Login with valid credentials")
    public void testLoginWithValidCredentials() {
        try {
            AuthenticationPage authPage = getAuthPage();
            String email = AuthenticationTestData.VALID_EMAIL;
            String password = AuthenticationTestData.VALID_PASSWORD;
            log.info(" Attempting login with existing user: {}", email);
            authPage.performLogin(email, password);
            Assert.assertTrue(authPage.isLoginSuccessful(), "Login should be successful with valid credentials");
            log.info("🎉 Login test completed successfully with user: {}", email);
        } catch (Exception e) {
            log.error("❌ Login test failed: ", e);
            throw e;
        }
    }
}

