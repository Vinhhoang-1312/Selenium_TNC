package tests;

import core.BaseTest;
import io.qameta.allure.*;
import listeners.BaseListener;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.LoginPage;
import pages.UserProfilePage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Listeners({BaseListener.class})
@Epic("User Management")
@Feature("User Profile")
public class UserProfileTests extends BaseTest {
    private static final Logger log = LoggerFactory.getLogger(UserProfileTests.class);

    @Test(groups = {"userprofile", "smoke"},
            description = "USER-01: Update phone number successfully")
    @Story("Profile Update")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify that user can successfully update their phone number in profile")
    public void testUpdatePhoneSuccess() {
        Allure.step("Login to user account");
        LoginPage loginPage = new LoginPage(getDriver());
        loginPage.performLogin("john5@test.com", "Abc12345");
        customWait(2000);

        UserProfilePage profilePage = new UserProfilePage(getDriver());
        String newPhone = "0912345678";

        Allure.step("Update phone number to: " + newPhone);
        profilePage.updatePhone(newPhone);

        Allure.step("Refresh page and verify phone number persists");
        getDriver().navigate().refresh();
        customWait(1500);

        String actualPhone = profilePage.getPhone();
        Allure.parameter("Expected Phone", newPhone);
        Allure.parameter("Actual Phone", actualPhone);
        Assert.assertEquals(actualPhone, newPhone, "Phone number should be updated successfully");
        log.info("Phone updated successfully to: {}", newPhone);
    }

    @Test(groups = {"userprofile"},
            description = "USER-02: Update fullname successfully")
    @Story("Profile Update")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify that user can successfully update their full name in profile")
    public void testUpdateFullname() {
        log.info("[TEST] Starting testUpdateFullname");

        Allure.step("Login to user account");
        LoginPage loginPage = new LoginPage(getDriver());
        loginPage.performLogin("john5@test.com", "Abc12345");
        customWait(2000);

        UserProfilePage profilePage = new UserProfilePage(getDriver());
        try {
            Allure.step("Get current fullname and update it");
            String oldValue = profilePage.getFullname();
            String newValue = oldValue + "_Test";
            profilePage.updateFullname(newValue);

            Allure.step("Refresh page and verify fullname persists");
            getDriver().navigate().refresh();
            customWait(1500);

            String actualValue = profilePage.getFullname();
            Allure.parameter("Expected Fullname", newValue);
            Allure.parameter("Actual Fullname", actualValue);
            Assert.assertEquals(actualValue, newValue, "Fullname should be updated successfully");
            log.info("Fullname updated successfully to: {}", newValue);
        } catch (Exception e) {
            log.error("[TEST] Error updating fullname: {}", e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    @Test(groups = {"userprofile"},
            description = "USER-03: Update address successfully")
    @Story("Profile Update")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify that user can successfully update their address in profile")
    public void testUpdateAddress() {
        log.info("[TEST] Starting testUpdateAddress");

        Allure.step("Login to user account");
        LoginPage loginPage = new LoginPage(getDriver());
        loginPage.performLogin("john5@test.com", "Abc12345");
        customWait(2000);

        UserProfilePage profilePage = new UserProfilePage(getDriver());
        String newAddress = "123 Test Street, Hanoi";

        Allure.step("Update address to: " + newAddress);
        profilePage.updateAddress(newAddress);

        Allure.step("Refresh page and verify address persists");
        getDriver().navigate().refresh();
        customWait(1500);

        String actualAddress = profilePage.getAddress();
        Allure.parameter("Expected Address", newAddress);
        Allure.parameter("Actual Address", actualAddress);
        Assert.assertEquals(actualAddress, newAddress, "Address should be updated successfully");
        log.info("Address updated successfully to: {}", newAddress);
    }

    @Test(groups = {"userprofile"},
            description = "USER-04: Verify profile page loads successfully")
    @Story("Profile Navigation")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify that user can access and view their profile page")
    public void testProfilePageLoads() {
        Allure.step("Login to user account");
        LoginPage loginPage = new LoginPage(getDriver());
        loginPage.performLogin("john5@test.com", "Abc12345");
        customWait(2000);

        Allure.step("Navigate to profile page");
        UserProfilePage profilePage = new UserProfilePage(getDriver());
        profilePage.navigateToProfile();

        Allure.step("Verify profile page loaded successfully");
        Assert.assertTrue(profilePage.isProfilePageLoaded(),
            "Profile page should load successfully");
        log.info("Profile page loaded successfully");
    }
}
