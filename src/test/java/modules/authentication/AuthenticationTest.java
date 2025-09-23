package modules.authentication;

import base.BaseTest;
import com.aventstack.extentreports.Status;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import utils.ExtentManager;
import data.AuthenticationTestData;

public class AuthenticationTest extends BaseTest {
    private AuthenticationPage authPage;

    @BeforeMethod
    public void setUpAuthTest() {
        authPage = new AuthenticationPage(driver);
    }

    // Sign up tests
    @Test(description = "AUTH-SU-01: Register with valid name, email, and password")
    public void testRegisterWithValidData() {
        test = ExtentManager.startTest("AUTH-SU-01: Register with valid data");

        try {
            // Use test data from AuthenticationTestData
            authPage.goToRegisterPage();
            test.log(Status.INFO, "Navigated to register page");

            authPage.performRegistration(
                AuthenticationTestData.VALID_NAME,
                AuthenticationTestData.VALID_EMAIL,
                AuthenticationTestData.VALID_PASSWORD
            );
            test.log(Status.INFO, "Filled registration form with valid data");

            Assert.assertTrue(authPage.isLoginSuccessful(), "User should be logged in after successful registration");
            test.log(Status.PASS, "Registration successful");

        } catch (Exception e) {
            test.log(Status.FAIL, "Test failed: " + e.getMessage());
            takeScreenshot("AUTH-SU-01_Failed");
            throw e;
        }
    }

    @Test(description = "AUTH-SU-02: Register with existing email")
    public void testRegisterWithExistingEmail() {
        test = ExtentManager.startTest("AUTH-SU-02: Register with existing email");

        try {
            authPage.goToRegisterPage();
            test.log(Status.INFO, "Navigated to register page");

            authPage.performRegistration(
                AuthenticationTestData.VALID_NAME,
                AuthenticationTestData.EXISTING_EMAIL,
                AuthenticationTestData.VALID_PASSWORD
            );
            test.log(Status.INFO, "Filled registration form with existing email");

            Assert.assertTrue(authPage.isEmailExistsErrorDisplayed(), "Email exists error should be displayed");
            test.log(Status.PASS, "Email exists error displayed correctly");

        } catch (Exception e) {
            test.log(Status.FAIL, "Test failed: " + e.getMessage());
            takeScreenshot("AUTH-SU-02_Failed");
            throw e;
        }
    }

    @Test(description = "AUTH-SU-03: Register with invalid email format")
    public void testRegisterWithInvalidEmail() {
        test = ExtentManager.startTest("AUTH-SU-03: Register with invalid email format");

        try {
            authPage.goToRegisterPage();
            test.log(Status.INFO, "Navigated to register page");

            authPage.performRegistration(
                AuthenticationTestData.VALID_NAME,
                AuthenticationTestData.INVALID_EMAIL_1,
                AuthenticationTestData.VALID_PASSWORD
            );
            test.log(Status.INFO, "Filled registration form with invalid email");

            Assert.assertTrue(authPage.isInvalidEmailErrorDisplayed(), "Invalid email error should be displayed");
            test.log(Status.PASS, "Invalid email error displayed correctly");

        } catch (Exception e) {
            test.log(Status.FAIL, "Test failed: " + e.getMessage());
            takeScreenshot("AUTH-SU-03_Failed");
            throw e;
        }
    }

    @Test(description = "AUTH-SU-04: Register with blank mandatory fields")
    public void testRegisterWithBlankFields() {
        test = ExtentManager.startTest("AUTH-SU-04: Register with blank mandatory fields");

        try {
            authPage.goToRegisterPage();
            test.log(Status.INFO, "Navigated to register page");

            authPage.performRegistration("", "", "");
            test.log(Status.INFO, "Submitted form with blank fields");

            Assert.assertTrue(authPage.isRequiredFieldErrorDisplayed(), "Required field error should be displayed");
            test.log(Status.PASS, "Required field error displayed correctly");

        } catch (Exception e) {
            test.log(Status.FAIL, "Test failed: " + e.getMessage());
            takeScreenshot("AUTH-SU-04_Failed");
            throw e;
        }
    }

    @Test(description = "AUTH-SU-05: Register with weak/short password")
    public void testRegisterWithWeakPassword() {
        test = ExtentManager.startTest("AUTH-SU-05: Register with weak/short password");

        try {
            authPage.goToRegisterPage();
            test.log(Status.INFO, "Navigated to register page");

            authPage.performRegistration(
                AuthenticationTestData.VALID_NAME,
                AuthenticationTestData.VALID_EMAIL_2,
                AuthenticationTestData.WEAK_PASSWORD_1
            );
            test.log(Status.INFO, "Filled registration form with weak password");

            Assert.assertTrue(authPage.isErrorMessageDisplayed(), "Password error should be displayed");
            test.log(Status.PASS, "Weak password rejected correctly");

        } catch (Exception e) {
            test.log(Status.FAIL, "Test failed: " + e.getMessage());
            takeScreenshot("AUTH-SU-05_Failed");
            throw e;
        }
    }

    // Sign in tests
    @Test(description = "AUTH-SI-01: Login with valid email and password")
    public void testLoginWithValidCredentials() {
        test = ExtentManager.startTest("AUTH-SI-01: Login with valid credentials");

        try {
            authPage.goToLoginPage();
            test.log(Status.INFO, "Navigated to login page");

            authPage.performLogin(
                AuthenticationTestData.VALID_EMAIL,
                AuthenticationTestData.VALID_PASSWORD
            );
            test.log(Status.INFO, "Entered valid login credentials");

            Assert.assertTrue(authPage.isLoginSuccessful(), "User should be logged in successfully");
            test.log(Status.PASS, "Login successful");

        } catch (Exception e) {
            test.log(Status.FAIL, "Test failed: " + e.getMessage());
            takeScreenshot("AUTH-SI-01_Failed");
            throw e;
        }
    }

