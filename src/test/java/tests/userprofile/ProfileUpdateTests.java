package tests.userprofile;

import base.BaseTest;
import pages.AuthenticationPage;
import pages.UserProfilePage;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import utils.ReportManager;

public class ProfileUpdateTests extends BaseTest {
    private AuthenticationPage authPage;
    private UserProfilePage profilePage;

    @BeforeMethod
    public void setUpTest() {
        // BaseTest tự động khởi tạo driver, chỉ cần tạo page objects
        authPage = new AuthenticationPage(driver);
        profilePage = new UserProfilePage(driver);
        ReportManager.setModule("userprofile-update");
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

    // Helper method for login
    private void loginBeforeTest() {
        driver.get("https://www.tncstore.vn/");
        authPage.performLogin("john@test.com", "Abc12345");

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        Assert.assertTrue(authPage.isLoginSuccessful(), "User should be logged in before tests");
    }
}
