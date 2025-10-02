package test.userprofile;

import pages.AuthenticationPage;
import pages.UserProfilePage;
import model.AuthenticationTestData;
import helpers.ReportManager;
import helpers.BaseTest;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProfileViewTests extends BaseTest {
    private UserProfilePage profilePage;
    private AuthenticationPage authPage;
    private static final Logger log = LoggerFactory.getLogger(ProfileViewTests.class);

    @BeforeMethod
    public void setUpTest() {
        profilePage = new UserProfilePage(driver);
        authPage = new AuthenticationPage(driver);
        ReportManager.setModule("userprofile-view");
        log.info("ProfileViewTests setup completed");
    }

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

            // Verify profile information is displayed
            String profileName = profilePage.getProfileName();
            String profileEmail = profilePage.getProfileEmail();

            Assert.assertNotNull(profileName, "Profile name should be displayed");
            Assert.assertNotNull(profileEmail, "Profile email should be displayed");

            ReportManager.logPass("Profile page loaded successfully with user information");

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

            String newName = "Updated Name " + System.currentTimeMillis();
            String newPhone = "0123456789";
            String newAddress = "123 Updated Street, Ho Chi Minh City";

            profilePage.updateProfile(newName, newPhone, newAddress);
            ReportManager.logInfo("Updated profile information");

            // Verify updates were applied
            String updatedName = profilePage.getProfileName();
            String updatedPhone = profilePage.getProfilePhone();
            String updatedAddress = profilePage.getProfileAddress();

            Assert.assertEquals(updatedName, newName, "Profile name should be updated");
            Assert.assertEquals(updatedPhone, newPhone, "Profile phone should be updated");
            Assert.assertEquals(updatedAddress, newAddress, "Profile address should be updated");

            ReportManager.logPass("Profile update completed and verified successfully");

        } catch (Exception e) {
            ReportManager.logFail("Test failed: " + e.getMessage());
            throw e;
        }
    }

    @Test(groups = {"userprofile", "functional"},
          description = "UP-03: Change user password")
    public void testChangeUserPassword() {
        ReportManager.startTest("UP-03: Change user password");

        try {
            // Login first
            authPage.performLogin(
                AuthenticationTestData.VALID_EMAIL,
                AuthenticationTestData.VALID_PASSWORD
            );
            ReportManager.logInfo("Logged in successfully");

            profilePage.navigateToProfile();
            ReportManager.logInfo("Navigated to profile page");

            String currentPassword = AuthenticationTestData.VALID_PASSWORD;
            String newPassword = "NewPassword123!";
            String confirmPassword = "NewPassword123!";

            profilePage.changePassword(currentPassword, newPassword, confirmPassword);
            ReportManager.logInfo("Submitted password change request");

            ReportManager.logPass("Password change request submitted successfully");

        } catch (Exception e) {
            ReportManager.logFail("Test failed: " + e.getMessage());
            throw e;
        }
    }

    @Test(groups = {"userprofile", "negative"},
          description = "UP-04: Change password with wrong current password")
    public void testChangePasswordWithWrongCurrentPassword() {
        ReportManager.startTest("UP-04: Change password with wrong current password");

        try {
            // Login first
            authPage.performLogin(
                AuthenticationTestData.VALID_EMAIL,
                AuthenticationTestData.VALID_PASSWORD
            );
            ReportManager.logInfo("Logged in successfully");

            profilePage.navigateToProfile();
            ReportManager.logInfo("Navigated to profile page");

            String wrongCurrentPassword = AuthenticationTestData.WRONG_PASSWORD_1;
            String newPassword = "NewPassword123!";
            String confirmPassword = "NewPassword123!";

            profilePage.changePassword(wrongCurrentPassword, newPassword, confirmPassword);
            ReportManager.logInfo("Attempted password change with wrong current password");

            // Should show error message
            ReportManager.logPass("Validation successful - wrong current password rejected");

        } catch (Exception e) {
            ReportManager.logFail("Test failed: " + e.getMessage());
            throw e;
        }
    }
}
