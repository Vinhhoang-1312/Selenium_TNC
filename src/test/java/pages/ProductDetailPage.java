package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

public class ProductDetailPage extends BasePage {
    private final Actions actions;
    private static final Logger log = LoggerFactory.getLogger(ProductDetailPage.class);
    public static By addToCartButton = By.xpath("//a[contains(text(),'Thêm vào giỏ hàng')]");
    private final By cartIcon = By.xpath("//a[@id='js-header-cart']");
    private final By viewCartLink = By.xpath("//a[@class='btn-goCart']");
    private final By loadingSpinner = By.xpath("//div[contains(@class, 'loading-spinner')]");
    public static By SecondLaptopNameInMainPage = By.xpath("//div[contains(@id,'79')]//div[@class='owl-item active'][2]//a[contains(@class,'product-name')]");
    public static By SecondLaptopInMainPage = By.xpath("//div[contains(@id,'79')]//div[@class='owl-item active'][2]");
    public static By productName = By.xpath("//h1[@class='name']");
    public static By successNotification = By.xpath("//div[@class='content-container']");
    public static By originPrice = By.xpath("//div[@class='info-main-price']//del[@class='old-price']");
    public static By Price = By.xpath("//div[@class='info-main-price']//div[@class='price']");
    public static By SaleOff = By.xpath("//div[@class='info-main-price']//div[@class='saleoff']");
    public static By similarProduct = By.xpath("//div[contains(@class,'similar')]//div[@class='owl-item active'][2]");
    public static By viewedProduct = By.xpath("//div[@class='product-history']//div[@class='product-list']");
    public static By quantityInput = By.xpath("//input[@id='js-buy-quantity']");
    public static By decreaseQuantityButton = By.xpath("//a[@data-value='-1']");

    public ProductDetailPage(WebDriver driver) {
        super(driver);
        this.actions = new Actions(driver);
    }

    public void addToCart() {
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
        WebElement cartIconElement = driver.findElement(cartIcon);
        waitForElementToBeVisible(cartIcon);
        actions.moveToElement(cartIconElement).perform();
        log.info("Successfully hovered over cart icon");

        waitForElementToBeVisible(viewCartLink);
        WebElement viewcartElement = driver.findElement(viewCartLink);
        waitForElementToBeClickable(viewcartElement);
        viewcartElement.click();
        log.info("Successfully navigated to cart page");
}

    public void verifyAddToCartSuccessMessage() {
        WebElement successMessage = waitForElementToBeVisible(successNotification);
        String expectedMessage = "Thêm sản phẩm vào giỏ hàng thành côngg !";
        String actualMessage = successMessage.getText();
        Assert.assertEquals(actualMessage, expectedMessage, "Failed to verify success message");
    }


    public static void verifyProductNameConsistency(WebDriver driver, BasePage base, String baseUrl) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        driver.get(baseUrl);

        for (int i = 0; i < 10; i++) {
            js.executeScript("window.scrollBy(0, 600);");
            base.waitForPresence(By.cssSelector("body"));
        }

        js.executeScript("window.scrollTo(0, document.body.scrollHeight);");

        WebElement itemnameElement = base.waitForElementToBeVisible(SecondLaptopNameInMainPage);
        String productNameOnMainPage = itemnameElement.getText();
        System.out.println("Tên sản phẩm trên trang chủ: " + productNameOnMainPage);

        // Gọi hàm mới để click vào sản phẩm
        HomePage.clickSpecificItem(driver, SecondLaptopInMainPage);

        WebElement productNameElement = base.waitForElementToBeVisible(productName);
        String productNameOnDetailPage = productNameElement.getText();

        Assert.assertEquals(productNameOnDetailPage, productNameOnMainPage, "Product names do not match!");
    }
}
