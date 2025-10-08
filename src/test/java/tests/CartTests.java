package tests;

import core.BaseTest;
import io.qameta.allure.*;
import listeners.BaseListener;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.HomePage;
import pages.ProductDetailPage;
import pages.CartPage;

@Listeners({BaseListener.class})
@Epic("E-Commerce")
@Feature("Shopping Cart")
public class CartTests extends BaseTest {

    @Test(groups = {"cart", "smoke"},
            description = "CART-01: Add single product to cart")
    @Story("Add Products to Cart")
    @Severity(SeverityLevel.BLOCKER)
    @Description("Verify that a user can successfully add a single product to the shopping cart")
    public void testAddSingleProductToCart() {
        // Initialize page objects
        HomePage homePage = new HomePage(getDriver());
        ProductDetailPage productDetailPage = new ProductDetailPage(getDriver());
        CartPage cartPage = new CartPage(getDriver());

        homePage.searchProduct("Màn Hình Samsung S3 LS24F320GAEXXV 24 Inch/ FHD/ IPS/ 120Hz/ 5ms");
        homePage.clickFirstProduct();

        productDetailPage.addToCart();
        productDetailPage.goToCart();

        cartPage.verifyFirstItemQuantity();
        logger.info("Single product added to cart successfully");
    }

    @Test(groups = {"cart"},
            description = "CART-02: Increase item quantity in cart")
    @Story("Manage Cart Quantity")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify that user can increase product quantity in the cart")
    public void testIncreaseItemQuantity() {
        // Initialize page objects
        HomePage homePage = new HomePage(getDriver());
        ProductDetailPage productDetailPage = new ProductDetailPage(getDriver());
        CartPage cartPage = new CartPage(getDriver());

        homePage.searchProduct("Màn Hình Gaming Dell Alienware AW2721D IPS/ QHD/ 240Hz");
        homePage.clickFirstProduct();

        productDetailPage.addToCart();
        productDetailPage.goToCart();

        cartPage.checkItemQuantityIncrease();
        logger.info("Item quantity increased successfully");
    }

    @Test(groups = {"cart"},
            description = "CART-03: Decrease item quantity in cart")
    @Story("Manage Cart Quantity")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify that user can decrease product quantity in the cart")
    public void testDecreaseItemQuantity() {
        // Initialize page objects
        HomePage homePage = new HomePage(getDriver());
        ProductDetailPage productDetailPage = new ProductDetailPage(getDriver());
        CartPage cartPage = new CartPage(getDriver());

        homePage.searchProduct("Màn Hình Samsung S3 LS24F320GAEXXV 24 Inch/ FHD/ IPS/ 120Hz/ 5ms");
        homePage.clickFirstProduct();

        productDetailPage.addToCart(2);
        productDetailPage.goToCart();

        cartPage.checkItemQuantityDecrease();
        logger.info("Item quantity decreased successfully");
    }

    @Test(groups = {"cart"},
            description = "CART-04: Add multiple products to cart")
    @Story("Add Products to Cart")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify that user can add multiple different products to the cart")
    public void testAddMultipleProductsToCart() {
        // Initialize page objects
        HomePage homePage = new HomePage(getDriver());
        ProductDetailPage productDetailPage = new ProductDetailPage(getDriver());
        CartPage cartPage = new CartPage(getDriver());

        // Add first product
        homePage.searchProduct("laptop");
        homePage.clickFirstProduct();
        productDetailPage.addToCart();
        homePage.navigateBack();

        // Add second product
        homePage.searchProduct("mouse");
        homePage.clickFirstProduct();
        productDetailPage.addToCart();
        productDetailPage.goToCart();

        cartPage.verifyMultipleProducts();
        logger.info("Multiple products added to cart successfully");
    }

    @Test(groups = {"cart"},
            description = "CART-05: Verify cart size")
    @Story("Cart Verification")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify that cart size is correctly displayed after adding products")
    public void testVerifyCartSize() {
        // Initialize page objects
        HomePage homePage = new HomePage(getDriver());
        ProductDetailPage productDetailPage = new ProductDetailPage(getDriver());
        CartPage cartPage = new CartPage(getDriver());

        homePage.searchProduct("laptop");
        homePage.clickFirstProduct();

        productDetailPage.addToCart();
        productDetailPage.goToCart();

        cartPage.verifyCartSize(1);
        logger.info("Cart size verified successfully");
    }

    @Test(groups = {"cart"},
            description = "CART-06: Verify first product name in cart")
    @Story("Cart Verification")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify that product name in cart matches the added product")
    public void testVerifyFirstProductName() {
        // Initialize page objects
        HomePage homePage = new HomePage(getDriver());
        ProductDetailPage productDetailPage = new ProductDetailPage(getDriver());
        CartPage cartPage = new CartPage(getDriver());

        homePage.searchProduct("laptop");
        homePage.clickFirstProduct();

        String productName = productDetailPage.getProductName();
        productDetailPage.addToCart();
        productDetailPage.goToCart();

        String cartProductName = cartPage.getFirstProductName();
        Assert.assertTrue(cartProductName.contains(productName.substring(0, Math.min(20, productName.length()))),
            "Product name in cart should match the added product");
        logger.info("Product name verified in cart: {}", cartProductName);
    }
}
