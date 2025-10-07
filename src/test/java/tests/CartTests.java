package tests;

import core.BaseTest;
import helpers.PopupHandler;
import listeners.BaseListener;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.HomePage;
import pages.ProductDetailPage;
import pages.CartPage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Listeners({BaseListener.class})
public class CartTests extends BaseTest {
    private static final Logger log = LoggerFactory.getLogger(CartTests.class);

    @Test(groups = {"cart", "smoke"},
            description = "CART-01: Add single product to cart")
    public void testAddSingleProductToCart() {
        PopupHandler popupHandler = new PopupHandler(getDriver());
        popupHandler.dismissAllPopups();

        HomePage homePage = new HomePage(getDriver());
        homePage.searchProduct("Màn Hình Samsung S3 LS24F320GAEXXV 24 Inch/ FHD/ IPS/ 120Hz/ 5ms");
        homePage.clickFirstProduct();

        ProductDetailPage productDetailPage = new ProductDetailPage(getDriver());
        productDetailPage.addToCart();
        productDetailPage.goToCart();

        CartPage cartPage = new CartPage(getDriver());
        cartPage.verifyFirstItemQuantity();
        log.info("Single product added to cart successfully");
    }

    @Test(groups = {"cart"},
            description = "CART-02: Increase item quantity in cart")
    public void testIncreaseItemQuantity() {
        PopupHandler popupHandler = new PopupHandler(getDriver());
        popupHandler.dismissAllPopups();

        HomePage homePage = new HomePage(getDriver());
        homePage.searchProduct("Màn Hình Gaming Dell Alienware AW2721D IPS/ QHD/ 240Hz");
        homePage.clickFirstProduct();

        ProductDetailPage productDetailPage = new ProductDetailPage(getDriver());
        productDetailPage.addToCart();
        productDetailPage.goToCart();

        CartPage cartPage = new CartPage(getDriver());
        cartPage.checkItemQuantityIncrease();
        log.info("Item quantity increased successfully");
    }

    @Test(groups = {"cart"},
            description = "CART-03: Decrease item quantity in cart")
    public void testDecreaseItemQuantity() {
        PopupHandler popupHandler = new PopupHandler(getDriver());
        popupHandler.dismissAllPopups();

        HomePage homePage = new HomePage(getDriver());
        homePage.searchProduct("Màn Hình Samsung S3 LS24F320GAEXXV 24 Inch/ FHD/ IPS/ 120Hz/ 5ms");
        homePage.clickFirstProduct();

        ProductDetailPage productDetailPage = new ProductDetailPage(getDriver());
        productDetailPage.addToCart(2);
        productDetailPage.goToCart();

        CartPage cartPage = new CartPage(getDriver());
        cartPage.checkItemQuantityDecrease();
        log.info("Item quantity decreased successfully");
    }

    @Test(groups = {"cart"},
            description = "CART-04: Add multiple products to cart")
    public void testAddMultipleProductsToCart() {
        PopupHandler popupHandler = new PopupHandler(getDriver());
        popupHandler.dismissAllPopups();

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
        log.info("Multiple products added to cart successfully");
    }

    @Test(groups = {"cart"},
            description = "CART-05: Verify cart size")
    public void testVerifyCartSize() {
        PopupHandler popupHandler = new PopupHandler(getDriver());
        popupHandler.dismissAllPopups();

        HomePage homePage = new HomePage(getDriver());
        homePage.searchProduct("laptop");
        homePage.clickFirstProduct();

        ProductDetailPage productDetailPage = new ProductDetailPage(getDriver());
        productDetailPage.addToCart();
        productDetailPage.goToCart();

        CartPage cartPage = new CartPage(getDriver());
        cartPage.verifyCartSize(1);
        log.info("Cart size verified successfully");
    }

    @Test(groups = {"cart"},
            description = "CART-06: Verify first product name in cart")
    public void testVerifyFirstProductName() {
        PopupHandler popupHandler = new PopupHandler(getDriver());
        popupHandler.dismissAllPopups();

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
        log.info("Product name verified in cart: {}", cartProductName);
    }
}
