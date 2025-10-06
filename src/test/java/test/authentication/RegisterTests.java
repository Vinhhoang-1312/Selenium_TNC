package test.authentication;

import pages.AuthenticationPage;
import data.AuthenticationTestData;
import helpers.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RegisterTests extends BaseTest {
    private static final Logger log = LoggerFactory.getLogger(RegisterTests.class);

    private AuthenticationPage getAuthPage() {
        if (driver == null) {
            lazyInitDriver();
        }
        return new AuthenticationPage(driver);
    }

    @Test(groups = {"authentication", "smoke", "signup"},
            description = "AUTH-SU-01: Register with valid name, email, and password")
    public void testRegisterWithValidData() {
        // ReportManager.startTest("AUTH-SU-01: Register with valid data");
        try {
            AuthenticationPage authPage = getAuthPage();
            AuthenticationTestData.TestUser testUser = AuthenticationTestData.createUniqueUser("RegisterTest");
            log.info("Starting registration test with unique user: {} ({})", testUser.name, testUser.email);

            boolean isLoggedIn = authPage.registerAndAssertSuccess(testUser);
            Assert.assertTrue(isLoggedIn, "User should be logged in after registration and login.");
            // ReportManager.logPass("Registration and login successful with unique email: " + testUser.email);
            log.info("Registration test completed successfully with user: {}", testUser.email);
        } catch (Exception e) {
            // ReportManager.logFail("Test failed: " + e.getMessage());
            log.error("Registration test failed: ", e);
            throw e;
        }
    }

    @Test(groups = {"authentication", "negative", "signup"},
            description = "AUTH-SU-02: Register with existing email")
    public void testRegisterWithExistingEmail() {
        // ReportManager.startTest("AUTH-SU-02: Register with existing email");
        try {
            AuthenticationPage authPage = getAuthPage();
            boolean foundError = authPage.registerAndCheckPopupError(
                    AuthenticationTestData.VALID_NAME,
                    AuthenticationTestData.EXISTING_EMAIL,
                    AuthenticationTestData.VALID_PASSWORD,
                    "Email đã được sử dụng"
            );
            if (!foundError) {
                foundError = authPage.registerAndCheckConsoleError(
                        AuthenticationTestData.VALID_NAME,
                        AuthenticationTestData.EXISTING_EMAIL,
                        AuthenticationTestData.VALID_PASSWORD,
                        "Email exist"
                );
            }
            Assert.assertTrue(foundError, "Phải hiển thị hoặc log lỗi 'Email exist' khi đăng ký với email đã tồn tại");
            // ReportManager.logPass("Đúng thông báo lỗi khi đăng ký với email đã tồn tại");
        } catch (Exception e) {
            // ReportManager.logFail("Test failed: " + e.getMessage());
            log.error("Existing email test failed: ", e);
            throw e;
        }
    }

    @Test(groups = {"authentication", "negative", "signup"},
            description = "AUTH-SU-03: Register with invalid email format")
    public void testRegisterWithInvalidEmail() {
        // ReportManager.startTest("AUTH-SU-03: Register with invalid email format");
        try {
            AuthenticationPage authPage = getAuthPage();
            authPage.performRegistration(
                    AuthenticationTestData.VALID_NAME_3,
                    AuthenticationTestData.INVALID_EMAIL_1,
                    AuthenticationTestData.VALID_PASSWORD_3
            );

            String expectedText = "Email không hợp lệ";
            log.error("bat tai div");
            org.openqa.selenium.By divBy = org.openqa.selenium.By.xpath("//div[text()='" + expectedText + "']");
            org.openqa.selenium.WebElement el = helpers.WaitUtils.waitForElementVisible(driver, divBy, 5);

            if (el == null) {
                org.openqa.selenium.By spanBy = org.openqa.selenium.By.xpath("//span[normalize-space(text())='" + expectedText + "']");
                el = helpers.WaitUtils.waitForElementVisible(driver, spanBy, 5);
            }

            Assert.assertNotNull(el, "Expected validation message element '" + expectedText + "' not found (div/span).");
            String actual = el.getText().trim();
            Assert.assertEquals(actual, expectedText, "Validation message text does not match expected text.");
            // ReportManager.logPass("Đúng thông báo popup: " + actual);

        } catch (Exception e) {
            // ReportManager.logFail("Test failed: " + e.getMessage());
            log.error("Register with invalid email test failed: ", e);
            throw e;
        }
    }

    @Test(groups = {"authentication", "negative", "signup"},
            description = "AUTH-SU-04: Register with weak password")
    public void testRegisterWithWeakPassword() {
        // ReportManager.startTest("AUTH-SU-04: Register with weak password");
        try {
            AuthenticationPage authPage = getAuthPage();
            boolean foundError = authPage.registerAndCheckPopupError(
                    AuthenticationTestData.VALID_NAME,
                    AuthenticationTestData.VALID_EMAIL_2,
                    AuthenticationTestData.WEAK_PASSWORD_1,
                    "Mật khẩu có tối thiểu 6 ký tự"
            );
            if (!foundError) {
                foundError = authPage.registerAndCheckConsoleError(
                        AuthenticationTestData.VALID_NAME,
                        AuthenticationTestData.VALID_EMAIL_2,
                        AuthenticationTestData.WEAK_PASSWORD_1,
                        "error"
                );
            }
            Assert.assertTrue(foundError, "Phải hiển thị hoặc log lỗi khi đăng ký với mật khẩu yếu");
            // ReportManager.logPass("Đúng thông báo lỗi khi đăng ký với mật khẩu yếu");
        } catch (Exception e) {
            log.error("Register with weak password test failed: ", e);
            throw e;
        }
    }
}
