package test;

import helpers.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.HomePage;
import pages.ProductDetailPage;
import pages.CartPage;

public class AddToCartTest extends BaseTest {
    @Test
    public void testAddToCart() {
        HomePage homePage = new HomePage(driver);
        homePage.clickFirstProduct();

        ProductDetailPage productDetailPage = new ProductDetailPage(driver);
        productDetailPage.addToCart();
        productDetailPage.goToCart();

        CartPage cartPage = new CartPage(driver);
        int quantity = cartPage.getFirstItemQuantity();
        Assert.assertEquals(quantity, 1, "Item should be added to cart with quantity 1");
    }
}