    @Test(description = "AUTH-SI-02: Login with wrong password")
    public void testLoginWithWrongPassword() {
        test = ExtentManager.startTest("AUTH-SI-02: Login with wrong password");

        try {
            authPage.goToLoginPage();
            test.log(Status.INFO, "Navigated to login page");

            authPage.performLogin(
                AuthenticationTestData.VALID_EMAIL,
                AuthenticationTestData.WRONG_PASSWORD_1
            );
            test.log(Status.INFO, "Entered wrong password");

            Assert.assertTrue(authPage.isErrorMessageDisplayed(), "Error message should be displayed");
            test.log(Status.PASS, "Wrong password error displayed correctly");

        } catch (Exception e) {
            test.log(Status.FAIL, "Test failed: " + e.getMessage());
            takeScreenshot("AUTH-SI-02_Failed");
            throw e;
        }
    }

    @Test(description = "AUTH-SI-03: Login with unregistered email")
    public void testLoginWithUnregisteredEmail() {
        test = ExtentManager.startTest("AUTH-SI-03: Login with unregistered email");

        try {
            authPage.goToLoginPage();
            test.log(Status.INFO, "Navigated to login page");

            authPage.performLogin(
                AuthenticationTestData.NON_EXISTING_EMAIL_1,
                AuthenticationTestData.VALID_PASSWORD
            );
            test.log(Status.INFO, "Entered unregistered email");

            Assert.assertTrue(authPage.isErrorMessageDisplayed(), "Error message should be displayed");
            test.log(Status.PASS, "Unregistered email error displayed correctly");

        } catch (Exception e) {
            test.log(Status.FAIL, "Test failed: " + e.getMessage());
            takeScreenshot("AUTH-SI-03_Failed");
            throw e;
        }
    }

    @Test(description = "AUTH-SI-04: Login with invalid email format")
    public void testLoginWithInvalidEmailFormat() {
        test = ExtentManager.startTest("AUTH-SI-04: Login with invalid email format");

        try {
            authPage.goToLoginPage();
            test.log(Status.INFO, "Navigated to login page");

            authPage.performLogin(
                AuthenticationTestData.INVALID_EMAIL_1,
                AuthenticationTestData.VALID_PASSWORD
            );
            test.log(Status.INFO, "Entered invalid email format");

            Assert.assertTrue(authPage.isErrorMessageDisplayed(), "Validation error should be displayed");
            test.log(Status.PASS, "Invalid email format error displayed correctly");

        } catch (Exception e) {
            test.log(Status.FAIL, "Test failed: " + e.getMessage());
            takeScreenshot("AUTH-SI-04_Failed");
            throw e;
        }
    }

    // Forgot password tests
    @Test(description = "AUTH-FP-01: Reset password with valid registered email")
    public void testForgotPasswordWithValidEmail() {
        test = ExtentManager.startTest("AUTH-FP-01: Reset password with valid email");

        try {
            authPage.goToLoginPage();
            test.log(Status.INFO, "Navigated to login page");

            authPage.performForgotPassword(AuthenticationTestData.VALID_EMAIL);
            test.log(Status.INFO, "Submitted forgot password request");

            test.log(Status.PASS, "Forgot password request processed");

        } catch (Exception e) {
            test.log(Status.FAIL, "Test failed: " + e.getMessage());
            takeScreenshot("AUTH-FP-01_Failed");
            throw e;
        }
    }

    @Test(description = "AUTH-FP-02: Reset password with non-existing email")
    public void testForgotPasswordWithNonExistingEmail() {
        test = ExtentManager.startTest("AUTH-FP-02: Reset password with non-existing email");

        try {
            authPage.goToLoginPage();
            test.log(Status.INFO, "Navigated to login page");

            authPage.performForgotPassword(AuthenticationTestData.FORGOT_NON_EXISTING_EMAIL_1);
            test.log(Status.INFO, "Submitted forgot password with non-existing email");

            Assert.assertTrue(authPage.isErrorMessageDisplayed(), "Error message should be displayed");
            test.log(Status.PASS, "Non-existing email error displayed correctly");

        } catch (Exception e) {
            test.log(Status.FAIL, "Test failed: " + e.getMessage());
            takeScreenshot("AUTH-FP-02_Failed");
            throw e;
        }
    }

    @Test(description = "AUTH-FP-03: Reset password with invalid email format")
    public void testForgotPasswordWithInvalidEmailFormat() {
        test = ExtentManager.startTest("AUTH-FP-03: Reset password with invalid email format");

        try {
            authPage.goToLoginPage();
            test.log(Status.INFO, "Navigated to login page");

            authPage.performForgotPassword(AuthenticationTestData.FORGOT_INVALID_EMAIL_1);
            test.log(Status.INFO, "Submitted forgot password with invalid email");

            Assert.assertTrue(authPage.isErrorMessageDisplayed(), "Validation error should be displayed");
            test.log(Status.PASS, "Invalid email format error displayed correctly");

        } catch (Exception e) {
            test.log(Status.FAIL, "Test failed: " + e.getMessage());
            takeScreenshot("AUTH-FP-03_Failed");
            throw e;
        }
    }
}
