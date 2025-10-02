package test.cart;

import helpers.BaseTest;
import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.HomePage;
import pages.ProductDetailPage;
import pages.CartPage;

public class IncreaseItemQuantityTest extends BaseTest {
    @Test
    public void testIncreaseItemQuantity() {
        HomePage homePage = new HomePage(driver);
        dismissPopupsIfPresent();
        homePage.searchProduct("Màn Hình Samsung S3 LS24F320GAEXXV 24 Inch/ FHD/ IPS/ 120Hz/ 5ms");
        homePage.clickProduct();

        ProductDetailPage productDetailPage = new ProductDetailPage(driver);
        productDetailPage.addToCart();
        productDetailPage.goToCart();

        CartPage cartPage = new CartPage(driver);
        int initialQuantity = cartPage.getFirstItemQuantity();
        int newQuantity = initialQuantity + 1;
        cartPage.clickFirstPlusSign();

        int updatedQuantity = cartPage.getFirstItemQuantity();
        Assert.assertEquals(updatedQuantity, newQuantity, "Item quantity should be increased by 1");
    }
}
