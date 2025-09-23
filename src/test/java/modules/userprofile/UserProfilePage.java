package modules.userprofile;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import java.time.Duration;

public class UserProfilePage {
    private WebDriver driver;
    private WebDriverWait wait;

    public UserProfilePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        PageFactory.initElements(driver, this);
    }

    // Profile navigation elements - Need real locators from TNC Store
    @FindBy(xpath = "//a[contains(@href,'profile') or contains(@href,'account')]")
    private WebElement profileLink;

    @FindBy(xpath = "//a[contains(text(),'Thông tin cá nhân')]")
    private WebElement profileMenuLink;

    // Profile form elements - Need real locators
    @FindBy(id = "profile-name")
    private WebElement nameField;

    @FindBy(id = "profile-email")
    private WebElement emailField;

    @FindBy(id = "profile-phone")
    private WebElement phoneField;

    @FindBy(id = "profile-address")
    private WebElement addressField;

    @FindBy(xpath = "//button[contains(text(),'Lưu thông tin')]")
    private WebElement saveProfileButton;

    // Password change elements - Need real locators
    @FindBy(id = "current-password")
    private WebElement currentPasswordField;

    @FindBy(id = "new-password")
    private WebElement newPasswordField;

    @FindBy(id = "confirm-password")
    private WebElement confirmPasswordField;

    @FindBy(xpath = "//button[contains(text(),'Đổi mật khẩu')]")
    private WebElement changePasswordButton;

    // Message elements - Need real locators
    @FindBy(xpath = "//div[contains(@class,'success-message')]")
    private WebElement successMessage;

    @FindBy(xpath = "//div[contains(@class,'error-message')]")
    private WebElement errorMessage;

    @FindBy(xpath = "//span[contains(text(),'Email đã được sử dụng')]")
    private WebElement emailExistsError;

    @FindBy(xpath = "//span[contains(text(),'This field is required')]")
    private WebElement requiredFieldError;

    // Login popup for authentication check
    @FindBy(id = "js-form-holder")
    private WebElement loginPopup;

    // Navigation methods
    public void navigateToProfile() {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(profileLink));
            profileLink.click();
            wait.until(ExpectedConditions.visibilityOf(nameField));
        } catch (Exception e) {
            // If direct navigation fails, try URL
            driver.get("https://www.tncstore.vn/account/profile");
        }
    }

    // Profile update methods
    public void enterName(String name) {
        if (!name.isEmpty()) {
            wait.until(ExpectedConditions.visibilityOf(nameField));
            nameField.clear();
            nameField.sendKeys(name);
        }
    }

    public void enterEmail(String email) {
        if (!email.isEmpty()) {
            wait.until(ExpectedConditions.visibilityOf(emailField));
            emailField.clear();
            emailField.sendKeys(email);
        }
    }

    public void enterPhone(String phone) {
        if (!phone.isEmpty()) {
            wait.until(ExpectedConditions.visibilityOf(phoneField));
            phoneField.clear();
            phoneField.sendKeys(phone);
        }
    }

    public void enterAddress(String address) {
        if (!address.isEmpty()) {
            wait.until(ExpectedConditions.visibilityOf(addressField));
            addressField.clear();
            addressField.sendKeys(address);
        }
    }

    public void clickSaveProfile() {
        wait.until(ExpectedConditions.elementToBeClickable(saveProfileButton));
        saveProfileButton.click();
    }

    // Password change methods
    public void enterCurrentPassword(String password) {
        wait.until(ExpectedConditions.visibilityOf(currentPasswordField));
        currentPasswordField.clear();
        currentPasswordField.sendKeys(password);
    }

    public void enterNewPassword(String password) {
        wait.until(ExpectedConditions.visibilityOf(newPasswordField));
        newPasswordField.clear();
        newPasswordField.sendKeys(password);
    }

    public void enterConfirmPassword(String password) {
        wait.until(ExpectedConditions.visibilityOf(confirmPasswordField));
        confirmPasswordField.clear();
        confirmPasswordField.sendKeys(password);
    }

    public void clickChangePassword() {
        wait.until(ExpectedConditions.elementToBeClickable(changePasswordButton));
        changePasswordButton.click();
    }

    // Validation methods
    public boolean isProfilePageLoaded() {
        try {
            return nameField.isDisplayed() && emailField.isDisplayed();
        } catch (Exception e) {
            return driver.getCurrentUrl().contains("profile") ||
                   driver.getCurrentUrl().contains("account");
        }
    }

    public boolean isSuccessMessageDisplayed() {
        try {
            return successMessage.isDisplayed();
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

    public boolean isRequiredFieldErrorDisplayed() {
        try {
            return requiredFieldError.isDisplayed();
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

    // Check if user is logged in
    public boolean isUserLoggedIn() {
        return driver.getPageSource().contains("Đăng xuất") ||
               driver.getCurrentUrl().contains("account") ||
               !isLoginPopupDisplayed();
    }

    // Complete workflows
    public void updateProfile(String name, String email, String phone, String address) {
        navigateToProfile();
        enterName(name);
        enterEmail(email);
        enterPhone(phone);
        enterAddress(address);
        clickSaveProfile();
    }

    public void changePassword(String currentPassword, String newPassword, String confirmPassword) {
        enterCurrentPassword(currentPassword);
        enterNewPassword(newPassword);
        enterConfirmPassword(confirmPassword);
        clickChangePassword();
    }
}
