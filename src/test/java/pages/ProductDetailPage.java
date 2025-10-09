package pages;

import io.qameta.allure.Allure;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.testng.Assert;

public class ProductDetailPage extends BasePage {
    private final Actions actions;

    private final By addToCartButton = By.xpath("//a[contains(text(),'Thêm vào giỏ hàng')]");
    private final By cartIcon = By.xpath("//a[@id='js-header-cart']");
    private final By viewCartLink = By.xpath("//a[@class='btn-goCart']");
    private final By loadingSpinner = By.xpath("//div[contains(@class, 'loading-spinner')]");
    private final By quantityInput = By.xpath("//input[@id='js-buy-quantity']");
    private final By productName = By.xpath("//h1[@class='name']");
    private final By successNotification = By.xpath("//div[contains(@class,'text-24')]");
    private final By decreaseButton = By.xpath("//a[@data-value='-1']");
    private final By originalPriceLocator = By.xpath("//div[@class='info-main-price']//del[@class='old-price']");
    private final By salePriceLocator = By.xpath("//div[@class='info-main-price']//div[@class='price']");
    private final By discountPercentLocator = By.xpath("//div[@class='info-main-price']//div[@class='saleoff']");
    private final By similarProducts = By.xpath("//div[contains(@class,'similar')]//div[@class='owl-item active'][2]");
    private final By viewedProductsSection = By.xpath("//div[@class='product-history']//div[@class='product-list']");
    private final By viewedProductNames = By.cssSelector("a.product-name");

    public ProductDetailPage(WebDriver driver) {
        super(driver);
        this.actions = new Actions(driver);
    }

    public void addToCart() {
        Allure.step("Add product to cart", () -> {
            waitForElementToDisappear(loadingSpinner);
            waitForElementToBeVisible(addToCartButton);
            clickElementWithRetry(addToCartButton, "add to cart");
            popupHandler.acceptAlert(3);
            waitForPageStability();
            logger.info("Successfully added product to cart");
        });
    }

    public void addToCart(int quantity) {
        Allure.step("Add " + quantity + " product(s) to cart", () -> {
            setQuantity(quantity);
            waitForElementToBeVisible(addToCartButton);
            clickElementWithRetry(addToCartButton, "add to cart");
            popupHandler.acceptAlert(3);
            waitForPageStability();
            logger.info("Successfully added {} product(s) to cart", quantity);
        });
    }

    public void setQuantity(int quantity) {
        waitForElementToBeVisible(quantityInput);
        clearAndType(quantityInput, String.valueOf(quantity));
        logger.info("Set product quantity to: {}", quantity);
    }


    public void goToCart() {
        Allure.step("Navigate to shopping cart", () -> {
            // Use waitAndFind so any unexpected JS alert is handled via PopupHandler before we interact
            WebElement cartIconElement = waitAndFind(cartIcon);
            waitForElementToBeVisible(cartIcon);
            actions.moveToElement(cartIconElement).perform();
            logger.info("Successfully hovered over cart icon");

            waitForElementToBeVisible(viewCartLink);
            clickElementWithRetry(viewCartLink, "view cart link");
            logger.info("Successfully navigated to cart page");
        });
    }

    public String getProductName() {
        WebElement element = waitForElementToBeVisible(productName);
        String title = element.getText();
        logger.info("Product title found: {}", title);
        return Allure.step("Get product name", () -> {
            return waitAndGetText(productName);
        });
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

    public void clickFirstSimilarProduct() {
        click(similarProducts);
        logger.info("Clicked first similar product");
    }

    public java.util.List<WebElement> getViewedProductNames() {
        waitForElementToBeVisible(viewedProductsSection);
        return driver.findElements(viewedProductNames);
    }

    public boolean isProductInViewedList(String productName) {
        java.util.List<WebElement> viewedProducts = getViewedProductNames();
        return viewedProducts.stream()
                .anyMatch(element -> element.getText().contains(productName));
    }

    /**
     * Sets quantity using input field (alternative method)
     */
    public void setQuantityByInput(String quantity) {
        waitForElementToBeVisible(quantityInput);
        clearAndType(quantityInput, quantity);
        logger.info("Set quantity to: {}", quantity);
    }

    /**
     * Clicks decrease button (uses base click with retry and alert handling)
     */
    public void clickDecreaseButtonAlt() {
        click(decreaseButton);
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
}
