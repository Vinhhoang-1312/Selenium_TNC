package test.register;

import pages.AuthenticationPage;
import data.AuthenticationTestData;
import test.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RegisterWithValidDataTest extends BaseTest {
    private static final Logger log = LoggerFactory.getLogger(RegisterWithValidDataTest.class);

    @Test(groups = {"authentication", "smoke", "signup"},
            description = "AUTH-SU-01: Register with valid name, email, and password")
    public void testRegisterWithValidData() {
        try {
            AuthenticationPage authPage = new AuthenticationPage(driver);
            AuthenticationTestData.TestUser testUser = AuthenticationTestData.createUniqueUser("RegisterTest");
            log.info("Starting registration test with unique user: {} ({})", testUser.name, testUser.email);

            boolean isLoggedIn = authPage.registerAndAssertSuccess(testUser);
            Assert.assertTrue(isLoggedIn, "User should be logged in after registration and login.");
            log.info("Registration test completed successfully with user: {}", testUser.email);
        } catch (Exception e) {
            log.error("Registration test failed: ", e);
            throw e;
        }
    }
}
