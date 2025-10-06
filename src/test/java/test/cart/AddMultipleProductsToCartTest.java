package test.cart;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.CartPage;
import pages.HomePage;
import pages.ProductDetailPage;
import test.BaseTest;

public class AddMultipleProductsToCartTest extends BaseTest {
    private HomePage homePage;
    private ProductDetailPage productDetailPage;
    private CartPage cartPage;

    @BeforeMethod
    public void setupPages() {
        homePage = new HomePage(driver);
        productDetailPage = new ProductDetailPage(driver);
        cartPage = new CartPage(driver);
    }

    @Test
    public void testAddMultipleProductsToCart() {
        dismissPopupsIfPresent();

        homePage.searchProduct("Màn Hình Samsung S3 LS24F320GAEXXV 24 Inch/ FHD/ IPS/ 120Hz/ 5ms");
        homePage.clickProduct();
        productDetailPage.addToCart();

        homePage.searchProduct("Card Màn Hình Asus Prime GeForce RTX 5070 Ti 16GB GDDR7 (PRIME-RTX5070TI-16G)");
        homePage.clickProduct();
        productDetailPage.addToCart();

        productDetailPage.goToCart();

        cartPage.checkCartSize();
    }
}

