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
        clickIfPresent(By.cssSelector(".widget-header--button-close"));
        clickIfPresent(By.cssSelector(".widget-preview--btn-close"));
        homePage.searchProduct("PC Đồ Họa Render 3D - Ryzen 7 9800X3D/ 32GB/ RTX 5080");
        homePage.clickProduct();

        ProductDetailPage productDetailPage = new ProductDetailPage(driver);
        productDetailPage.addToCart();
        productDetailPage.goToCart();

        CartPage cartPage = new CartPage(driver);
        int quantity = cartPage.getFirstItemQuantity();
        Assert.assertEquals(quantity, 1, "Item should be added to cart with quantity 1");
    }
}
