package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import utils.Reporter;

public class LoginPage extends BasePage {

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

    public void openLoginPopup() {
        Reporter.Action("Open login popup");
        try {
            clickElementWithRetry(accountButton, "account button");
            waitForElementToBeVisible(loginPopup);
            Reporter.Success("Login popup opened successfully");
        } catch (Exception e) {
            logger.error("Cannot open login popup", e);
            Reporter.Error("Failed to open login popup: " + e.getMessage());
            throw new RuntimeException("Cannot open login popup", e);
        }
    }

    public LoginPage setEmail(String email) {
        Reporter.Action("Set login email: " + email);
        clearAndType(loginEmailField, email);
        return this;
    }

    public LoginPage setPassword(String password) {
        Reporter.Action("Set login password");
        clearAndType(loginPasswordField, password);
        return this;
    }

    public void submitLogin() {
        Reporter.Action("Submit login form");
        clickElementWithRetry(loginButton, "login button");
    }

    public void performLogin(String email, String password) {
        Reporter.Action("Login with email: " + email);
        try {
            openLoginPopup();
            setEmail(email).setPassword(password).submitLogin();
            logger.info("Login performed for: {}", email);
            Reporter.Success("Login action completed");
        } catch (Exception e) {
            logger.error("Cannot perform login", e);
            Reporter.Error("Login failed: " + e.getMessage());
            throw new RuntimeException("Cannot perform login", e);
        }
    }
//
    public boolean isLoginSuccessful() {
        try {
            waitForElementToBeVisible(loggedInUserName);
            if (isDisplayed(loggedInUserName)) {
                WebElement accountElement = waitAndFind(loggedInUserName);
                String accountText = accountElement.getText().trim();
                if (!accountText.isEmpty() && !accountText.equals("Tài khoản") && !accountText.equals("Account")) {
                    Reporter.Success("Login successful, user: " + accountText);
                    return true;
                }
            }
            Reporter.Warn("Login verification failed");
            return false;
        } catch (Exception e) {
            Reporter.Warn("Login verification failed: " + e.getMessage());
            return false;
        }
    }

    public RegisterPage navigateToRegister() {
        Reporter.Action("Navigate to registration form");
        try {
            openLoginPopup();
            clickElementWithRetry(createAccountLink, "create account link");
            Reporter.Success("Navigated to register page");
            return new RegisterPage(driver);
        } catch (Exception e) {
            logger.error("Failed to navigate to register page", e);
            Reporter.Error("Failed to navigate to register page: " + e.getMessage());
            throw new RuntimeException("Cannot navigate to register page", e);
        }
    }
}
