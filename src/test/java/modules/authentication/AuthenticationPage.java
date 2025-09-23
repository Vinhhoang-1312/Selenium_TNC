package modules.authentication;

import org.openqa.selenium.By;
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

    // Constructor
    public AuthenticationPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        PageFactory.initElements(driver, this);
    }

    // Login Elements - Cần bạn cung cấp locators chính xác
    @FindBy(id = "email")
    private WebElement loginEmailField;

    @FindBy(id = "password")
    private WebElement loginPasswordField;

    @FindBy(xpath = "//button[contains(text(),'Đăng nhập')]")
    private WebElement loginButton;

    @FindBy(xpath = "//a[contains(text(),'Quên mật khẩu')]")
    private WebElement forgotPasswordLink;

    // Register Elements - Cần bạn cung cấp locators chính xác
    @FindBy(id = "register-name")
    private WebElement registerNameField;

    @FindBy(id = "register-email")
    private WebElement registerEmailField;

    @FindBy(id = "register-password")
    private WebElement registerPasswordField;

    @FindBy(xpath = "//button[contains(text(),'Tạo tài khoản')]")
    private WebElement registerButton;

    // Error message elements
    @FindBy(xpath = "//div[contains(@class,'error') or contains(@class,'alert')]")
    private WebElement errorMessage;

    @FindBy(xpath = "//span[contains(text(),'Email đã được sử dụng')]")
    private WebElement emailExistsError;

    @FindBy(xpath = "//span[contains(text(),'Email không hợp lệ')]")
    private WebElement invalidEmailError;

    @FindBy(xpath = "//span[contains(text(),'This field is required')]")
    private WebElement requiredFieldError;

    // Navigation elements
    @FindBy(xpath = "//a[contains(@href,'register')]")
    private WebElement registerLink;

    @FindBy(xpath = "//a[contains(@href,'login')]")
    private WebElement loginLink;

    // Methods for Login
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

    public void clickForgotPasswordLink() {
        wait.until(ExpectedConditions.elementToBeClickable(forgotPasswordLink));
        forgotPasswordLink.click();
    }

    // Methods for Registration
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

    // Navigation methods
    public void goToRegisterPage() {
        wait.until(ExpectedConditions.elementToBeClickable(registerLink));
        registerLink.click();
    }

    public void goToLoginPage() {
        wait.until(ExpectedConditions.elementToBeClickable(loginLink));
        loginLink.click();
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
        wait.until(ExpectedConditions.visibilityOf(errorMessage));
        return errorMessage.getText();
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
        // Check if user is redirected to dashboard or profile page
        // This needs to be updated based on actual behavior
        return driver.getCurrentUrl().contains("account") || driver.getCurrentUrl().contains("profile");
    }

    // Complete login flow
    public void performLogin(String email, String password) {
        enterLoginEmail(email);
        enterLoginPassword(password);
        clickLoginButton();
    }

    // Complete registration flow
    public void performRegistration(String name, String email, String password) {
        enterRegisterName(name);
        enterRegisterEmail(email);
        enterRegisterPassword(password);
        clickRegisterButton();
    }

    // Forgot password flow
    public void performForgotPassword(String email) {
        clickForgotPasswordLink();
        // Assuming there's an email field in forgot password form
        enterLoginEmail(email); // Reuse the email field
        // Click submit button for forgot password
        clickLoginButton(); // This might need to be different button
    }
}
