package test;

import pages.AuthenticationPage;
import pages.UserProfilePage;
import model.AuthenticationTestData;
import helpers.ReportManager;
import helpers.BaseTest;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class TNCStoreTests extends BaseTest {
    private AuthenticationPage authPage;
    private UserProfilePage profilePage;

    @BeforeMethod
    public void initPages() {
        authPage = new AuthenticationPage(driver);
        profilePage = new UserProfilePage(driver);
    }

    // ========== AUTHENTICATION GROUP ==========

    @Test(groups = {"authentication", "smoke", "signup"},
          description = "AUTH-SU-01: Register with valid name, email, and password")
    public void testRegisterWithValidData() {
        ReportManager.startTest("AUTH-SU-01: Register with valid data");

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
            // takeScreenshot("AUTH-SU-01_Failed"); // This would be implemented in BaseTest
            throw e;
        }
    }

    @Test(groups = {"authentication", "negative", "signup"},
          description = "AUTH-SU-02: Register with existing email")
    public void testRegisterWithExistingEmail() {
        ReportManager.startTest("AUTH-SU-02: Register with existing email");

        try {
            authPage.goToRegisterPage();
            ReportManager.logInfo("Navigated to register page");

            authPage.performRegistration(
                AuthenticationTestData.VALID_NAME_2,
                AuthenticationTestData.EXISTING_EMAIL,
                AuthenticationTestData.VALID_PASSWORD_2
            );
            ReportManager.logInfo("Attempted registration with existing email");

            Assert.assertTrue(authPage.isErrorMessageDisplayed(), "Error message should be displayed");
            String errorMessage = authPage.getErrorMessage();
            Assert.assertTrue(errorMessage.contains("Email đã được sử dụng"),
                "Error message should indicate email already exists");
            ReportManager.logPass("Validation successful - existing email rejected");

        } catch (Exception e) {
            ReportManager.logFail("Test failed: " + e.getMessage());
            throw e;
        }
    }

    @Test(groups = {"authentication", "negative", "signup"},
          description = "AUTH-SU-03: Register with invalid email format")
    public void testRegisterWithInvalidEmail() {
        ReportManager.startTest("AUTH-SU-03: Register with invalid email format");

        try {
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

    // ========== LOGIN GROUP ==========

    @Test(groups = {"authentication", "smoke", "login"},
          description = "AUTH-LI-01: Login with valid credentials")
    public void testLoginWithValidCredentials() {
        ReportManager.startTest("AUTH-LI-01: Login with valid credentials");

        try {
            authPage.performLogin(
                AuthenticationTestData.VALID_EMAIL,
                AuthenticationTestData.VALID_PASSWORD
            );
            ReportManager.logInfo("Attempted login with valid credentials");

            Assert.assertTrue(authPage.isLoginSuccessful(), "Login should be successful with valid credentials");
            ReportManager.logPass("Login successful");

        } catch (Exception e) {
            ReportManager.logFail("Test failed: " + e.getMessage());
            throw e;
        }
    }

    @Test(groups = {"authentication", "negative", "login"},
          description = "AUTH-LI-02: Login with invalid credentials")
    public void testLoginWithInvalidCredentials() {
        ReportManager.startTest("AUTH-LI-02: Login with invalid credentials");

        try {
            authPage.performLogin(
                AuthenticationTestData.NON_EXISTING_EMAIL_1,
                AuthenticationTestData.WRONG_PASSWORD_1
            );
            ReportManager.logInfo("Attempted login with invalid credentials");

            Assert.assertFalse(authPage.isLoginSuccessful(), "Login should fail with invalid credentials");
            Assert.assertTrue(authPage.isErrorMessageDisplayed(), "Error message should be displayed");
            ReportManager.logPass("Validation successful - invalid credentials rejected");

        } catch (Exception e) {
            ReportManager.logFail("Test failed: " + e.getMessage());
            throw e;
        }
    }

    // ========== USER PROFILE GROUP ==========

    @Test(groups = {"userprofile", "smoke"},
          description = "UP-01: View user profile information")
    public void testViewUserProfile() {
        ReportManager.startTest("UP-01: View user profile information");

        try {
            // Login first
            authPage.performLogin(
                AuthenticationTestData.VALID_EMAIL,
                AuthenticationTestData.VALID_PASSWORD
            );
            ReportManager.logInfo("Logged in successfully");

            profilePage.navigateToProfile();
            ReportManager.logInfo("Navigated to profile page");

            Assert.assertTrue(profilePage.isProfilePageLoaded(), "Profile page should be loaded");
            ReportManager.logPass("Profile page loaded successfully");

        } catch (Exception e) {
            ReportManager.logFail("Test failed: " + e.getMessage());
            throw e;
        }
    }

    @Test(groups = {"userprofile", "functional"},
          description = "UP-02: Update user profile information")
    public void testUpdateUserProfile() {
        ReportManager.startTest("UP-02: Update user profile information");

        try {
            // Login first
            authPage.performLogin(
                AuthenticationTestData.VALID_EMAIL,
                AuthenticationTestData.VALID_PASSWORD
            );
            ReportManager.logInfo("Logged in successfully");

            profilePage.navigateToProfile();
            ReportManager.logInfo("Navigated to profile page");

            String newName = "Updated Name";
            String newPhone = "0123456789";
            String newAddress = "123 Updated Street";

            profilePage.updateProfile(newName, newPhone, newAddress);
            ReportManager.logInfo("Updated profile information");

            ReportManager.logPass("Profile update completed");

        } catch (Exception e) {
            ReportManager.logFail("Test failed: " + e.getMessage());
            throw e;
        }
    }
}
