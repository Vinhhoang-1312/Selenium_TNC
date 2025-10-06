package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserProfilePage extends BasePage {
    private static final Logger log = LoggerFactory.getLogger(UserProfilePage.class);

    private final By profileLink = By.xpath("//a[contains(@href,'profile') or contains(@href,'account')]");
    private final By profileMenuLink = By.xpath("//a[contains(text(),'Thông tin cá nhân')]");
    private final By nameField = By.cssSelector("#profile-name");
    private final By emailField = By.cssSelector("#profile-email");
    private final By phoneField = By.cssSelector("#profile-phone");
    private final By addressField = By.cssSelector("#profile-address");
    private final By saveProfileButton = By.xpath("//button[contains(text(),'Lưu thông tin')]");
    private final By currentPasswordField = By.cssSelector("#current-password");
    private final By newPasswordField = By.cssSelector("#new-password");
    private final By confirmPasswordField = By.cssSelector("#confirm-password");
    private final By changePasswordButton = By.xpath("//button[contains(text(),'Đổi mật khẩu')]");

    private final By fullnameInput = By.id("fullname");
    private final By phoneInput = By.id("mobile");
    private final By addressInput = By.id("address");
    private final By saveButton = By.cssSelector("button.btn-submit");

    public UserProfilePage(WebDriver driver) {
        super(driver);
    }

    public void navigateToProfile() {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(profileLink));
            driver.findElement(profileLink).click();
            log.info("Navigated to profile page");
        } catch (Exception e) {
            log.error("Failed to navigate to profile: {}", e.getMessage());
            throw new RuntimeException("Cannot navigate to profile page", e);
        }
    }

    public void updateProfile(String name, String phone, String address) {
        try {
            if (name != null && !name.isEmpty()) {
                wait.until(ExpectedConditions.elementToBeClickable(nameField));
                driver.findElement(nameField).clear();
                driver.findElement(nameField).sendKeys(name);
            }
            if (phone != null && !phone.isEmpty()) {
                wait.until(ExpectedConditions.elementToBeClickable(phoneField));
                driver.findElement(phoneField).clear();
                driver.findElement(phoneField).sendKeys(phone);
            }
            if (address != null && !address.isEmpty()) {
                wait.until(ExpectedConditions.elementToBeClickable(addressField));
                driver.findElement(addressField).clear();
                driver.findElement(addressField).sendKeys(address);
            }
            driver.findElement(saveProfileButton).click();
            log.info("Profile updated successfully");
        } catch (Exception e) {
            log.error("Failed to update profile: {}", e.getMessage());
            throw new RuntimeException("Cannot update profile", e);
        }
    }

    public void changePassword(String currentPassword, String newPassword, String confirmPassword) {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(currentPasswordField));
            driver.findElement(currentPasswordField).clear();
            driver.findElement(currentPasswordField).sendKeys(currentPassword);
            wait.until(ExpectedConditions.elementToBeClickable(newPasswordField));
            driver.findElement(newPasswordField).clear();
            driver.findElement(newPasswordField).sendKeys(newPassword);
            wait.until(ExpectedConditions.elementToBeClickable(confirmPasswordField));
            driver.findElement(confirmPasswordField).clear();
            driver.findElement(confirmPasswordField).sendKeys(confirmPassword);
            driver.findElement(changePasswordButton).click();
            log.info("Password change request submitted");
        } catch (Exception e) {
            log.error("Failed to change password: {}", e.getMessage());
            throw new RuntimeException("Cannot change password", e);
        }
    }

    public boolean isProfilePageLoaded() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(nameField)).isDisplayed();
        } catch (Exception e) {
            return false;
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

    public void updateFullname(String newFullname) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(fullnameInput)).clear();
        driver.findElement(fullnameInput).sendKeys(newFullname);
        driver.findElement(saveButton).click();
    }

    public void updatePhone(String newPhone) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(phoneInput)).clear();
        driver.findElement(phoneInput).sendKeys(newPhone);
        org.openqa.selenium.WebElement saveBtn = driver.findElement(saveButton);
        try {
            saveBtn.click();
        } catch (org.openqa.selenium.ElementClickInterceptedException e) {
            ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(false);", saveBtn);
            try {
                Thread.sleep(500);
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
            }
            try {
                saveBtn.click();
            } catch (Exception ex) {
                ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].click();", saveBtn);
            }
        } catch (Exception e) {
            ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].click();", saveBtn);
        }
    }

    public void updateAddress(String newAddress) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(addressInput)).clear();
        driver.findElement(addressInput).sendKeys(newAddress);
        driver.findElement(saveButton).click();
    }

    public String getFullname() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(fullnameInput)).getAttribute("value");
    }

    public String getPhone() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(phoneInput)).getAttribute("value");
    }

    public String getAddress() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(addressInput)).getAttribute("value");
    }
}
