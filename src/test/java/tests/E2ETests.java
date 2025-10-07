package tests;

import core.BaseTest;
import data.TestUser;
import helpers.PopupHandler;
import listeners.BaseListener;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.CartPage;
import pages.HomePage;
import pages.LoginPage;
import pages.ProductDetailPage;
import pages.RegisterPage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Listeners({BaseListener.class})
public class E2ETests extends BaseTest {
    private static final Logger log = LoggerFactory.getLogger(E2ETests.class);

    @Test(groups = {"e2e", "regression"},
            description = "E2E-01: Register -> Login -> Search -> Add to cart -> Verify cart")
    public void testRegisterLoginSearchAddToCart() {
        try {
            // Create unique user
            TestUser user = TestUser.createUniqueUser("E2EUser");
            log.info("Starting E2E test with user: {}", user.email);

            PopupHandler popupHandler = new PopupHandler(getDriver());
            popupHandler.dismissAllPopups();

            // Register
            LoginPage loginPage = new LoginPage(getDriver());
            RegisterPage registerPage = loginPage.navigateToRegister();
            registerPage.performRegister(user.name, user.email, user.password);
            customWait(2000);
            log.info("Registration completed for: {}", user.email);

            // Login
            getDriver().get(baseUrl);
            customWait(1000);
            popupHandler.dismissAllPopups();
            loginPage.performLogin(user.email, user.password);
            Assert.assertTrue(loginPage.isLoginSuccessful(), "User should be able to login with newly created credentials");
            log.info("Login successful for: {}", user.email);

            // Search
            HomePage homePage = new HomePage(getDriver());
            homePage.searchProduct("laptop");
            homePage.clickFirstProduct();
            log.info("Product search and selection completed");

            // Add to cart
            ProductDetailPage productDetailPage = new ProductDetailPage(getDriver());
            productDetailPage.addToCart();
            customWait(2000);
            log.info("Product added to cart");

            // Verify cart
            productDetailPage.goToCart();
            CartPage cartPage = new CartPage(getDriver());
            int cartSize = cartPage.getCartSize();
            log.info("Cart size after add: {}", cartSize);

            Assert.assertTrue(cartSize >= 1, "Cart should contain at least one item after adding product");
            log.info("E2E test completed successfully - Product verified in cart");

        } catch (Exception e) {
            log.error("E2E test failed: {}", e.getMessage(), e);
            throw e;
        }
    }
}
