package modules.userprofile;

import base.BaseTest;
import com.aventstack.extentreports.Status;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import utils.ExtentManager;
import data.ExcelReader;

public class UserProfileTest extends BaseTest {
    private UserProfilePage profilePage;

    @BeforeMethod
    public void setUpProfileTest() {
        profilePage = new UserProfilePage(driver);
    }

    // Profile view tests
    @Test(description = "UP-VW-01: View profile information when logged in")
    public void testViewProfileWhenLoggedIn() {
        test = ExtentManager.startTest("UP-VW-01: View profile when logged in");

        try {
            // Login first
            loginBeforeProfileTest();

            // Navigate to profile page
            profilePage.navigateToProfile();
            test.log(Status.INFO, "Navigated to profile page");

            // Verify profile page loads
            Assert.assertTrue(profilePage.isProfilePageLoaded(), "Profile page should load successfully");
            test.log(Status.PASS, "Profile page loaded successfully");

        } catch (Exception e) {
            test.log(Status.FAIL, "Test failed: " + e.getMessage());
            takeScreenshot("UP-VW-01_Failed");
            throw e;
        }
    }

    @Test(description = "UP-VW-02: Redirect to login when not logged in")
    public void testRedirectToLoginWhenNotLoggedIn() {
        test = ExtentManager.startTest("UP-VW-02: Redirect to login when not logged in");

        try {
            // Try to access profile without login
            driver.get("https://www.tncstore.vn/account/profile");
            test.log(Status.INFO, "Attempted to access profile without login");

            // Should redirect to login or show login popup
            Assert.assertTrue(
                driver.getCurrentUrl().contains("login") ||
                profilePage.isLoginPopupDisplayed(),
                "Should redirect to login or show login popup"
            );
            test.log(Status.PASS, "Correctly redirected to login when not authenticated");

        } catch (Exception e) {
            test.log(Status.FAIL, "Test failed: " + e.getMessage());
            takeScreenshot("UP-VW-02_Failed");
            throw e;
        }
    }

    // Profile update tests
    @Test(description = "UP-UD-01: Update profile with valid information")
    public void testUpdateProfileWithValidData() {
        test = ExtentManager.startTest("UP-UD-01: Update profile with valid data");

        try {
            // Login first
            loginBeforeProfileTest();

            // Get test data
            String newName = "Updated Name";
            String newPhone = "0123456789";
            String newAddress = "123 Test Street, Ha Noi";

            // Update profile
            profilePage.updateProfile(newName, "", newPhone, newAddress);
            test.log(Status.INFO, "Updated profile with new information");

            // Verify success message
            Assert.assertTrue(profilePage.isSuccessMessageDisplayed(), "Success message should be displayed");
            test.log(Status.PASS, "Profile updated successfully");

        } catch (Exception e) {
            test.log(Status.FAIL, "Test failed: " + e.getMessage());
            takeScreenshot("UP-UD-01_Failed");
            throw e;
        }
    }

    @Test(description = "UP-UD-02: Update with existing email")
    public void testUpdateWithExistingEmail() {
        test = ExtentManager.startTest("UP-UD-02: Update with existing email");

        try {
            // Login first
            loginBeforeProfileTest();

            // Try to update with existing email
            profilePage.updateProfile("", "existing@tncstore.vn", "", "");
            test.log(Status.INFO, "Attempted to update with existing email");

            // Verify error message
            Assert.assertTrue(profilePage.isEmailExistsErrorDisplayed(), "Email exists error should be displayed");
            test.log(Status.PASS, "Correctly rejected existing email");

        } catch (Exception e) {
            test.log(Status.FAIL, "Test failed: " + e.getMessage());
            takeScreenshot("UP-UD-02_Failed");
            throw e;
        }
    }

    @Test(description = "UP-UD-03: Update with invalid data")
    public void testUpdateWithInvalidData() {
        test = ExtentManager.startTest("UP-UD-03: Update with invalid data");

        try {
            // Login first
            loginBeforeProfileTest();

            // Try to update with invalid data
            String longName = "This is a very very long name that exceeds maximum allowed length";
            String invalidPhone = "abc123";

            profilePage.updateProfile(longName, "", invalidPhone, "");
            test.log(Status.INFO, "Attempted to update with invalid data");

            // Verify error message
            Assert.assertTrue(profilePage.isErrorMessageDisplayed(), "Error message should be displayed for invalid data");
            test.log(Status.PASS, "Invalid data properly rejected");

        } catch (Exception e) {
            test.log(Status.FAIL, "Test failed: " + e.getMessage());
            takeScreenshot("UP-UD-03_Failed");
            throw e;
        }
    }

