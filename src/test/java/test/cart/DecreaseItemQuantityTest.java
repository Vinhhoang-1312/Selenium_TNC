package test.cart;

import org.testng.annotations.Test;
import test.BaseTest;

public class DecreaseItemQuantityTest extends BaseTest {

    @Test
    public void testDecreaseItemQuantity() {
        popupHandler.dismissAllPopups();

        homePage.searchProduct("Màn Hình Samsung S3 LS24F320GAEXXV 24 Inch/ FHD/ IPS/ 120Hz/ 5ms");
        homePage.clickProduct();

        productDetailPage.addToCart(2);
        productDetailPage.goToCart();

        cartPage.checkItemQuantityDecrease();
    }
}
