package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoginPage extends BasePage {
    private static final Logger logger = LoggerFactory.getLogger(LoginPage.class);

    private final By accountButton = By.xpath("//a[contains(@class,'item') and contains(@class,'account')]//span[contains(@class,'hover-txt')]");
    private final By loginPopup = By.cssSelector("#js-form-holder");
    private final By loginEmailField = By.xpath("//input[@id='js-login-email']");
    private final By loginPasswordField = By.xpath("//input[@id='js-login-password']");
    private final By loginButton = By.xpath("//a[@class='btn-submit']");
    private final By logoutLink = By.xpath("//a[contains(text(),'Đăng xuất') or contains(text(),'Logout')]");
    private final By createAccountLink = By.xpath("//*[@id='js-form-login']/div[2]/div[4]/a");
    private final By emailErrorMessage = By.xpath("//span[@id='js-login-email-error']");
    private final By passwordErrorMessage = By.xpath("//span[@id='js-login-password-error']");
    private final By generalErrorMessage = By.xpath("//div[contains(@class,'error-message')]");
    private final By loggedInUserName = By.xpath("//span[@class='hover-txt line-clamp-1']");

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    /**
     * Opens login popup with retry mechanism and popup handling
     */
    public void openLoginPopup() {
        int maxAttempts = 2;
        for (int attempt = 1; attempt <= maxAttempts; attempt++) {
            try {
                popupHandler.dismissAllPopups();
                waitForElementToBeClickable(accountButton);
                try {
                    driver.findElement(accountButton).click();
                } catch (ElementClickInterceptedException e) {
                    WebElement btn = driver.findElement(accountButton);
                    ((JavascriptExecutor) driver).executeScript("arguments[0].click();", btn);
                }
                customWait(500);
                waitForElementToBeVisible(loginPopup);
                return;
            } catch (Exception e) {
                if (attempt < maxAttempts) {
                    try {
                        customWait(1000);
                    } catch (Exception ie) {
                        Thread.currentThread().interrupt();
                    }
                } else {
                    logger.error("Cannot open login popup after {} attempts", maxAttempts, e);
                    throw new RuntimeException("Cannot open login popup after " + maxAttempts + " attempts", e);
                }
            }
        }
    }

    public LoginPage setEmail(String email) {
        waitForElementToBeClickable(loginEmailField);
        clearAndType(loginEmailField, email);
        return this;
    }

    public LoginPage setPassword(String password) {
        waitForElementToBeClickable(loginPasswordField);
        clearAndType(loginPasswordField, password);
        return this;
    }

    public void submitLogin() {
        clickElementWithRetry(loginButton, "login button");
    }

    public void performLogin(String email, String password) {
        try {
            openLoginPopup();
            setEmail(email).setPassword(password).submitLogin();
        } catch (Exception e) {
            logger.error("Cannot perform login", e);
            throw new RuntimeException("Cannot perform login", e);
        }
    }

    public boolean isLoginSuccessful() {
        try {
            customWait(3000);
            if (isDisplayed(loggedInUserName)) {
                WebElement accountElement = driver.findElement(loggedInUserName);
                String accountText = accountElement.getText().trim();
                // If text is not default "Tài khoản" or "Account", user is logged in
                if (!accountText.isEmpty() && !accountText.equals("Tài khoản") && !accountText.equals("Account")) {
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    public String getLoggedInUserName() {
        try {
            if (isDisplayed(loggedInUserName)) {
                WebElement userNameEl = driver.findElement(loggedInUserName);
                return userNameEl.getText().trim();
            }
        } catch (Exception e) {
            logger.warn("Could not get logged in user name", e);
        }
        return "";
    }

    public boolean isUserLoggedIn() {
        return isLoginSuccessful();
    }

    public void logout() {
        try {
            if (isDisplayed(logoutLink)) {
                click(logoutLink);
                logger.info("User logged out successfully");
            }
        } catch (Exception e) {
            logger.warn("Logout failed or user not logged in");
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

    public String getGeneralError() {
        try {
            return waitAndGetText(generalErrorMessage);
        } catch (Exception e) {
            return "";
        }
    }

    public RegisterPage navigateToRegister() {
        try {
            openLoginPopup();
            clickElementWithRetry(createAccountLink, "create account link");
            return new RegisterPage(driver);
        } catch (Exception e) {
            logger.error("Failed to navigate to register page", e);
            throw new RuntimeException("Cannot navigate to register page", e);
        }
    }
}
