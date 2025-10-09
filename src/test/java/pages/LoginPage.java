package pages;

import io.qameta.allure.Allure;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

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

    /**
     * Opens login popup with retry mechanism and popup handling
     */
    public void openLoginPopup() {
        Allure.step("Open login popup", () -> {
            try {
                clickElementWithRetry(accountButton, "account button");
                waitForElementToBeVisible(loginPopup);
            } catch (Exception e) {
                logger.error("Cannot open login popup", e);
                throw new RuntimeException("Cannot open login popup", e);
            }
        });
    }

    public LoginPage setEmail(String email) {
        Allure.step("Set login email: " + email, () -> {
            clearAndType(loginEmailField, email);
        });
        return this;
    }

    public LoginPage setPassword(String password) {
        Allure.step("Set login password", () -> {
            clearAndType(loginPasswordField, password);
        });
        return this;
    }

    public void submitLogin() {
        Allure.step("Submit login form", () -> {
            clickElementWithRetry(loginButton, "login button");
        });
    }

    public void performLogin(String email, String password) {
        Allure.step("Login with email: " + email, () -> {
            try {
                openLoginPopup();
                setEmail(email).setPassword(password).submitLogin();
                logger.info("Login performed for: {}", email);
            } catch (Exception e) {
                logger.error("Cannot perform login", e);
                throw new RuntimeException("Cannot perform login", e);
            }
        });
    }

    public boolean isLoginSuccessful() {
        try {
            // Wait for page to stabilize after login
            waitForElementToBeVisible(loggedInUserName);
            if (isDisplayed(loggedInUserName)) {
                WebElement accountElement = waitAndFind(loggedInUserName);
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

    public RegisterPage navigateToRegister() {
        return Allure.step("Navigate to registration form", () -> {
            try {
                openLoginPopup();
                clickElementWithRetry(createAccountLink, "create account link");
                return new RegisterPage(driver);
            } catch (Exception e) {
                logger.error("Failed to navigate to register page", e);
                throw new RuntimeException("Cannot navigate to register page", e);
            }
        });
    }
}
