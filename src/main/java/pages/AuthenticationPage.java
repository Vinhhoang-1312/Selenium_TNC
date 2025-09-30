package pages;

import locators.TNCStoreLocators;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import java.time.Duration;
import java.util.List;

public class AuthenticationPage extends BasePage {

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

    @FindBy(xpath = TNCStoreLocators.LOGGED_IN_USER_NAME_ALT1)
    private WebElement loggedInUserNameAlt1;

    @FindBy(xpath = TNCStoreLocators.NOT_LOGGED_IN_TEXT)
    private WebElement notLoggedInText;

    // ========== NAVIGATION METHODS ==========
    public void openLoginPopup() {
        try {
            // Wait 20 seconds for page to fully load (network, scripts, etc.)
            System.out.println(" Waiting 20 seconds for website to load completely...");
            Thread.sleep(20000); // 20 seconds wait
            System.out.println(" Waited 20 seconds, starting to open login popup");

            wait.until(ExpectedConditions.elementToBeClickable(accountButton));
            accountButton.click();
            wait.until(ExpectedConditions.visibilityOf(loginPopup));
            System.out.println(" Successfully opened login popup");
        } catch (Exception e) {
            System.out.println(" Error when opening login popup: " + e.getMessage());
            throw new RuntimeException("Cannot open login popup", e);
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
            System.out.println(" Successfully performed login with email: " + email);

        } catch (Exception e) {
            System.out.println(" Error during login: " + e.getMessage());
            throw new RuntimeException("Cannot perform login", e);
        }
    }

    // ========== REGISTRATION METHODS ==========
    public void goToRegisterPage() {
        try {
            openLoginPopup();
            wait.until(ExpectedConditions.elementToBeClickable(createAccountLink));
            createAccountLink.click();
            System.out.println(" Successfully switched to registration form");
        } catch (Exception e) {
            System.out.println(" Error when switching to registration form: " + e.getMessage());
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

            // FIX: Use robust click method to handle overlays like chat widgets
            clickElementWithRetry(registerButton, "register button");
            System.out.println("✅ Successfully performed registration with email: " + email);

        } catch (Exception e) {
            System.out.println("❌ Error during registration: " + e.getMessage());
            throw new RuntimeException("Cannot perform registration", e);
        }
    }

