package tests;

import core.BaseTest;
import helpers.PopupHandler;
import listeners.BaseListener;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.CartPage;
import pages.HomePage;
import pages.ProductDetailPage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Listeners({BaseListener.class})
public class CheckoutTests extends BaseTest {
    private static final Logger log = LoggerFactory.getLogger(CheckoutTests.class);

    @Test(groups = {"checkout"},
            description = "CHECKOUT-01: Verify error when phone number is missing")
    public void testCheckoutMissingPhoneNumber() {
        PopupHandler popupHandler = new PopupHandler(getDriver());
        popupHandler.dismissAllPopups();

        HomePage homePage = new HomePage(getDriver());
        homePage.searchProduct("Màn Hình Samsung S3 LS24F320GAEXXV 24 Inch/ FHD/ IPS/ 120Hz/ 5ms");
        homePage.clickFirstProduct();

        ProductDetailPage productDetailPage = new ProductDetailPage(getDriver());
        productDetailPage.addToCart();
        productDetailPage.goToCart();

        CartPage cartPage = new CartPage(getDriver());
        cartPage.proceedToCheckout();
        cartPage.verifyMissingPhoneNumberError();

        log.info("Missing phone number error verified during checkout");
    }
}

