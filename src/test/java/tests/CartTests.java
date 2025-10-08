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
        HomePage homePage = new HomePage(getDriver());
        homePage.searchProduct("Màn Hình Samsung S3 LS24F320GAEXXV 24 Inch/ FHD/ IPS/ 120Hz/ 5ms");
        homePage.clickFirstProduct();

        ProductDetailPage productDetailPage = new ProductDetailPage(getDriver());
        productDetailPage.addToCart();
        productDetailPage.goToCart();

        CartPage cartPage = new CartPage(getDriver());
        cartPage.verifyFirstItemQuantity();
        logger.info("Single product added to cart successfully");
    }

    @Test(groups = {"cart"},
            description = "CART-02: Increase item quantity in cart")
    @Story("Manage Cart Quantity")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify that user can increase product quantity in the cart")
    public void testIncreaseItemQuantity() {
        HomePage homePage = new HomePage(getDriver());
        homePage.searchProduct("Màn Hình Gaming Dell Alienware AW2721D IPS/ QHD/ 240Hz");
        homePage.clickFirstProduct();

        ProductDetailPage productDetailPage = new ProductDetailPage(getDriver());
        productDetailPage.addToCart();
        productDetailPage.goToCart();

        CartPage cartPage = new CartPage(getDriver());
        cartPage.checkItemQuantityIncrease();
        logger.info("Item quantity increased successfully");
    }

    @Test(groups = {"cart"},
            description = "CART-03: Decrease item quantity in cart")
    @Story("Manage Cart Quantity")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify that user can decrease product quantity in the cart")
    public void testDecreaseItemQuantity() {
        HomePage homePage = new HomePage(getDriver());
        homePage.searchProduct("Màn Hình Samsung S3 LS24F320GAEXXV 24 Inch/ FHD/ IPS/ 120Hz/ 5ms");
        homePage.clickFirstProduct();

        ProductDetailPage productDetailPage = new ProductDetailPage(getDriver());
        productDetailPage.addToCart(2);
        productDetailPage.goToCart();

        CartPage cartPage = new CartPage(getDriver());
        cartPage.checkItemQuantityDecrease();
        logger.info("Item quantity decreased successfully");
    }

    @Test(groups = {"cart"},
            description = "CART-04: Add multiple products to cart")
    @Story("Add Products to Cart")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify that user can add multiple different products to the cart")
    public void testAddMultipleProductsToCart() {
        HomePage homePage = new HomePage(getDriver());
        ProductDetailPage productDetailPage = new ProductDetailPage(getDriver());

        // Add first product
        homePage.searchProduct("laptop");
        customWait(1000);
        homePage.clickFirstProduct();
        productDetailPage.addToCart();
        getDriver().navigate().back();
        customWait(1000);

        // Add second product
        homePage.searchProduct("mouse");
        customWait(1000);
        homePage.clickFirstProduct();
        productDetailPage.addToCart();
        productDetailPage.goToCart();

        CartPage cartPage = new CartPage(getDriver());
        cartPage.verifyMultipleProducts();
        logger.info("Multiple products added to cart successfully");
    }

    @Test(groups = {"cart"},
            description = "CART-05: Verify cart size")
    @Story("Cart Verification")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify that cart size is correctly displayed after adding products")
    public void testVerifyCartSize() {
        HomePage homePage = new HomePage(getDriver());
        homePage.searchProduct("laptop");
        homePage.clickFirstProduct();

        ProductDetailPage productDetailPage = new ProductDetailPage(getDriver());
        productDetailPage.addToCart();
        productDetailPage.goToCart();

        CartPage cartPage = new CartPage(getDriver());
        cartPage.verifyCartSize(1);
        logger.info("Cart size verified successfully");
    }

    @Test(groups = {"cart"},
            description = "CART-06: Verify first product name in cart")
    @Story("Cart Verification")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify that product name in cart matches the added product")
    public void testVerifyFirstProductName() {
        HomePage homePage = new HomePage(getDriver());
        homePage.searchProduct("laptop");
        homePage.clickFirstProduct();

        ProductDetailPage productDetailPage = new ProductDetailPage(getDriver());
        String productName = productDetailPage.getProductName();
        productDetailPage.addToCart();
        productDetailPage.goToCart();

        CartPage cartPage = new CartPage(getDriver());
        String cartProductName = cartPage.getFirstProductName();
        Assert.assertTrue(cartProductName.contains(productName.substring(0, Math.min(20, productName.length()))),
            "Product name in cart should match the added product");
        logger.info("Product name verified in cart: {}", cartProductName);
    }
}
