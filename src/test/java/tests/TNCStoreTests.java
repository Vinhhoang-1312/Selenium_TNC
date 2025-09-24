package tests;

import base.BaseTest;
import pages.AuthenticationPage;
import pages.UserProfilePage;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import utils.ReportManager;
import data.AuthenticationTestData;

public class TNCStoreTests extends BaseTest {
    private AuthenticationPage authPage;
    private UserProfilePage profilePage;

    @BeforeMethod
    public void setUpTest() {
        // BaseTest tự động khởi tạo driver, chỉ cần tạo page objects
        authPage = new AuthenticationPage(driver);
        profilePage = new UserProfilePage(driver);
        ReportManager.setModule("tnc-store");
    }

    // ========== AUTHENTICATION GROUP ==========

    @Test(groups = {"authentication", "smoke", "signup"},
          description = "AUTH-SU-01: Register with valid name, email, and password")
    public void testRegisterWithValidData() {
        test = ReportManager.startTest("AUTH-SU-01: Register with valid data");

        try {
            authPage.goToRegisterPage();
            ReportManager.logInfo("Navigated to register page");

            authPage.performRegistration(
                AuthenticationTestData.VALID_NAME,
                AuthenticationTestData.VALID_EMAIL,
                AuthenticationTestData.VALID_PASSWORD
            );
            ReportManager.logInfo("Filled registration form with valid data");

            Assert.assertTrue(authPage.isLoginSuccessful(), "User should be logged in after successful registration");
            ReportManager.logPass("Registration successful");

        } catch (Exception e) {
            ReportManager.logFail("Test failed: " + e.getMessage());
            takeScreenshot("AUTH-SU-01_Failed");
            throw e;
        }
    }

    @Test(groups = {"authentication", "regression", "signup"},
          description = "AUTH-SU-02: Register with existing email")
    public void testRegisterWithExistingEmail() {
        test = ReportManager.startTest("AUTH-SU-02: Register with existing email");

        try {
            authPage.goToRegisterPage();
            ReportManager.logInfo("Navigated to register page");

            authPage.performRegistration(
                AuthenticationTestData.VALID_NAME,
                AuthenticationTestData.EXISTING_EMAIL,
                AuthenticationTestData.VALID_PASSWORD
            );
            ReportManager.logInfo("Attempted registration with existing email");

            Assert.assertTrue(authPage.isErrorMessageDisplayed(), "Error message should be displayed for existing email");
            ReportManager.logPass("Existing email properly rejected");

        } catch (Exception e) {
            ReportManager.logFail("Test failed: " + e.getMessage());
            takeScreenshot("AUTH-SU-02_Failed");
            throw e;
        }
    }

    @Test(groups = {"authentication", "regression", "signup"},
          description = "AUTH-SU-03: Register with invalid email format")
    public void testRegisterWithInvalidEmail() {
        test = ReportManager.startTest("AUTH-SU-03: Register with invalid email format");

        try {
            authPage.goToRegisterPage();
            ReportManager.logInfo("Navigated to register page");

            authPage.performRegistration(
                AuthenticationTestData.VALID_NAME,
                AuthenticationTestData.INVALID_EMAIL_1,
                AuthenticationTestData.VALID_PASSWORD
            );
            ReportManager.logInfo("Filled registration form with invalid email");

            Assert.assertTrue(authPage.isInvalidEmailErrorDisplayed(), "Invalid email error should be displayed");
            ReportManager.logPass("Invalid email error displayed correctly");

        } catch (Exception e) {
            ReportManager.logFail("Test failed: " + e.getMessage());
            takeScreenshot("AUTH-SU-03_Failed");
            throw e;
        }
    }

    @Test(groups = {"authentication", "regression", "signup"},
          description = "AUTH-SU-04: Register with blank mandatory fields")
    public void testRegisterWithBlankFields() {
        test = ReportManager.startTest("AUTH-SU-04: Register with blank mandatory fields");

        try {
            authPage.goToRegisterPage();
            ReportManager.logInfo("Navigated to register page");

            authPage.performRegistration("", "", "");
            ReportManager.logInfo("Submitted form with blank fields");

            Assert.assertTrue(authPage.isRequiredFieldErrorDisplayed(), "Required field error should be displayed");
            ReportManager.logPass("Required field error displayed correctly");

        } catch (Exception e) {
            ReportManager.logFail("Test failed: " + e.getMessage());
            takeScreenshot("AUTH-SU-04_Failed");
            throw e;
        }
    }

