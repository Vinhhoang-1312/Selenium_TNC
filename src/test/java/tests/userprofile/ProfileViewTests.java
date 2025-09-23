package tests.userprofile;

import base.BaseTest;
import pages.AuthenticationPage;
import pages.UserProfilePage;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import utils.ReportManager;

public class ProfileViewTests extends BaseTest {
    private AuthenticationPage authPage;
    private UserProfilePage profilePage;

    @BeforeMethod
    public void setUpTest() {
        super.setUp("chrome");
        authPage = new AuthenticationPage(driver);
        profilePage = new UserProfilePage(driver);
        ReportManager.setModule("userprofile-view");
    }

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
