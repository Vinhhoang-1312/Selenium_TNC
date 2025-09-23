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

public class UserProfilePage {
    private WebDriver driver;
    private WebDriverWait wait;

    public UserProfilePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(TNCStoreConfig.ELEMENT_WAIT));
        PageFactory.initElements(driver, this);
    }

    // Profile navigation elements using centralized locators
    @FindBy(xpath = TNCStoreLocators.PROFILE_LINK)
    private WebElement profileLink;

    @FindBy(xpath = TNCStoreLocators.PROFILE_MENU_LINK)
    private WebElement profileMenuLink;

    // Profile form elements
    @FindBy(css = TNCStoreLocators.PROFILE_NAME_FIELD)
    private WebElement nameField;

    @FindBy(css = TNCStoreLocators.PROFILE_EMAIL_FIELD)
    private WebElement emailField;

    @FindBy(css = TNCStoreLocators.PROFILE_PHONE_FIELD)
    private WebElement phoneField;

    @FindBy(css = TNCStoreLocators.PROFILE_ADDRESS_FIELD)
    private WebElement addressField;

    @FindBy(xpath = TNCStoreLocators.SAVE_PROFILE_BUTTON)
    private WebElement saveProfileButton;

    // Password change elements
    @FindBy(css = TNCStoreLocators.CURRENT_PASSWORD_FIELD)
    private WebElement currentPasswordField;

    @FindBy(css = TNCStoreLocators.NEW_PASSWORD_FIELD)
    private WebElement newPasswordField;

    @FindBy(css = TNCStoreLocators.CONFIRM_PASSWORD_FIELD)
    private WebElement confirmPasswordField;

    @FindBy(xpath = TNCStoreLocators.CHANGE_PASSWORD_BUTTON)
    private WebElement changePasswordButton;

    // Message elements
    @FindBy(xpath = TNCStoreLocators.SUCCESS_MESSAGE)
    private WebElement successMessage;

    @FindBy(xpath = TNCStoreLocators.ERROR_MESSAGE_GENERAL)
    private WebElement errorMessage;

    @FindBy(xpath = TNCStoreLocators.EMAIL_EXISTS_ERROR)
    private WebElement emailExistsError;

    @FindBy(xpath = TNCStoreLocators.REQUIRED_FIELD_ERROR)
    private WebElement requiredFieldError;

    // Login popup for authentication check
    @FindBy(css = TNCStoreLocators.LOGIN_POPUP)
    private WebElement loginPopup;

    // Navigation methods
    public void navigateToProfile() {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(profileLink));
            profileLink.click();
            wait.until(ExpectedConditions.visibilityOf(nameField));
        } catch (Exception e) {
            // If direct navigation fails, try URL
            driver.get(TNCStoreConfig.PROFILE_URL);
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
