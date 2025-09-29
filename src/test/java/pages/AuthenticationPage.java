package pages;

import team.three.automation.commons.TNCStoreLocators;
import team.three.automation.commons.TNCStoreConfig;
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

    // Navigation elements - Nút "Tài khoản" để mở popup
    @FindBy(xpath = TNCStoreLocators.ACCOUNT_BUTTON)
    private WebElement accountButton;

    // Popup khung đăng nhập
    @FindBy(css = TNCStoreLocators.LOGIN_POPUP)
    private WebElement loginPopup;

    // Link "Tạo tài khoản" trong popup login
    @FindBy(css = TNCStoreLocators.CREATE_ACCOUNT_LINK)
    private WebElement createAccountLink;

    // ========== LOGIN ELEMENTS ==========
    @FindBy(css = TNCStoreLocators.LOGIN_EMAIL_FIELD)
    private WebElement loginEmailField;

    @FindBy(css = TNCStoreLocators.LOGIN_PASSWORD_FIELD)
    private WebElement loginPasswordField;

    @FindBy(css = TNCStoreLocators.LOGIN_BUTTON)
    private WebElement loginButton;

    // ========== REGISTER ELEMENTS ==========
    @FindBy(css = TNCStoreLocators.REGISTER_NAME_FIELD)
    private WebElement registerNameField;

    @FindBy(css = TNCStoreLocators.REGISTER_EMAIL_FIELD)
    private WebElement registerEmailField;

    @FindBy(css = TNCStoreLocators.REGISTER_PASSWORD_FIELD)
    private WebElement registerPasswordField;

    @FindBy(css = TNCStoreLocators.REGISTER_BUTTON)
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
            wait.until(ExpectedConditions.elementToBeClickable(accountButton));
            accountButton.click();
            wait.until(ExpectedConditions.visibilityOf(loginPopup));
            System.out.println("✅ Đã mở popup login thành công");
        } catch (Exception e) {
            System.out.println("❌ Lỗi khi mở popup login: " + e.getMessage());
            throw new RuntimeException("Không thể mở popup login", e);
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
            System.out.println("✅ Đã thực hiện login với email: " + email);

        } catch (Exception e) {
            System.out.println("❌ Lỗi khi đăng nhập: " + e.getMessage());
            throw new RuntimeException("Không thể đăng nhập", e);
        }
    }

    // ========== REGISTRATION METHODS ==========
    public void goToRegisterPage() {
        try {
            openLoginPopup();
            wait.until(ExpectedConditions.elementToBeClickable(createAccountLink));
            createAccountLink.click();
            System.out.println("✅ Đã chuyển sang form đăng ký");
        } catch (Exception e) {
            System.out.println("❌ Lỗi khi chuyển sang form đăng ký: " + e.getMessage());
            throw new RuntimeException("Không thể chuyển sang form đăng ký", e);
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
            System.out.println("✅ Đã thực hiện đăng ký với email: " + email);

        } catch (Exception e) {
            System.out.println("❌ Lỗi khi đăng ký: " + e.getMessage());
            throw new RuntimeException("Không thể đăng ký", e);
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
