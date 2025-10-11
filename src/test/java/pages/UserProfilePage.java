package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import utils.Reporter;

public class UserProfilePage extends BasePage {

    private final By profileLink = By.xpath("//a[@href='?view=change-info']");
    private final By nameField = By.cssSelector("#profile-name");
    private final By emailField = By.cssSelector("#profile-email");
    private final By phoneField = By.cssSelector("#profile-phone");
    private final By phoneInput = By.id("mobile");
    private final By fullnameInput = By.id("fullname");
    private final By addressField = By.cssSelector("#profile-address");
    private final By addressInput = By.id("address");
    private final By saveProfileButton = By.xpath("//button[contains(text(),'Lưu thông tin')]");
    private final By saveButton = By.cssSelector("button.btn-submit");

    // Password change fields
    private final By currentPasswordField = By.cssSelector("#current-password");
    private final By newPasswordField = By.cssSelector("#new-password");
    private final By confirmPasswordField = By.cssSelector("#confirm-password");
    private final By changePasswordButton = By.xpath("//button[contains(text(),'Đổi mật khẩu')]");

    public UserProfilePage(WebDriver driver) {
        super(driver);
    }

    public void navigateToProfile() {
        Reporter.Action("Navigate to user profile page");
        try {
            click(profileLink);
            logger.info("Navigated to profile page");
            Reporter.Success("Profile page opened");
        } catch (Exception e) {
            logger.error("Failed to navigate to profile: {}", e.getMessage());
            Reporter.Error("Failed to navigate to profile: " + e.getMessage());
            throw new RuntimeException("Cannot navigate to profile page", e);
        }
    }

    public void updatePhone(String phone) {
        Reporter.Action("Update phone number to: " + phone);
        try {
            WebElement phoneElement = waitAndFind(phoneInput);
            jsUtils.scrollToElement(phoneElement);
            clearAndType(phoneInput, phone);
            click(saveButton);
            logger.info("Phone updated to: {}", phone);
            Reporter.Success("Phone updated successfully");
        } catch (Exception e) {
            logger.error("Failed to update phone: {}", e.getMessage());
            Reporter.Error("Failed to update phone: " + e.getMessage());
            throw new RuntimeException("Cannot update phone", e);
        }
    }

    public void updateFullname(String fullname) {
        Reporter.Action("Update fullname to: " + fullname);
        try {
            WebElement fullnameElement = waitAndFind(fullnameInput);
            jsUtils.scrollToElement(fullnameElement);
            clearAndType(fullnameInput, fullname);
            click(saveButton);
            logger.info("Fullname updated to: {}", fullname);
            Reporter.Success("Fullname updated successfully");
        } catch (Exception e) {
            logger.error("Failed to update fullname: {}", e.getMessage());
            Reporter.Error("Failed to update fullname: " + e.getMessage());
            throw new RuntimeException("Cannot update fullname", e);
        }
    }

    public void updateAddress(String address) {
        Reporter.Action("Update address to: " + address);
        try {
            WebElement addressElement = waitAndFind(addressInput);
            jsUtils.scrollToElement(addressElement);
            clearAndType(addressInput, address);
            click(saveButton);
            logger.info("Address updated to: {}", address);
            Reporter.Success("Address updated successfully");
        } catch (Exception e) {
            logger.error("Failed to update address: {}", e.getMessage());
            Reporter.Error("Failed to update address: " + e.getMessage());
            throw new RuntimeException("Cannot update address", e);
        }
    }

    public String getPhone() {
        Reporter.Action("Get phone number from profile");
        try {
            String phone = waitAndFind(phoneInput).getAttribute("value");
            Reporter.Success("Retrieved phone: " + phone);
            return phone;
        } catch (Exception e) {
            Reporter.Warn("Failed to get phone: " + e.getMessage());
            return "";
        }
    }

    public String getFullname() {
        Reporter.Action("Get fullname from profile");
        try {
            String fullname = waitAndFind(fullnameInput).getAttribute("value");
            Reporter.Success("Retrieved fullname: " + fullname);
            return fullname;
        } catch (Exception e) {
            Reporter.Warn("Failed to get fullname: " + e.getMessage());
            return "";
        }
    }

    public String getAddress() {
        Reporter.Action("Get address from profile");
        try {
            String address = waitAndFind(addressInput).getAttribute("value");
            Reporter.Success("Retrieved address: " + address);
            return address;
        } catch (Exception e) {
            Reporter.Warn("Failed to get address: " + e.getMessage());
            return "";
        }
    }

    public boolean isProfilePageLoaded() {
        Reporter.Action("Check if profile page loaded successfully");
        try {
            waitForElementToBeVisible(nameField);
            boolean loaded = isDisplayed(nameField) || isDisplayed(phoneInput) || isDisplayed(fullnameInput);
            if (loaded) {
                Reporter.Success("Profile page loaded successfully");
            } else {
                Reporter.Warn("Profile page did not load");
            }
            return loaded;
        } catch (Exception e) {
            logger.warn("Profile page did not load successfully", e);
            Reporter.Warn("Profile page load failed: " + e.getMessage());
            return false;
        }
    }
}