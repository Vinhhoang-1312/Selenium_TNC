package pages;

import helpers.PopupHandler;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.OutputType;
import data.AuthenticationTestData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.json.JSONObject;

public class AuthenticationPage extends BasePage {
    private static final Logger log = LoggerFactory.getLogger(AuthenticationPage.class);
    private final PopupHandler popupHandler;

    private final By accountButton = By.xpath("//a[contains(@class,'item') and contains(@class,'account')]//span[contains(@class,'hover-txt')]");
    private final By loginPopup = By.cssSelector("#js-form-holder");
    private final By createAccountLink = By.xpath("//*[@id='js-form-login']/div[2]/div[4]/a");
    private final By loginEmailField = By.xpath("//input[@id='js-login-email']");
    private final By loginPasswordField = By.xpath("//input[@id='js-login-password']");
    private final By loginButton = By.xpath("//a[@class='btn-submit']");
    private final By registerNameField = By.xpath("//input[@id='js-popup-register-name']");
    private final By registerEmailField = By.xpath("//input[@id='js-popup-register-email']");
    private final By registerPasswordField = By.xpath("//input[@id='js-popup-register-password']");
    private final By registerButton = By.xpath("//a[@class='btn-submit']");
    private final By logoutLink = By.xpath("//a[contains(text(),'Đăng xuất') or contains(text(),'Logout')]");
    private final By loggedInUserName = By.xpath("//span[@class='hover-txt line-clamp-1']");
    private final By accountDropdownLoggedIn = By.xpath("/html/body/div[4]/div[2]/div/div/div[2]/a[1]/span");

    public AuthenticationPage(WebDriver driver) {
        super(driver);
        this.popupHandler = new PopupHandler(driver);
    }

