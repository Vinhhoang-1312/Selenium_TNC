package tests.authentication;

import base.BaseTest;
import pages.AuthenticationPage;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import utils.ReportManager;
import data.AuthenticationTestData;

public class RegisterTests extends BaseTest {
    private AuthenticationPage authPage;

    @BeforeMethod
    public void setUpTest() {
        // Gọi parent setup trước để đảm bảo driver được khởi tạo
        super.setUp();

        // Kiểm tra driver
        if (driver == null) {
            throw new RuntimeException("Driver not initialized by BaseTest");
        }

        // Khởi tạo page objects
        authPage = new AuthenticationPage(driver);
        ReportManager.setModule("authentication-register");

        System.out.println("✅ RegisterTests setup completed");
    }

    @Test(groups = {"authentication", "smoke", "signup"},
          description = "AUTH-SU-01: Register with valid name, email, and password")
    public void testRegisterWithValidData() {
        test = ReportManager.startTest("AUTH-SU-01: Register with valid data");

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
            takeScreenshot("AUTH-SU-01_Failed");
            throw e;
        }
    }

    @Test(groups = {"authentication", "regression", "signup"},
          description = "AUTH-SU-02: Register with existing email")
    public void testRegisterWithExistingEmail() {
        test = ReportManager.startTest("AUTH-SU-02: Register with existing email");

        try {
            authPage.goToRegisterPage();
            ReportManager.logInfo("Navigated to register page");

            authPage.performRegistration(
                AuthenticationTestData.VALID_NAME,
                AuthenticationTestData.EXISTING_EMAIL,
                AuthenticationTestData.VALID_PASSWORD
            );
            ReportManager.logInfo("Attempted registration with existing email");

            Assert.assertTrue(authPage.isErrorMessageDisplayed(), "Error message should be displayed for existing email");
            ReportManager.logPass("Existing email properly rejected");

        } catch (Exception e) {
            ReportManager.logFail("Test failed: " + e.getMessage());
            takeScreenshot("AUTH-SU-02_Failed");
            throw e;
        }
    }

    @Test(groups = {"authentication", "regression", "signup"},
          description = "AUTH-SU-03: Register with invalid email format")
    public void testRegisterWithInvalidEmail() {
        test = ReportManager.startTest("AUTH-SU-03: Register with invalid email format");

        try {
            authPage.goToRegisterPage();
            ReportManager.logInfo("Navigated to register page");

            authPage.performRegistration(
                AuthenticationTestData.VALID_NAME,
                AuthenticationTestData.INVALID_EMAIL_1,
                AuthenticationTestData.VALID_PASSWORD
            );
            ReportManager.logInfo("Filled registration form with invalid email");

            Assert.assertTrue(authPage.isInvalidEmailErrorDisplayed(), "Invalid email error should be displayed");
            ReportManager.logPass("Invalid email error displayed correctly");

        } catch (Exception e) {
            ReportManager.logFail("Test failed: " + e.getMessage());
            takeScreenshot("AUTH-SU-03_Failed");
            throw e;
        }
    }

    @Test(groups = {"authentication", "regression", "signup"},
          description = "AUTH-SU-04: Register with blank mandatory fields")
    public void testRegisterWithBlankFields() {
        test = ReportManager.startTest("AUTH-SU-04: Register with blank mandatory fields");

        try {
            authPage.goToRegisterPage();
            ReportManager.logInfo("Navigated to register page");

            authPage.performRegistration("", "", "");
            ReportManager.logInfo("Submitted form with blank fields");

            Assert.assertTrue(authPage.isRequiredFieldErrorDisplayed(), "Required field error should be displayed");
            ReportManager.logPass("Required field error displayed correctly");

        } catch (Exception e) {
            ReportManager.logFail("Test failed: " + e.getMessage());
            takeScreenshot("AUTH-SU-04_Failed");
            throw e;
        }
    }

    @Test(groups = {"authentication", "regression", "signup"},
          description = "AUTH-SU-05: Register with weak/short password")
    public void testRegisterWithWeakPassword() {
        test = ReportManager.startTest("AUTH-SU-05: Register with weak/short password");

        try {
            authPage.goToRegisterPage();
            ReportManager.logInfo("Navigated to register page");

            authPage.performRegistration(
                AuthenticationTestData.VALID_NAME,
                AuthenticationTestData.VALID_EMAIL_2,
                AuthenticationTestData.WEAK_PASSWORD_1
            );
            ReportManager.logInfo("Filled registration form with weak password");

            Assert.assertTrue(authPage.isErrorMessageDisplayed(), "Password error should be displayed");
            ReportManager.logPass("Weak password rejected correctly");

        } catch (Exception e) {
            ReportManager.logFail("Test failed: " + e.getMessage());
            takeScreenshot("AUTH-SU-05_Failed");
            throw e;
        }
    }
}
