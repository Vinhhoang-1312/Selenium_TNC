package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ProductDetailPage extends BasePage {
    private final Actions actions;
    private static final Logger log = LoggerFactory.getLogger(ProductDetailPage.class);
    public static By addToCartButton = By.xpath("//a[contains(text(),'Thêm vào giỏ hàng')]");
    private final By cartIcon = By.xpath("//a[@id='js-header-cart']");
    private final By viewCartLink = By.xpath("//a[@class='btn-goCart']");
    private final By loadingSpinner = By.xpath("//div[contains(@class, 'loading-spinner')]");
    public static By itemNameInMainPage = By.xpath("//div[@id=\"js-product-cate-79\"]//div[@class=\"owl-item active\"][2]//a[contains(@class,'product-name')]");
    public static By itemInMainPage = By.xpath("//div[contains(@id,'79')]//div[@class='owl-item active'][2]");
    public static By productName = By.xpath("//h1[@class='name']");
    public static By successNotification = By.xpath("//div[@class='content-container']");
    public static By originPrice = By.xpath("//div[@class='info-main-price']//del[@class='old-price']");
    public static By Price = By.xpath("//div[@class='info-main-price']//div[@class='price']");
    public static By SaleOff = By.xpath("//div[@class='info-main-price']//div[@class='saleoff']");
    private final By quantityInput = By.xpath("//input[@id='js-buy-quantity']");


    public ProductDetailPage(WebDriver driver) {
        super(driver);
        this.actions = new Actions(driver);
    }

    public void addToCart() {
        waitForPageLoad();
        waitForElementToDisappear(loadingSpinner);
        waitForElementToBeVisible(addToCartButton);
        WebElement addToCartElement = driver.findElement(addToCartButton);
        waitForElementToBeClickable(addToCartElement);
        addToCartElement.click();
        log.info("Successfully added product to cart");
    }

    public void addToCart(int quantity) {
//        waitForPageLoad();
        waitForElementToDisappear(loadingSpinner);
        try {
            WebElement qtyInput = driver.findElement(quantityInput);
            waitForElementToBeVisible(quantityInput);
            waitForElementToBeClickable(qtyInput);
            qtyInput.click();
            qtyInput.clear();
            qtyInput.sendKeys(String.valueOf(quantity));
            log.info("Set product quantity to: {}", quantity);
        } catch (org.openqa.selenium.NoSuchElementException e) {
            log.info("Quantity input not found, defaulting to add one item");
        }
        waitForElementToBeVisible(addToCartButton);
        WebElement addToCartElement = driver.findElement(addToCartButton);
        waitForElementToBeClickable(addToCartElement);
        addToCartElement.click();
        log.info("Successfully added"+quantity+"product to cart");
    }

    public void goToCart() {
//        waitForPageLoad();

        // First hover over the cart icon
        WebElement cartIconElement = driver.findElement(cartIcon);
        waitForElementToBeVisible(cartIcon);
        actions.moveToElement(cartIconElement).perform();
        log.info("Successfully hovered over cart icon");

        // Then wait for and click the view cart link
        waitForElementToBeVisible(viewCartLink);
        WebElement cartElement = driver.findElement(viewCartLink);
        waitForElementToBeClickable(cartElement);
        cartElement.click();
        log.info("Successfully navigated to cart page");
    }
}
