package pages;

import config.TNCStoreLocators;
import config.TNCStoreConfig;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import java.time.Duration;

public class AuthenticationPage {
    private WebDriver driver;
    private WebDriverWait wait;

    public AuthenticationPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(TNCStoreConfig.ELEMENT_WAIT));
        PageFactory.initElements(driver, this);
    }

    // Navigation elements using centralized locators
    @FindBy(xpath = TNCStoreLocators.ACCOUNT_BUTTON)
    private WebElement accountButton;

    @FindBy(css = TNCStoreLocators.LOGIN_POPUP)
    private WebElement loginPopup;

    @FindBy(xpath = TNCStoreLocators.CREATE_ACCOUNT_LINK)
    private WebElement createAccountLink;

    // Login elements
    @FindBy(css = TNCStoreLocators.LOGIN_EMAIL_FIELD)
    private WebElement loginEmailField;

    @FindBy(css = TNCStoreLocators.LOGIN_PASSWORD_FIELD)
    private WebElement loginPasswordField;

    @FindBy(xpath = TNCStoreLocators.LOGIN_BUTTON)
    private WebElement loginButton;

    // Register elements
    @FindBy(css = TNCStoreLocators.REGISTER_NAME_FIELD)
    private WebElement registerNameField;

    @FindBy(css = TNCStoreLocators.REGISTER_EMAIL_FIELD)
    private WebElement registerEmailField;

    @FindBy(css = TNCStoreLocators.REGISTER_PASSWORD_FIELD)
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

    // Navigation methods
    public void openLoginPopup() {
        wait.until(ExpectedConditions.elementToBeClickable(accountButton));
        accountButton.click();
        wait.until(ExpectedConditions.visibilityOf(loginPopup));
    }

    public void goToRegisterForm() {
        openLoginPopup();
        wait.until(ExpectedConditions.elementToBeClickable(createAccountLink));
        createAccountLink.click();
        wait.until(ExpectedConditions.visibilityOf(registerNameField));
    }

    public void goToLoginForm() {
        openLoginPopup();
        wait.until(ExpectedConditions.visibilityOf(loginEmailField));
    }

    // Registration methods
    public void enterRegisterName(String name) {
        wait.until(ExpectedConditions.visibilityOf(registerNameField));
        registerNameField.clear();
        registerNameField.sendKeys(name);
    }

    public void enterRegisterEmail(String email) {
        wait.until(ExpectedConditions.visibilityOf(registerEmailField));
        registerEmailField.clear();
        registerEmailField.sendKeys(email);
    }

    public void enterRegisterPassword(String password) {
        wait.until(ExpectedConditions.visibilityOf(registerPasswordField));
        registerPasswordField.clear();
        registerPasswordField.sendKeys(password);
    }

    public void clickRegisterButton() {
        wait.until(ExpectedConditions.elementToBeClickable(registerButton));
        registerButton.click();
    }

    // Login methods
    public void enterLoginEmail(String email) {
        wait.until(ExpectedConditions.visibilityOf(loginEmailField));
        loginEmailField.clear();
        loginEmailField.sendKeys(email);
    }

    public void enterLoginPassword(String password) {
        wait.until(ExpectedConditions.visibilityOf(loginPasswordField));
        loginPasswordField.clear();
        loginPasswordField.sendKeys(password);
    }

    public void clickLoginButton() {
        wait.until(ExpectedConditions.elementToBeClickable(loginButton));
        loginButton.click();
    }

    // Validation methods
    public boolean isErrorMessageDisplayed() {
        try {
            return errorMessage.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public String getErrorMessage() {
        try {
            wait.until(ExpectedConditions.visibilityOf(errorMessage));
            return errorMessage.getText();
        } catch (Exception e) {
            return "";
        }
    }

    public boolean isEmailExistsErrorDisplayed() {
        try {
            return emailExistsError.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isInvalidEmailErrorDisplayed() {
        try {
            return invalidEmailError.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isRequiredFieldErrorDisplayed() {
        try {
            return requiredFieldError.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isLoginSuccessful() {
        try {
            Thread.sleep(2000);
            return !isLoginPopupDisplayed() &&
                   (driver.getCurrentUrl().contains("account") ||
                    driver.getCurrentUrl().contains("profile") ||
                    driver.getPageSource().contains("Đăng xuất"));
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isLoginPopupDisplayed() {
        try {
            return loginPopup.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    // Complete workflows
    public void performRegistration(String name, String email, String password) {
        goToRegisterForm();
        enterRegisterName(name);
        enterRegisterEmail(email);
        enterRegisterPassword(password);
        clickRegisterButton();
    }

    public void performLogin(String email, String password) {
        goToLoginForm();
        enterLoginEmail(email);
        enterLoginPassword(password);
        clickLoginButton();
    }

    // Compatibility methods for old tests
    public void goToRegisterPage() {
        goToRegisterForm();
    }

    public void goToLoginPage() {
        goToLoginForm();
    }

    public void clickForgotPasswordLink() {
        // Not implemented yet - no forgot password in current flow
    }

    public void performForgotPassword(String email) {
        // Not implemented yet - no forgot password in current flow
    }
}
