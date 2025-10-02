package test.authentication;

import pages.AuthenticationPage;
import model.AuthenticationTestData;
import helpers.ReportManager;
import helpers.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RegisterTests extends BaseTest {
    private static final Logger log = LoggerFactory.getLogger(RegisterTests.class);

    private AuthenticationPage getAuthPage() {
        if (driver == null) {
            lazyInitDriver();
        }
        return new AuthenticationPage(driver);
    }

    @Test(groups = {"authentication", "smoke", "signup"},
          description = "AUTH-SU-01: Register with valid name, email, and password")
    public void testRegisterWithValidData() {
        ReportManager.startTest("AUTH-SU-01: Register with valid data");

        try {
            AuthenticationPage authPage = getAuthPage();

            // TẠO UNIQUE USER MỖI LẦN CHẠY TEST - FIX LỖI EMAIL ĐÃ TỒN TẠI
            AuthenticationTestData.TestUser testUser = AuthenticationTestData.createUniqueUser("RegisterTest");
            log.info("🔄 Starting registration test with unique user: {} ({})", testUser.name, testUser.email);

            authPage.goToRegisterPage();
            ReportManager.logInfo("Navigated to register page");

            authPage.performRegistration(
                testUser.name,
                testUser.email,
                testUser.password
            );
            ReportManager.logInfo("Filled registration form with unique data");

            Assert.assertTrue(authPage.isLoginSuccessful(), "User should be logged in after successful registration");
            ReportManager.logPass("Registration successful with unique email: " + testUser.email);
            log.info("🎉 Registration test completed successfully with user: {}", testUser.email);

        } catch (Exception e) {
            ReportManager.logFail("Test failed: " + e.getMessage());
            log.error("❌ Registration test failed: ", e);
            throw e;
        }
    }

    @Test(groups = {"authentication", "negative", "signup"},
          description = "AUTH-SU-02: Register with existing email")
    public void testRegisterWithExistingEmail() {
        ReportManager.startTest("AUTH-SU-02: Register with existing email");

        try {
            AuthenticationPage authPage = getAuthPage();

            authPage.goToRegisterPage();
            ReportManager.logInfo("Navigated to register page");

            // SỬ DỤNG EMAIL CỐ ĐỊNH CHO TEST NEGATIVE
            authPage.performRegistration(
                AuthenticationTestData.VALID_NAME,
                AuthenticationTestData.EXISTING_EMAIL,
                AuthenticationTestData.VALID_PASSWORD
            );
            ReportManager.logInfo("Attempted registration with existing email");

            // SỬA LỖI: Sử dụng method có sẵn thay vì method không tồn tại
            Assert.assertTrue(authPage.isErrorMessageDisplayed(),
                            "Should show error message for existing email");
            ReportManager.logPass("Correctly showed error for existing email");

        } catch (Exception e) {
            ReportManager.logFail("Test failed: " + e.getMessage());
            log.error("❌ Existing email test failed: ", e);
            throw e;
        }
    }

    @Test(groups = {"authentication", "negative", "signup"},
          description = "AUTH-SU-03: Register with invalid email format")
    public void testRegisterWithInvalidEmail() {
        ReportManager.startTest("AUTH-SU-03: Register with invalid email format");

        try {
            AuthenticationPage authPage = getAuthPage();

            authPage.goToRegisterPage();
            ReportManager.logInfo("Navigated to register page");

            authPage.performRegistration(
                AuthenticationTestData.VALID_NAME_3,
                AuthenticationTestData.INVALID_EMAIL_1,
                AuthenticationTestData.VALID_PASSWORD_3
            );
            ReportManager.logInfo("Attempted registration with invalid email format");

            Assert.assertTrue(authPage.isErrorMessageDisplayed(), "Error message should be displayed");
            ReportManager.logPass("Validation successful - invalid email format rejected");

        } catch (Exception e) {
            ReportManager.logFail("Test failed: " + e.getMessage());
            throw e;
        }
    }

    @Test(groups = {"authentication", "negative", "signup"},
          description = "AUTH-SU-04: Register with weak password")
    public void testRegisterWithWeakPassword() {
        ReportManager.startTest("AUTH-SU-04: Register with weak password");

        try {
            AuthenticationPage authPage = getAuthPage();

            authPage.goToRegisterPage();
            ReportManager.logInfo("Navigated to register page");

            authPage.performRegistration(
                AuthenticationTestData.VALID_NAME,
                AuthenticationTestData.VALID_EMAIL_2,
                AuthenticationTestData.WEAK_PASSWORD_1
            );
            ReportManager.logInfo("Attempted registration with weak password");

            Assert.assertTrue(authPage.isErrorMessageDisplayed(), "Error message should be displayed");
            ReportManager.logPass("Validation successful - weak password rejected");

        } catch (Exception e) {
            ReportManager.logFail("Test failed: " + e.getMessage());
            throw e;
        }
    }
}
