package tests;

import core.BaseTest;
import io.qameta.allure.*;
import listeners.BaseListener;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.HomePage;
import pages.ProductDetailPage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Listeners({BaseListener.class})
@Epic("E-Commerce")
@Feature("Product Details")
public class ProductDetailTests extends BaseTest {
    private static final Logger log = LoggerFactory.getLogger(ProductDetailTests.class);

    @Test(groups = {"product", "smoke"},
            description = "PRODUCT-01: Verify product details displayed")
    @Story("Product Information")
    @Severity(SeverityLevel.BLOCKER)
    @Description("Verify that product details page displays product name and information correctly")
    public void testVerifyProductDetailsDisplayed() {
        Allure.step("Search and select a product");
        HomePage homePage = new HomePage(getDriver());
        homePage.searchProduct("laptop");
        homePage.clickFirstProduct();

        Allure.step("Verify product name is displayed");
        ProductDetailPage productDetailPage = new ProductDetailPage(getDriver());
        String productName = productDetailPage.getProductName();

        Assert.assertFalse(productName.isEmpty(), "Product name should be displayed");
        Allure.parameter("Product Name", productName);
        log.info("Product name verified: {}", productName);
    }

    @Test(groups = {"product"},
            description = "PRODUCT-02: Add product to cart shows success notification")
    @Story("Add to Cart")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify that success notification is displayed after adding product to cart")
    public void testAddProductToCartShowsNotification() {
        Allure.step("Search and select a product");
        HomePage homePage = new HomePage(getDriver());
        homePage.searchProduct("laptop");
        homePage.clickFirstProduct();

        Allure.step("Add product to cart");
        ProductDetailPage productDetailPage = new ProductDetailPage(getDriver());
        productDetailPage.addToCart();

        customWait(2000);
        Allure.step("Verify success notification is displayed");
        Assert.assertTrue(productDetailPage.isSuccessNotificationDisplayed(),
            "Success notification should be displayed after adding to cart");
        log.info("Success notification verified");
    }

    @Test(groups = {"product"},
            description = "PRODUCT-03: Verify sale price calculation is correct")
    @Story("Pricing and Discounts")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify that sale price is calculated correctly based on original price and discount percentage")
    public void testSalePriceCorrect() {
        Allure.step("Navigate to specific product page");
        getDriver().get("https://www.tncstore.vn/man-hinh-gaming-asus-tuf-gaming-vg249q3a.html");

        ProductDetailPage productDetailPage = new ProductDetailPage(getDriver());

        Allure.step("Get original price");
        double originalPrice = productDetailPage.getOriginalPrice();
        log.info("Original price: {}", originalPrice);
        Allure.parameter("Original Price", originalPrice);

        Allure.step("Get sale price");
        double salePrice = productDetailPage.getSalePrice();
        log.info("Sale price: {}", salePrice);
        Allure.parameter("Sale Price", salePrice);

        Allure.step("Get discount percentage");
        double discountPercent = productDetailPage.getDiscountPercent();
        log.info("Discount percent: {}%", discountPercent);
        Allure.parameter("Discount %", discountPercent);

        Allure.step("Calculate and verify expected price");
        double expectedPrice = productDetailPage.calculateExpectedSalePrice();
        Allure.parameter("Expected Price", expectedPrice);

        Assert.assertEquals(salePrice, expectedPrice,
            "Sale price should match calculation. Expected: " + expectedPrice + " | Actual: " + salePrice);
        log.info("Sale price calculation verified: {}", salePrice);
    }

    @Test(groups = {"product"},
            description = "PRODUCT-04: Verify viewed products list")
    @Story("Product History")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify that previously viewed products appear in the viewed products list")
    public void testProductViewedList() {
        Allure.step("Navigate to first product");
        getDriver().get("https://www.tncstore.vn/man-hinh-gaming-asus-tuf-gaming-vg249q3a.html");
        customWait(2000);

        ProductDetailPage productDetailPage = new ProductDetailPage(getDriver());

        Allure.step("Get initial product name");
        String initialTitle = productDetailPage.getProductNameH1();
        log.info("Initial product: {}", initialTitle);
        Allure.parameter("First Product", initialTitle);

        Allure.step("Click on similar product");
        productDetailPage.clickFirstSimilarProduct();
        customWait(2000);

        Allure.step("Verify initial product is in viewed list");
        boolean found = productDetailPage.isProductInViewedList(initialTitle);
        Assert.assertTrue(found, "Initial product should be in viewed products list");
        log.info("Product successfully added to viewed list");
    }

    @Test(groups = {"product"},
            description = "PRODUCT-05: Verify alert when decreasing quantity to 0")
    @Story("Quantity Management")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify that an alert is shown when trying to decrease quantity below 1")
    public void testMinusQuantityShowsAlert() {
        Allure.step("Search and select a product");
        HomePage homePage = new HomePage(getDriver());
        homePage.searchProduct("laptop");
        homePage.clickFirstProduct();
        customWait(2000);

        ProductDetailPage productDetailPage = new ProductDetailPage(getDriver());

        Allure.step("Set quantity to 1");
        productDetailPage.setQuantityByInput("1");

        Allure.step("Click decrease button");
        productDetailPage.clickDecreaseButtonAlt();

        customWait(1000);

        Allure.step("Verify alert message");
        String expectedAlertText = "Quý khách cần chọn số lượng sản phẩm lớn hơn 0";
        String actualAlertText = productDetailPage.getAlertText().trim();
        Allure.parameter("Expected Alert", expectedAlertText);
        Allure.parameter("Actual Alert", actualAlertText);

        Assert.assertEquals(actualAlertText, expectedAlertText, "Alert text should match expected message");
        log.info("Alert verified: {}", actualAlertText);

        Allure.step("Accept alert");
        productDetailPage.acceptAlert();
    }
}
