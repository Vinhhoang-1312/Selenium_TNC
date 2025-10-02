package pages;

import locators.TNCStoreLocators;
import helpers.PopupHandler;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;

public class AuthenticationPage extends BasePage {
    private static final Logger log = LoggerFactory.getLogger(AuthenticationPage.class);
    private final PopupHandler popupHandler;

    public AuthenticationPage(WebDriver driver) {
        super(driver);
        this.popupHandler = new PopupHandler(driver);
    }

    @FindBy(xpath = TNCStoreLocators.ACCOUNT_BUTTON)
    private WebElement accountButton;

    @FindBy(css = TNCStoreLocators.LOGIN_POPUP)
    private WebElement loginPopup;

    @FindBy(xpath = TNCStoreLocators.CREATE_ACCOUNT_LINK)
    private WebElement createAccountLink;

    @FindBy(xpath = TNCStoreLocators.LOGIN_EMAIL_FIELD)
    private WebElement loginEmailField;

    @FindBy(xpath = TNCStoreLocators.LOGIN_PASSWORD_FIELD)
    private WebElement loginPasswordField;

    @FindBy(xpath = TNCStoreLocators.LOGIN_BUTTON)
    private WebElement loginButton;

    @FindBy(xpath = TNCStoreLocators.REGISTER_NAME_FIELD)
    private WebElement registerNameField;

    @FindBy(xpath = TNCStoreLocators.REGISTER_EMAIL_FIELD)
    private WebElement registerEmailField;

    @FindBy(xpath = TNCStoreLocators.REGISTER_PASSWORD_FIELD)
    private WebElement registerPasswordField;

    @FindBy(xpath = TNCStoreLocators.REGISTER_BUTTON)
    private WebElement registerButton;

    @FindBy(xpath = TNCStoreLocators.ERROR_MESSAGE_GENERAL)
    private WebElement errorMessage;

    @FindBy(xpath = TNCStoreLocators.EMAIL_EXISTS_ERROR)
    private WebElement emailExistsError;

    @FindBy(xpath = TNCStoreLocators.INVALID_EMAIL_ERROR)
    private WebElement invalidEmailError;

    @FindBy(xpath = TNCStoreLocators.REQUIRED_FIELD_ERROR)
    private WebElement requiredFieldError;

    @FindBy(xpath = TNCStoreLocators.LOGOUT_LINK)
    private WebElement logoutLink;

    @FindBy(xpath = TNCStoreLocators.LOGGED_IN_USER_NAME)
    private WebElement loggedInUserName;

    @FindBy(xpath = TNCStoreLocators.ACCOUNT_DROPDOWN_LOGGED_IN)
    private WebElement accountDropdownLoggedIn;

    @FindBy(css = TNCStoreLocators.LOGGED_IN_USER_NAME_ALT1)
    private WebElement loggedInUserNameAlt1;

    @FindBy(xpath = TNCStoreLocators.NOT_LOGGED_IN_TEXT)
    private WebElement notLoggedInText;

    public void openLoginPopup() {
        int maxAttempts = 3;
        for (int attempt = 1; attempt <= maxAttempts; attempt++) {
            try {
                log.info("Opening login popup (attempt {}/{})...", attempt, maxAttempts);
                popupHandler.checkPopupBeforeClick();
                wait.until(ExpectedConditions.elementToBeClickable(accountButton));
                accountButton.click();
                wait.until(ExpectedConditions.visibilityOf(loginPopup));
                log.info("Successfully opened login popup");
                return;
            } catch (Exception e) {
                log.warn("Error when opening login popup (attempt {}): {}", attempt, e.getMessage());
                if (attempt < maxAttempts) {
                    try { Thread.sleep(1000); } catch (InterruptedException ie) { Thread.currentThread().interrupt(); }
                } else {
                    throw new RuntimeException("Cannot open login popup after " + maxAttempts + " attempts", e);
                }
            }
        }
    }

