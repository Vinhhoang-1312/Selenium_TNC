package test.cart;

import org.testng.annotations.Test;
import test.BaseTest;

public class AddMultipleProductsToCartTest extends BaseTest {

    @Test
    public void testAddMultipleProductsToCart() {
        popupHandler.dismissAllPopups();

        homePage.searchProduct("Màn Hình Gaming Dell Alienware AW2721D IPS/ QHD/ 240Hz");
        homePage.clickProduct();
        productDetailPage.addToCart();

        homePage.searchProduct("Card Màn Hình Asus Prime GeForce RTX 5070 Ti 16GB GDDR7 (PRIME-RTX5070TI-16G)");
        homePage.clickProduct();
        productDetailPage.addToCart();

        productDetailPage.goToCart();

        cartPage.verifyMultipleProducts();
    }
}

