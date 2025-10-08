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
    private final By decreaseButton = By.cssSelector(".qty-down");
    private final By originalPriceLocator = By.cssSelector(".product-price-original");
    private final By salePriceLocator = By.cssSelector(".product-price");
    private final By discountPercentLocator = By.cssSelector(".product-price-sale");
    private final By productNameH1 = By.cssSelector("h1.product-name");
    private final By similarProducts = By.cssSelector(".product-similar .product-item:first-child a");
    private final By viewedProductsSection = By.cssSelector(".product-viewed");
    private final By viewedProductNames = By.cssSelector("a.product-name");

    public ProductDetailPage(WebDriver driver) {
        super(driver);
        this.actions = new Actions(driver);
    }

    public void addToCart() {
        waitForElementToDisappear(loadingSpinner);
        waitForElementToBeVisible(addToCartButton);
        clickElementWithRetry(addToCartButton, "add to cart");
        popupHandler.acceptAlert(3);

        logger.info("Successfully added product to cart");
    }

    public void addToCart(int quantity) {
        setQuantity(quantity);
        waitForElementToBeVisible(addToCartButton);
        clickElementWithRetry(addToCartButton, "add to cart");
        popupHandler.acceptAlert(3);

        logger.info("Successfully added {} product(s) to cart", quantity);
    }

    public void setQuantity(int quantity) {
        waitForElementToBeVisible(quantityInput);
        clearAndType(quantityInput, String.valueOf(quantity));
        logger.info("Set product quantity to: {}", quantity);
    }

    public void clickDecreaseButton() {
        try {
            waitForElementToBeClickable(decreaseButton).click();
            logger.info("Clicked decrease quantity button");
        } catch (Exception e) {
            logger.warn("Failed to click decrease button normally, attempting JS click", e);
            WebElement btn = waitAndFind(decreaseButton);
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", btn);
        }
    }

    public void goToCart() {
        // Use waitAndFind so any unexpected JS alert is handled via PopupHandler before we interact
        WebElement cartIconElement = waitAndFind(cartIcon);
        waitForElementToBeVisible(cartIcon);
        actions.moveToElement(cartIconElement).perform();
        logger.info("Successfully hovered over cart icon");

        waitForElementToBeVisible(viewCartLink);
        clickElementWithRetry(viewCartLink, "view cart link");
        logger.info("Successfully navigated to cart page");
    }

    public String getProductName() {
        return waitAndGetText(productName);
    }

    public boolean isSuccessNotificationDisplayed() {
        return isDisplayed(successNotification);
    }

    /**
     * Gets original price as double
     */
    public double getOriginalPrice() {
        String priceText = waitAndGetText(originalPriceLocator).replaceAll("[^0-9]", "");
        return Double.parseDouble(priceText);
    }

    /**
     * Gets sale price as double
     */
    public double getSalePrice() {
        String priceText = waitAndGetText(salePriceLocator).replaceAll("[^0-9]", "");
        return Double.parseDouble(priceText);
    }

    /**
     * Gets discount percentage as double
     */
    public double getDiscountPercent() {
        String discountText = waitAndGetText(discountPercentLocator).replaceAll("[^0-9]", "");
        return Double.parseDouble(discountText);
    }

    /**
     * Calculates expected sale price based on original price and discount
     */
    public double calculateExpectedSalePrice() {
        double originalPrice = getOriginalPrice();
        double discountPercent = getDiscountPercent();
        return originalPrice - (originalPrice * discountPercent / 100);
    }

    /**
     * Verifies that sale price calculation is correct
     */
    public boolean isSalePriceCorrect() {
        double actualSalePrice = getSalePrice();
        double expectedSalePrice = calculateExpectedSalePrice();
        return Math.abs(actualSalePrice - expectedSalePrice) < 0.01; // Allow small floating point difference
    }

    /**
     * Gets the product name from H1 tag
     */
    public String getProductNameH1() {
        return waitAndGetText(productNameH1);
    }

    /**
     * Clicks on first similar product
     */
    public void clickFirstSimilarProduct() {
        waitForElementToBeClickable(similarProducts).click();
        logger.info("Clicked first similar product");
    }

    /**
     * Gets all viewed product names
     */
    public java.util.List<WebElement> getViewedProductNames() {
        waitForElementToBeVisible(viewedProductsSection);
        return driver.findElements(viewedProductNames);
    }

    /**
     * Checks if a product name exists in viewed products list
     */
    public boolean isProductInViewedList(String productName) {
        java.util.List<WebElement> viewedProducts = getViewedProductNames();
        return viewedProducts.stream()
                .anyMatch(element -> element.getText().contains(productName));
    }

    /**
     * Sets quantity using input field (alternative method)
     */
    public void setQuantityByInput(String quantity) {
        // Use the canonical quantityInput locator to avoid selector mismatch
        WebElement qtyInput = waitAndFind(quantityInput);
        qtyInput.clear();
        qtyInput.sendKeys(quantity);
        logger.info("Set quantity to: {}", quantity);
    }

    /**
     * Clicks decrease button using alternative selector
     */
    public void clickDecreaseButtonAlt() {
        WebElement minusButton = waitAndFind(By.cssSelector(".qty-down"));
        minusButton.click();
        logger.info("Clicked decrease button");
    }

    /**
     * Waits for alert and gets its text
     */
    public String getAlertText() {
        return popupHandler.getAlertText(5);
    }

    /**
     * Accepts the alert
     */
    public void acceptAlert() {
        popupHandler.acceptAlert(5);
    }

    /**
     * Gets alert text and accepts it in one call
     */
    public String getAlertTextAndAccept() {
        return popupHandler.getAlertTextAndAccept(5);
    }
}
