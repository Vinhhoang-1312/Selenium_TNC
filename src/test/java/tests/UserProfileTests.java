package tests;

import core.BaseTest;
import io.qameta.allure.*;
import listeners.BaseListener;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.LoginPage;
import pages.UserProfilePage;
import utils.Reporter;
import utils.TestDataProviders;

@Listeners({BaseListener.class})
@Epic("User Management")
@Feature("User Profile")
public class UserProfileTests extends BaseTest {

    @Test(groups = {"userprofile", "smoke"},
            description = "USER-01: Update phone number successfully",
            dataProvider = "validLoginData", dataProviderClass = TestDataProviders.class)
    @Story("Profile Update")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify that user can successfully update their phone number in profile")
    public void testUpdatePhoneSuccess(Object[] validRow) {
        LoginPage loginPage = new LoginPage(getDriver());
        UserProfilePage profilePage = new UserProfilePage(getDriver());

        String email = (String) validRow[1];
        String password = (String) validRow[2];

        Reporter.LogToReport("Step 1: Login with valid credentials");
        loginPage.performLogin(email, password);

        Reporter.LogToReport("Step 2: Navigate to profile page");
        profilePage.navigateToProfile();

        String newPhone = "0912777777";
        Reporter.LogToReport("Step 3: Update phone number to " + newPhone);
        profilePage.updatePhone(newPhone);

        Reporter.LogToReport("Step 4: Refresh and verify phone");
        getDriver().navigate().refresh();
        String actualPhone = profilePage.getPhone();
        getSoftAssert().assertEquals(actualPhone, newPhone, "Phone number should be updated successfully");

        logger.info("Phone updated successfully to: {}", newPhone);
    }

    @Test(groups = {"userprofile"},
            description = "USER-02: Update fullname successfully",
            dataProvider = "validLoginData", dataProviderClass = TestDataProviders.class)
    @Story("Profile Update")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify that user can successfully update their full name in profile")
    public void testUpdateFullname(Object[] validRow) {
        LoginPage loginPage = new LoginPage(getDriver());
        UserProfilePage profilePage = new UserProfilePage(getDriver());

        String email = (String) validRow[1];
        String password = (String) validRow[2];

        Reporter.LogToReport("Step 1: Login with valid credentials");
        loginPage.performLogin(email, password);

        Reporter.LogToReport("Step 2: Navigate to profile page");
        profilePage.navigateToProfile();

        Reporter.LogToReport("Step 3: Update fullname");
        String oldValue = profilePage.getFullname();
        String newValue = oldValue + "_Test";
        profilePage.updateFullname(newValue);

        getDriver().navigate().refresh();

        String actualValue = profilePage.getFullname();
        getSoftAssert().assertEquals(actualValue, newValue, "Fullname should be updated successfully");
        logger.info("Fullname updated successfully to: {}", newValue);
    }

    @Test(groups = {"userprofile"},
            description = "USER-03: Update address successfully",
            dataProvider = "validLoginData", dataProviderClass = TestDataProviders.class)
    @Story("Profile Update")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify that user can successfully update their address in profile")
    public void testUpdateAddress(Object[] validRow) {
        LoginPage loginPage = new LoginPage(getDriver());
        UserProfilePage profilePage = new UserProfilePage(getDriver());

        String email = (String) validRow[1];
        String password = (String) validRow[2];

        Reporter.LogToReport("Step 1: Login with valid credentials");
        loginPage.performLogin(email, password);

        Reporter.LogToReport("Step 2: Navigate to profile page");
        profilePage.navigateToProfile();

        String newAddress = "123 Test Street, Hanoi";
        Reporter.LogToReport("Step 3: Update address to " + newAddress);
        profilePage.updateAddress(newAddress);

        Reporter.LogToReport("Step 4: Refresh and verify address");
        getDriver().navigate().refresh();
        String actualAddress = profilePage.getAddress();
        getSoftAssert().assertEquals(actualAddress, newAddress, "Address should be updated successfully");

        logger.info("Address updated successfully to: {}", newAddress);
    }

    @Test(groups = {"userprofile"},
            description = "USER-04: Verify profile page loads successfully",
            dataProvider = "validLoginData", dataProviderClass = TestDataProviders.class)
    @Story("Profile Navigation")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify that user can access and view their profile page")
    public void testProfilePageLoads(Object[] validRow) {
        LoginPage loginPage = new LoginPage(getDriver());
        UserProfilePage profilePage = new UserProfilePage(getDriver());

        String email = (String) validRow[1];
        String password = (String) validRow[2];

        Reporter.LogToReport("Step 1: Login with valid credentials");
        loginPage.performLogin(email, password);

        Reporter.LogToReport("Step 2: Navigate to profile page");
        profilePage.navigateToProfile();

        Reporter.LogToReport("Step 3: Verify profile page loaded");
        getSoftAssert().assertTrue(profilePage.isProfilePageLoaded(),
            "Profile page should load successfully");

        logger.info("Profile page loaded successfully");
    }
}
