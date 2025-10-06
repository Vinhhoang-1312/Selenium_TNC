package test.cart;

import commons.DriverFactory;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.HomePage;
import pages.ProductDetailPage;
import pages.CartPage;
import test.BaseTest;

public class AddNormalProductToCartTest extends BaseTest {
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
    public void testAddToCart() {

        dismissPopupsIfPresent();

        homePage.searchProduct("Màn Hình Samsung S3 LS24F320GAEXXV 24 Inch/ FHD/ IPS/ 120Hz/ 5ms");
        homePage.clickProduct();

        productDetailPage.addToCart();
        productDetailPage.goToCart();

        cartPage.checkFirstItemQuantity();
    }
}
