package pages;

import helpers.PopupHandler;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RegisterPage extends BasePage {
    private static final Logger logger = LoggerFactory.getLogger(RegisterPage.class);
    private final PopupHandler popupHandler;

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
        this.popupHandler = new PopupHandler(driver);
    }

    public RegisterPage setName(String name) {
        try {
            waitForElementToBeVisible(registerNameField);
            waitForElementToBeClickable(registerNameField);
            WebElement nameField = driver.findElement(registerNameField);
            nameField.clear();
            nameField.sendKeys(name);
        } catch (Exception e) {
            logger.error("Failed to set name: {}", e.getMessage());
            throw new RuntimeException("Cannot set name", e);
        }
        return this;
    }

    public RegisterPage setEmail(String email) {
        try {
            waitForElementToBeVisible(registerEmailField);
            waitForElementToBeClickable(registerEmailField);
            WebElement emailField = driver.findElement(registerEmailField);
            emailField.clear();
            emailField.sendKeys(email);
        } catch (Exception e) {
            logger.error("Failed to set email: {}", e.getMessage());
            throw new RuntimeException("Cannot set email", e);
        }
        return this;
    }

    public RegisterPage setPassword(String password) {
        try {
            waitForElementToBeClickable(registerPasswordField);
            WebElement passwordField = driver.findElement(registerPasswordField);
            passwordField.clear();
            passwordField.sendKeys(password);
        } catch (Exception e) {
            logger.error("Failed to set password: {}", e.getMessage());
            throw new RuntimeException("Cannot set password", e);
        }
        return this;
    }

    public void submitRegister() {
        clickElementWithRetry(registerButton, "register button");
    }

    /**
     * Performs full registration with name, email and password
     */
    public void performRegister(String name, String email, String password) {
        try {
            setName(name).setEmail(email).setPassword(password).submitRegister();
            logger.info("Registration submitted for email: {}", email);
        } catch (Exception e) {
            logger.error("Cannot perform registration", e);
            throw new RuntimeException("Cannot perform registration", e);
        }
    }

    /**
     * Click element with retry and popup handling
     */
    private void clickElementWithRetry(By by, String elementName) {
        int maxAttempts = 3;
        for (int attempt = 1; attempt <= maxAttempts; attempt++) {
            try {
                popupHandler.dismissAllPopups();
                waitForElementToBeClickable(by);
                driver.findElement(by).click();
                return;
            } catch (ElementClickInterceptedException e) {
                try {
                    popupHandler.dismissAllPopups();
                    WebElement el = driver.findElement(by);
                    ((JavascriptExecutor) driver).executeScript("arguments[0].click();", el);
                    return;
                } catch (Exception jsError) {
                    if (attempt == maxAttempts) {
                        throw new RuntimeException("Failed to click " + elementName + " after " + maxAttempts + " attempts", e);
                    }
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                    }
                }
            } catch (Exception e) {
                if (attempt == maxAttempts) {
                    throw new RuntimeException("Failed to click " + elementName + " after " + maxAttempts + " attempts", e);
                }
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    public String getErrorMessage() {
        try {
            return waitAndGetText(errorMessage);
        } catch (Exception e) {
            return "";
        }
    }

    public String getEmailError() {
        try {
            return waitAndGetText(emailErrorMessage);
        } catch (Exception e) {
            return "";
        }
    }

    public String getPasswordError() {
        try {
            return waitAndGetText(passwordErrorMessage);
        } catch (Exception e) {
            return "";
        }
    }

    public String getNameError() {
        try {
            return waitAndGetText(nameErrorMessage);
        } catch (Exception e) {
            return "";
        }
    }

    public String getSuccessMessage() {
        try {
            return waitAndGetText(successMessage);
        } catch (Exception e) {
            return "";
        }
    }

    public boolean isRegistrationSuccessful() {
        try {
            customWait(2000);
            String success = getSuccessMessage();
            return success != null && !success.isEmpty();
        } catch (Exception e) {
            return false;
        }
    }
}
