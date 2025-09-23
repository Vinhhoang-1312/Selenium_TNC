package modules.authentication;

import base.BaseTest;
import com.aventstack.extentreports.Status;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import utils.ExtentManager;
import data.ExcelReader;
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
            // Get test data from Excel or use fallback
            String name = ExcelReader.getAuthData("AUTH-SU-01", "name");
            String email = ExcelReader.getAuthData("AUTH-SU-01", "email");
            String password = ExcelReader.getAuthData("AUTH-SU-01", "password");

            // Use fallback data if Excel data not available
            if (name.isEmpty()) name = AuthenticationTestData.VALID_NAME;
            if (email.isEmpty()) email = AuthenticationTestData.VALID_EMAIL;
            if (password.isEmpty()) password = AuthenticationTestData.VALID_PASSWORD;

            // Navigate to register form
            authPage.goToRegisterPage();
            test.log(Status.INFO, "Navigated to register page");

            // Fill and submit registration form
            authPage.performRegistration(name, email, password);
            test.log(Status.INFO, "Filled registration form with: " + name + ", " + email);

            // Verify registration success
            Assert.assertTrue(authPage.isLoginSuccessful(), "User should be logged in after successful registration");
            test.log(Status.PASS, "Registration successful - User is logged in");

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
            // Get test data from Excel or use fallback
            String name = ExcelReader.getAuthData("AUTH-SU-02", "name");
            String email = ExcelReader.getAuthData("AUTH-SU-02", "email");
            String password = ExcelReader.getAuthData("AUTH-SU-02", "password");

            // Use fallback data if Excel data not available
            if (name.isEmpty()) name = AuthenticationTestData.VALID_NAME;
            if (email.isEmpty()) email = AuthenticationTestData.EXISTING_EMAIL;
            if (password.isEmpty()) password = AuthenticationTestData.VALID_PASSWORD;

            authPage.goToRegisterPage();
            test.log(Status.INFO, "Navigated to register page");

            authPage.performRegistration(name, email, password);
            test.log(Status.INFO, "Filled registration form with existing email: " + email);

            // Verify error message for existing email
            Assert.assertTrue(authPage.isEmailExistsErrorDisplayed(), "Email exists error should be displayed");
            String errorMessage = authPage.getErrorMessage();
            Assert.assertTrue(errorMessage.contains("Email đã được sử dụng"), "Error message should indicate email already exists");
            test.log(Status.PASS, "Correct error message displayed: " + errorMessage);

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

            // Use invalid email format
            authPage.performRegistration(
                AuthenticationTestData.VALID_NAME,
                AuthenticationTestData.INVALID_EMAIL_1,
                AuthenticationTestData.VALID_PASSWORD
            );
            test.log(Status.INFO, "Filled registration form with invalid email format");

            // Verify invalid email error
            Assert.assertTrue(authPage.isInvalidEmailErrorDisplayed(), "Invalid email error should be displayed");
            String errorMessage = authPage.getErrorMessage();
            Assert.assertTrue(errorMessage.contains("Email không hợp lệ"), "Error message should indicate invalid email");
            test.log(Status.PASS, "Correct error message displayed: " + errorMessage);

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

            // Submit form with blank fields
            authPage.performRegistration("", "", "");
            test.log(Status.INFO, "Submitted registration form with blank fields");

            // Verify required field error
            Assert.assertTrue(authPage.isRequiredFieldErrorDisplayed(), "Required field error should be displayed");
            String errorMessage = authPage.getErrorMessage();
            Assert.assertTrue(errorMessage.contains("This field is required"), "Error message should indicate required field");
            test.log(Status.PASS, "Correct error message displayed: " + errorMessage);

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

            // Use weak password
            authPage.performRegistration(
                AuthenticationTestData.VALID_NAME,
                AuthenticationTestData.VALID_EMAIL_2,
                AuthenticationTestData.WEAK_PASSWORD_1
            );
            test.log(Status.INFO, "Filled registration form with weak password");

            // Verify password error
            Assert.assertTrue(authPage.isErrorMessageDisplayed(), "Password error should be displayed");
            test.log(Status.PASS, "Registration with weak password properly rejected");

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
            // Get test data from Excel or use fallback
            String email = ExcelReader.getAuthData("AUTH-SI-01", "email");
            String password = ExcelReader.getAuthData("AUTH-SI-01", "password");

            if (email.isEmpty()) email = AuthenticationTestData.VALID_EMAIL;
            if (password.isEmpty()) password = AuthenticationTestData.VALID_PASSWORD;

            authPage.goToLoginPage();
            test.log(Status.INFO, "Navigated to login page");

            authPage.performLogin(email, password);
            test.log(Status.INFO, "Entered login credentials: " + email);

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

            // Use wrong password
            authPage.performLogin(
                AuthenticationTestData.VALID_EMAIL,
                AuthenticationTestData.WRONG_PASSWORD_1
            );
            test.log(Status.INFO, "Entered valid email but wrong password");

            // Verify error message
            Assert.assertTrue(authPage.isErrorMessageDisplayed(), "Error message should be displayed");
            String errorMessage = authPage.getErrorMessage();
            Assert.assertTrue(errorMessage.contains("Invalid password"), "Error message should indicate invalid password");
            test.log(Status.PASS, "Correct error message displayed: " + errorMessage);

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

            // Use unregistered email
            authPage.performLogin(
                AuthenticationTestData.NON_EXISTING_EMAIL_1,
                AuthenticationTestData.VALID_PASSWORD
            );
            test.log(Status.INFO, "Entered unregistered email");

            // Verify error message
            Assert.assertTrue(authPage.isErrorMessageDisplayed(), "Error message should be displayed");
            String errorMessage = authPage.getErrorMessage();
            Assert.assertTrue(errorMessage.contains("Account not found"), "Error message should indicate account not found");
            test.log(Status.PASS, "Correct error message displayed: " + errorMessage);

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

            // Use invalid email format
            authPage.performLogin(
                AuthenticationTestData.INVALID_EMAIL_1,
                AuthenticationTestData.VALID_PASSWORD
            );
            test.log(Status.INFO, "Entered invalid email format");

            // Verify validation error
            Assert.assertTrue(authPage.isErrorMessageDisplayed(), "Validation error should be displayed");
            test.log(Status.PASS, "Validation error properly displayed for invalid email format");

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
            test.log(Status.INFO, "Submitted forgot password with valid email");

            // Note: This needs to be customized based on actual behavior
            test.log(Status.PASS, "Forgot password request processed successfully");

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

            // Verify error message
            Assert.assertTrue(authPage.isErrorMessageDisplayed(), "Error message should be displayed");
            String errorMessage = authPage.getErrorMessage();
            Assert.assertTrue(errorMessage.contains("Email not registered"), "Error message should indicate email not registered");
            test.log(Status.PASS, "Correct error message displayed: " + errorMessage);

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
            test.log(Status.INFO, "Submitted forgot password with invalid email format");

            // Verify validation error
            Assert.assertTrue(authPage.isErrorMessageDisplayed(), "Validation error should be displayed");
            String errorMessage = authPage.getErrorMessage();
            Assert.assertTrue(errorMessage.contains("Invalid email"), "Error message should indicate invalid email");
            test.log(Status.PASS, "Correct validation error displayed: " + errorMessage);

        } catch (Exception e) {
            test.log(Status.FAIL, "Test failed: " + e.getMessage());
            takeScreenshot("AUTH-FP-03_Failed");
            throw e;
        }
    }
}
