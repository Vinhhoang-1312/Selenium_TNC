package pages;

<<<<<<< HEAD
import locators.TNCStoreLocators;
import config.TNCStoreConfig;
=======
import locators.authenticationLocators;
>>>>>>> 2b511788fd639212808e02d46f60d65effaea549
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
<<<<<<< HEAD
import java.time.Duration;
=======
>>>>>>> 2b511788fd639212808e02d46f60d65effaea549

public class UserProfilePage extends BasePage {

    private static final Logger log = LoggerFactory.getLogger(UserProfilePage.class);

    public UserProfilePage(WebDriver driver) {
        super(driver);
    }

    // Profile navigation elements using centralized locators
<<<<<<< HEAD
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
=======
    @FindBy(xpath = authenticationLocators.PROFILE_LINK)
    private WebElement profileLink;

    @FindBy(xpath = authenticationLocators.PROFILE_MENU_LINK)
    private WebElement profileMenuLink;

    // Profile form elements
    @FindBy(css = authenticationLocators.PROFILE_NAME_FIELD)
    private WebElement nameField;

    @FindBy(css = authenticationLocators.PROFILE_EMAIL_FIELD)
    private WebElement emailField;

    @FindBy(css = authenticationLocators.PROFILE_PHONE_FIELD)
    private WebElement phoneField;

    @FindBy(css = authenticationLocators.PROFILE_ADDRESS_FIELD)
    private WebElement addressField;

    @FindBy(xpath = authenticationLocators.SAVE_PROFILE_BUTTON)
    private WebElement saveProfileButton;

    // Password change elements
    @FindBy(css = authenticationLocators.CURRENT_PASSWORD_FIELD)
    private WebElement currentPasswordField;

    @FindBy(css = authenticationLocators.NEW_PASSWORD_FIELD)
    private WebElement newPasswordField;

    @FindBy(css = authenticationLocators.CONFIRM_PASSWORD_FIELD)
    private WebElement confirmPasswordField;

    @FindBy(xpath = authenticationLocators.CHANGE_PASSWORD_BUTTON)
>>>>>>> 2b511788fd639212808e02d46f60d65effaea549
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
