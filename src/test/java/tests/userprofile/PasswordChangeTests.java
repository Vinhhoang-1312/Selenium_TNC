package tests.userprofile;

import base.BaseTest;
import pages.AuthenticationPage;
import pages.UserProfilePage;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import utils.ReportManager;

public class PasswordChangeTests extends BaseTest {
    private AuthenticationPage authPage;
    private UserProfilePage profilePage;

    @BeforeMethod
    public void setUpTest() {
        super.setUp("chrome");
        authPage = new AuthenticationPage(driver);
        profilePage = new UserProfilePage(driver);
        ReportManager.setModule("userprofile-password");
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
