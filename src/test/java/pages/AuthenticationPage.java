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
    // Input email trong popup login (không cần click "Tạo tài khoản")
    @FindBy(css = TNCStoreLocators.LOGIN_EMAIL_FIELD)
    private WebElement loginEmailField;

    // Input password trong popup login
    @FindBy(css = TNCStoreLocators.LOGIN_PASSWORD_FIELD)
    private WebElement loginPasswordField;

    // Button submit login
    @FindBy(css = TNCStoreLocators.LOGIN_BUTTON)
    private WebElement loginButton;

    // ========== REGISTER ELEMENTS ==========
    // 3 input fields xuất hiện sau khi click "Tạo tài khoản"
    @FindBy(css = TNCStoreLocators.REGISTER_NAME_FIELD)      // 1. Họ và tên
    private WebElement registerNameField;

    @FindBy(css = TNCStoreLocators.REGISTER_EMAIL_FIELD)     // 2. Email
    private WebElement registerEmailField;

    @FindBy(css = TNCStoreLocators.REGISTER_PASSWORD_FIELD)  // 3. Mật khẩu
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

    /**
     * Bước 1: Click nút "Tài khoản" để mở popup login
     */
    public void openLoginPopup() {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(accountButton));
            accountButton.click();

            // Đợi popup xuất hiện
            wait.until(ExpectedConditions.visibilityOf(loginPopup));
            System.out.println("✅ Đã mở popup login thành công");
        } catch (Exception e) {
            System.out.println("❌ Lỗi khi mở popup login: " + e.getMessage());
            throw new RuntimeException("Không thể mở popup login", e);
        }
    }

    // ========== LOGIN FLOW ==========

    /**
     * Flow đăng nhập: Click "Tài khoản" → Nhập email/password trực tiếp
     * (KHÔNG click "Tạo tài khoản")
     */
    public void goToLoginForm() {
        openLoginPopup();
        // Popup đã mở, có thể nhập email/password ngay
        wait.until(ExpectedConditions.visibilityOf(loginEmailField));
        System.out.println("✅ Form login đã sẵn sàng");
    }

    public void performLogin(String email, String password) {
        try {
            // Bước 1: Mở popup
            goToLoginForm();

            // Bước 2: Nhập email
            wait.until(ExpectedConditions.visibilityOf(loginEmailField));
            loginEmailField.clear();
            loginEmailField.sendKeys(email);

            // Bước 3: Nhập password
            wait.until(ExpectedConditions.visibilityOf(loginPasswordField));
            loginPasswordField.clear();
            loginPasswordField.sendKeys(password);

            // Bước 4: Click login
            wait.until(ExpectedConditions.elementToBeClickable(loginButton));
            loginButton.click();

            System.out.println("✅ Đã thực hiện login với email: " + email);

            // Đợi popup đóng hoặc redirect
            Thread.sleep(2000);

        } catch (Exception e) {
            System.out.println("❌ Lỗi khi login: " + e.getMessage());
            throw new RuntimeException("Login thất bại", e);
        }
    }

    // ========== REGISTRATION FLOW ==========

    /**
     * Flow đăng ký: Click "Tài khoản" → Click "Tạo tài khoản" → Nhập 3 fields
     */
    public void goToRegisterForm() {
        try {
            // Bước 1: Mở popup login
            openLoginPopup();

            // Bước 2: Click "Tạo tài khoản"
            wait.until(ExpectedConditions.elementToBeClickable(createAccountLink));
            createAccountLink.click();

            // Bước 3: Đợi 3 input fields xuất hiện
            wait.until(ExpectedConditions.visibilityOf(registerNameField));
            wait.until(ExpectedConditions.visibilityOf(registerEmailField));
            wait.until(ExpectedConditions.visibilityOf(registerPasswordField));

            System.out.println("✅ Form đăng ký đã hiển thị với 3 input fields");

        } catch (Exception e) {
            System.out.println("❌ Lỗi khi mở form đăng ký: " + e.getMessage());
            throw new RuntimeException("Không thể mở form đăng ký", e);
        }
    }

    public void performRegistration(String name, String email, String password) {
        try {
            // Bước 1: Mở form đăng ký
            goToRegisterForm();

            // Bước 2: Nhập họ và tên
            registerNameField.clear();
            registerNameField.sendKeys(name);

            // Bước 3: Nhập email
            registerEmailField.clear();
            registerEmailField.sendKeys(email);

            // Bước 4: Nhập mật khẩu
            registerPasswordField.clear();
            registerPasswordField.sendKeys(password);

            // Bước 5: Submit
            wait.until(ExpectedConditions.elementToBeClickable(registerButton));
            registerButton.click();

            System.out.println("✅ Đã thực hiện đăng ký với:");
            System.out.println("   - Tên: " + name);
            System.out.println("   - Email: " + email);

            // Đợi xử lý
            Thread.sleep(2000);

        } catch (Exception e) {
            System.out.println("❌ Lỗi khi đăng ký: " + e.getMessage());
            throw new RuntimeException("Đăng ký thất bại", e);
        }
    }

    // ========== VALIDATION METHODS ==========

    public boolean isLoginSuccessful() {
        try {
            Thread.sleep(2000);

            // Kiểm tra nhiều dấu hiệu login thành công
            boolean popupClosed = !isLoginPopupDisplayed();
            boolean hasLogoutLink = driver.getPageSource().contains("Đăng xuất");
            boolean urlChanged = driver.getCurrentUrl().contains("account") ||
                                driver.getCurrentUrl().contains("profile");

            return popupClosed || hasLogoutLink || urlChanged;

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

    public boolean isErrorMessageDisplayed() {
        try {
            return errorMessage.isDisplayed();
        } catch (Exception e) {
            return false;
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

    // ========== COMPATIBILITY METHODS ==========
    // Để tương thích với code cũ

    public void goToLoginPage() {
        goToLoginForm();
    }

    public void goToRegisterPage() {
        goToRegisterForm();
    }

    public void performForgotPassword(String email) {
        // Chưa implement - cần xem flow forgot password của TNC Store
        System.out.println("⚠️ Forgot password chưa được implement");
    }
}
