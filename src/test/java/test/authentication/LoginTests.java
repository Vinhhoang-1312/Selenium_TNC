package test.authentication;

import pages.AuthenticationPage;
import model.AuthenticationTestData;
import helpers.ReportManager;
import helpers.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoginTests extends BaseTest {
    private static final Logger log = LoggerFactory.getLogger(LoginTests.class);

    private AuthenticationPage getAuthPage() {
        if (driver == null) {
            throw new RuntimeException("Driver is null - BaseTest setup may have failed");
        }
        return new AuthenticationPage(driver);
    }

    @Test(groups = {"authentication", "smoke", "login"},
          description = "AUTH-LI-01: Login with valid credentials")
    public void testLoginWithValidCredentials() {
        ReportManager.startTest("AUTH-LI-01: Login with valid credentials");

        try {
            AuthenticationPage authPage = getAuthPage();

            // TẠO UNIQUE USER VÀ REGISTER TRƯỚC KHI LOGIN - FIX LỖI EMAIL ĐÃ TỒN TẠI
            AuthenticationTestData.TestUser testUser = AuthenticationTestData.createUniqueUser("LoginTest");
            log.info("🔄 Starting login test with unique user: {} ({})", testUser.name, testUser.email);

            // Đăng ký user trước để có thể login
            authPage.goToRegisterPage();
            authPage.performRegistration(testUser.name, testUser.email, testUser.password);
            ReportManager.logInfo("Pre-registered user for login test");

            // Bây giờ test login với user vừa tạo
            authPage.performLogin(testUser.email, testUser.password);
            ReportManager.logInfo("Attempted login with newly registered credentials");

            Assert.assertTrue(authPage.isLoginSuccessful(), "Login should be successful with valid credentials");
            ReportManager.logPass("Login successful with unique user: " + testUser.email);
            log.info("🎉 Login test completed successfully with user: {}", testUser.email);

        } catch (Exception e) {
            ReportManager.logFail("Test failed: " + e.getMessage());
            log.error("❌ Login test failed: ", e);
            throw e;
        }
    }

    @Test(groups = {"authentication", "negative", "login"},
          description = "AUTH-LI-02: Login with invalid email")
    public void testLoginWithInvalidEmail() {
        ReportManager.startTest("AUTH-LI-02: Login with invalid email");

        try {
            AuthenticationPage authPage = getAuthPage();

            authPage.performLogin(
                AuthenticationTestData.INVALID_EMAIL_1,
                AuthenticationTestData.VALID_PASSWORD
            );
            ReportManager.logInfo("Attempted login with invalid email");

            Assert.assertTrue(authPage.isErrorMessageDisplayed(), "Error message should be displayed for invalid email");
            ReportManager.logPass("Validation successful - invalid email rejected");

        } catch (Exception e) {
            ReportManager.logFail("Test failed: " + e.getMessage());
            log.error("❌ Invalid email test failed: ", e);
            throw e;
        }
    }

    @Test(groups = {"authentication", "negative", "login"},
          description = "AUTH-LI-03: Login with incorrect password")
    public void testLoginWithIncorrectPassword() {
        ReportManager.startTest("AUTH-LI-03: Login with incorrect password");

        try {
            AuthenticationPage authPage = getAuthPage();

            authPage.performLogin(
                AuthenticationTestData.VALID_EMAIL,
                AuthenticationTestData.INVALID_PASSWORD
            );
            ReportManager.logInfo("Attempted login with incorrect password");

            Assert.assertTrue(authPage.isErrorMessageDisplayed(), "Error message should be displayed for incorrect password");
            ReportManager.logPass("Validation successful - incorrect password rejected");

        } catch (Exception e) {
            ReportManager.logFail("Test failed: " + e.getMessage());
            log.error("❌ Incorrect password test failed: ", e);
            throw e;
        }
    }

    @Test(groups = {"authentication", "negative", "login"},
          description = "AUTH-LI-04: Login with non-existent account")
    public void testLoginWithNonExistentAccount() {
        ReportManager.startTest("AUTH-LI-04: Login with non-existent account");

        try {
            AuthenticationPage authPage = getAuthPage();

            // TẠO EMAIL KHÔNG TỒN TẠI
            AuthenticationTestData.TestUser nonExistentUser = AuthenticationTestData.createUniqueUser("NonExistent");

            authPage.performLogin(nonExistentUser.email, nonExistentUser.password);
            ReportManager.logInfo("Attempted login with non-existent account");

            Assert.assertTrue(authPage.isErrorMessageDisplayed(), "Error message should be displayed for non-existent account");
            ReportManager.logPass("Validation successful - non-existent account rejected");

        } catch (Exception e) {
            ReportManager.logFail("Test failed: " + e.getMessage());
            log.error("❌ Non-existent account test failed: ", e);
            throw e;
        }
    }
}
