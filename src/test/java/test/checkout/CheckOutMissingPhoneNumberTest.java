package test.checkout;

import org.testng.annotations.Test;
import test.BaseTest;

public class CheckOutMissingPhoneNumberTest extends BaseTest {

    @Test
    public void testCheckoutMissingPhoneNumber() {
        popupHandler.dismissAllPopups();

        homePage.searchProduct("Màn Hình Samsung S3 LS24F320GAEXXV 24 Inch/ FHD/ IPS/ 120Hz/ 5ms");
        homePage.clickProduct();

        productDetailPage.addToCart();
        productDetailPage.goToCart();

        cartPage.proceedToCheckout();
        cartPage.verifyMissingPhoneNumberError();
    }
}
