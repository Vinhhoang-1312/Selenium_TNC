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
            AuthenticationTestData.TestUser testUser = AuthenticationTestData.createUniqueUser("RegisterTest");
            log.info("\uD83D\uDD04 Starting registration test with unique user: {} ({})", testUser.name, testUser.email);

            authPage.goToRegisterPage();
            ReportManager.logInfo("Navigated to register page");

            authPage.performRegistration(testUser.name, testUser.email, testUser.password);
            ReportManager.logInfo("Filled registration form with unique data");

            // In ra text tài khoản ngay sau đăng ký
            String accountTextAfterRegister = authPage.getLoggedInUserNameRobust();
            log.info("[DEBUG] Account text after registration: {}", accountTextAfterRegister);

            // Debug: Print console logs and page source after registration
            String regConsoleLogs = authPage.getBrowserConsoleLogs();
            log.info("[DEBUG] Console logs after registration: {}", regConsoleLogs);
            String regPageSource = driver.getPageSource();
            log.info("[DEBUG] Page source after registration (first 1000 chars): {}", regPageSource.substring(0, Math.min(1000, regPageSource.length())));
            // Print any visible error messages
            if (authPage.isErrorMessageDisplayed()) {
                log.warn("[DEBUG] Error message displayed after registration");
            }

            // Nếu chưa login, thử login lại
            if (!authPage.isLoginSuccessful()) {
                log.info("Registration did not auto-login, attempting manual login with new credentials...");
                authPage.openLoginPopup();
                authPage.performLogin(testUser.email, testUser.password);

                // In ra text tài khoản sau đăng nhập
                String accountTextAfterLogin = authPage.getLoggedInUserNameRobust();
                log.info("[DEBUG] Account text after login: {}", accountTextAfterLogin);

                // Debug: Print console logs and page source after login
                String loginConsoleLogs = authPage.getBrowserConsoleLogs();
                log.info("[DEBUG] Console logs after login: {}", loginConsoleLogs);
                String loginPageSource = driver.getPageSource();
                log.info("[DEBUG] Page source after login (first 1000 chars): {}", loginPageSource.substring(0, Math.min(1000, loginPageSource.length())));
                // Print any visible error messages
                if (authPage.isErrorMessageDisplayed()) {
                    log.warn("[DEBUG] Error message displayed after login");
                }
            }

            // Sau khi đăng nhập, reload lại trang và kiểm tra text tài khoản
            authPage.openLoginPopup();
            authPage.performLogin(testUser.email, testUser.password);
            driver.navigate().refresh();
            try {
                Thread.sleep(5000); // Chờ trang load lại lâu hơn
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
            }
            String accountText = authPage.getLoggedInUserNameRobust();
            log.info("[DEBUG] Account text after login and reload: {}", accountText);
            Assert.assertTrue(!accountText.equals("Tài khoản") && !accountText.equals("Account") && !accountText.isEmpty(),
                "User should be logged in after registration and login. Account text: " + accountText);
            ReportManager.logPass("Registration and login successful with unique email: " + testUser.email);
            log.info("\uD83C\uDF89 Registration test completed successfully with user: {}", testUser.email);

        } catch (Exception e) {
            ReportManager.logFail("Test failed: " + e.getMessage());
            log.error("\u274c Registration test failed: ", e);
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

            authPage.performRegistration(
                AuthenticationTestData.VALID_NAME,
                AuthenticationTestData.EXISTING_EMAIL,
                AuthenticationTestData.VALID_PASSWORD
            );
            ReportManager.logInfo("Attempted registration with existing email");

            // Check browser console logs for error messages
            String consoleLogs = authPage.getBrowserConsoleLogs();
            log.info("Browser console logs after registration attempt: {}", consoleLogs);
            boolean hasEmailError = consoleLogs.contains("Email error") || consoleLogs.contains("error") || consoleLogs.contains("tồn tại");
            Assert.assertTrue(hasEmailError || authPage.isErrorMessageDisplayed(), "Should show error for existing email. Console logs: " + consoleLogs);
            ReportManager.logPass("Correctly showed error for existing email (console or UI)");

        } catch (Exception e) {
            ReportManager.logFail("Test failed: " + e.getMessage());
            log.error("\u274c Existing email test failed: ", e);
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

            String registerResponse = null;
            if (driver instanceof org.openqa.selenium.chrome.ChromeDriver) {
                registerResponse = helpers.NetworkResponseHelper.captureRegisterResponse(
                    (org.openqa.selenium.chrome.ChromeDriver) driver,
                    () -> authPage.performRegistration(
                        AuthenticationTestData.VALID_NAME_3,
                        AuthenticationTestData.INVALID_EMAIL_1,
                        AuthenticationTestData.VALID_PASSWORD_3
                    )
                );
                ReportManager.logInfo("Captured registration API response for invalid email");
            } else {
                authPage.performRegistration(
                    AuthenticationTestData.VALID_NAME_3,
                    AuthenticationTestData.INVALID_EMAIL_1,
                    AuthenticationTestData.VALID_PASSWORD_3
                );
                ReportManager.logInfo("Attempted registration with invalid email format");
            }

            if (registerResponse != null) {
                log.info("Registration API response: {}", registerResponse);
                boolean hasError = registerResponse.contains("error") ||
                                 registerResponse.contains("Email error") ||
                                 registerResponse.contains("invalid") ||
                                 registerResponse.contains("không hợp lệ");
                Assert.assertTrue(hasError, "Should show error for invalid email. API response: " + registerResponse);
                ReportManager.logPass("✅ API correctly returned error for invalid email");
            } else {
                Assert.assertTrue(authPage.isErrorMessageDisplayed(), "Error message should be displayed");
                ReportManager.logPass("Validation successful - invalid email format rejected");
            }

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

            String registerResponse = null;
            if (driver instanceof org.openqa.selenium.chrome.ChromeDriver) {
                registerResponse = helpers.NetworkResponseHelper.captureRegisterResponse(
                    (org.openqa.selenium.chrome.ChromeDriver) driver,
                    () -> authPage.performRegistration(
                        AuthenticationTestData.VALID_NAME,
                        AuthenticationTestData.VALID_EMAIL_2,
                        AuthenticationTestData.WEAK_PASSWORD_1
                    )
                );
                ReportManager.logInfo("Captured registration API response for weak password");
            } else {
                authPage.performRegistration(
                    AuthenticationTestData.VALID_NAME,
                    AuthenticationTestData.VALID_EMAIL_2,
                    AuthenticationTestData.WEAK_PASSWORD_1
                );
                ReportManager.logInfo("Attempted registration with weak password");
            }

            if (registerResponse != null) {
                log.info("Registration API response: {}", registerResponse);
                boolean hasError = registerResponse.contains("error") ||
                                 registerResponse.contains("password") ||
                                 registerResponse.contains("weak") ||
                                 registerResponse.contains("yếu");
                Assert.assertTrue(hasError, "Should show error for weak password. API response: " + registerResponse);
                ReportManager.logPass("✅ API correctly returned error for weak password");
            } else {
                Assert.assertTrue(authPage.isErrorMessageDisplayed(), "Error message should be displayed");
                ReportManager.logPass("Validation successful - weak password rejected");
            }

        } catch (Exception e) {
            ReportManager.logFail("Test failed: " + e.getMessage());
            throw e;
        }
    }
}
