package modules.cart;

import base.BaseTest;
import com.aventstack.extentreports.Status;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import utils.ExtentManager;
import data.ExcelReader;
import data.CartTestData;

public class CartTest extends BaseTest {
    private CartPage cartPage;

    @BeforeMethod
    public void setUpCartTest() {
        cartPage = new CartPage(driver);
    }

    @Test(description = "TC001: Add product to cart - User navigates to product page and clicks Add to Cart")
    public void testAddProductToCart() {
        test = ExtentManager.startTest("TC001: Add product to cart");

        try {
            logInfo("Starting test: Add product to cart");

            // Get test data from Excel or fallback
            String productUrl = ExcelReader.getCartData("TC001", "productUrl");
            if (productUrl.isEmpty()) {
                productUrl = CartTestData.PRODUCT_URLS[0];
            }

            // Step 1: Navigate to any product page
            String fullProductUrl = driver.getCurrentUrl() + productUrl;
            driver.get(fullProductUrl);
            logInfo("Navigated to product page: " + fullProductUrl);

            // Get initial cart count
            String initialCartCount = cartPage.getCartCounter();
            logInfo("Initial cart count: " + initialCartCount);

            // Step 2: Click "Add to Cart" button
            Assert.assertTrue(cartPage.isAddToCartButtonVisible(), "Add to Cart button should be visible");
            cartPage.addProductToCart();
            logInfo("Clicked Add to Cart button");

            // Wait for cart to update
            cartPage.waitForCartUpdate();

            // Verify: Product is added to cart and appears in the cart icon
            Assert.assertTrue(cartPage.isProductAddedToCart(), "Product should be added to cart");
            logPass("Product successfully added to cart");

            // Verify cart counter increased
            String newCartCount = cartPage.getCartCounter();
            logInfo("New cart count: " + newCartCount);
            Assert.assertNotEquals(initialCartCount, newCartCount, "Cart counter should increase");

            test.log(Status.PASS, "TC001 PASSED: Product successfully added to cart");

        } catch (Exception e) {
            logFail("Test failed: " + e.getMessage());
            takeScreenshot("TC001_AddProductToCart_Failed");
            test.log(Status.FAIL, "TC001 FAILED: " + e.getMessage());
            throw e;
        }
    }

    @Test(description = "TC002: Update product quantity in cart - Change quantity from 1 to 2")
    public void testUpdateProductQuantityInCart() {
        test = ExtentManager.startTest("TC002: Update product quantity in cart");

        try {
            logInfo("Starting test: Update product quantity in cart");

            // Get test data from Excel or fallback
            String newQuantityStr = ExcelReader.getCartData("TC002", "quantity");
            int newQuantity = newQuantityStr.isEmpty() ? CartTestData.UPDATED_QUANTITY : Integer.parseInt(newQuantityStr);

            // Prerequisite: Add product to cart first
            testAddProductToCart();

            // Step 1: Go to the cart page
            cartPage.navigateToCart();
            logInfo("Navigated to cart page");

            // Verify cart contains at least one product
            Assert.assertTrue(cartPage.getCartItemCount() > 0, "Cart should contain at least one product");

            // Get initial quantity and total price
            int initialQuantity = cartPage.getProductQuantity(0);
            String initialTotal = cartPage.getTotalPrice();
            logInfo("Initial quantity: " + initialQuantity + ", Initial total: " + initialTotal);

            // Step 2: Change product quantity
            cartPage.updateProductQuantity(0, newQuantity);
            logInfo("Updated quantity to: " + newQuantity);

            // Wait for update
            cartPage.waitForCartUpdate();

            // Step 3: Verify quantity is updated and total price reflects the change
            int updatedQuantity = cartPage.getProductQuantity(0);
            String newTotal = cartPage.getTotalPrice();

            Assert.assertEquals(updatedQuantity, newQuantity,
                "Quantity should be updated to " + newQuantity);
            Assert.assertNotEquals(initialTotal, newTotal, "Total price should change when quantity changes");

            logPass("Product quantity successfully updated and total price reflected the change");
            test.log(Status.PASS, "TC002 PASSED: Product quantity updated successfully");

        } catch (Exception e) {
            logFail("Test failed: " + e.getMessage());
            takeScreenshot("TC002_UpdateQuantity_Failed");
            test.log(Status.FAIL, "TC002 FAILED: " + e.getMessage());
            throw e;
        }
    }

