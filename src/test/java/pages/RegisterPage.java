package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import utils.Reporter;

public class RegisterPage extends BasePage {

    private final By registerNameField = By.xpath("//input[@id='js-popup-register-name']");
    private final By registerEmailField = By.xpath("//input[@id='js-popup-register-email']");
    private final By registerPasswordField = By.xpath("//input[@id='js-popup-register-password']");
    private final By registerButton = By.xpath("//a[@class='btn-submit']");
    private final By errorMessage = By.cssSelector(".error-message, .note-error");
    private final By successMessage = By.cssSelector(".success-message");
    private final By emailErrorMessage = By.xpath("//span[@id='js-register-email-error']");
    private final By passwordErrorMessage = By.xpath("//span[@id='js-register-password-error']");
    private final By nameErrorMessage = By.xpath("//span[@id='js-register-name-error']");

    public RegisterPage(WebDriver driver) {
        super(driver);
    }

    public RegisterPage setName(String name) {
        Reporter.Action("Set registration name: " + name);
        try {
            clearAndType(registerNameField, name);
            return this;
        } catch (Exception e) {
            logger.error("Failed to set name: {}", e.getMessage());
            Reporter.Error("Failed to set name: " + e.getMessage());
            throw new RuntimeException("Cannot set name", e);
        }
    }

    public RegisterPage setEmail(String email) {
        Reporter.Action("Set registration email: " + email);
        try {
            clearAndType(registerEmailField, email);
            return this;
        } catch (Exception e) {
            logger.error("Failed to set email: {}", e.getMessage());
            Reporter.Error("Failed to set email: " + e.getMessage());
            throw new RuntimeException("Cannot set email", e);
        }
    }

    public RegisterPage setPassword(String password) {
        Reporter.Action("Set registration password");
        try {
            clearAndType(registerPasswordField, password);
            return this;
        } catch (Exception e) {
            logger.error("Failed to set password: {}", e.getMessage());
            Reporter.Error("Failed to set password: " + e.getMessage());
            throw new RuntimeException("Cannot set password", e);
        }
    }

    public void submitRegister() {
        Reporter.Action("Submit registration form");
        click(registerButton);
    }

    public void performRegister(String name, String email, String password) {
        Reporter.Action("Register new user with email: " + email);
        try {
            setName(name).setEmail(email).setPassword(password).submitRegister();
            waitForPageStability();
            logger.info("Registration submitted for email: {}", email);
            Reporter.Success("Registration submitted successfully");
        } catch (Exception e) {
            logger.error("Cannot perform registration", e);
            Reporter.Error("Registration failed: " + e.getMessage());
            throw new RuntimeException("Cannot perform registration", e);
        }
    }

}