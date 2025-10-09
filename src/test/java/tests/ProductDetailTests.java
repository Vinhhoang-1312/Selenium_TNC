package tests;

import core.BaseTest;
import io.qameta.allure.*;
import listeners.BaseListener;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.HomePage;
import pages.ProductDetailPage;

import java.time.Duration;

@Listeners({BaseListener.class})
@Epic("E-Commerce")
@Feature("Product Details")
public class ProductDetailTests extends BaseTest {

    @Test(groups = {"product", "smoke"},
            description = "PRODUCT-01: Verify product details displayed")
    @Story("Product Information")
    @Severity(SeverityLevel.BLOCKER)
    @Description("Verify that product details page displays product name and information correctly")
    public void testVerifyProductDetailsDisplayed() {
        // Initialize page objects
        HomePage homePage = new HomePage(getDriver());
        ProductDetailPage productDetailPage = new ProductDetailPage(getDriver());

        homePage.searchProduct("laptop");
        homePage.clickFirstProduct();

        String productName = productDetailPage.getProductName();

        Assert.assertFalse(productName.isEmpty(), "Product name should be displayed");
        Allure.parameter("Product Name", productName);
        logger.info("Product name verified: {}", productName);
    }

    @Test(groups = {"product"},
            description = "PRODUCT-02: Add product to cart shows success notification")
    @Story("Add to Cart")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify that success notification is displayed after adding product to cart")
    public void testAddProductToCartShowsNotification() {
        Allure.step("Search and select a product");
        HomePage homePage = new HomePage(getDriver());
        ProductDetailPage productDetailPage = new ProductDetailPage(getDriver());

        homePage.searchProduct("laptop");
        homePage.clickFirstProduct();

        productDetailPage.addToCart();

        Assert.assertTrue(productDetailPage.isSuccessNotificationDisplayed(),
            "Success notification should be displayed after adding to cart");
        logger.info("Success notification verified");

    }

    @Test(groups = {"product"},
            description = "PRODUCT-03: Verify sale price calculation is correct")
    @Story("Pricing and Discounts")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify that sale price is calculated correctly based on original price and discount percentage")
    public void testSalePriceCorrect() {
        // Initialize page objects
        ProductDetailPage productDetailPage = new ProductDetailPage(getDriver());

        getDriver().get("https://www.tncstore.vn/man-hinh-gaming-asus-tuf-gaming-vg249q3a.html");

        double originalPrice = productDetailPage.getOriginalPrice();
        logger.info("Original price: {}", originalPrice);
        Allure.parameter("Original Price", originalPrice);

        double salePrice = productDetailPage.getSalePrice();
        logger.info("Sale price: {}", salePrice);
        Allure.parameter("Sale Price", salePrice);

        double discountPercent = productDetailPage.getDiscountPercent();
        logger.info("Discount percent: {}%", discountPercent);
        Allure.parameter("Discount %", discountPercent);

        double expectedPrice = productDetailPage.calculateExpectedSalePrice();
        logger.info("Expected Price: {}", expectedPrice);
        Allure.parameter("Expected Price", expectedPrice);

        Assert.assertEquals(salePrice, expectedPrice,
            "Sale price should match calculation. Expected: " + expectedPrice + " | Actual: " + salePrice);
        logger.info("Sale price calculation verified: {}", salePrice);
    }

    @Test(groups = {"product"},
            description = "PRODUCT-04: Verify viewed products list")
    @Story("Product History")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify that previously viewed products appear in the viewed products list")
    public void testProductViewedList() {
        // Initialize page objects
        ProductDetailPage productDetailPage = new ProductDetailPage(getDriver());
        getDriver().get("https://www.tncstore.vn/man-hinh-gaming-asus-tuf-gaming-vg249q3a.html");
        Allure.step("Get initial product name");
        String initialTitle = productDetailPage.getProductName();
        logger.info("Initial product: {}", initialTitle);
        Allure.parameter("First Product", initialTitle);

        productDetailPage.clickFirstSimilarProduct();

        boolean found = productDetailPage.isProductInViewedList(initialTitle);
        Assert.assertTrue(found, "Initial product should be in viewed products list");
        logger.info("Product successfully added to viewed list");
    }

    @Test(groups = {"product"},
            description = "PRODUCT-05: Verify alert when decreasing quantity to 0")
    @Story("Quantity Management")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify that an alert is shown when trying to decrease quantity below 1")
    public void testMinusQuantityShowsAlert() {
        // Initialize page objects
        HomePage homePage = new HomePage(getDriver());
        ProductDetailPage productDetailPage = new ProductDetailPage(getDriver());

        homePage.searchProduct("laptop");
        homePage.clickFirstProduct();

        productDetailPage.setQuantityByInput("1");
        productDetailPage.clickDecreaseButtonAlt();

        String expectedAlertText = "Quý khách cần chọn số lượng sản phẩm lớn hơn 0";
        String actualAlertText = productDetailPage.getAlertText().trim();
        Allure.parameter("Expected Alert", expectedAlertText);
        Allure.parameter("Actual Alert", actualAlertText);

        Assert.assertEquals(actualAlertText, expectedAlertText, "Alert text should match expected message");
        logger.info("Alert verified: {}", actualAlertText);

        productDetailPage.acceptAlert();
    }
}