    @Test(description = "TC003: Remove product from cart - Click remove button next to product")
    public void testRemoveProductFromCart() {
        test = ExtentManager.startTest("TC003: Remove product from cart");

        try {
            logInfo("Starting test: Remove product from cart");

            // Prerequisite: Add product to cart first
            testAddProductToCart();

            // Step 1: Go to the cart page
            cartPage.navigateToCart();
            logInfo("Navigated to cart page");

            // Verify cart contains at least one product
            Assert.assertTrue(cartPage.getCartItemCount() > 0, "Cart should contain at least one product");

            int initialItemCount = cartPage.getCartItemCount();
            logInfo("Initial cart item count: " + initialItemCount);

            // Step 2: Click "Remove" or trash icon next to the product
            cartPage.removeProductFromCart(0);
            logInfo("Clicked remove button for first product");

            // Wait for cart to update
            cartPage.waitForCartUpdate();

            // Verify: Product is removed from the cart and total price is updated
            Assert.assertTrue(cartPage.isProductRemovedFromCart(initialItemCount),
                "Product should be removed from cart");

            int newItemCount = cartPage.getCartItemCount();
            logInfo("New cart item count: " + newItemCount);
            Assert.assertTrue(newItemCount < initialItemCount, "Cart item count should decrease");

            logPass("Product successfully removed from cart");
            test.log(Status.PASS, "TC003 PASSED: Product successfully removed from cart");

        } catch (Exception e) {
            logFail("Test failed: " + e.getMessage());
            takeScreenshot("TC003_RemoveProduct_Failed");
            test.log(Status.FAIL, "TC003 FAILED: " + e.getMessage());
            throw e;
        }
    }

    @Test(description = "TC004: Verify cart total price - Check displayed total price calculation")
    public void testVerifyCartTotalPrice() {
        test = ExtentManager.startTest("TC004: Verify cart total price");

        try {
            logInfo("Starting test: Verify cart total price");

            // Prerequisite: Add product to cart first
            testAddProductToCart();

            // Step 1: Go to the cart page
            cartPage.navigateToCart();
            logInfo("Navigated to cart page");

            // Verify cart contains at least one product
            Assert.assertTrue(cartPage.getCartItemCount() > 0, "Cart should contain at least one product");

            // Step 2: Check the displayed total price
            String displayedTotal = cartPage.getTotalPrice();
            logInfo("Displayed total price: " + displayedTotal);

            // Verify: Total price is correctly calculated based on product quantity and unit price
            Assert.assertTrue(cartPage.isTotalPriceCorrect(),
                "Total price should be correctly calculated based on product quantity and unit price");

            double calculatedTotal = cartPage.calculateExpectedTotal();
            logInfo("Calculated expected total: " + calculatedTotal);

            logPass("Cart total price is correctly calculated");
            test.log(Status.PASS, "TC004 PASSED: Cart total price calculation is correct");

        } catch (Exception e) {
            logFail("Test failed: " + e.getMessage());
            takeScreenshot("TC004_VerifyTotalPrice_Failed");
            test.log(Status.FAIL, "TC004 FAILED: " + e.getMessage());
            throw e;
        }
    }

    @Test(description = "TC005: Navigate to checkout page - Click Checkout button from cart")
    public void testNavigateToCheckoutPage() {
        test = ExtentManager.startTest("TC005: Navigate to checkout page");

        try {
            logInfo("Starting test: Navigate to checkout page");

            // Prerequisite: Add product to cart first
            testAddProductToCart();

            // Step 1: Go to the cart page
            cartPage.navigateToCart();
            logInfo("Navigated to cart page");

            // Verify cart contains at least one product
            Assert.assertTrue(cartPage.getCartItemCount() > 0, "Cart should contain at least one product");

            // Step 2: Click "Checkout" or "Proceed to Payment" button
            cartPage.proceedToCheckout();
            logInfo("Clicked checkout button");

            // Verify: User is redirected to the checkout page showing shipping and payment options
            Assert.assertTrue(cartPage.isCheckoutPageLoaded(),
                "User should be redirected to checkout page");

            String currentUrl = getCurrentUrl();
            logInfo("Current URL after checkout: " + currentUrl);
            Assert.assertTrue(currentUrl.contains("checkout") || currentUrl.contains("thanh-toan"),
                "URL should contain checkout or thanh-toan");

            logPass("Successfully navigated to checkout page");
            test.log(Status.PASS, "TC005 PASSED: Successfully navigated to checkout page");

        } catch (Exception e) {
            logFail("Test failed: " + e.getMessage());
            takeScreenshot("TC005_NavigateToCheckout_Failed");
            test.log(Status.FAIL, "TC005 FAILED: " + e.getMessage());
            throw e;
        }
    }

    // Additional helper test methods

    @Test(description = "Helper: Test cart when empty")
    public void testEmptyCartBehavior() {
        test = ExtentManager.startTest("Helper: Test empty cart behavior");

        try {
            logInfo("Testing empty cart behavior");

            // Navigate to cart page
            cartPage.navigateToCart();

            // If cart has items, remove them all
            while (cartPage.getCartItemCount() > 0) {
                cartPage.removeProductFromCart(0);
                cartPage.waitForCartUpdate();
            }

            // Verify empty cart message is displayed
            Assert.assertTrue(cartPage.isCartEmpty(), "Cart should be empty");
            logPass("Empty cart behavior verified");

        } catch (Exception e) {
            logFail("Test failed: " + e.getMessage());
            takeScreenshot("EmptyCart_Test_Failed");
            throw e;
        }
    }
}
