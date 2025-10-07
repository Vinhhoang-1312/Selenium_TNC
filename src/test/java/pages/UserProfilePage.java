package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserProfilePage extends BasePage {
    private static final Logger logger = LoggerFactory.getLogger(UserProfilePage.class);

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
        try {
            waitForElementToBeClickable(profileLink).click();
            logger.info("Navigated to profile page");
        } catch (Exception e) {
            logger.error("Failed to navigate to profile: {}", e.getMessage());
            throw new RuntimeException("Cannot navigate to profile page", e);
        }
    }

    public void updateProfile(String name, String phone, String address) {
        try {
            if (name != null && !name.isEmpty()) {
                waitForElementToBeClickable(nameField);
                driver.findElement(nameField).clear();
                driver.findElement(nameField).sendKeys(name);
            }
            if (phone != null && !phone.isEmpty()) {
                waitForElementToBeClickable(phoneField);
                driver.findElement(phoneField).clear();
                driver.findElement(phoneField).sendKeys(phone);
            }
            if (address != null && !address.isEmpty()) {
                waitForElementToBeClickable(addressField);
                driver.findElement(addressField).clear();
                driver.findElement(addressField).sendKeys(address);
            }
            driver.findElement(saveProfileButton).click();
            logger.info("Profile updated successfully");
        } catch (Exception e) {
            logger.error("Failed to update profile: {}", e.getMessage());
            throw new RuntimeException("Cannot update profile", e);
        }
    }

    /**
     * Changes user password with current, new and confirm password
     */
    public void changePassword(String currentPassword, String newPassword, String confirmPassword) {
        try {
            waitForElementToBeClickable(currentPasswordField);
            driver.findElement(currentPasswordField).clear();
            driver.findElement(currentPasswordField).sendKeys(currentPassword);

            waitForElementToBeClickable(newPasswordField);
            driver.findElement(newPasswordField).clear();
            driver.findElement(newPasswordField).sendKeys(newPassword);

            waitForElementToBeClickable(confirmPasswordField);
            driver.findElement(confirmPasswordField).clear();
            driver.findElement(confirmPasswordField).sendKeys(confirmPassword);

            driver.findElement(changePasswordButton).click();
            logger.info("Password change request submitted");
        } catch (Exception e) {
            logger.error("Failed to change password: {}", e.getMessage());
            throw new RuntimeException("Cannot change password", e);
        }
    }

    public void updatePhone(String phone) {
        try {
            waitForElementToBeClickable(phoneInput);
            driver.findElement(phoneInput).clear();
            driver.findElement(phoneInput).sendKeys(phone);

            WebElement saveBtn = driver.findElement(saveButton);
            try {
                saveBtn.click();
            } catch (org.openqa.selenium.ElementClickInterceptedException e) {
                // Handle click interception with scroll and retry
                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(false);", saveBtn);
                waitForElementToBeClickable(saveButton);
                try {
                    saveBtn.click();
                } catch (Exception ex) {
                    ((JavascriptExecutor) driver).executeScript("arguments[0].click();", saveBtn);
                }
            } catch (Exception e) {
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", saveBtn);
            }
            logger.info("Phone updated to: {}", phone);
        } catch (Exception e) {
            logger.error("Failed to update phone: {}", e.getMessage());
            throw new RuntimeException("Cannot update phone", e);
        }
    }

    public void updateFullname(String fullname) {
        try {
            waitForElementToBeClickable(fullnameInput);
            driver.findElement(fullnameInput).clear();
            driver.findElement(fullnameInput).sendKeys(fullname);
            driver.findElement(saveButton).click();
            logger.info("Fullname updated to: {}", fullname);
        } catch (Exception e) {
            logger.error("Failed to update fullname: {}", e.getMessage());
            throw new RuntimeException("Cannot update fullname", e);
        }
    }

    public void updateAddress(String address) {
        try {
            waitForElementToBeClickable(addressInput);
            driver.findElement(addressInput).clear();
            driver.findElement(addressInput).sendKeys(address);
            driver.findElement(saveButton).click();
            logger.info("Address updated to: {}", address);
        } catch (Exception e) {
            logger.error("Failed to update address: {}", e.getMessage());
            throw new RuntimeException("Cannot update address", e);
        }
    }

    // Getters
    public String getPhone() {
        try {
            return driver.findElement(phoneInput).getAttribute("value");
        } catch (Exception e) {
            return "";
        }
    }

    public String getFullname() {
        try {
            return driver.findElement(fullnameInput).getAttribute("value");
        } catch (Exception e) {
            return "";
        }
    }

    public String getAddress() {
        try {
            return driver.findElement(addressInput).getAttribute("value");
        } catch (Exception e) {
            return "";
        }
    }

    public String getProfileName() {
        try {
            return driver.findElement(nameField).getAttribute("value");
        } catch (Exception e) {
            return "";
        }
    }

    public String getProfileEmail() {
        try {
            return driver.findElement(emailField).getAttribute("value");
        } catch (Exception e) {
            return "";
        }
    }

    public String getProfilePhone() {
        try {
            return driver.findElement(phoneField).getAttribute("value");
        } catch (Exception e) {
            return "";
        }
    }

    public String getProfileAddress() {
        try {
            return driver.findElement(addressField).getAttribute("value");
        } catch (Exception e) {
            return "";
        }
    }

    public boolean isProfilePageLoaded() {
        try {
            return waitForElementToBeVisible(nameField).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
}
