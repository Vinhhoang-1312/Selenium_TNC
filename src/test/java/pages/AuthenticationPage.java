package pages;

import helpers.PopupHandler;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.time.Duration;
import java.util.List;

public class AuthenticationPage extends BasePage {
    private static final Logger log = LoggerFactory.getLogger(AuthenticationPage.class);
    private final PopupHandler popupHandler;

    private final By accountButton = By.xpath("//span[contains(text(),'Tài khoản')]");
    private final By loginPopup = By.cssSelector("#js-form-holder");
    private final By createAccountLink = By.xpath("//a[contains(text(),'o tài')]");
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
                log.info("Opening login popup (attempt {}/{})...", attempt, maxAttempts);
                popupHandler.dismissAllPopups();
                waitForElementToBeClickable(accountButton);
                try {
                    driver.findElement(accountButton).click();
                } catch (ElementClickInterceptedException e) {
                    log.warn("Normal click failed, trying JavaScript click for accountButton");
                    WebElement btn = driver.findElement(accountButton);
                    ((JavascriptExecutor) driver).executeScript("arguments[0].click();", btn);
                }
                // Wait for overlays/popups to disappear if any
                Thread.sleep(500);
                // Wait for login popup to be visible
                waitForElementToBeVisible(loginPopup);
                log.info("Successfully opened login popup");
                return;
            } catch (Exception e) {
                log.warn("Error when opening login popup (attempt {}): {}", attempt, e.getMessage());
                takeScreenshot(driver, "openLoginPopup_fail_attempt" + attempt);
                if (attempt < maxAttempts) {
                    try { Thread.sleep(1000); } catch (InterruptedException ie) { Thread.currentThread().interrupt(); }
                } else {
                    // Log all overlays/popups for debug
                    try {
                        for (WebElement overlay : driver.findElements(By.xpath("//*[contains(@style,'z-index') and contains(@style,'visible') or contains(@class,'popup') or contains(@class,'modal') or contains(@class,'overlay')]"))) {
                            log.warn("Overlay/popup still present: {}", overlay.getAttribute("outerHTML"));
                        }
                    } catch (Exception ex) { log.warn("Could not log overlays: {}", ex.getMessage()); }
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
            log.info("Successfully performed login with email: {}", email);
        } catch (Exception e) {
            log.error("Error during login: {}", e.getMessage());
            throw new RuntimeException("Cannot perform login", e);
        }
    }

    public void goToRegisterPage() {
        try {
            openLoginPopup();
            boolean clickSuccessful = false;
            int maxAttempts = 3;
            for (int attempt = 1; attempt <= maxAttempts; attempt++) {
                try {
                    log.info("Attempting to click 'Tạo tài khoản' link (attempt {}/{})", attempt, maxAttempts);
                    popupHandler.dismissAllPopups();
                    waitForElementToBeClickable(createAccountLink);
                    driver.findElement(createAccountLink).click();
                    Thread.sleep(1000);
                    if (isElementPresent(registerNameField)) {
                        clickSuccessful = true;
                        log.info("✅ Successfully clicked create account link and registration form appeared (attempt {})", attempt);
                        break;
                    }
                } catch (Exception e) {
                    log.warn("⚠️ Attempt {} failed: {}", attempt, e.getMessage());
                    if (attempt < maxAttempts) {
                        Thread.sleep(2000);
                    }
                }
            }
            if (!clickSuccessful) {
                log.info("Standard click failed, trying JavaScript click...");
                try {
                    popupHandler.dismissAllPopups();
                    WebElement link = driver.findElement(createAccountLink);
                    ((JavascriptExecutor) driver).executeScript("arguments[0].click();", link);
                    Thread.sleep(1000);
                    if (isElementPresent(registerNameField)) {
                        clickSuccessful = true;
                        log.info("✅ Successfully clicked create account link via JavaScript");
                    }
                } catch (Exception e) {
                    log.warn("⚠️ JavaScript click failed: {}", e.getMessage());
                }
            }
            if (!clickSuccessful) {
                log.info("JavaScript click failed, trying direct function call...");
                try {
                    popupHandler.dismissAllPopups();
                    ((JavascriptExecutor) driver).executeScript("_showCustomerForm('register');");
                    Thread.sleep(1000);
                    if (isElementPresent(registerNameField)) {
                        clickSuccessful = true;
                        log.info("✅ Successfully opened registration form via direct function call");
                    }
                } catch (Exception e) {
                    log.warn("⚠️ Direct function call failed: {}", e.getMessage());
                }
            }
            if (!clickSuccessful) {
                throw new RuntimeException("Could not open registration form after trying all strategies");
            }
            log.info("Successfully switched to registration form");
        } catch (Exception e) {
            log.error("Error when switching to registration form: {}", e.getMessage());
            throw new RuntimeException("Cannot switch to registration form", e);
        }
    }

    public void performRegistration(String name, String email, String password) {
        try {
            log.info("Starting registration with name: {}, email: {}", name, email);
            waitForElementToBeClickable(registerNameField);
            WebElement nameField = driver.findElement(registerNameField);
            nameField.clear();
            nameField.sendKeys(name);
            waitForElementToBeClickable(registerEmailField);
            WebElement emailField = driver.findElement(registerEmailField);
            emailField.clear();
            emailField.sendKeys(email);
            waitForElementToBeClickable(registerPasswordField);
            WebElement passwordField = driver.findElement(registerPasswordField);
            passwordField.clear();
            passwordField.sendKeys(password);
            clickElementWithRetry(registerButton, "register button");
            log.info("Successfully performed registration with email: {}", email);
        } catch (Exception e) {
            log.error("Error during registration: {}", e.getMessage());
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
                log.info("Successfully clicked {} on attempt {}", elementName, attempt);
                return;
            } catch (ElementClickInterceptedException e) {
                log.warn("Click intercepted on {} (attempt {}), trying JavaScript click...", elementName, attempt);
                try {
                    popupHandler.dismissAllPopups();
                    WebElement el = driver.findElement(by);
                    ((JavascriptExecutor) driver).executeScript("arguments[0].click();", el);
                    log.info("Successfully clicked {} using JavaScript on attempt {}", elementName, attempt);
                    return;
                } catch (Exception jsError) {
                    log.error("JavaScript click failed on attempt {}: {}", attempt, jsError.getMessage());
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
                log.error("Unexpected error clicking {} on attempt {}: {}", elementName, attempt, e.getMessage());
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

    public void clickIfPresent(By locator) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
            WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(locator));
            if (element.isDisplayed() && element.isEnabled()) {
                element.click();
                log.debug("Clicked on element: {}", locator.toString());
            }
        } catch (TimeoutException e) {
            log.debug("Element not found within timeout, skip clicking: {}", locator.toString());
        }
    }

    private void dismissChatWidget() {
        try {
            log.info("Starting professional popup dismissal with clickIfPresent...");
            clickIfPresent(By.xpath("//div[@class='widget-header--inner widget-header--inner--collapsed']//span[@class='widget-header--button-close-icon']"));
            clickIfPresent(By.xpath("//div[@class='widget-preview--btn-close']"));
            clickIfPresent(By.cssSelector(".widget-header--button-close"));
            clickIfPresent(By.cssSelector(".widget-preview--btn-close"));
            forceHideBlockingElements();
            log.info("Professional popup dismissal completed");
        } catch (Exception e) {
            log.warn("Error in popup dismissal: {}", e.getMessage());
        }
    }

    private void forceHideBlockingElements() {
        try {
            log.debug("Force hiding known blocking elements...");
            ((JavascriptExecutor) driver).executeScript(
                "var blockingElements = document.querySelectorAll('.widget-preview--action-text');" +
                "for(var i = 0; i < blockingElements.length; i++) {" +
                "  blockingElements[i].style.display = 'none';" +
                "  blockingElements[i].style.visibility = 'hidden';" +
                "  blockingElements[i].style.opacity = '0';" +
                "  blockingElements[i].style.zIndex = '-9999';" +
                "  blockingElements[i].style.pointerEvents = 'none';" +
                "}" +
                "console.log('Forced hiding of blocking elements completed');"
            );
            log.debug("Force hiding completed");
        } catch (Exception e) {
            log.warn("Force hiding failed: {}", e.getMessage());
        }
    }

    public boolean isLoginSuccessful() {
        try {
            Thread.sleep(3000);
            log.info("Checking login success by examining account text element...");
            if (isElementPresent(loggedInUserName)) {
                WebElement accountElement = driver.findElement(loggedInUserName);
                if (accountElement.isDisplayed()) {
                    String accountText = accountElement.getText().trim();
                    log.info("Account element text: '{}'", accountText);
                    if (accountText.equals("Tài khoản") || accountText.equals("Account")) {
                        log.info("Login verification: Still showing 'Tài khoản' - user not logged in");
                        return false;
                    }
                    if (!accountText.isEmpty() && !accountText.equals("Tài khoản") && !accountText.equals("Account")) {
                        log.info("Login verification: Account text changed to '{}' - user logged in!", accountText);
                        return true;
                    }
                }
            }
            log.warn("Login verification: All methods failed - login likely unsuccessful");
            return false;
        } catch (Exception e) {
            log.error("Login verification failed with exception: {}", e.getMessage());
            return false;
        }
    }

    public String getLoggedInUserName() {
        try {
            if (isElementPresent(loggedInUserName)) {
                WebElement userNameEl = driver.findElement(loggedInUserName);
                if (userNameEl.isDisplayed()) {
                    String userName = userNameEl.getText().trim();
                    log.info("Retrieved logged-in user name: {}", userName);
                    return userName;
                }
            }
        } catch (Exception e) {
            log.warn("Could not get user name from primary element, trying alternative");
            try {
                if (isElementPresent(accountDropdownLoggedIn)) {
                    WebElement userNameEl = driver.findElement(accountDropdownLoggedIn);
                    if (userNameEl.isDisplayed()) {
                        String userName = userNameEl.getText().trim();
                        log.info("Retrieved logged-in user name from alternative element: {}", userName);
                        return userName;
                    }
                }
            } catch (Exception e2) {
                log.error("Failed to get logged-in user name: {}", e2.getMessage());
            }
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
            log.info("Attempting to log out...");
            popupHandler.dismissAllPopups();
            waitForElementToBeClickable(accountButton);
            driver.findElement(accountButton).click();
            waitForElementToBeClickable(logoutLink);
            driver.findElement(logoutLink).click();
            log.info("Logout successful");
        } catch (Exception e) {
            log.warn("Logout failed or not needed: {}", e.getMessage());
        }
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
        } catch (Exception e) {
            log.warn("Could not fetch browser console logs: {}", e.getMessage());
        }
        return "";
    }

//    // Helper methods for waits and element presence
//    protected void waitForElementToBeVisible(By by) {
//        wait.until(ExpectedConditions.visibilityOfElementLocated(by));
//    }
//    protected void waitForElementToBeClickable(By by) {
//        wait.until(ExpectedConditions.elementToBeClickable(by));
//    }
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
            log.warn("[DEBUG] Screenshot saved to: {}", screenshotPath);
        } catch (Exception ex) {
            log.error("[DEBUG] Could not take screenshot: {}", ex.getMessage());
        }
    }
}
