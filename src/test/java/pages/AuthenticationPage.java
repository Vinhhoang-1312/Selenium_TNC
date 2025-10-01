package pages;

import locators.TNCStoreLocators;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.time.Duration;
import java.util.List;

public class authenticationPage extends BasePage {
    private static final Logger log = LoggerFactory.getLogger(authenticationPage.class);

    public authenticationPage(WebDriver driver) {
        super(driver);
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
        try {
            log.info("Opening login popup and dismissing popups immediately...");
            dismissAllPopupsOnPageLoad();
            wait.until(ExpectedConditions.elementToBeClickable(accountButton));
            accountButton.click();
            wait.until(ExpectedConditions.visibilityOf(loginPopup));
            log.info("Successfully opened login popup");
        } catch (Exception e) {
            log.error("Error when opening login popup: {}", e.getMessage());
            throw new RuntimeException("Cannot open login popup", e);
        }
    }

    private void dismissAllPopupsOnPageLoad() {
        try {
            log.info("Checking and dismissing popups immediately...");
            clickIfPresent(By.cssSelector(".widget-header--button-close"));
            clickIfPresent(By.cssSelector(".widget-preview--btn-close"));
            forceHideBlockingElements();
            log.info("Popup dismissal completed");
        } catch (Exception e) {
            log.warn("Error dismissing popups: {}", e.getMessage());
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
            wait.until(ExpectedConditions.elementToBeClickable(createAccountLink));
            createAccountLink.click();
            log.info("Successfully switched to registration form");
        } catch (Exception e) {
            log.error("Error when switching to registration form: {}", e.getMessage());
            throw new RuntimeException("Cannot switch to registration form", e);
        }
    }

    public void performRegistration(String name, String email, String password) {
        try {
            goToRegisterPage();
            wait.until(ExpectedConditions.elementToBeClickable(registerNameField));
            registerNameField.clear();
            registerNameField.sendKeys(name);
            wait.until(ExpectedConditions.elementToBeClickable(registerEmailField));
            registerEmailField.clear();
            registerEmailField.sendKeys(email);
            wait.until(ExpectedConditions.elementToBeClickable(registerPasswordField));
            registerPasswordField.clear();
            registerPasswordField.sendKeys(password);
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
                dismissChatWidget();
                wait.until(ExpectedConditions.elementToBeClickable(element));
                element.click();
                log.info("Successfully clicked {} on attempt {}", elementName, attempt);
                return;
            } catch (ElementClickInterceptedException e) {
                log.warn("Click intercepted on {} (attempt {}), trying JavaScript click...", elementName, attempt);
                try {
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
        try {
            log.info("Checking for error messages after login attempt...");
            Thread.sleep(3000);

            try {
                WebElement generalError = driver.findElement(By.xpath(TNCStoreLocators.ERROR_MESSAGE_GENERAL));
                if (generalError.isDisplayed()) {
                    String errorText = generalError.getText().trim();
                    log.info("Found general error message: '{}'", errorText);
                    if (!isMarketingText(errorText)) {
                        return true;
                    }
                }
            } catch (Exception e) {
                log.warn("General error message not found: {}", e.getMessage());
            }

            try {
                if (emailExistsError.isDisplayed()) {
                    log.info("Found email exists error");
                    return true;
                }
            } catch (Exception e) {
                log.warn("Email exists error not found");
            }

            try {
                if (invalidEmailError.isDisplayed()) {
                    log.info("Found invalid email error");
                    return true;
                }
            } catch (Exception e) {
                log.warn("Invalid email error not found");
            }

            try {
                if (requiredFieldError.isDisplayed()) {
                    log.info("Found required field error");
                    return true;
                }
            } catch (Exception e) {
                log.warn("Required field error not found");
            }

            String[] loginErrorSelectors = {
                "//div[contains(@class,'alert') and (contains(text(),'sai') or contains(text(),'wrong') or contains(text(),'incorrect') or contains(text(),'không đúng'))]",
                "//div[contains(@class,'error') and (contains(text(),'sai') or contains(text(),'wrong') or contains(text(),'incorrect') or contains(text(),'không đúng'))]",
                "//span[contains(@class,'error') and (contains(text(),'sai') or contains(text(),'wrong') or contains(text(),'incorrect') or contains(text(),'không đúng'))]",
                "//div[contains(text(),'Email') and (contains(text(),'sai') or contains(text(),'wrong') or contains(text(),'không đúng'))]",
                "//div[contains(text(),'Mật khẩu') and (contains(text(),'sai') or contains(text(),'wrong') or contains(text(),'không đúng'))]",
                "//div[contains(text(),'không tồn tại') or contains(text(),'not exist') or contains(text(),'not found')]",
                "//span[contains(text(),'không tồn tại') or contains(text(),'not exist') or contains(text(),'not found')]",
                "//div[contains(@class,'popup')]//div[contains(text(),'sai') or contains(text(),'wrong')]",
                "//div[contains(@style,'color') and contains(text(),'sai')]",
                "//span[contains(@style,'color') and contains(text(),'sai')]"
            };

            for (String selector : loginErrorSelectors) {
                try {
                    List<WebElement> errorElements = driver.findElements(By.xpath(selector));
                    for (WebElement element : errorElements) {
                        if (element.isDisplayed() && !element.getText().trim().isEmpty()) {
                            String errorText = element.getText().trim();
                            if (!isMarketingText(errorText)) {
                                log.info("Found login error with selector '{}': '{}'", selector, errorText);
                                return true;
                            }
                        }
                    }
                } catch (Exception e) {
                }
            }

            try {
                WebElement popup = driver.findElement(By.cssSelector(TNCStoreLocators.LOGIN_POPUP));
                if (popup.isDisplayed()) {
                    log.warn("Login popup still visible - checking for error indicators...");
                    try {
                        List<WebElement> popupErrors = popup.findElements(By.xpath(".//*[(contains(@style,'color') and contains(@style,'red')) or contains(@class,'error') or contains(@class,'invalid')]"));
                        for (WebElement errorElement : popupErrors) {
                            if (errorElement.isDisplayed() && !errorElement.getText().trim().isEmpty()) {
                                String errorText = errorElement.getText().trim();
                                if (!isMarketingText(errorText)) {
                                    log.info("Found error styling in popup: '{}'", errorText);
                                    return true;
                                }
                            }
                        }
                    } catch (Exception e) {
                    }
                }
            } catch (Exception e) {
                log.warn("Could not check login popup: {}", e.getMessage());
            }

            log.info("No actual login error messages found");
            return false;
        } catch (Exception e) {
            log.error("Error message detection failed: {}", e.getMessage());
            return false;
        }
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
}
