package test.cart;

import helpers.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.HomePage;
import pages.ProductDetailPage;
import pages.CartPage;
import org.openqa.selenium.By;

public class AddNormalProductToCartTest extends BaseTest {
    @Test
    public void testAddToCart() {
        HomePage homePage = new HomePage(driver);
        dismissPopupsIfPresent(); // Initial page
        homePage.searchProduct("Màn Hình Samsung S3 LS24F320GAEXXV 24 Inch/ FHD/ IPS/ 120Hz/ 5ms");
        homePage.clickProduct();

        ProductDetailPage productDetailPage = new ProductDetailPage(driver);
        productDetailPage.addToCart();
        productDetailPage.goToCart();

        CartPage cartPage = new CartPage(driver);
        int quantity = cartPage.getFirstItemQuantity();
        Assert.assertEquals(quantity, 1, "Item should be added to cart with quantity 1");
    }
}
