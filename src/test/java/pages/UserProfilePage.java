package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserProfilePage extends BasePage {
    private static final Logger log = LoggerFactory.getLogger(UserProfilePage.class);

    // Locators nội bộ
    private static final By PROFILE_LINK = By.xpath("//a[contains(@href,'profile') or contains(@href,'account')]");
    private static final By PROFILE_MENU_LINK = By.xpath("//a[contains(text(),'Thông tin cá nhân')]");
    private static final By PROFILE_NAME_FIELD = By.cssSelector("#profile-name");
    private static final By PROFILE_EMAIL_FIELD = By.cssSelector("#profile-email");
    private static final By PROFILE_PHONE_FIELD = By.cssSelector("#profile-phone");
    private static final By PROFILE_ADDRESS_FIELD = By.cssSelector("#profile-address");
    private static final By SAVE_PROFILE_BUTTON = By.xpath("//button[contains(text(),'Lưu thông tin')]");
    private static final By CURRENT_PASSWORD_FIELD = By.cssSelector("#current-password");
    private static final By NEW_PASSWORD_FIELD = By.cssSelector("#new-password");
    private static final By CONFIRM_PASSWORD_FIELD = By.cssSelector("#confirm-password");
    private static final By CHANGE_PASSWORD_BUTTON = By.xpath("//button[contains(text(),'Đổi mật khẩu')]");

    public UserProfilePage(WebDriver driver) {
        super(driver);
    }

    // Profile navigation elements
    @FindBy(xpath = "//a[contains(@href,'profile') or contains(@href,'account')]")
    private WebElement profileLink;

    @FindBy(xpath = "//a[contains(text(),'Thông tin cá nhân')]")
    private WebElement profileMenuLink;

    // Profile form elements
    @FindBy(css = "#profile-name")
    private WebElement nameField;

    @FindBy(css = "#profile-email")
    private WebElement emailField;

    @FindBy(css = "#profile-phone")
    private WebElement phoneField;

    @FindBy(css = "#profile-address")
    private WebElement addressField;

    @FindBy(xpath = "//button[contains(text(),'Lưu thông tin')]")
    private WebElement saveProfileButton;

    // Password change elements
    @FindBy(css = "#current-password")
    private WebElement currentPasswordField;

    @FindBy(css = "#new-password")
    private WebElement newPasswordField;

    @FindBy(css = "#confirm-password")
    private WebElement confirmPasswordField;

    @FindBy(xpath = "//button[contains(text(),'Đổi mật khẩu')]")
    private WebElement changePasswordButton;

    // ========== NAVIGATION METHODS ==========
    public void navigateToProfile() {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(profileLink));
            profileLink.click();
            log.info("Navigated to profile page");
        } catch (Exception e) {
            log.error("Failed to navigate to profile: {}", e.getMessage());
            throw new RuntimeException("Cannot navigate to profile page", e);
        }
    }

    // ========== PROFILE UPDATE METHODS ==========
    public void updateProfile(String name, String phone, String address) {
        try {
            if (name != null && !name.isEmpty()) {
                wait.until(ExpectedConditions.elementToBeClickable(nameField));
                nameField.clear();
                nameField.sendKeys(name);
            }

            if (phone != null && !phone.isEmpty()) {
                wait.until(ExpectedConditions.elementToBeClickable(phoneField));
                phoneField.clear();
                phoneField.sendKeys(phone);
            }

            if (address != null && !address.isEmpty()) {
                wait.until(ExpectedConditions.elementToBeClickable(addressField));
                addressField.clear();
                addressField.sendKeys(address);
            }

            saveProfileButton.click();
            log.info("Profile updated successfully");

        } catch (Exception e) {
            log.error("Failed to update profile: {}", e.getMessage());
            throw new RuntimeException("Cannot update profile", e);
        }
    }

    // ========== PASSWORD CHANGE METHODS ==========
    public void changePassword(String currentPassword, String newPassword, String confirmPassword) {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(currentPasswordField));
            currentPasswordField.clear();
            currentPasswordField.sendKeys(currentPassword);

            wait.until(ExpectedConditions.elementToBeClickable(newPasswordField));
            newPasswordField.clear();
            newPasswordField.sendKeys(newPassword);

            wait.until(ExpectedConditions.elementToBeClickable(confirmPasswordField));
            confirmPasswordField.clear();
            confirmPasswordField.sendKeys(confirmPassword);

            changePasswordButton.click();
            log.info("Password change request submitted");

        } catch (Exception e) {
            log.error("Failed to change password: {}", e.getMessage());
            throw new RuntimeException("Cannot change password", e);
        }
    }

    // ========== VALIDATION METHODS ==========
    public boolean isProfilePageLoaded() {
        try {
            return wait.until(ExpectedConditions.visibilityOf(nameField)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public String getProfileName() {
        try {
            return nameField.getAttribute("value");
        } catch (Exception e) {
            return "";
        }
    }

    public String getProfileEmail() {
        try {
            return emailField.getAttribute("value");
        } catch (Exception e) {
            return "";
        }
    }

    public String getProfilePhone() {
        try {
            return phoneField.getAttribute("value");
        } catch (Exception e) {
            return "";
        }
    }

    public String getProfileAddress() {
        try {
            return addressField.getAttribute("value");
        } catch (Exception e) {
            return "";
        }
    }
}
