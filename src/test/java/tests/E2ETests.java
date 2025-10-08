package tests;

import core.BaseTest;
import data.TestUser;
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
import pages.RegisterPage;

@Listeners({BaseListener.class})
@Epic("E-Commerce")
@Feature("End-to-End User Journey")
public class E2ETests extends BaseTest {

    @Test(groups = {"e2e", "regression"},
            description = "E2E-01: Register -> Login -> Search -> Add to cart -> Verify cart")
    @Story("Complete User Journey")
    @Severity(SeverityLevel.BLOCKER)
    @Description("Verify complete user journey from registration to cart verification")
    public void testRegisterLoginSearchAddToCart() {
        // Initialize page objects
        LoginPage loginPage = new LoginPage(getDriver());
        RegisterPage registerPage;
        HomePage homePage = new HomePage(getDriver());
        ProductDetailPage productDetailPage = new ProductDetailPage(getDriver());
        CartPage cartPage = new CartPage(getDriver());
        PopupHandler popupHandler = new PopupHandler(getDriver());

        try {
            // Create unique user
            TestUser user = TestUser.createUniqueUser("E2EUser");
            logger.info("Starting E2E test with user: {}", user.email);
            Allure.parameter("Test User Email", user.email);

            popupHandler.dismissAllPopups();

            // Register
            Allure.step("Step 1: Register new user - " + user.email);
            registerPage = loginPage.navigateToRegister();
            registerPage.performRegister(user.name, user.email, user.password);
            logger.info("Registration completed for: {}", user.email);

            // Login
            Allure.step("Step 2: Login with newly created user");
            getDriver().get(baseUrl);
            popupHandler.dismissAllPopups();
            loginPage.performLogin(user.email, user.password);
            Assert.assertTrue(loginPage.isLoginSuccessful(), "User should be able to login with newly created credentials");
            logger.info("Login successful for: {}", user.email);

            // Search
            Allure.step("Step 3: Search for product");
            homePage.searchProduct("laptop");
            homePage.clickFirstProduct();
            logger.info("Product search and selection completed");

            // Add to cart
            Allure.step("Step 4: Add product to cart");
            productDetailPage.addToCart();
            logger.info("Product added to cart");

            // Verify cart
            Allure.step("Step 5: Verify cart contains product");
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
