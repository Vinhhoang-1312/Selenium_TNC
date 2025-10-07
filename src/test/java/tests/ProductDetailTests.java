package tests;

import core.BaseTest;
import listeners.BaseListener;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.HomePage;
import pages.ProductDetailPage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Listeners({BaseListener.class})
public class ProductDetailTests extends BaseTest {
    private static final Logger log = LoggerFactory.getLogger(ProductDetailTests.class);

    @Test(groups = {"product", "smoke"},
            description = "PRODUCT-01: Verify product details displayed")
    public void testVerifyProductDetailsDisplayed() {
        HomePage homePage = new HomePage(getDriver());
        homePage.searchProduct("laptop");
        homePage.clickFirstProduct();

        ProductDetailPage productDetailPage = new ProductDetailPage(getDriver());
        String productName = productDetailPage.getProductName();

        Assert.assertFalse(productName.isEmpty(), "Product name should be displayed");
        log.info("Product name verified: {}", productName);
    }

    @Test(groups = {"product"},
            description = "PRODUCT-02: Add product to cart shows success notification")
    public void testAddProductToCartShowsNotification() {
        HomePage homePage = new HomePage(getDriver());
        homePage.searchProduct("laptop");
        homePage.clickFirstProduct();

        ProductDetailPage productDetailPage = new ProductDetailPage(getDriver());
        productDetailPage.addToCart();

        customWait(2000);
        Assert.assertTrue(productDetailPage.isSuccessNotificationDisplayed(),
            "Success notification should be displayed after adding to cart");
        log.info("Success notification verified");
    }

    @Test(groups = {"product"},
            description = "PRODUCT-03: Verify sale price calculation is correct")
    public void testSalePriceCorrect() {
        getDriver().get("https://www.tncstore.vn/man-hinh-gaming-asus-tuf-gaming-vg249q3a.html");

        ProductDetailPage productDetailPage = new ProductDetailPage(getDriver());

        // Get original price
        String originalPriceText = getDriver().findElement(By.cssSelector(".product-price-original")).getText().replaceAll("[^0-9]", "");
        double originalPrice = Double.parseDouble(originalPriceText);
        log.info("Original price: {}", originalPrice);

        // Get sale price
        String salePriceText = getDriver().findElement(By.cssSelector(".product-price")).getText().replaceAll("[^0-9]", "");
        double salePrice = Double.parseDouble(salePriceText);
        log.info("Sale price: {}", salePrice);

        // Get discount percent
        String discountText = getDriver().findElement(By.cssSelector(".product-price-sale")).getText().replaceAll("[^0-9]", "");
        double discountPercent = Double.parseDouble(discountText);
        log.info("Discount percent: {}%", discountPercent);

        // Calculate expected price
        double expectedPrice = originalPrice - (originalPrice * discountPercent / 100);

        Assert.assertEquals(salePrice, expectedPrice,
            "Sale price should match calculation. Expected: " + expectedPrice + " | Actual: " + salePrice);
        log.info("Sale price calculation verified: {}", salePrice);
    }

    @Test(groups = {"product"},
            description = "PRODUCT-04: Verify viewed products list")
    public void testProductViewedList() {
        getDriver().get("https://www.tncstore.vn/man-hinh-gaming-asus-tuf-gaming-vg249q3a.html");
        customWait(2000);

        // Get initial product name
        String initialTitle = getDriver().findElement(By.cssSelector("h1.product-name")).getText();
        log.info("Initial product: {}", initialTitle);

        // Click on similar product
        WebElement otherProduct = getDriver().findElement(By.cssSelector(".product-similar .product-item:first-child a"));
        otherProduct.click();
        customWait(2000);

        // Check viewed products section
        WebElement viewedSection = getDriver().findElement(By.cssSelector(".product-viewed"));
        List<WebElement> viewedTitles = viewedSection.findElements(By.cssSelector("a.product-name"));

        log.info("Viewed products list:");
        for (WebElement titleElement : viewedTitles) {
            log.info("- {}", titleElement.getText());
        }

        boolean found = viewedTitles.stream().anyMatch(e -> e.getText().contains(initialTitle));
        Assert.assertTrue(found, "Initial product should be in viewed products list");
        log.info("Product successfully added to viewed list");
    }

    @Test(groups = {"product"},
            description = "PRODUCT-05: Verify alert when decreasing quantity to 0")
    public void testMinusQuantityShowsAlert() {
        HomePage homePage = new HomePage(getDriver());
        homePage.searchProduct("laptop");
        homePage.clickFirstProduct();
        customWait(2000);

        ProductDetailPage productDetailPage = new ProductDetailPage(getDriver());

        // Set quantity to 1
        WebElement quantityInput = getDriver().findElement(By.cssSelector("input[name='quantity']"));
        quantityInput.clear();
        quantityInput.sendKeys("1");

        // Click decrease button
        WebElement minusButton = getDriver().findElement(By.cssSelector(".qty-down"));
        minusButton.click();

        customWait(1000);

        // Verify alert
        String expectedAlertText = "Quý khách cần chọn số lượng sản phẩm lớn hơn 0";
        String actualAlertText = getDriver().switchTo().alert().getText().trim();

        Assert.assertEquals(actualAlertText, expectedAlertText, "Alert text should match expected message");
        log.info("Alert verified: {}", actualAlertText);

        // Accept alert
        getDriver().switchTo().alert().accept();
    }
}