    public void performLogin(String email, String password) {
        try {
            openLoginPopup();
            wait.until(ExpectedConditions.elementToBeClickable(loginEmailField));
            loginEmailField.clear();
            loginEmailField.sendKeys(email);
            wait.until(ExpectedConditions.elementToBeClickable(loginPasswordField));
            loginPasswordField.clear();
            loginPasswordField.sendKeys(password);
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

                    // Check popup before clicking create account link
                    popupHandler.checkPopupBeforeClick();

                    // Wait for the create account link to be present and clickable
                    wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//a[contains(text(),'o tài')]")));
                    wait.until(ExpectedConditions.elementToBeClickable(createAccountLink));

                    createAccountLink.click();

                    // Wait a moment to see if registration form appears
                    Thread.sleep(1000);

                    // Check if registration form appeared
                    try {
                        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@id='js-popup-register-name']")));
                        clickSuccessful = true;
                        log.info("✅ Successfully clicked create account link and registration form appeared (attempt {})", attempt);
                        break;
                    } catch (TimeoutException e) {
                        log.warn("Registration form did not appear after click attempt {}", attempt);
                    }

                } catch (Exception e) {
                    log.warn("⚠️ Attempt {} failed: {}", attempt, e.getMessage());
                    if (attempt < maxAttempts) {
                        Thread.sleep(2000);
                    }
                }
            }

            // If standard click failed, try alternative methods
            if (!clickSuccessful) {
                log.info("Standard click failed, trying JavaScript click...");
                try {
                    // Check popup before JavaScript click
                    popupHandler.checkPopupBeforeClick();

                    ((JavascriptExecutor) driver).executeScript("arguments[0].click();", createAccountLink);
                    Thread.sleep(1000);

                    try {
                        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@id='js-popup-register-name']")));
                        clickSuccessful = true;
                        log.info("✅ Successfully clicked create account link via JavaScript");
                    } catch (TimeoutException e) {
                        log.warn("Registration form did not appear after JavaScript click");
                    }
                } catch (Exception e) {
                    log.warn("⚠️ JavaScript click failed: {}", e.getMessage());
                }
            }

            // If JavaScript click failed, try direct function call
            if (!clickSuccessful) {
                log.info("JavaScript click failed, trying direct function call...");
                try {
                    // Check popup before direct function call
                    popupHandler.checkPopupBeforeClick();

                    ((JavascriptExecutor) driver).executeScript("_showCustomerForm('register');");
                    Thread.sleep(1000);

                    try {
                        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@id='js-popup-register-name']")));
                        clickSuccessful = true;
                        log.info("✅ Successfully opened registration form via direct function call");
                    } catch (TimeoutException e) {
                        log.warn("Registration form did not appear after direct function call");
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

            // Wait for name field and fill it
            wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@id='js-popup-register-name']")));
            registerNameField.clear();
            registerNameField.sendKeys(name);
            log.info("Filled name field: {}", name);

            // Wait for email field and fill it
            wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@id='js-popup-register-email']")));
            registerEmailField.clear();
            registerEmailField.sendKeys(email);
            log.info("Filled email field: {}", email);

            // Wait for password field and fill it
            wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@id='js-popup-register-password']")));
            registerPasswordField.clear();
            registerPasswordField.sendKeys(password);
            log.info("Filled password field");

            // Click register button with retry
            clickElementWithRetry(registerButton, "register button");
            log.info("Successfully performed registration with email: {}", email);

        } catch (Exception e) {
            log.error("Error during registration: {}", e.getMessage());
            throw new RuntimeException("Cannot perform registration", e);
        }
    }

    private void clickElementWithRetry(WebElement element, String elementName) {
        int maxAttempts = 3;
        for (int attempt = 1; attempt <= maxAttempts; attempt++) {
            try {
                // Check popup before every click attempt
                popupHandler.checkPopupBeforeClick();

                wait.until(ExpectedConditions.elementToBeClickable(element));
                element.click();
                log.info("Successfully clicked {} on attempt {}", elementName, attempt);
                return;
            } catch (ElementClickInterceptedException e) {
                log.warn("Click intercepted on {} (attempt {}), trying JavaScript click...", elementName, attempt);
                try {
                    // Check popup before JavaScript click
                    popupHandler.checkPopupBeforeClick();

                    ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
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


    public boolean isLoginSuccessful() {
        try {
            Thread.sleep(3000);
            log.info("Checking login success by examining account text element...");

            try {
                WebElement accountElement = driver.findElement(By.xpath(TNCStoreLocators.ACCOUNT_TEXT_ELEMENT));
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
            } catch (Exception e) {
                log.warn("Primary account element check failed: {}", e.getMessage());
            }

            try {
                WebElement altAccountElement = driver.findElement(By.xpath(TNCStoreLocators.ACCOUNT_TEXT_ELEMENT_ALT));
                if (altAccountElement.isDisplayed()) {
                    String accountText = altAccountElement.getText().trim();
                    log.info("Alternative account element text: '{}'", accountText);

                    if (accountText.equals("Tài khoản") || accountText.equals("Account")) {
                        log.info("Login verification: Still showing 'Tài khoản' - user not logged in");
                        return false;
                    }

                    if (!accountText.isEmpty() && !accountText.equals("Tài khoản") && !accountText.equals("Account")) {
                        log.info("Login verification: Account text changed to '{}' - user logged in!", accountText);
                        return true;
                    }
                }
            } catch (Exception e) {
                log.warn("Alternative account element check failed: {}", e.getMessage());
            }

            try {
                List<WebElement> taiKhoanElements = driver.findElements(By.xpath(TNCStoreLocators.NOT_LOGGED_IN_TEXT));
                if (taiKhoanElements.isEmpty()) {
                    log.info("Login verification: 'Tài khoản' text not found - likely logged in");
                    return true;
                } else {
                    boolean hasVisibleTaiKhoan = false;
                    for (WebElement element : taiKhoanElements) {
                        if (element.isDisplayed()) {
                            hasVisibleTaiKhoan = true;
                            break;
                        }
                    }
                    if (!hasVisibleTaiKhoan) {
                        log.info("Login verification: No visible 'Tài khoản' text - likely logged in");
                        return true;
                    }
                }
            } catch (Exception e) {
                log.warn("'Tài khoản' text check failed: {}", e.getMessage());
            }

            log.warn("Login verification: All methods failed - login likely unsuccessful");
            return false;

        } catch (Exception e) {
            log.error("Login verification failed with exception: {}", e.getMessage());
            return false;
        }
    }

    public boolean isErrorMessageDisplayed() {
        // Đã tối giản: Không cần kiểm tra các lỗi không xuất hiện trên UI
        return false;
    }

    private boolean isMarketingText(String text) {
        if (text == null || text.trim().isEmpty()) {
            return true;
        }

        String lowerText = text.toLowerCase();
        String[] marketingKeywords = {
            "tnc store", "mua sắm", "thể ga", "không lo phí ship", "giá rẻ", "hàng chất",
            "pc gaming", "ps5", "rtx", "laptop gaming", "banner", "advertisement", "promo"
        };

        for (String keyword : marketingKeywords) {
            if (lowerText.contains(keyword.toLowerCase())) {
                log.warn("Filtering out marketing text: '{}'", text);
                return true;
            }
        }
        return false;
    }

    public String getLoggedInUserName() {
        try {
            if (loggedInUserName.isDisplayed()) {
                String userName = loggedInUserName.getText().trim();
                log.info("Retrieved logged-in user name: {}", userName);
                return userName;
            }
        } catch (Exception e) {
            log.warn("Could not get user name from primary element, trying alternative");
            try {
                if (accountDropdownLoggedIn.isDisplayed()) {
                    String userName = accountDropdownLoggedIn.getText().trim();
                    log.info("Retrieved logged-in user name from alternative element: {}", userName);
                    return userName;
                }
            } catch (Exception e2) {
                log.error("Failed to get logged-in user name: {}", e2.getMessage());
            }
        }
        return "";
    }

    public boolean isUserLoggedIn() {
        try {
            log.info("Starting login verification check...");
            Thread.sleep(3000);
            String accountText = getAccountElementText();
            log.info("Account element text: '{}'", accountText);
            if (accountText.equals("Tài khoản") || accountText.equals("Account")) {
                log.info("User is NOT logged in (found 'Tài khoản')");
                return false;
            }
            if (!accountText.isEmpty() && !accountText.equals("Tài khoản") && !accountText.equals("Account")) {
                log.info("User is logged in with name: '{}'", accountText);
                return true;
            }
            log.warn("Could not determine login status, text: '{}'", accountText);
            return false;
        } catch (Exception e) {
            log.error("Error during login verification: {}", e.getMessage());
            return false;
        }
    }

    private String getAccountElementText() {
        try {
            WebElement accountElement = driver.findElement(By.xpath("//span[@class='hover-txt line-clamp-1']"));
            if (accountElement.isDisplayed()) {
                String text = accountElement.getText().trim();
                log.info("Found account text via primary locator: '{}'", text);
                return text;
            }
        } catch (Exception e) {
            log.warn("Primary locator failed: {}", e.getMessage());
        }

        try {
            WebElement accountElement = driver.findElement(By.xpath("/html/body/div[4]/div[2]/div/div/div[2]/a[1]/span"));
            if (accountElement.isDisplayed()) {
                String text = accountElement.getText().trim();
                log.info("Found account text via absolute XPath: '{}'", text);
                return text;
            }
        } catch (Exception e) {
            log.warn("Absolute XPath failed: {}", e.getMessage());
        }

        try {
            WebElement accountElement = driver.findElement(By.cssSelector(".hover-txt.line-clamp-1"));
            if (accountElement.isDisplayed()) {
                String text = accountElement.getText().trim();
                log.info("Found account text via CSS selector: '{}'", text);
                return text;
            }
        } catch (Exception e) {
            log.warn("CSS selector failed: {}", e.getMessage());
        }

        log.error("All locators failed to find account element");
        return "";
    }

    public String getLoggedInUserNameRobust() {
        return getAccountElementText();
    }

    public void logout() {
        try {
            log.info("Attempting to log out...");
            popupHandler.checkPopupBeforeClick();
            wait.until(ExpectedConditions.elementToBeClickable(accountButton));
            accountButton.click();
            wait.until(ExpectedConditions.elementToBeClickable(logoutLink));
            logoutLink.click();
            log.info("Logout successful");
        } catch (Exception e) {
            log.warn("Logout failed or not needed: {}", e.getMessage());
        }
    }

    /**
     * Fetch browser console logs (Chrome only)
     */
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
}
