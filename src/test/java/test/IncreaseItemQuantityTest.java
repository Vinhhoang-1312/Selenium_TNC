package test;

import helpers.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.HomePage;
import pages.ProductDetailPage;
import pages.CartPage;

public class IncreaseItemQuantityTest extends BaseTest {
    @Test
    public void testIncreaseItemQuantity() {
        HomePage homePage = new HomePage(driver);
        homePage.clickFirstProduct();

        ProductDetailPage productDetailPage = new ProductDetailPage(driver);
        productDetailPage.addToCart();
        productDetailPage.goToCart();

        CartPage cartPage = new CartPage(driver);
        int initialQuantity = cartPage.getFirstItemQuantity();
        int newQuantity = initialQuantity + 1;
        cartPage.setFirstItemQuantity(newQuantity);

        // Optionally, wait for cart update (could add explicit wait if needed)
        int updatedQuantity = cartPage.getFirstItemQuantity();
        Assert.assertEquals(updatedQuantity, newQuantity, "Item quantity should be increased by 1");
    }
}