    @Test(description = "UP-UD-04: Update with blank required fields")
    public void testUpdateWithBlankRequiredFields() {
        test = ExtentManager.startTest("UP-UD-04: Update with blank required fields");

        try {
            // Login first
            loginBeforeProfileTest();

            // Try to update with blank required fields
            profilePage.updateProfile("", "", "", "");
            test.log(Status.INFO, "Attempted to update with blank required fields");

            // Verify required field error
            Assert.assertTrue(profilePage.isRequiredFieldErrorDisplayed(), "Required field error should be displayed");
            test.log(Status.PASS, "Blank required fields properly rejected");

        } catch (Exception e) {
            test.log(Status.FAIL, "Test failed: " + e.getMessage());
            takeScreenshot("UP-UD-04_Failed");
            throw e;
        }
    }

    // Password change tests
    @Test(description = "UP-PC-01: Change password with correct current password")
    public void testChangePasswordWithCorrectCurrentPassword() {
        test = ExtentManager.startTest("UP-PC-01: Change password with correct current password");

        try {
            // Login first
            loginBeforeProfileTest();

            // Navigate to profile
            profilePage.navigateToProfile();

            // Change password with valid data
            profilePage.changePassword("Abc12345", "NewPassword123", "NewPassword123");
            test.log(Status.INFO, "Submitted password change with valid data");

            // Verify success
            Assert.assertTrue(profilePage.isSuccessMessageDisplayed(), "Success message should be displayed");
            test.log(Status.PASS, "Password changed successfully");

        } catch (Exception e) {
            test.log(Status.FAIL, "Test failed: " + e.getMessage());
            takeScreenshot("UP-PC-01_Failed");
            throw e;
        }
    }

    @Test(description = "UP-PC-02: Change password with wrong current password")
    public void testChangePasswordWithWrongCurrentPassword() {
        test = ExtentManager.startTest("UP-PC-02: Change password with wrong current password");

        try {
            // Login first
            loginBeforeProfileTest();

            // Navigate to profile
            profilePage.navigateToProfile();

            // Try to change password with wrong current password
            profilePage.changePassword("WrongPassword", "NewPassword123", "NewPassword123");
            test.log(Status.INFO, "Attempted password change with wrong current password");

            // Verify error message
            Assert.assertTrue(profilePage.isErrorMessageDisplayed(), "Error message should be displayed");
            test.log(Status.PASS, "Wrong current password properly rejected");

        } catch (Exception e) {
            test.log(Status.FAIL, "Test failed: " + e.getMessage());
            takeScreenshot("UP-PC-02_Failed");
            throw e;
        }
    }

    @Test(description = "UP-PC-03: Change password with weak new password")
    public void testChangePasswordWithWeakNewPassword() {
        test = ExtentManager.startTest("UP-PC-03: Change password with weak new password");

        try {
            // Login first
            loginBeforeProfileTest();

            // Navigate to profile
            profilePage.navigateToProfile();

            // Try to change to weak password
            profilePage.changePassword("Abc12345", "123", "123");
            test.log(Status.INFO, "Attempted to change to weak password");

            // Verify error message
            Assert.assertTrue(profilePage.isErrorMessageDisplayed(), "Error message should be displayed for weak password");
            test.log(Status.PASS, "Weak password properly rejected");

        } catch (Exception e) {
            test.log(Status.FAIL, "Test failed: " + e.getMessage());
            takeScreenshot("UP-PC-03_Failed");
            throw e;
        }
    }

    // Helper method to login before profile tests
    private void loginBeforeProfileTest() {
        // Navigate to login and perform login
        driver.get("https://www.tncstore.vn/");

        // Use authentication page to login
        AuthenticationPage authPage = new AuthenticationPage(driver);
        authPage.performLogin("john@test.com", "Abc12345");

        // Wait for login to complete
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Verify login success
        Assert.assertTrue(profilePage.isUserLoggedIn(), "User should be logged in before profile tests");
    }
}
