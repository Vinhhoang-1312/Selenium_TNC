package tests;

import core.BaseTest;
import data.AuthenticationTestData;
import helpers.PopupHandler;
import io.qameta.allure.*;
import listeners.BaseListener;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.CartPage;
import pages.HomePage;
import pages.LoginPage;
import pages.ProductDetailPage;
import utils.TestDataProviders;

@Listeners({BaseListener.class})
@Epic("E-Commerce")
@Feature("End-to-End User Journey")
public class E2ETests extends BaseTest {

    @Test(groups = {"e2e", "regression"},
            description = "E2E-01: Login -> Search -> Add to cart -> Verify cart",
            dataProvider = "validLoginData", dataProviderClass = TestDataProviders.class)
    @Story("Complete User Journey")
    @Severity(SeverityLevel.BLOCKER)
    @Description("Verify complete user journey from login to cart verification")
    public void testLoginSearchAddToCart(Object[] validRow) {
        // Initialize page objects
        LoginPage loginPage = new LoginPage(getDriver());
        HomePage homePage = new HomePage(getDriver());
        ProductDetailPage productDetailPage = new ProductDetailPage(getDriver());
        CartPage cartPage = new CartPage(getDriver());
        PopupHandler popupHandler = new PopupHandler(getDriver());

        try {
            // Get valid credentials from Excel file
            String email = (String) validRow[1];
            String password = (String) validRow[2];

            logger.info("Starting E2E test with user: {}", email);
            Allure.parameter("Test User Email", email);

            popupHandler.dismissAllPopups();

            // Login
            loginPage.performLogin(email, password);
            Assert.assertTrue(loginPage.isLoginSuccessful(), "User should be able to login with valid credentials");
            logger.info("Login successful for: {}", email);

            // Search
            homePage.searchProduct("laptop");
            homePage.clickFirstProduct();
            logger.info("Product search and selection completed");

            // Add to cart
            productDetailPage.addToCart();
            logger.info("Product added to cart");

            // Verify cart
            productDetailPage.goToCart();
            int cartSize = cartPage.getCartSize();
            logger.info("Cart size after add: {}", cartSize);
            Allure.parameter("Cart Size", cartSize);

            Assert.assertTrue(cartSize >= 1, "Cart should contain at least one item after adding product");
            logger.info("E2E test completed successfully - Product verified in cart");

        } catch (Exception e) {
            logger.error("E2E test failed: {}", e.getMessage(), e);
            throw e;
        }
    }
}