    @Test(groups = {"authentication", "regression", "signup"},
          description = "AUTH-SU-05: Register with weak/short password")
    public void testRegisterWithWeakPassword() {
        test = ReportManager.startTest("AUTH-SU-05: Register with weak/short password");

        try {
            authPage.goToRegisterPage();
            ReportManager.logInfo("Navigated to register page");

            authPage.performRegistration(
                AuthenticationTestData.VALID_NAME,
                AuthenticationTestData.VALID_EMAIL_2,
                AuthenticationTestData.WEAK_PASSWORD_1
            );
            ReportManager.logInfo("Filled registration form with weak password");

            Assert.assertTrue(authPage.isErrorMessageDisplayed(), "Password error should be displayed");
            ReportManager.logPass("Weak password rejected correctly");

        } catch (Exception e) {
            ReportManager.logFail("Test failed: " + e.getMessage());
            takeScreenshot("AUTH-SU-05_Failed");
            throw e;
        }
    }

    // ========== LOGIN GROUP ==========

    @Test(groups = {"authentication", "smoke", "login"},
          description = "AUTH-SI-01: Login with valid email and password")
    public void testLoginWithValidCredentials() {
        test = ReportManager.startTest("AUTH-SI-01: Login with valid credentials");

        try {
            authPage.goToLoginPage();
            ReportManager.logInfo("Navigated to login page");

            authPage.performLogin(
                AuthenticationTestData.VALID_EMAIL,
                AuthenticationTestData.VALID_PASSWORD
            );
            ReportManager.logInfo("Entered valid login credentials");

            Assert.assertTrue(authPage.isLoginSuccessful(), "User should be logged in successfully");
            ReportManager.logPass("Login successful");

        } catch (Exception e) {
            ReportManager.logFail("Test failed: " + e.getMessage());
            takeScreenshot("AUTH-SI-01_Failed");
            throw e;
        }
    }

    @Test(groups = {"authentication", "regression", "login"},
          description = "AUTH-SI-02: Login with wrong password")
    public void testLoginWithWrongPassword() {
        test = ReportManager.startTest("AUTH-SI-02: Login with wrong password");

        try {
            authPage.goToLoginPage();
            ReportManager.logInfo("Navigated to login page");

            authPage.performLogin(
                AuthenticationTestData.VALID_EMAIL,
                AuthenticationTestData.WRONG_PASSWORD_1
            );
            ReportManager.logInfo("Entered wrong password");

            Assert.assertTrue(authPage.isErrorMessageDisplayed(), "Error message should be displayed");
            ReportManager.logPass("Wrong password error displayed correctly");

        } catch (Exception e) {
            ReportManager.logFail("Test failed: " + e.getMessage());
            takeScreenshot("AUTH-SI-02_Failed");
            throw e;
        }
    }

    @Test(groups = {"authentication", "regression", "login"},
          description = "AUTH-SI-03: Login with unregistered email")
    public void testLoginWithUnregisteredEmail() {
        test = ReportManager.startTest("AUTH-SI-03: Login with unregistered email");

        try {
            authPage.goToLoginPage();
            ReportManager.logInfo("Navigated to login page");

            authPage.performLogin(
                AuthenticationTestData.NON_EXISTING_EMAIL_1,
                AuthenticationTestData.VALID_PASSWORD
            );
            ReportManager.logInfo("Entered unregistered email");

            Assert.assertTrue(authPage.isErrorMessageDisplayed(), "Error message should be displayed");
            ReportManager.logPass("Unregistered email error displayed correctly");

        } catch (Exception e) {
            ReportManager.logFail("Test failed: " + e.getMessage());
            takeScreenshot("AUTH-SI-03_Failed");
            throw e;
        }
    }

    @Test(groups = {"authentication", "regression", "login"},
          description = "AUTH-SI-04: Login with invalid email format")
    public void testLoginWithInvalidEmailFormat() {
        test = ReportManager.startTest("AUTH-SI-04: Login with invalid email format");

        try {
            authPage.goToLoginPage();
            ReportManager.logInfo("Navigated to login page");

            authPage.performLogin(
                AuthenticationTestData.INVALID_EMAIL_1,
                AuthenticationTestData.VALID_PASSWORD
            );
            ReportManager.logInfo("Entered invalid email format");

            Assert.assertTrue(authPage.isErrorMessageDisplayed(), "Validation error should be displayed");
            ReportManager.logPass("Invalid email format error displayed correctly");

        } catch (Exception e) {
            ReportManager.logFail("Test failed: " + e.getMessage());
            takeScreenshot("AUTH-SI-04_Failed");
            throw e;
        }
    }

