package pages;

import locators.TNCStoreLocators;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * AuthenticationPage - Handles login, registration and user verification
 * Updated: 2025-09-30 - Added proper login verification methods
 */

public class AuthenticationPage extends BasePage {

    private static final Logger log = LoggerFactory.getLogger(AuthenticationPage.class);

    public AuthenticationPage(WebDriver driver) {
        super(driver);
    }

    // Navigation elements - Account button to open popup
    @FindBy(xpath = TNCStoreLocators.ACCOUNT_BUTTON)
    private WebElement accountButton;

    // Login popup frame
    @FindBy(css = TNCStoreLocators.LOGIN_POPUP)
    private WebElement loginPopup;

    // Create account link in login popup
    @FindBy(xpath = TNCStoreLocators.CREATE_ACCOUNT_LINK)
    private WebElement createAccountLink;

    // ========== LOGIN ELEMENTS ==========
    @FindBy(xpath = TNCStoreLocators.LOGIN_EMAIL_FIELD)
    private WebElement loginEmailField;

    @FindBy(xpath = TNCStoreLocators.LOGIN_PASSWORD_FIELD)
    private WebElement loginPasswordField;

    @FindBy(xpath = TNCStoreLocators.LOGIN_BUTTON)
    private WebElement loginButton;

    // ========== REGISTER ELEMENTS ==========
    @FindBy(xpath = TNCStoreLocators.REGISTER_NAME_FIELD)
    private WebElement registerNameField;

    @FindBy(xpath = TNCStoreLocators.REGISTER_EMAIL_FIELD)
    private WebElement registerEmailField;

    @FindBy(xpath = TNCStoreLocators.REGISTER_PASSWORD_FIELD)
    private WebElement registerPasswordField;

    @FindBy(xpath = TNCStoreLocators.REGISTER_BUTTON)
    private WebElement registerButton;

    // Error message elements
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

    // ========== SUCCESS VERIFICATION ELEMENTS ==========
    @FindBy(xpath = TNCStoreLocators.LOGGED_IN_USER_NAME)
    private WebElement loggedInUserName;

    @FindBy(xpath = TNCStoreLocators.ACCOUNT_DROPDOWN_LOGGED_IN)
    private WebElement accountDropdownLoggedIn;

    // Alternative locators for better reliability
    @FindBy(xpath = TNCStoreLocators.LOGGED_IN_USER_NAME_ALT1)
    private WebElement loggedInUserNameAlt1;

    @FindBy(xpath = TNCStoreLocators.NOT_LOGGED_IN_TEXT)
    private WebElement notLoggedInText;

    // ========== NAVIGATION METHODS ==========
    public void openLoginPopup() {
        try {
            log.info("Waiting 5 seconds for website to load completely...");
            Thread.sleep(5000);
            log.info("Waited 5 seconds, starting to open login popup");

            wait.until(ExpectedConditions.elementToBeClickable(accountButton));
            accountButton.click();
            wait.until(ExpectedConditions.visibilityOf(loginPopup));
            log.info("Successfully opened login popup");
        } catch (Exception e) {
            log.error("Error when opening login popup: {}", e.getMessage());
            throw new RuntimeException("Cannot open login popup", e);
        }
    }

    // ========== IMPROVED NAVIGATION METHODS ==========
    public void openLoginPopupImproved() {
        try {
            log.info("🔄 Starting to open login popup with improved logic...");

            // Dynamic wait instead of fixed sleep
            wait.until(ExpectedConditions.elementToBeClickable(accountButton));

            // Dismiss any overlays BEFORE clicking
            dismissOverlays();

            // Click account button with retry logic
            boolean clicked = clickElementWithRetry(accountButton, "Account Button");

            if (clicked) {
                // Wait for popup to appear
                wait.until(ExpectedConditions.visibilityOf(loginPopup));

                // Wait for popup to be stable
                wait.until(ExpectedConditions.elementToBeClickable(loginPopup));

                log.info("✅ Successfully opened login popup");
            } else {
                throw new RuntimeException("Failed to click account button after all strategies");
            }

        } catch (Exception e) {
            log.error("❌ Error when opening login popup: {}", e.getMessage());
            throw new RuntimeException("Cannot open login popup", e);
        }
    }

