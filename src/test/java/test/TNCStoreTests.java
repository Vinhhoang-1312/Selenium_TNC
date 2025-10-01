package test;

import pages.authenticationPage;
import pages.UserProfilePage;
import model.AuthenticationTestData;
import helpers.ReportManager;
import helpers.BaseTest;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TNCStoreTests extends BaseTest {
    // Add a logger for this class
    private static final Logger log = LoggerFactory.getLogger(TNCStoreTests.class);

    @BeforeMethod
    public void initPages() {
        try {
            // Simply verify driver is properly initialized - removed unnecessary null checks
            if (driver == null) {
                throw new RuntimeException("WebDriver is null during page initialization");
            }
            log.info("✅ WebDriver initialized successfully");

        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize test: " + e.getMessage(), e);
        }
    }

    // ========== AUTHENTICATION GROUP ==========

    @Test(groups = {"authentication", "smoke", "signup"},
            description = "AUTH-SU-01: Register with valid name, email, and password")
    public void testRegisterWithValidData() {
        ReportManager.startTest("AUTH-SU-01: Register with valid data");

        // Initialize pages for this test method
        authenticationPage authPage = new authenticationPage(driver);

        try {
            // IMPROVED: Use dynamic test data instead of hardcoded
            AuthenticationTestData.TestUser testUser = AuthenticationTestData.createUniqueUser("AutoTest");

            log.info("🔄 Starting registration test with user: {} ({})", testUser.name, testUser.email);

            // Step 1: Perform registration with improved error handling
            authPage.goToRegisterPage();
            ReportManager.logInfo(" Navigated to register page");

            authPage.performRegistration(
                    testUser.name,
                    testUser.email,
                    testUser.password
            );
            ReportManager.logInfo(" Completed registration form submission");

            // Step 2: IMPROVED login verification with retry logic
            boolean isLoggedIn = false;
            String userName = "";

            // Try multiple times as login might take a moment
            for (int attempt = 1; attempt <= 3; attempt++) {
                log.info("🔍 Login verification attempt {}/3", attempt);

                isLoggedIn = authPage.isUserLoggedIn();
                userName = authPage.getLoggedInUserNameRobust();

                if (isLoggedIn && !userName.isEmpty()) {
                    log.info(" Login verified successfully on attempt {}", attempt);
                    break;
                }

                if (attempt < 3) {
                    log.info("⏳ Waiting 2 seconds before next verification attempt...");
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        log.warn("Sleep interrupted during login verification", e);
                        break;
                    }
                }
            }

            // Assertions with detailed error messages
            Assert.assertTrue(isLoggedIn,
                    String.format("User should be logged in after registration. Current status: %s, Username found: '%s'",
                            isLoggedIn, userName));

            Assert.assertFalse(userName.isEmpty(),
                    String.format("User name should not be empty after successful login. Found: '%s'", userName));

            Assert.assertNotEquals(userName, "Tài khoản",
                    String.format("Should show actual user name, not default 'Tài khoản'. Found: '%s'", userName));

            ReportManager.logPass("✅ Registration successful - User logged in as: " + userName);
            log.info("🎉 Test completed successfully. User '{}' registered with email '{}'", userName, testUser.email);

        } catch (Exception e) {
            String errorMsg = "Registration test failed: " + e.getMessage();
            ReportManager.logFail(errorMsg);
            log.error(errorMsg, e);
            throw e;
        }
    }

    // ========== ADDITIONAL IMPROVED TEST METHODS ==========

    @Test(groups = {"authentication", "smoke", "login"},
            description = "AUTH-LI-01: Login with valid credentials")
    public void testLoginWithValidCredentials() {
        ReportManager.startTest("AUTH-LI-01: Login with valid credentials");

        // Initialize pages for this test method
        authenticationPage authPage = new authenticationPage(driver);

        try {
            // First register a user to test login
            AuthenticationTestData.TestUser testUser = AuthenticationTestData.createUniqueUser("LoginTest");

            // Register the user first
            authPage.goToRegisterPage();
            authPage.performRegistration(testUser.name, testUser.email, testUser.password);

            // Wait a bit and then logout (if logged in automatically)
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                log.warn("Sleep interrupted during login test", e);
            }

            // Now test login
            authPage.performLogin(testUser.email, testUser.password);
            ReportManager.logInfo("Attempted login with registered credentials");

            // Verify login is successful by checking for user name display
            boolean isLoggedIn = authPage.isUserLoggedIn();
            String userName = authPage.getLoggedInUserNameRobust();

            Assert.assertTrue(isLoggedIn, "Login should be successful with valid credentials");
            Assert.assertFalse(userName.isEmpty(), "User name should be displayed after successful login");

            ReportManager.logPass("Login successful - User logged in as: " + userName);
            log.info("🎉 Login test completed successfully. User '{}' logged in with email '{}'", userName, testUser.email);

        } catch (Exception e) {
            String errorMsg = "Login test failed: " + e.getMessage();
            ReportManager.logFail(errorMsg);
            log.error(errorMsg, e);
            throw e;
        }
    }
}