    public void openLoginPopup() {
        int maxAttempts = 3;
        for (int attempt = 1; attempt <= maxAttempts; attempt++) {
            try {
                popupHandler.dismissAllPopups();
                waitForElementToBeClickable(accountButton);
                try {
                    driver.findElement(accountButton).click();
                } catch (ElementClickInterceptedException e) {
                    WebElement btn = driver.findElement(accountButton);
                    ((JavascriptExecutor) driver).executeScript("arguments[0].click();", btn);
                }
                Thread.sleep(500);
                waitForElementToBeVisible(loginPopup);
                return;
            } catch (Exception e) {
                takeScreenshot(driver, "openLoginPopup_fail_attempt" + attempt);
                if (attempt < maxAttempts) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                    }
                } else {
                    try {
                        for (WebElement overlay : driver.findElements(By.xpath("//*[contains(@style,'z-index') and contains(@style,'visible') or contains(@class,'popup') or contains(@class,'modal') or contains(@class,'overlay')]") )) {
                            overlay.getAttribute("outerHTML");
                        }
                    } catch (Exception ex) {}
                    throw new RuntimeException("Cannot open login popup after " + maxAttempts + " attempts", e);
                }
            }
        }
    }

    public void performLogin(String email, String password) {
        try {
            openLoginPopup();
            waitForElementToBeClickable(loginEmailField);
            WebElement emailField = driver.findElement(loginEmailField);
            emailField.clear();
            emailField.sendKeys(email);
            waitForElementToBeClickable(loginPasswordField);
            WebElement passwordField = driver.findElement(loginPasswordField);
            passwordField.clear();
            passwordField.sendKeys(password);
            clickElementWithRetry(loginButton, "login button");
        } catch (Exception e) {
            throw new RuntimeException("Cannot perform login", e);
        }
    }

    public void goToRegisterPage() {
        try {
            openLoginPopup();
            boolean clickSuccessful = false;
            int maxAttempts = 2;
            for (int attempt = 1; attempt <= maxAttempts; attempt++) {
                try {
                    popupHandler.dismissAllPopups();
                    waitForElementToBeClickable(createAccountLink);
                    driver.findElement(createAccountLink).click();
                    Thread.sleep(1000);
                    if (isElementPresent(registerNameField)) {
                        clickSuccessful = true;
                        break;
                    }
                } catch (Exception e) {
                    if (attempt < maxAttempts) {
                        Thread.sleep(2000);
                    }
                }
            }
            if (!clickSuccessful) {
                try {
                    popupHandler.dismissAllPopups();
                    WebElement link = driver.findElement(createAccountLink);
                    ((JavascriptExecutor) driver).executeScript("arguments[0].click();", link);
                    Thread.sleep(1000);
                    if (isElementPresent(registerNameField)) {
                        clickSuccessful = true;
                    }
                } catch (Exception e) {}
            }
            if (!clickSuccessful) {
                try {
                    popupHandler.dismissAllPopups();
                    ((JavascriptExecutor) driver).executeScript("_showCustomerForm('register');");
                    Thread.sleep(1000);
                    if (isElementPresent(registerNameField)) {
                        clickSuccessful = true;
                    }
                } catch (Exception e) {}
            }
            if (!clickSuccessful) {
                throw new RuntimeException("Could not open registration form after trying all strategies");
            }
        } catch (Exception e) {
            throw new RuntimeException("Cannot switch to registration form", e);
        }
    }

    public void performRegistration(String name, String email, String password) {
        try {
            // Ensure the registration form is visible/open before filling fields
//            goToRegisterPage();
            waitForElementToBeVisible(registerNameField);
            waitForElementToBeClickable(registerNameField);
            WebElement nameField = driver.findElement(registerNameField);
            nameField.clear();
            nameField.sendKeys(name);
            waitForElementToBeVisible(registerEmailField);
            waitForElementToBeClickable(registerEmailField);
            WebElement emailField = driver.findElement(registerEmailField);
            emailField.clear();
            emailField.sendKeys(email);
            waitForElementToBeClickable(registerPasswordField);
            WebElement passwordField = driver.findElement(registerPasswordField);
            passwordField.clear();
            passwordField.sendKeys(password);
            clickElementWithRetry(registerButton, "register button");
        } catch (Exception e) {
            throw new RuntimeException("Cannot perform registration", e);
        }
    }

    private void clickElementWithRetry(By by, String elementName) {
        int maxAttempts = 3;
        for (int attempt = 1; attempt <= maxAttempts; attempt++) {
            try {
                popupHandler.dismissAllPopups();
                waitForElementToBeClickable(by);
                driver.findElement(by).click();
                return;
            } catch (ElementClickInterceptedException e) {
                try {
                    popupHandler.dismissAllPopups();
                    WebElement el = driver.findElement(by);
                    ((JavascriptExecutor) driver).executeScript("arguments[0].click();", el);
                    return;
                } catch (Exception jsError) {
                    if (attempt == maxAttempts) {
                        throw new RuntimeException("Failed to click " + elementName + " after " + maxAttempts + " attempts", e);
                    }
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                    }
                }
            } catch (Exception e) {
                if (attempt == maxAttempts) {
                    throw new RuntimeException("Failed to click " + elementName + " after " + maxAttempts + " attempts", e);
                }
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    public boolean isLoginSuccessful() {
        try {
            Thread.sleep(3000);
            if (isElementPresent(loggedInUserName)) {
                WebElement accountElement = driver.findElement(loggedInUserName);
                if (accountElement.isDisplayed()) {
                    String accountText = accountElement.getText().trim();
                    if (accountText.equals("Tài khoản") || accountText.equals("Account")) {
                        return false;
                    }
                    if (!accountText.isEmpty() && !accountText.equals("Tài khoản") && !accountText.equals("Account")) {
                        return true;
                    }
                }
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    public String getLoggedInUserName() {
        try {
            if (isElementPresent(loggedInUserName)) {
                WebElement userNameEl = driver.findElement(loggedInUserName);
                if (userNameEl.isDisplayed()) {
                    String userName = userNameEl.getText().trim();
                    return userName;
                }
            }
        } catch (Exception e) {
            try {
                if (isElementPresent(accountDropdownLoggedIn)) {
                    WebElement userNameEl = driver.findElement(accountDropdownLoggedIn);
                    if (userNameEl.isDisplayed()) {
                        String userName = userNameEl.getText().trim();
                        return userName;
                    }
                }
            } catch (Exception e2) {}
        }
        return "";
    }

    public boolean isUserLoggedIn() {
        return isLoginSuccessful();
    }

    public String getLoggedInUserNameRobust() {
        return getLoggedInUserName();
    }

    public boolean isErrorMessageDisplayed() {
        return false;
    }

    public void logout() {
        try {
            popupHandler.dismissAllPopups();
            waitForElementToBeClickable(accountButton);
            driver.findElement(accountButton).click();
            waitForElementToBeClickable(logoutLink);
            driver.findElement(logoutLink).click();
        } catch (Exception e) {}
    }

    public String getBrowserConsoleLogs() {
        try {
            if (driver instanceof org.openqa.selenium.chrome.ChromeDriver) {
                org.openqa.selenium.chrome.ChromeDriver chromeDriver = (org.openqa.selenium.chrome.ChromeDriver) driver;
                StringBuilder logs = new StringBuilder();
                for (org.openqa.selenium.logging.LogEntry entry : chromeDriver.manage().logs().get("browser")) {
                    logs.append(entry.getMessage()).append("\n");
                }
                return logs.toString();
            }
        } catch (Exception e) {}
        return "";
    }


    public boolean registerAndAssertSuccess(AuthenticationTestData.TestUser user) {
//        goToRegisterPage();
        performRegistration(user.name, user.email, user.password);
        // Explicitly perform login after registration using provided xpaths
        org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(AuthenticationPage.class);
        try {
            log.info("Attempting to log in with email: {}", user.email);
            org.openqa.selenium.By emailBy = org.openqa.selenium.By.xpath("//input[@id='js-login-email']");
            org.openqa.selenium.By passwordBy = org.openqa.selenium.By.xpath("//input[@id='js-login-password']");
            org.openqa.selenium.By loginBtnBy = org.openqa.selenium.By.xpath("//span[contains(text(),'Đăng nhập')]");

            helpers.WaitUtils.waitForElementVisible(driver, emailBy, 10);
            org.openqa.selenium.WebElement emailField = driver.findElement(emailBy);
            emailField.clear();
            emailField.sendKeys(user.email);
            log.info("Entered email: {}", user.email);

            helpers.WaitUtils.waitForElementVisible(driver, passwordBy, 10);
            org.openqa.selenium.WebElement passwordField = driver.findElement(passwordBy);
            passwordField.clear();
            passwordField.sendKeys(user.password);
            log.info("Entered password for user: {}", user.email);

            helpers.WaitUtils.waitForElementClickable(driver, loginBtnBy, 10);
            org.openqa.selenium.WebElement loginBtn = driver.findElement(loginBtnBy);
            loginBtn.click();
            log.info("Clicked login button for user: {}", user.email);

            // Wait for login to complete and check if user is logged in
            Thread.sleep(2000); // Optionally replace with a more robust wait for a logged-in indicator
            String accountText = getLoggedInUserNameRobust();
            log.info("Account text after login: {}", accountText);
            return !accountText.equals("Tài khoản") && !accountText.isEmpty();
        } catch (Exception e) {
            log.error("Login after registration failed for user: {}", user.email, e);
            return false;
        }
    }

    public boolean registerAndCheckPopupError(String name, String email, String password, String expectedError) {
        goToRegisterPage();
        performRegistration(name, email, password);
        String popupError = getPopupErrorText();
        return popupError != null && popupError.contains(expectedError);
    }

    public boolean registerAndCheckConsoleError(String name, String email, String password, String expectedError) {
        goToRegisterPage();
        performRegistration(name, email, password);
        String logError = getConsoleErrorText(expectedError);
        return logError != null && logError.contains(expectedError);
    }

    public String getPopupErrorText() {
        try {
            By noteBy = By.xpath("//div[@id='js-popup-register-note']");
            WebElement noteElem = helpers.WaitUtils.waitForElementVisible(driver, noteBy, 2);
            if (noteElem != null && noteElem.isDisplayed()) {
                return noteElem.getText().trim();
            }
        } catch (Exception ex) {}
        return null;
    }

    public String getConsoleErrorText(String expectedError) {
        try {
            java.util.List<org.openqa.selenium.logging.LogEntry> logs = driver.manage().logs().get(org.openqa.selenium.logging.LogType.BROWSER).getAll();
            for (org.openqa.selenium.logging.LogEntry entry : logs) {
                String msg = entry.getMessage();
                if (msg.contains(expectedError)) {
                    return msg;
                }
            }
        } catch (Exception ex) {}
        return null;
    }

    public String getErrorLogFromConsole() {
        try {
            java.util.List<org.openqa.selenium.logging.LogEntry> logs = driver.manage().logs().get(org.openqa.selenium.logging.LogType.BROWSER).getAll();
            for (org.openqa.selenium.logging.LogEntry entry : logs) {
                String msg = entry.getMessage();
                int jsonStart = msg.indexOf("{\"status\"");
                if (jsonStart != -1) {
                    String json = msg.substring(jsonStart);
                    try {
                        JSONObject obj = new JSONObject(json);
                        if ("error".equals(obj.optString("status"))) {
                            return obj.optString("message");
                        }
                    } catch (Exception ignore) {}
                }
                if (msg.contains("status") && msg.contains("error") && msg.contains("message")) {
                    if (msg.contains("status=error")) {
                        int mIdx = msg.indexOf("message=");
                        if (mIdx != -1) {
                            int end = msg.indexOf(',', mIdx);
                            if (end == -1) end = msg.length();
                            return msg.substring(mIdx + 8, end).replaceAll("[{}]", "").trim();
                        }
                    }
                }
            }
        } catch (Exception ex) {}
        return null;
    }

    protected boolean isElementPresent(By by) {
        try {
            return !driver.findElements(by).isEmpty();
        } catch (Exception e) {
            return false;
        }
    }

    private static void takeScreenshot(WebDriver driver, String fileName) {
        try {
            String screenshotPath = "report/screenshots/" + fileName + "_" + System.currentTimeMillis() + ".png";
            TakesScreenshot ts = (TakesScreenshot) driver;
            java.nio.file.Files.write(java.nio.file.Paths.get(screenshotPath), ts.getScreenshotAs(OutputType.BYTES));
        } catch (Exception ex) {}
    }
}
