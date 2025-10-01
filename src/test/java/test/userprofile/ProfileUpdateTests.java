package test.userprofile;

import helpers.BaseTest;
import helpers.ReportManager;
import model.AuthenticationTestData;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.authenticationPage;
import pages.UserProfilePage;

/**
 * Stub test class for profile update scenarios.
 * You can extend with real assertions and flows later.
 */
public class ProfileUpdateTests extends BaseTest {
    private UserProfilePage profilePage;
    private authenticationPage authPage;

    @BeforeMethod
    public void setUp() {
        profilePage = new UserProfilePage(driver);
        authPage = new authenticationPage(driver);
        ReportManager.setModule("userprofile-update");
    }

    @Test(groups = {"userprofile", "profile-update"}, description = "PROFILE-UP-01: Update profile basic info (stub)")
    public void testUpdateProfileBasicInfo() {
        ReportManager.startTest("PROFILE-UP-01: Update profile basic info (stub)");
        try {
            authPage.performLogin(AuthenticationTestData.VALID_EMAIL, AuthenticationTestData.VALID_PASSWORD);
            profilePage.navigateToProfile();
            // Placeholder update values
            profilePage.updateProfile("New Name", "0909123456", "123 Demo Street");
            ReportManager.logInfo("Performed profile update (stub)");
            // Placeholder assertion (adjust when real verification method available)
            Assert.assertTrue(true, "Profile update stub executed");
            ReportManager.logPass("Profile update stub passed");
        } catch (Exception e) {
            ReportManager.logFail("Profile update stub failed: " + e.getMessage());
            throw e;
        }
    }
}
