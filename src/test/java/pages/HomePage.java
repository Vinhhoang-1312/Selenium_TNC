package pages;

import data.AuthenticationTestData;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import helpers.PopupHandler;

import java.time.Duration;
import java.util.List;

public class HomePage extends BasePage {
    private static final Logger log = LoggerFactory.getLogger(HomePage.class);

    private final By searchBox = By.xpath("//input[@id='js-global-seach']");
    private final By searchButton = By.xpath("//span[contains(text(),'Tìm kiếm')]");
    private final By productTitleLinks = By.xpath("//div[@id='js-product-list']//a[@class='product-name line-clamp-2']");
    private final By loadingSpinner = By.xpath("//div[contains(@class, 'loading-spinner')]");
    private final PopupHandler popupHandler;

    public HomePage(WebDriver driver) {
        super(driver);
        this.popupHandler = new PopupHandler(driver);
    }

    public void searchProduct(String productName) {
        // Wait for search box to be visible and interactive
//        waitForPageLoad();
        waitForElementToBeVisible(searchBox);
        WebElement searchInput = driver.findElement(searchBox);
        searchInput.clear();
        searchInput.sendKeys(productName);

        // Click search button if it exists
        List<WebElement> searchButtons = driver.findElements(searchButton);
        if (!searchButtons.isEmpty()) {
            searchButtons.get(0).click();
        }

        // Wait for loading to complete
        waitForElementToDisappear(loadingSpinner);
        log.info("Successfully searched for product: {}", productName);
    }

    public void clickProduct() {
        // Wait for products to be visible after search
        waitForElementToBeVisible(productTitleLinks);
        List<WebElement> availableProducts = driver.findElements(productTitleLinks);

        if (!availableProducts.isEmpty()) {
            WebElement firstProduct = availableProducts.get(0);
            waitForElementToBeClickable(firstProduct);
            firstProduct.click();
            log.info("Successfully clicked product");
        } else {
            log.error("No products found on the page");
            log.error("Current URL: {}", driver.getCurrentUrl());
            throw new RuntimeException("No products found on homepage - Please check if products are loaded correctly");
        }
    }

    public void clickFirstNormalProduct() {
        List<WebElement> products = driver.findElements(By.cssSelector(".product-box"));
        for (WebElement product : products) {
            List<WebElement> saleTag = product.findElements(By.cssSelector(".old-price"));
            if (saleTag.isEmpty()) {
                product.click();
                return;
            }
        }
        throw new RuntimeException("No normal product found");
    }

    public void logIn() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        By accountButton = By.xpath("//a[contains(@class, 'account')]");
        By emailField = By.xpath("//input[@id='js-login-email']");
        By passwordField = By.xpath("//input[@id='js-login-password']");
        By loginButton = By.xpath("//a[@class='btn-submit']");
        int maxAttempts = 3;
        for (int attempt = 1; attempt <= maxAttempts; attempt++) {
            try {
                log.info("[LOGIN] Opening login popup (attempt {}/{})...", attempt, maxAttempts);
                popupHandler.dismissAllPopups();
                wait.until(ExpectedConditions.elementToBeClickable(accountButton));
                try {
                    driver.findElement(accountButton).click();
                } catch (org.openqa.selenium.ElementClickInterceptedException e) {
                    log.warn("Normal click failed, trying JavaScript click for accountButton");
                    WebElement btn = driver.findElement(accountButton);
                    ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].click();", btn);
                }
                Thread.sleep(500);
                wait.until(ExpectedConditions.visibilityOfElementLocated(emailField));
                driver.findElement(emailField).clear();
                driver.findElement(emailField).sendKeys(AuthenticationTestData.VALID_EMAIL);
                driver.findElement(passwordField).clear();
                driver.findElement(passwordField).sendKeys(AuthenticationTestData.VALID_PASSWORD);
                wait.until(ExpectedConditions.elementToBeClickable(loginButton)).click();
                Thread.sleep(1000);
                return;
            } catch (Exception e) {
                log.warn("[LOGIN] Error when opening login popup (attempt {}): {}", attempt, e.getMessage());
                if (attempt < maxAttempts) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                    }
                } else {
                    throw new RuntimeException("Cannot login after " + maxAttempts + " attempts", e);
                }
            }
        }
    }

    public void goToProfilePage() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.urlContains("/taikhoan"));
        By changeInfoLink = By.xpath("//a[@href='?view=change-info']");
        try {
            wait.until(ExpectedConditions.elementToBeClickable(changeInfoLink)).click();
            log.info("[PROFILE] Đã click vào link đổi thông tin cá nhân");
        } catch (Exception e) {
            log.error("[PROFILE] Không thể click vào link đổi thông tin cá nhân: {}", e.getMessage());
            throw new RuntimeException("Không thể click vào link đổi thông tin cá nhân", e);
        }
    }

}
