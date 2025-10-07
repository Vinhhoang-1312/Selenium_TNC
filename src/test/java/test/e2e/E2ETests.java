package test.e2e;

import data.AuthenticationTestData;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import commons.DriverFactory;
import test.BaseTest;
import pages.AuthenticationPage;
import pages.HomePage;
import pages.ProductDetailPage;
import pages.CartPage;
import helpers.PopupHandler;

import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class E2ETests {
    private static final Logger log = LoggerFactory.getLogger(E2ETests.class);
    private WebDriver driver;
    private String baseUrl = "https://www.tncstore.vn/";
    private PopupHandler popupHandler;
    @BeforeClass
    public void setUp() {
        // ✅ Tự khởi tạo driver từ DriverFactory
        driver = DriverFactory.getDriver();
        driver.get(baseUrl);
        popupHandler = new PopupHandler(driver);
    }

    @Test(groups = {"e2e", "regression"}, description = "E2E-01: Register -> Login -> Search -> Add to cart -> Verify cart")
    public void testRegisterLoginSearchAddToCart() throws InterruptedException {
        System.out.println("E2E-01: Register -> Login -> Search -> Add to cart -> Verify cart");

        try {
            AuthenticationTestData.TestUser user = AuthenticationTestData.createUniqueUser("E2EUser3");
            popupHandler.dismissAllPopups();
            AuthenticationPage authPage = new AuthenticationPage(driver);
            boolean registered = authPage.registerAndAssertSuccess(user);
            System.out.println("Registration attempted for: " + user.email);
//            Assert.assertTrue(registered, "User should be registered and auto-logged in after registration");
//            System.out.println("Registration successful: " + user.email);
//
//            Thread.sleep(4000);
//
//            authPage.logout();
//            dismissPopupsIfPresent();
            authPage.performLogin(user.email, user.password);
            Assert.assertTrue(authPage.isUserLoggedIn(), "User should be able to login with newly created credentials");
            System.out.println("Login successful for: " + user.email);

            HomePage home = new HomePage(driver);
            home.searchProduct("laptop");
            home.clickProduct();

            ProductDetailPage product = new ProductDetailPage(driver);
            product.addToCart();

            product.goToCart();
            CartPage cart = new CartPage(driver);
            int cartSize = cart.getCartSize();
            System.out.println("Cart size after add: " + cartSize);
            Assert.assertTrue(cartSize >= 1, "Cart should contain at least one item after adding product");
            System.out.println("Product successfully added to cart and verified");


        } catch (Exception e) {
            System.out.println("E2E test failed: " + e.getMessage());
            log.error("E2E test error", e);
            throw e;
        }
    }

    @AfterClass
    public void tearDown() {
        // ✅ Tự đóng driver
        DriverFactory.quitDriver();
    }
}
