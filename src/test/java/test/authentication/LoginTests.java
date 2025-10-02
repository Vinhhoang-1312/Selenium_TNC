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
<<<<<<< HEAD
=======
        if (driver == null) {
            // Attempt lazy initialization before failing
            lazyInitDriver();
        }
>>>>>>> 2b511788fd639212808e02d46f60d65effaea549
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
<<<<<<< HEAD
=======

    @Test(groups = {"authentication", "negative", "login"},
          description = "AUTH-LI-05: Login with empty email")
    public void testLoginWithEmptyEmail() {
        ReportManager.startTest("AUTH-LI-05: Login with empty email");

        try {
            AuthenticationPage authPage = getAuthPage();

            authPage.performLogin("", AuthenticationTestData.VALID_PASSWORD);
            ReportManager.logInfo("Attempted login with empty email");

            boolean loginFailed = !authPage.isLoginSuccessful();
            Assert.assertTrue(loginFailed, "Login should fail with empty email");
            ReportManager.logPass("Validation successful - empty email rejected");

        } catch (Exception e) {
            ReportManager.logFail("Test failed: " + e.getMessage());
            log.error("❌ Empty email test failed: ", e);
            throw e;
        }
    }

    @Test(groups = {"authentication", "negative", "login"},
          description = "AUTH-LI-06: Login with empty password")
    public void testLoginWithEmptyPassword() {
        ReportManager.startTest("AUTH-LI-06: Login with empty password");

        try {
            AuthenticationPage authPage = getAuthPage();

            authPage.performLogin(AuthenticationTestData.VALID_EMAIL, "");
            ReportManager.logInfo("Attempted login with empty password");

            boolean loginFailed = !authPage.isLoginSuccessful();
            Assert.assertTrue(loginFailed, "Login should fail with empty password");
            ReportManager.logPass("Validation successful - empty password rejected");

        } catch (Exception e) {
            ReportManager.logFail("Test failed: " + e.getMessage());
            log.error("❌ Empty password test failed: ", e);
            throw e;
        }
    }

    @Test(groups = {"authentication", "negative", "login"},
          description = "AUTH-LI-07: Login with both fields empty")
    public void testLoginWithBothFieldsEmpty() {
        ReportManager.startTest("AUTH-LI-07: Login with both fields empty");

        try {
            AuthenticationPage authPage = getAuthPage();

            authPage.performLogin("", "");
            ReportManager.logInfo("Attempted login with both fields empty");

            boolean loginFailed = !authPage.isLoginSuccessful();
            Assert.assertTrue(loginFailed, "Login should fail with both fields empty");
            ReportManager.logPass("Validation successful - both empty fields rejected");

        } catch (Exception e) {
            ReportManager.logFail("Test failed: " + e.getMessage());
            log.error("❌ Both fields empty test failed: ", e);
            throw e;
        }
    }
