package test.cart;

import test.BaseTest;
import org.testng.annotations.Test;

public class AddSingleProductToCartTest extends BaseTest {

    @Test
    public void testAddSingleProductToCart() {

        popupHandler.dismissAllPopups();

        homePage.searchProduct("Màn Hình Samsung S3 LS24F320GAEXXV 24 Inch/ FHD/ IPS/ 120Hz/ 5ms");
        homePage.clickProduct();

        productDetailPage.addToCart();
        productDetailPage.goToCart();

        cartPage.verifyFirstItemQuantity();
    }
}
