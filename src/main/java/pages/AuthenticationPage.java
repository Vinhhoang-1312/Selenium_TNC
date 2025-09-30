package pages;

import locators.TNCStoreLocators;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import java.time.Duration;

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

    // ========== NAVIGATION METHODS ==========
    public void openLoginPopup() {
        try {
            // Wait 20 seconds for page to fully load (network, scripts, etc.)
            System.out.println("⏳ Waiting 20 seconds for website to load completely...");
            Thread.sleep(20000); // 20 seconds wait
            System.out.println("✅ Waited 20 seconds, starting to open login popup");

            wait.until(ExpectedConditions.elementToBeClickable(accountButton));
            accountButton.click();
            wait.until(ExpectedConditions.visibilityOf(loginPopup));
            System.out.println("✅ Successfully opened login popup");
        } catch (Exception e) {
            System.out.println("❌ Error when opening login popup: " + e.getMessage());
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
            System.out.println("✅ Successfully performed login with email: " + email);

        } catch (Exception e) {
            System.out.println("❌ Error during login: " + e.getMessage());
            throw new RuntimeException("Cannot perform login", e);
        }
    }

    // ========== REGISTRATION METHODS ==========
    public void goToRegisterPage() {
        try {
            openLoginPopup();
            wait.until(ExpectedConditions.elementToBeClickable(createAccountLink));
            createAccountLink.click();
            System.out.println("✅ Successfully switched to registration form");
        } catch (Exception e) {
            System.out.println("❌ Error when switching to registration form: " + e.getMessage());
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
            System.out.println("✅ Successfully performed registration with email: " + email);

        } catch (Exception e) {
            System.out.println("❌ Error during registration: " + e.getMessage());
            throw new RuntimeException("Cannot perform registration", e);
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
}
