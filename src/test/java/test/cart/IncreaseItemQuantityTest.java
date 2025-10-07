package test.cart;

import org.testng.annotations.Test;
import test.BaseTest;

public class IncreaseItemQuantityTest extends BaseTest {
    @Test
    public void testIncreaseItemQuantity() {
        popupHandler.dismissAllPopups();

        homePage.searchProduct("Màn Hình Gaming Dell Alienware AW2721D IPS/ QHD/ 240Hz");
        homePage.clickProduct();

        productDetailPage.addToCart();
        productDetailPage.goToCart();

        cartPage.checkItemQuantityIncrease();
    }
}
