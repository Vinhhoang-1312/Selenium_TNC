package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProductDetailPage extends BasePage {
    private static final Logger logger = LoggerFactory.getLogger(ProductDetailPage.class);
    private final Actions actions;

    private final By addToCartButton = By.xpath("//a[contains(text(),'Thêm vào giỏ hàng')]");
    private final By cartIcon = By.xpath("//a[@id='js-header-cart']");
    private final By viewCartLink = By.xpath("//a[@class='btn-goCart']");
    private final By loadingSpinner = By.xpath("//div[contains(@class, 'loading-spinner')]");
    private final By quantityInput = By.xpath("//input[@id='js-buy-quantity']");
    private final By productName = By.xpath("//h1[@class='name']");
    private final By successNotification = By.xpath("//div[@class='content-container']");

    public ProductDetailPage(WebDriver driver) {
        super(driver);
        this.actions = new Actions(driver);
    }

    public void addToCart() {
        waitForElementToDisappear(loadingSpinner);
        waitForElementToBeVisible(addToCartButton);
        waitForElementToBeClickable(addToCartButton).click();
        logger.info("Successfully added product to cart");
    }

    public void addToCart(int quantity) {
        waitForElementToDisappear(loadingSpinner);
        try {
            WebElement qtyInput = driver.findElement(quantityInput);
            waitForElementToBeVisible(quantityInput);
            waitForElementToBeClickable(quantityInput);
            qtyInput.click();
            qtyInput.clear();
            qtyInput.sendKeys(String.valueOf(quantity));
            logger.info("Set product quantity to: {}", quantity);
        } catch (org.openqa.selenium.NoSuchElementException e) {
            logger.info("Quantity input not found, defaulting to add one item");
        }
        waitForElementToBeVisible(addToCartButton);
        waitForElementToBeClickable(addToCartButton).click();
        logger.info("Successfully added {} product(s) to cart", quantity);
    }

    public void goToCart() {
        WebElement cartIconElement = driver.findElement(cartIcon);
        waitForElementToBeVisible(cartIcon);
        actions.moveToElement(cartIconElement).perform();
        logger.info("Successfully hovered over cart icon");

        waitForElementToBeVisible(viewCartLink);
        waitForElementToBeClickable(viewCartLink).click();
        logger.info("Successfully navigated to cart page");
    }

    public String getProductName() {
        return waitAndGetText(productName);
    }

    public boolean isSuccessNotificationDisplayed() {
        return isDisplayed(successNotification);
    }
}

