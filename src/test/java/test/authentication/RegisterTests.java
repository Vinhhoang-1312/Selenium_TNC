package test.authentication;
import pages.AuthenticationPage;
import model.AuthenticationTestData;
import helpers.ReportManager;
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
        ReportManager.startTest("AUTH-SU-01: Register with valid data");
        try {
            AuthenticationPage authPage = getAuthPage();
            AuthenticationTestData.TestUser testUser = AuthenticationTestData.createUniqueUser("RegisterTest");
            log.info("Starting registration test with unique user: {} ({})", testUser.name, testUser.email);

            authPage.goToRegisterPage();
            ReportManager.logInfo("Navigated to register page");

            authPage.performRegistration(testUser.name, testUser.email, testUser.password);
            ReportManager.logInfo("Filled registration form with unique data");

            boolean isLoggedIn = false;
            String accountText = "";
            for (int attempt = 1; attempt <= 2; attempt++) {
                accountText = authPage.getLoggedInUserNameRobust();
                log.info("[DEBUG] Account text after registration/login attempt {}: {}", attempt, accountText);
                if (!accountText.equals("Tài khoản") && !accountText.isEmpty()) {
                    isLoggedIn = true;
                    break;
                }
                log.info("Registration did not auto-login, attempting manual login with new credentials (attempt {})...", attempt);
                authPage.openLoginPopup();
                authPage.performLogin(testUser.email, testUser.password);
            }
            Assert.assertTrue(isLoggedIn, "User should be logged in after registration and login. Account text: " + accountText);
            ReportManager.logPass("Registration and login successful with unique email: " + testUser.email);
            log.info("Registration test completed successfully with user: {}", testUser.email);
        } catch (Exception e) {
            ReportManager.logFail("Test failed: " + e.getMessage());
            log.error("Registration test failed: ", e);
            throw e;
        }
    }

    @Test(groups = {"authentication", "negative", "signup"},
          description = "AUTH-SU-02: Register with existing email")
    public void testRegisterWithExistingEmail() {
        ReportManager.startTest("AUTH-SU-02: Register with existing email");
        try {
            AuthenticationPage authPage = getAuthPage();
            authPage.goToRegisterPage();
            ReportManager.logInfo("Navigated to register page");

            authPage.performRegistration(
                AuthenticationTestData.VALID_NAME,
                AuthenticationTestData.EXISTING_EMAIL,
                AuthenticationTestData.VALID_PASSWORD
            );
            ReportManager.logInfo("Attempted registration with existing email");

            boolean foundError = false;
            try {
                org.openqa.selenium.By noteBy = org.openqa.selenium.By.xpath("//div[@id='js-popup-register-note']");
                org.openqa.selenium.WebElement noteElem = helpers.WaitUtils.waitForElementVisible(driver, noteBy, 2);
                if (noteElem != null && noteElem.isDisplayed()) {
                    String noteText = noteElem.getText().trim();
                    log.info("Popup note text: {}", noteText);
                    if (noteText.contains("Email đã được sử dụng") || noteText.contains("Email exist")) {
                        foundError = true;
                    }
                }
            } catch (Exception ex) {
                log.info("[DEBUG] Error note element not found or disappeared quickly");
            }
            if (!foundError) {
                java.util.List<org.openqa.selenium.logging.LogEntry> logs = driver.manage().logs().get(org.openqa.selenium.logging.LogType.BROWSER).getAll();
                for (org.openqa.selenium.logging.LogEntry entry : logs) {
                    String msg = entry.getMessage();
                    String messageValue = helpers.JsonUtils.extractField(msg, "message");
                    String statusValue = helpers.JsonUtils.extractField(msg, "status");
                    if (("Email exist".equals(messageValue) || "Email đã được sử dụng".equals(messageValue)) && "error".equals(statusValue)) {
                        foundError = true;
                        log.info("[DEBUG] Found error in console log: {}", msg);
                        break;
                    }
                }
            }
            Assert.assertTrue(foundError, "Phải hiển thị hoặc log lỗi 'Email exist' khi đăng ký với email đã tồn tại");
            ReportManager.logPass("Đúng thông báo lỗi khi đăng ký với email đã tồn tại");
        } catch (Exception e) {
            ReportManager.logFail("Test failed: " + e.getMessage());
            log.error("Existing email test failed: ", e);
            throw e;
        }
    }

    @Test(groups = {"authentication", "negative", "signup"},
          description = "AUTH-SU-03: Register with invalid email format")
    public void testRegisterWithInvalidEmail() {
        ReportManager.startTest("AUTH-SU-03: Register with invalid email format");
        try {
            AuthenticationPage authPage = getAuthPage();
            authPage.goToRegisterPage();
            ReportManager.logInfo("Navigated to register page");

            authPage.performRegistration(
                AuthenticationTestData.VALID_NAME_3,
                AuthenticationTestData.INVALID_EMAIL_1,
                AuthenticationTestData.VALID_PASSWORD_3
            );
            ReportManager.logInfo("Attempted registration with invalid email format");

            boolean foundError = false;
            try {
                org.openqa.selenium.By errorBy = org.openqa.selenium.By.xpath("//div[contains(text(),'Email không hợp lệ')]");
                org.openqa.selenium.WebElement errorElem = helpers.WaitUtils.waitForElementVisible(driver, errorBy, 2);
                if (errorElem != null && errorElem.isDisplayed()) {
                    foundError = true;
                    log.info("[DEBUG] Found error element: {}", errorElem.getText());
                }
            } catch (Exception ex) {
                log.info("[DEBUG] Error element not found or disappeared quickly");
            }
            if (!foundError) {
                java.util.List<org.openqa.selenium.logging.LogEntry> logs = driver.manage().logs().get(org.openqa.selenium.logging.LogType.BROWSER).getAll();
                for (org.openqa.selenium.logging.LogEntry entry : logs) {
                    String msg = entry.getMessage();
                    String messageValue = helpers.JsonUtils.extractField(msg, "message");
                    String statusValue = helpers.JsonUtils.extractField(msg, "status");
                    if ("Email error".equals(messageValue) && "error".equals(statusValue)) {
                        foundError = true;
                        log.info("[DEBUG] Found error in console log: {}", msg);
                        break;
                    }
                }
            }
            Assert.assertTrue(foundError, "Phải hiển thị hoặc log lỗi 'Email error' khi đăng ký với email không hợp lệ");
            ReportManager.logPass("Đúng thông báo lỗi khi đăng ký với email không hợp lệ");
        } catch (Exception e) {
            try {
                java.util.List<org.openqa.selenium.logging.LogEntry> logs = driver.manage().logs().get(org.openqa.selenium.logging.LogType.BROWSER).getAll();
                log.error("[DEBUG] Browser console logs on failure:");
                for (org.openqa.selenium.logging.LogEntry entry : logs) {
                    log.error(entry.getMessage());
                }
                String logFile = "report/screenshots/consolelog_testRegisterWithInvalidEmail_" + System.currentTimeMillis() + ".txt";
                java.nio.file.Files.write(java.nio.file.Paths.get(logFile),
                    logs.stream().map(org.openqa.selenium.logging.LogEntry::getMessage).collect(java.util.stream.Collectors.toList()));
                log.error("[DEBUG] Browser console logs saved to: {}", logFile);
            } catch (Exception ex) {
                log.error("[DEBUG] Could not fetch browser logs on failure: {}", ex.getMessage());
            }
            try {
                String screenshotPath = "report/screenshots/fail_testRegisterWithInvalidEmail_" + System.currentTimeMillis() + ".png";
                org.openqa.selenium.TakesScreenshot ts = (org.openqa.selenium.TakesScreenshot) driver;
                java.nio.file.Files.write(java.nio.file.Paths.get(screenshotPath), ts.getScreenshotAs(org.openqa.selenium.OutputType.BYTES));
                log.error("[DEBUG] Screenshot saved to: {}", screenshotPath);
            } catch (Exception ex) {
                log.error("[DEBUG] Could not take screenshot: {}", ex.getMessage());
            }
            ReportManager.logFail("Test failed: " + e.getMessage());
            throw e;
        }
    }

    @Test(groups = {"authentication", "negative", "signup"},
          description = "AUTH-SU-04: Register with weak password")
    public void testRegisterWithWeakPassword() {
        ReportManager.startTest("AUTH-SU-04: Register with weak password");
        try {
            AuthenticationPage authPage = getAuthPage();
            authPage.goToRegisterPage();
            ReportManager.logInfo("Navigated to register page");

            authPage.performRegistration(
                AuthenticationTestData.VALID_NAME,
                AuthenticationTestData.VALID_EMAIL_2,
                AuthenticationTestData.WEAK_PASSWORD_1
            );
            ReportManager.logInfo("Attempted registration with weak password");

            boolean foundError = false;
            try {
                org.openqa.selenium.By errorBy = org.openqa.selenium.By.xpath("//div[contains(text(),'Mật khẩu có tối thiểu 6 ký tự')]");
                org.openqa.selenium.WebElement errorElem = helpers.WaitUtils.waitForElementVisible(driver, errorBy, 2);
                if (errorElem != null && errorElem.isDisplayed()) {
                    foundError = true;
                    log.info("[DEBUG] Found error element: {}", errorElem.getText());
                }
            } catch (Exception ex) {
                log.info("[DEBUG] Error element not found or disappeared quickly");
            }
            if (!foundError) {
                java.util.List<org.openqa.selenium.logging.LogEntry> logs = driver.manage().logs().get(org.openqa.selenium.logging.LogType.BROWSER).getAll();
                for (org.openqa.selenium.logging.LogEntry entry : logs) {
                    String msg = entry.getMessage();
                    String statusValue = helpers.JsonUtils.extractField(msg, "status");
                    if ("error".equals(statusValue)) {
                        foundError = true;
                        log.info("[DEBUG] Found error in console log: {}", msg);
                        break;
                    }
                }
            }
            Assert.assertTrue(foundError, "Phải hiển thị hoặc log lỗi khi đăng ký với mật khẩu yếu");
            ReportManager.logPass("Đúng thông báo lỗi khi đăng ký với mật khẩu yếu");
        } catch (Exception e) {
            ReportManager.logFail("Test failed: " + e.getMessage());
            throw e;
        }
    }
}