    private void dismissOverlays() {
        try {
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript(
                "// Dismiss common overlays that might interfere with clicks" +
                "var overlays = document.querySelectorAll('.widget-layout, .overlay, .modal-backdrop, .popup-overlay');" +
                "overlays.forEach(function(overlay) { " +
                "   if (overlay.style) overlay.style.display = 'none'; " +
                "   overlay.remove(); " +
                "});" +
                "// Also try to close any notification popups" +
                "var notifications = document.querySelectorAll('[class*=\"notification\"], [class*=\"toast\"]');" +
                "notifications.forEach(function(notif) { notif.style.display = 'none'; });"
            );
            Thread.sleep(500); // Short wait for DOM changes
            log.debug("✅ Dismissed potential overlay elements");
        } catch (Exception e) {
            log.debug("⚠️ Could not dismiss overlays: {}", e.getMessage());
        }
    }

    private boolean clickElementWithRetry(WebElement element, String elementName) {
        int maxRetries = 3;

        for (int attempt = 1; attempt <= maxRetries; attempt++) {
            try {
                log.debug("Attempt {} to click {}", attempt, elementName);

                // Strategy 1: Regular click (preferred)
                if (attempt == 1) {
                    element.click();
                    log.info("✅ Successfully clicked {} using regular click", elementName);
                    return true;
                }

                // Strategy 2: JavaScript click
                if (attempt == 2) {
                    JavascriptExecutor js = (JavascriptExecutor) driver;
                    js.executeScript("arguments[0].click();", element);
                    log.info("✅ Successfully clicked {} using JavaScript", elementName);
                    return true;
                }

                // Strategy 3: Actions click
                if (attempt == 3) {
                    Actions actions = new Actions(driver);
                    actions.moveToElement(element).click().perform();
                    log.info("✅ Successfully clicked {} using Actions", elementName);
                    return true;
                }

            } catch (Exception e) {
                log.warn("❌ Attempt {} failed for {}: {}", attempt, elementName, e.getMessage());

                if (attempt < maxRetries) {
                    try {
                        Thread.sleep(1000); // Wait before retry
                        dismissOverlays(); // Clear overlays before retry
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
        }

        log.error("❌ All {} attempts failed to click {}", maxRetries, elementName);
        return false;
    }

    // ========== REGISTRATION METHODS ==========
    public void goToRegisterPage() {
        try {
            openLoginPopup();

            // Wait for popup to be fully loaded and stable
            Thread.sleep(2000);

            // Try to dismiss any overlays that might be blocking the element
            try {
                JavascriptExecutor js = (JavascriptExecutor) driver;
                js.executeScript(
                    "var overlays = document.querySelectorAll('.widget-layout'); " +
                    "overlays.forEach(function(overlay) { overlay.style.display = 'none'; });"
                );
                Thread.sleep(1000);
            } catch (Exception overlayEx) {
                log.warn("Could not dismiss overlays: {}", overlayEx.getMessage());
            }

            // Try multiple strategies to click the create account link
            boolean clicked = false;

            // Strategy 1: Regular click
            try {
                wait.until(ExpectedConditions.elementToBeClickable(createAccountLink));
                createAccountLink.click();
                clicked = true;
                log.info("Successfully clicked create account link using regular click");
            } catch (Exception e1) {
                log.warn("Regular click failed, trying JavaScript click");

                // Strategy 2: JavaScript click
                try {
                    JavascriptExecutor js = (JavascriptExecutor) driver;
                    js.executeScript("arguments[0].click();", createAccountLink);
                    clicked = true;
                    log.info("Successfully clicked create account link using JavaScript");
                } catch (Exception e2) {
                    log.warn("JavaScript click failed, trying Actions click");

                    // Strategy 3: Actions click
                    try {
                        Actions actions = new Actions(driver);
                        actions.moveToElement(createAccountLink).click().perform();
                        clicked = true;
                        log.info("Successfully clicked create account link using Actions");
                    } catch (Exception e3) {
                        log.error("All click strategies failed for create account link");
                        throw new RuntimeException("Cannot click create account link after trying multiple strategies", e3);
                    }
                }
            }

            if (clicked) {
                Thread.sleep(2000);
                log.info("Successfully switched to registration form");
            }

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

            registerButton.click();
            log.info("Successfully performed registration with email: {}", email);

        } catch (Exception e) {
            log.error("Error during registration: {}", e.getMessage());
            throw new RuntimeException("Cannot perform registration", e);
        }
    }

    // ========== LOGIN METHODS ==========
    public void performLogin(String email, String password) {
        try {
            openLoginPopup();

            wait.until(ExpectedConditions.elementToBeClickable(loginEmailField));
            loginEmailField.clear();
            loginEmailField.sendKeys(email);

            wait.until(ExpectedConditions.elementToBeClickable(loginPasswordField));
            loginPasswordField.clear();
            loginPasswordField.sendKeys(password);

            loginButton.click();
            log.info("Successfully performed login with email: {}", email);

        } catch (Exception e) {
            log.error("Error during login: {}", e.getMessage());
            throw new RuntimeException("Cannot perform login", e);
        }
    }

    // ========== VALIDATION METHODS ==========
    public boolean isLoginSuccessful() {
        try {
            // Wait for popup to close first
            Thread.sleep(3000);

            // Check if user name is displayed instead of "Tài khoản"
            // This indicates successful login
            return wait.until(ExpectedConditions.visibilityOf(loggedInUserName)).isDisplayed();
        } catch (Exception e) {
            log.warn("Login success check failed, trying alternative method: {}", e.getMessage());
            try {
                return accountDropdownLoggedIn.isDisplayed();
            } catch (Exception e2) {
                log.error("All login success checks failed: {}", e2.getMessage());
                return false;
            }
        }
    }

    // ========== USER LOGIN VERIFICATION METHODS ==========

    /**
     * Retrieves the logged-in user's name from the account dropdown
     * @return String - The user's display name if logged in, empty string otherwise
     */
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

    /**
     * IMPROVED: Checks if a user is currently logged in with robust logic
     * @return boolean - true if user is logged in, false otherwise
     */
    public boolean isUserLoggedIn() {
        try {
            log.info("Starting login verification check...");

            // Wait for page to stabilize after login/registration
            Thread.sleep(3000);

            // Strategy 1: Check if NOT logged in text is present
            try {
                if (notLoggedInText.isDisplayed()) {
                    log.info("Found 'Tài khoản' text - user is NOT logged in");
                    return false;
                }
            } catch (Exception e) {
                log.debug("'Tài khoản' text not found, checking for user name...");
            }

            // Strategy 2: Try to get user name from multiple locators
            String userName = getLoggedInUserNameRobust();

            if (!userName.isEmpty() &&
                !userName.equals("Tài khoản") &&
                !userName.equals("Account") &&
                !userName.equals("Login") &&
                !userName.equals("Đăng nhập")) {

                log.info("✅ User is logged in with name: '{}'", userName);
                return true;
            }

            log.warn("❌ User appears not to be logged in. Found text: '{}'", userName);
            return false;

        } catch (Exception e) {
            log.error("Error during login verification: {}", e.getMessage());
            return false;
        }
    }

    /**
     * IMPROVED: Robust method to get logged-in user name with multiple fallbacks
     * @return String - The user's display name if logged in, empty string otherwise
     */
    public String getLoggedInUserNameRobust() {
        // List of strategies to try
        WebElement[] elementsToTry = {
            loggedInUserName,
            accountDropdownLoggedIn,
            loggedInUserNameAlt1
        };

        String[] strategyNames = {
            "Primary user name element",
            "Account dropdown element",
            "Alternative user name element"
        };

        for (int i = 0; i < elementsToTry.length; i++) {
            try {
                WebElement element = elementsToTry[i];
                if (element != null && element.isDisplayed()) {
                    String text = element.getText().trim();
                    if (!text.isEmpty()) {
                        log.info("✅ Found user name '{}' using: {}", text, strategyNames[i]);
                        return text;
                    }
                }
            } catch (Exception e) {
                log.debug("Strategy {} failed: {}", strategyNames[i], e.getMessage());
            }
        }

        // Fallback: try original method
        return getLoggedInUserName();
    }

    public boolean isErrorMessageDisplayed() {
        try {
            return errorMessage.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public String getErrorMessage() {
        try {
            if (emailExistsError.isDisplayed()) {
                return emailExistsError.getText();
            } else if (invalidEmailError.isDisplayed()) {
                return invalidEmailError.getText();
            } else if (requiredFieldError.isDisplayed()) {
                return requiredFieldError.getText();
            } else if (errorMessage.isDisplayed()) {
                return errorMessage.getText();
            }
            return "";
        } catch (Exception e) {
            return "";
        }
    }
}