//
//    @Test(groups = {"authentication", "negative", "login"},
//          description = "AUTH-LI-08: Login with SQL injection in email")
//    public void testLoginWithSQLInjectionEmail() {
//        ReportManager.startTest("AUTH-LI-08: Login with SQL injection in email");
//
//        try {
//            AuthenticationPage authPage = getAuthPage();
//
//            String sqlInjectionEmail = "admin'; DROP TABLE users; --";
//            authPage.performLogin(sqlInjectionEmail, AuthenticationTestData.VALID_PASSWORD);
//            ReportManager.logInfo("Attempted login with SQL injection in email");
//
//            boolean loginFailed = !authPage.isLoginSuccessful();
//            Assert.assertTrue(loginFailed, "Login should fail and prevent SQL injection");
//            ReportManager.logPass("Security validation successful - SQL injection prevented");
//
//        } catch (Exception e) {
//            ReportManager.logFail("Test failed: " + e.getMessage());
//            log.error("❌ SQL injection test failed: ", e);
//            throw e;
//        }
//    }
//
//    @Test(groups = {"authentication", "negative", "login"},
//          description = "AUTH-LI-09: Login with XSS in email")
//    public void testLoginWithXSSEmail() {
//        ReportManager.startTest("AUTH-LI-09: Login with XSS in email");
//
//        try {
//            AuthenticationPage authPage = getAuthPage();
//
//            String xssEmail = "<script>alert('XSS')</script>@test.com";
//            authPage.performLogin(xssEmail, AuthenticationTestData.VALID_PASSWORD);
//            ReportManager.logInfo("Attempted login with XSS payload in email");
//
//            boolean loginFailed = !authPage.isLoginSuccessful();
//            Assert.assertTrue(loginFailed, "Login should fail and prevent XSS");
//            ReportManager.logPass("Security validation successful - XSS prevented");
//
//        } catch (Exception e) {
//            ReportManager.logFail("Test failed: " + e.getMessage());
//            log.error("❌ XSS test failed: ", e);
//            throw e;
//        }
//    }
//
//    @Test(groups = {"authentication", "negative", "login"},
//          description = "AUTH-LI-10: Login with very long email")
//    public void testLoginWithVeryLongEmail() {
//        ReportManager.startTest("AUTH-LI-10: Login with very long email");
//
//        try {
//            AuthenticationPage authPage = getAuthPage();
//
//            String longEmail = "a".repeat(300) + "@test.com";
//            authPage.performLogin(longEmail, AuthenticationTestData.VALID_PASSWORD);
//            ReportManager.logInfo("Attempted login with very long email");
//
//            boolean loginFailed = !authPage.isLoginSuccessful();
//            Assert.assertTrue(loginFailed, "Login should fail with very long email");
//            ReportManager.logPass("Validation successful - very long email rejected");
//
//        } catch (Exception e) {
//            ReportManager.logFail("Test failed: " + e.getMessage());
//            log.error("❌ Very long email test failed: ", e);
//            throw e;
//        }
//    }
//
//    @Test(groups = {"authentication", "negative", "login"},
//          description = "AUTH-LI-11: Login with very long password")
//    public void testLoginWithVeryLongPassword() {
//        ReportManager.startTest("AUTH-LI-11: Login with very long password");
//
//        try {
//            AuthenticationPage authPage = getAuthPage();
//
//            String longPassword = "P".repeat(500);
//            authPage.performLogin(AuthenticationTestData.VALID_EMAIL, longPassword);
//            ReportManager.logInfo("Attempted login with very long password");
//
//            boolean loginFailed = !authPage.isLoginSuccessful();
//            Assert.assertTrue(loginFailed, "Login should fail with very long password");
//            ReportManager.logPass("Validation successful - very long password rejected");
//
//        } catch (Exception e) {
//            ReportManager.logFail("Test failed: " + e.getMessage());
//            log.error("❌ Very long password test failed: ", e);
//            throw e;
//        }
//    }
//
//    @Test(groups = {"authentication", "negative", "login"},
//          description = "AUTH-LI-12: Login with special characters in email")
//    public void testLoginWithSpecialCharactersEmail() {
//        ReportManager.startTest("AUTH-LI-12: Login with special characters in email");
//
//        try {
//            AuthenticationPage authPage = getAuthPage();
//
//            String specialCharEmail = "test!@#$%^&*()@test.com";
//            authPage.performLogin(specialCharEmail, AuthenticationTestData.VALID_PASSWORD);
//            ReportManager.logInfo("Attempted login with special characters in email");
//
//            boolean loginFailed = !authPage.isLoginSuccessful();
//            Assert.assertTrue(loginFailed, "Login should fail with invalid special characters in email");
//            ReportManager.logPass("Validation successful - special characters in email rejected");
//
//        } catch (Exception e) {
//            ReportManager.logFail("Test failed: " + e.getMessage());
//            log.error("❌ Special characters email test failed: ", e);
//            throw e;
//        }
//    }
//
//    @Test(groups = {"authentication", "boundary", "login"},
//          description = "AUTH-LI-13: Login with minimum valid email length")
//    public void testLoginWithMinimumValidEmail() {
//        ReportManager.startTest("AUTH-LI-13: Login with minimum valid email length");
//
//        try {
//            AuthenticationPage authPage = getAuthPage();
//
//            String minEmail = "a@b.co";
//            authPage.performLogin(minEmail, AuthenticationTestData.VALID_PASSWORD);
//            ReportManager.logInfo("Attempted login with minimum valid email length");
//
//            boolean loginFailed = !authPage.isLoginSuccessful();
//            Assert.assertTrue(loginFailed, "Login should fail with non-existent minimum email");
//            ReportManager.logPass("Validation successful - minimum email format handled correctly");
//
//        } catch (Exception e) {
//            ReportManager.logFail("Test failed: " + e.getMessage());
//            log.error("❌ Minimum email test failed: ", e);
//            throw e;
//        }
//    }
//
//    @Test(groups = {"authentication", "boundary", "login"},
//          description = "AUTH-LI-14: Login with spaces in credentials")
//    public void testLoginWithSpacesInCredentials() {
//        ReportManager.startTest("AUTH-LI-14: Login with spaces in credentials");
//
//        try {
//            AuthenticationPage authPage = getAuthPage();
//
//            String emailWithSpaces = " " + AuthenticationTestData.VALID_EMAIL + " ";
//            String passwordWithSpaces = " " + AuthenticationTestData.VALID_PASSWORD + " ";
//            authPage.performLogin(emailWithSpaces, passwordWithSpaces);
//            ReportManager.logInfo("Attempted login with spaces in credentials");
//
//            boolean loginFailed = !authPage.isLoginSuccessful();
//            Assert.assertTrue(loginFailed, "Login should fail with spaces in credentials");
//            ReportManager.logPass("Validation successful - spaces in credentials handled correctly");
//
//        } catch (Exception e) {
//            ReportManager.logFail("Test failed: " + e.getMessage());
//            log.error("❌ Spaces in credentials test failed: ", e);
//            throw e;
//        }
//    }
//
//    @Test(groups = {"authentication", "functional", "login"},
//          description = "AUTH-LI-15: Case sensitivity test for email")
//    public void testLoginEmailCaseSensitivity() {
//        ReportManager.startTest("AUTH-LI-15: Case sensitivity test for email");
//
//        try {
//            AuthenticationPage authPage = getAuthPage();
//
//            String upperCaseEmail = AuthenticationTestData.VALID_EMAIL.toUpperCase();
//            authPage.performLogin(upperCaseEmail, AuthenticationTestData.VALID_PASSWORD);
//            ReportManager.logInfo("Attempted login with uppercase email");
//
//            boolean loginResult = authPage.isLoginSuccessful();
//            if (loginResult) {
//                ReportManager.logPass("Email is case-insensitive - login successful");
//            } else {
//                ReportManager.logPass("Email is case-sensitive - login failed as expected");
//            }
//
//        } catch (Exception e) {
//            ReportManager.logFail("Test failed: " + e.getMessage());
//            log.error("❌ Email case sensitivity test failed: ", e);
//            throw e;
//        }
//    }
>>>>>>> 2b511788fd639212808e02d46f60d65effaea549
}
