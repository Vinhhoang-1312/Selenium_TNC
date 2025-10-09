package pages;

import io.qameta.allure.Allure;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class UserProfilePage extends BasePage {

    private final By profileLink = By.xpath("//a[contains(@href,'profile') or contains(@href,'account')]");
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
        Allure.step("Navigate to user profile page", () -> {
            try {
                click(profileLink);
                logger.info("Navigated to profile page");
            } catch (Exception e) {
                logger.error("Failed to navigate to profile: {}", e.getMessage());
                throw new RuntimeException("Cannot navigate to profile page", e);
            }
        });
    }

    public void updatePhone(String phone) {
        Allure.step("Update phone number to: " + phone, () -> {
            try {
                clearAndType(phoneInput, phone);
                clickElementWithRetry(saveButton, "save button");
                logger.info("Phone updated to: {}", phone);
            } catch (Exception e) {
                logger.error("Failed to update phone: {}", e.getMessage());
                throw new RuntimeException("Cannot update phone", e);
            }
        });
    }

    public void updateFullname(String fullname) {
        Allure.step("Update fullname to: " + fullname, () -> {
            try {
                clearAndType(fullnameInput, fullname);
                clickElementWithRetry(saveButton, "save button");
                logger.info("Fullname updated to: {}", fullname);
            } catch (Exception e) {
                logger.error("Failed to update fullname: {}", e.getMessage());
                throw new RuntimeException("Cannot update fullname", e);
            }
        });
    }

    public void updateAddress(String address) {
        Allure.step("Update address to: " + address, () -> {
            try {
                clearAndType(addressInput, address);
                clickElementWithRetry(saveButton, "save button");
                logger.info("Address updated to: {}", address);
            } catch (Exception e) {
                logger.error("Failed to update address: {}", e.getMessage());
                throw new RuntimeException("Cannot update address", e);
            }
        });
    }

    // Getters - Use waitAndFind instead of direct driver.findElement
    public String getPhone() {
        return Allure.step("Get phone number from profile", () -> {
            try {
                return waitAndFind(phoneInput).getAttribute("value");
            } catch (Exception e) {
                return "";
            }
        });
    }

    public String getFullname() {
        return Allure.step("Get fullname from profile", () -> {
            try {
                return waitAndFind(fullnameInput).getAttribute("value");
            } catch (Exception e) {
                return "";
            }
        });
    }

    public String getAddress() {
        return Allure.step("Get address from profile", () -> {
            try {
                return waitAndFind(addressInput).getAttribute("value");
            } catch (Exception e) {
                return "";
            }
        });
    }

    /**
     * Checks if profile page loaded successfully
     */
    public boolean isProfilePageLoaded() {
        return Allure.step("Check if profile page loaded successfully", () -> {
            try {
                waitForElementToBeVisible(nameField);
                return isDisplayed(nameField) || isDisplayed(phoneInput) || isDisplayed(fullnameInput);
            } catch (Exception e) {
                logger.warn("Profile page did not load successfully", e);
                return false;
            }
        });
    }
}
