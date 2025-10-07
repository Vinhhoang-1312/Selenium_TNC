package tests;

import core.BaseTest;
import listeners.BaseListener;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.LoginPage;
import pages.UserProfilePage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Listeners({BaseListener.class})
public class UserProfileTests extends BaseTest {
    private static final Logger log = LoggerFactory.getLogger(UserProfileTests.class);

    @Test(groups = {"userprofile", "smoke"},
            description = "USER-01: Update phone number successfully")
    public void testUpdatePhoneSuccess() {
        // Login first
        LoginPage loginPage = new LoginPage(getDriver());
        loginPage.performLogin("john5@test.com", "Abc12345");
        customWait(2000);

        UserProfilePage profilePage = new UserProfilePage(getDriver());
        String newPhone = "0912345678";
        profilePage.updatePhone(newPhone);

        getDriver().navigate().refresh();
        customWait(1500);

        String actualPhone = profilePage.getPhone();
        Assert.assertEquals(actualPhone, newPhone, "Phone number should be updated successfully");
        log.info("Phone updated successfully to: {}", newPhone);
    }

    @Test(groups = {"userprofile"},
            description = "USER-02: Update fullname successfully")
    public void testUpdateFullname() {
        log.info("[TEST] Starting testUpdateFullname");

        // Login first
        LoginPage loginPage = new LoginPage(getDriver());
        loginPage.performLogin("john5@test.com", "Abc12345");
        customWait(2000);

        UserProfilePage profilePage = new UserProfilePage(getDriver());
        try {
            String oldValue = profilePage.getFullname();
            String newValue = oldValue + "_Test";
            profilePage.updateFullname(newValue);

            getDriver().navigate().refresh();
            customWait(1500);

            String actualValue = profilePage.getFullname();
            Assert.assertEquals(actualValue, newValue, "Fullname should be updated successfully");
            log.info("Fullname updated successfully to: {}", newValue);
        } catch (Exception e) {
            log.error("[TEST] Error updating fullname: {}", e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    @Test(groups = {"userprofile"},
            description = "USER-03: Update address successfully")
    public void testUpdateAddress() {
        log.info("[TEST] Starting testUpdateAddress");

        // Login first
        LoginPage loginPage = new LoginPage(getDriver());
        loginPage.performLogin("john5@test.com", "Abc12345");
        customWait(2000);

        UserProfilePage profilePage = new UserProfilePage(getDriver());
        String newAddress = "123 Test Street, Hanoi";
        profilePage.updateAddress(newAddress);

        getDriver().navigate().refresh();
        customWait(1500);

        String actualAddress = profilePage.getAddress();
        Assert.assertEquals(actualAddress, newAddress, "Address should be updated successfully");
        log.info("Address updated successfully to: {}", newAddress);
    }

    @Test(groups = {"userprofile"},
            description = "USER-04: Verify profile page loads successfully")
    public void testProfilePageLoads() {
        LoginPage loginPage = new LoginPage(getDriver());
        loginPage.performLogin("john5@test.com", "Abc12345");
        customWait(2000);

        UserProfilePage profilePage = new UserProfilePage(getDriver());
        profilePage.navigateToProfile();

        Assert.assertTrue(profilePage.isProfilePageLoaded(),
            "Profile page should load successfully");
        log.info("Profile page loaded successfully");
    }
}