    // ========== FORGOT PASSWORD GROUP ==========

    @Test(groups = {"authentication", "regression", "forgot-password"},
          description = "AUTH-FP-01: Reset password with valid registered email")
    public void testForgotPasswordWithValidEmail() {
        test = ReportManager.startTest("AUTH-FP-01: Reset password with valid email");

        try {
            authPage.goToLoginPage();
            ReportManager.logInfo("Navigated to login page");

            authPage.performForgotPassword(AuthenticationTestData.VALID_EMAIL);
            ReportManager.logInfo("Submitted forgot password request");

            ReportManager.logPass("Forgot password request processed");

        } catch (Exception e) {
            ReportManager.logFail("Test failed: " + e.getMessage());
            takeScreenshot("AUTH-FP-01_Failed");
            throw e;
        }
    }

    @Test(groups = {"authentication", "regression", "forgot-password"},
          description = "AUTH-FP-02: Reset password with non-existing email")
    public void testForgotPasswordWithNonExistingEmail() {
        test = ReportManager.startTest("AUTH-FP-02: Reset password with non-existing email");

        try {
            authPage.goToLoginPage();
            ReportManager.logInfo("Navigated to login page");

            authPage.performForgotPassword(AuthenticationTestData.FORGOT_NON_EXISTING_EMAIL_1);
            ReportManager.logInfo("Submitted forgot password with non-existing email");

            Assert.assertTrue(authPage.isErrorMessageDisplayed(), "Error message should be displayed");
            ReportManager.logPass("Non-existing email error displayed correctly");

        } catch (Exception e) {
            ReportManager.logFail("Test failed: " + e.getMessage());
            takeScreenshot("AUTH-FP-02_Failed");
            throw e;
        }
    }

    @Test(groups = {"authentication", "regression", "forgot-password"},
          description = "AUTH-FP-03: Reset password with invalid email format")
    public void testForgotPasswordWithInvalidEmailFormat() {
        test = ReportManager.startTest("AUTH-FP-03: Reset password with invalid email format");

        try {
            authPage.goToLoginPage();
            ReportManager.logInfo("Navigated to login page");

            authPage.performForgotPassword(AuthenticationTestData.FORGOT_INVALID_EMAIL_1);
            ReportManager.logInfo("Submitted forgot password with invalid email");

            Assert.assertTrue(authPage.isErrorMessageDisplayed(), "Validation error should be displayed");
            ReportManager.logPass("Invalid email format error displayed correctly");

        } catch (Exception e) {
            ReportManager.logFail("Test failed: " + e.getMessage());
            takeScreenshot("AUTH-FP-03_Failed");
            throw e;
        }
    }

    // ========== USER PROFILE GROUP ==========

    @Test(groups = {"userprofile", "smoke", "profile-view"},
          description = "UP-VW-01: View profile information when logged in")
    public void testViewProfileWhenLoggedIn() {
        test = ReportManager.startTest("UP-VW-01: View profile when logged in");

        try {
            // Login first
            loginBeforeTest();

            // Navigate to profile page
            profilePage.navigateToProfile();
            ReportManager.logInfo("Navigated to profile page");

            // Verify profile page loads
            Assert.assertTrue(profilePage.isProfilePageLoaded(), "Profile page should load successfully");
            ReportManager.logPass("Profile page loaded successfully");

        } catch (Exception e) {
            ReportManager.logFail("Test failed: " + e.getMessage());
            takeScreenshot("UP-VW-01_Failed");
            throw e;
        }
    }

    @Test(groups = {"userprofile", "regression", "profile-view"},
          description = "UP-VW-02: Redirect to login when not logged in")
    public void testRedirectToLoginWhenNotLoggedIn() {
        test = ReportManager.startTest("UP-VW-02: Redirect to login when not logged in");

        try {
            // Try to access profile without login
            driver.get("https://www.tncstore.vn/account/profile");
            ReportManager.logInfo("Attempted to access profile without login");

            // Should redirect to login or show login popup
            Assert.assertTrue(
                driver.getCurrentUrl() != null && driver.getCurrentUrl().contains("login") ||
                profilePage.isLoginPopupDisplayed(),
                "Should redirect to login or show login popup"
            );
            ReportManager.logPass("Correctly redirected to login when not authenticated");

        } catch (Exception e) {
            ReportManager.logFail("Test failed: " + e.getMessage());
            takeScreenshot("UP-VW-02_Failed");
            throw e;
        }
    }