    /**
     * Robust click method that handles overlays and intercepted clicks
     */
    private void clickElementWithRetry(WebElement element, String elementName) {
        int maxAttempts = 3;

        for (int attempt = 1; attempt <= maxAttempts; attempt++) {
            try {
                // First, try to dismiss any chat widgets or overlays
                dismissChatWidget();

                // Wait for element to be clickable
                wait.until(ExpectedConditions.elementToBeClickable(element));

                // Try regular click first
                element.click();
                System.out.println("✅ Successfully clicked " + elementName + " on attempt " + attempt);
                return;

            } catch (ElementClickInterceptedException e) {
                System.out.println("⚠️ Click intercepted on " + elementName + " (attempt " + attempt + "), trying JavaScript click...");

                try {
                    // Use JavaScript click as fallback
                    ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
                    System.out.println("✅ Successfully clicked " + elementName + " using JavaScript on attempt " + attempt);
                    return;

                } catch (Exception jsError) {
                    System.out.println("❌ JavaScript click failed on attempt " + attempt + ": " + jsError.getMessage());

                    if (attempt == maxAttempts) {
                        throw new RuntimeException("Failed to click " + elementName + " after " + maxAttempts + " attempts", e);
                    }

                    // Wait before retry
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                    }
                }
            } catch (Exception e) {
                System.out.println("❌ Unexpected error clicking " + elementName + " on attempt " + attempt + ": " + e.getMessage());

                if (attempt == maxAttempts) {
                    throw new RuntimeException("Failed to click " + elementName + " after " + maxAttempts + " attempts", e);
                }

                // Wait before retry
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    /**
     * Try to dismiss chat widget or other overlays that might block clicks
     */
    private void dismissChatWidget() {
        try {
            // Try to find and close chat widget - UPDATED với widget mới
            List<WebElement> chatWidgets = driver.findElements(By.cssSelector(
                ".agent-convo-event-wrapper, " +
                ".widget-layout, " +
                ".widget-layout--right, " +
                ".chat-widget, " +
                ".live-chat, " +
                "[class*='chat'], " +
                "[class*='messenger'], " +
                "[id*='chat'], " +
                "[class*='widget']"
            ));

            for (WebElement widget : chatWidgets) {
                try {
                    if (widget.isDisplayed()) {
                        // Try to find close button
                        List<WebElement> closeButtons = widget.findElements(By.cssSelector(
                            ".close, .x, [aria-label*='close'], [title*='close'], " +
                            ".fa-times, .fa-close, .icon-close"
                        ));

                        for (WebElement closeBtn : closeButtons) {
                            if (closeBtn.isDisplayed()) {
                                closeBtn.click();
                                System.out.println("✅ Closed chat widget");
                                Thread.sleep(1000);
                                return;
                            }
                        }

                        // If no close button, try to hide with JavaScript
                        ((JavascriptExecutor) driver).executeScript(
                            "arguments[0].style.display = 'none'; " +
                            "arguments[0].style.visibility = 'hidden'; " +
                            "arguments[0].style.opacity = '0'; " +
                            "arguments[0].style.zIndex = '-9999';",
                            widget
                        );
                        System.out.println("✅ Hidden widget with JavaScript: " + widget.getAttribute("class"));
                        Thread.sleep(1000);
                    }
                } catch (Exception e) {
                    // Ignore errors when trying to close chat widget
                    System.out.println("⚠️ Could not close widget: " + e.getMessage());
                }
            }

            // THÊM: Ẩn tất cả widgets có thể block click
            ((JavascriptExecutor) driver).executeScript(
                "var widgets = document.querySelectorAll('.widget-layout, .agent-convo-event-wrapper, [class*=\"widget\"]');" +
                "for(var i = 0; i < widgets.length; i++) {" +
                "  widgets[i].style.display = 'none';" +
                "  widgets[i].style.visibility = 'hidden';" +
                "  widgets[i].style.opacity = '0';" +
                "  widgets[i].style.zIndex = '-9999';" +
                "}"
            );
            System.out.println("✅ Disabled all potential widget overlays");

        } catch (Exception e) {
            // Ignore errors when trying to dismiss chat widgets
            System.out.println("⚠️ Error dismissing chat widgets: " + e.getMessage());
        }
    }

    // ========== VALIDATION METHODS ==========
    public boolean isLoginSuccessful() {
        try {
            // Check if logout link is present (indicates successful login)
            return wait.until(ExpectedConditions.visibilityOf(logoutLink)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
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

    // ========== USER LOGIN VERIFICATION METHODS ==========

    /**
     * Retrieves the logged-in user's name from the account dropdown
     * @return String - The user's display name if logged in, empty string otherwise
     */
    public String getLoggedInUserName() {
        try {
            if (loggedInUserName.isDisplayed()) {
                String userName = loggedInUserName.getText().trim();
                System.out.println("Retrieved logged-in user name: " + userName);
                return userName;
            }
        } catch (Exception e) {
            System.out.println("Could not get user name from primary element, trying alternative");
            try {
                if (accountDropdownLoggedIn.isDisplayed()) {
                    String userName = accountDropdownLoggedIn.getText().trim();
                    System.out.println("Retrieved logged-in user name from alternative element: " + userName);
                    return userName;
                }
            } catch (Exception e2) {
                System.out.println("Failed to get logged-in user name: " + e2.getMessage());
            }
        }
        return "";
    }

    /**
     * Checks if a user is currently logged in with robust logic
     * @return boolean - true if user is logged in, false otherwise
     */
    public boolean isUserLoggedIn() {
        try {
            System.out.println("Starting login verification check...");

            // Wait for page to stabilize after login/registration
            Thread.sleep(3000);

            // Strategy 1: Check if NOT logged in text is present
            try {
                if (notLoggedInText.isDisplayed()) {
                    System.out.println("Found 'Tài khoản' text - user is NOT logged in");
                    return false;
                }
            } catch (Exception e) {
                System.out.println("'Tài khoản' text not found, checking for user name...");
            }

            // Strategy 2: Try to get user name from multiple locators
            String userName = getLoggedInUserNameRobust();

            if (!userName.isEmpty() &&
                !userName.equals("Tài khoản") &&
                !userName.equals("Account") &&
                !userName.equals("Login") &&
                !userName.equals("Đăng nhập")) {

                System.out.println(" User is logged in with name: '" + userName + "'");
                return true;
            }

            System.out.println(" User appears not to be logged in. Found text: '" + userName + "'");
            return false;

        } catch (Exception e) {
            System.out.println("Error during login verification: " + e.getMessage());
            return false;
        }
    }

    /**
     * Robust method to get logged-in user name with multiple fallbacks
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
                        System.out.println(" Found user name '" + text + "' using: " + strategyNames[i]);
                        return text;
                    }
                }
            } catch (Exception e) {
                System.out.println("Strategy " + strategyNames[i] + " failed: " + e.getMessage());
            }
        }

        // Fallback: try original method
        return getLoggedInUserName();
    }
}
