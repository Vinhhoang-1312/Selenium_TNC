package tests;

import core.BaseTest;
import io.qameta.allure.*;
import listeners.BaseListener;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.CartPage;
import pages.HomePage;
import pages.ProductDetailPage;

@Listeners({BaseListener.class})
@Epic("E-Commerce")
@Feature("Checkout Process")
public class CheckoutTests extends BaseTest {

    @Test(groups = {"checkout"},
            description = "CHECKOUT-01: Verify error when phone number is missing")
    @Story("Checkout Validation")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify that checkout process shows error when mandatory phone number field is missing")
    public void testCheckoutMissingPhoneNumber() {
        // Initialize page objects
        HomePage homePage = new HomePage(getDriver());
        ProductDetailPage productDetailPage = new ProductDetailPage(getDriver());
        CartPage cartPage = new CartPage(getDriver());

        homePage.searchProduct("Màn Hình Samsung S3 LS24F320GAEXXV 24 Inch/ FHD/ IPS/ 120Hz/ 5ms");
        homePage.clickFirstProduct();

        productDetailPage.addToCart();
        productDetailPage.goToCart();

        cartPage.proceedToCheckout();
        cartPage.verifyMissingPhoneNumberError();

        logger.info("Missing phone number error verified during checkout");
    }
}