    @Test(groups = {"userprofile", "regression", "profile-update"},
          description = "UP-UD-01: Update profile with valid information")
    public void testUpdateProfileWithValidData() {
        test = ReportManager.startTest("UP-UD-01: Update profile with valid data");

        try {
            // Login first
            loginBeforeTest();

            // Get test data
            String newName = "Updated Name";
            String newPhone = "0123456789";
            String newAddress = "123 Test Street, Ha Noi";

            // Update profile
            profilePage.updateProfile(newName, "", newPhone, newAddress);
            ReportManager.logInfo("Updated profile with new information");

            // Verify success message
            Assert.assertTrue(profilePage.isSuccessMessageDisplayed(), "Success message should be displayed");
            ReportManager.logPass("Profile updated successfully");

        } catch (Exception e) {
            ReportManager.logFail("Test failed: " + e.getMessage());
            takeScreenshot("UP-UD-01_Failed");
            throw e;
        }
    }

    @Test(groups = {"userprofile", "regression", "profile-update"},
          description = "UP-UD-02: Update with existing email")
    public void testUpdateWithExistingEmail() {
        test = ReportManager.startTest("UP-UD-02: Update with existing email");

        try {
            // Login first
            loginBeforeTest();

            // Try to update with existing email
            profilePage.updateProfile("", "existing@tncstore.vn", "", "");
            ReportManager.logInfo("Attempted to update with existing email");

            // Verify error message
            Assert.assertTrue(profilePage.isEmailExistsErrorDisplayed(), "Email exists error should be displayed");
            ReportManager.logPass("Correctly rejected existing email");

        } catch (Exception e) {
            ReportManager.logFail("Test failed: " + e.getMessage());
            takeScreenshot("UP-UD-02_Failed");
            throw e;
        }
    }

    @Test(groups = {"userprofile", "regression", "password-change"},
          description = "UP-PC-01: Change password with correct current password")
    public void testChangePasswordWithCorrectCurrentPassword() {
        test = ReportManager.startTest("UP-PC-01: Change password with correct current password");

        try {
            // Login first
            loginBeforeTest();

            // Navigate to profile
            profilePage.navigateToProfile();

            // Change password with valid data
            profilePage.changePassword("Abc12345", "NewPassword123", "NewPassword123");
            ReportManager.logInfo("Submitted password change with valid data");

            // Verify success
            Assert.assertTrue(profilePage.isSuccessMessageDisplayed(), "Success message should be displayed");
            ReportManager.logPass("Password changed successfully");

        } catch (Exception e) {
            ReportManager.logFail("Test failed: " + e.getMessage());
            takeScreenshot("UP-PC-01_Failed");
            throw e;
        }
    }

    @Test(groups = {"userprofile", "regression", "password-change"},
          description = "UP-PC-02: Change password with wrong current password")
    public void testChangePasswordWithWrongCurrentPassword() {
        test = ReportManager.startTest("UP-PC-02: Change password with wrong current password");

        try {
            // Login first
            loginBeforeTest();

            // Navigate to profile
            profilePage.navigateToProfile();

            // Try to change password with wrong current password
            profilePage.changePassword("WrongPassword", "NewPassword123", "NewPassword123");
            ReportManager.logInfo("Attempted password change with wrong current password");

            // Verify error message
            Assert.assertTrue(profilePage.isErrorMessageDisplayed(), "Error message should be displayed");
            ReportManager.logPass("Wrong current password properly rejected");

        } catch (Exception e) {
            ReportManager.logFail("Test failed: " + e.getMessage());
            takeScreenshot("UP-PC-02_Failed");
            throw e;
        }
    }

    // ========== HELPER METHODS ==========

    private void loginBeforeTest() {
        // Navigate to login and perform login
        driver.get("https://www.tncstore.vn/");

        // Use authentication page to login
        authPage.performLogin("john@test.com", "Abc12345");

        // Wait for login to complete
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Verify login success
        Assert.assertTrue(authPage.isLoginSuccessful(), "User should be logged in before tests");
    }
}
