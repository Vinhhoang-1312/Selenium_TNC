package modules.productdetail;

import base.BaseTest;
import com.aventstack.extentreports.Status;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import utils.ExtentManager;
import data.ExcelReader;
import data.ProductDetailTestData;

public class ProductDetailTest extends BaseTest {
    private ProductDetailPage productDetailPage;

    @BeforeMethod
    public void setUpProductDetailTest() {
        productDetailPage = new ProductDetailPage(driver);
    }

    @Test(description = "DTL-001: Access to detail product - Navigate from main page to product detail")
    public void testAccessToDetailProduct() {
        test = ExtentManager.startTest("DTL-001: Access to detail product");

        try {
            logInfo("Starting test: Access to detail product from main page");

            // Step 1: Stay at main page (already done in BaseTest setup)
            Assert.assertTrue(productDetailPage.isOnMainPage(), "Should be on main page");
            logInfo("Confirmed on main page");

            // Step 2: Scroll down and click to any product
            logInfo("Scrolling down to find products");
            productDetailPage.scrollDownAndClickProduct();
            logInfo("Clicked on a product");

            // Verify: Redirect to detail product page
            Assert.assertTrue(productDetailPage.isProductDetailPageLoaded(),
                "Should be redirected to product detail page");

            String currentUrl = productDetailPage.getCurrentUrl();
            logInfo("Successfully navigated to product detail page: " + currentUrl);

            // Additional verification - check if product details are visible
            String productTitle = productDetailPage.getProductTitle();
            String productPrice = productDetailPage.getProductPrice();

            Assert.assertFalse(productTitle.isEmpty(), "Product title should be displayed");
            Assert.assertFalse(productPrice.isEmpty(), "Product price should be displayed");

            logInfo("Product details loaded: " + productTitle + " - " + productPrice);
            logPass("Successfully accessed product detail page");
            test.log(Status.PASS, "DTL-001 PASSED: Successfully navigated to product detail page");

        } catch (Exception e) {
            logFail("Test failed: " + e.getMessage());
            takeScreenshot("DTL-001_AccessProductDetail_Failed");
            test.log(Status.FAIL, "DTL-001 FAILED: " + e.getMessage());
            throw e;
        }
    }

    @Test(description = "DTL-002: Verify add to cart button functionality")
    public void testVerifyAddToCartButton() {
        test = ExtentManager.startTest("DTL-002: Verify add to cart button");

        try {
            logInfo("Starting test: Verify add to cart button functionality");

            // Prerequisite: Navigate to product detail page first
            testAccessToDetailProduct();

            // Verify we're on product detail page
            Assert.assertTrue(productDetailPage.isProductDetailPageLoaded(),
                "Should be on product detail page");
            logInfo("Confirmed on product detail page");

            // Step 1: Click to "add to cart" button
            Assert.assertTrue(productDetailPage.isAddToCartButtonVisible(),
                "Add to cart button should be visible");

            productDetailPage.clickAddToCartButton();
            logInfo("Clicked add to cart button");

            // Verify: A pop-up notification about "success add to cart" shows up
            Assert.assertTrue(productDetailPage.isSuccessNotificationDisplayed(),
                "Success notification should be displayed after adding to cart");

            logPass("Add to cart functionality works correctly");
            test.log(Status.PASS, "DTL-002 PASSED: Add to cart button works and shows success notification");

        } catch (Exception e) {
            logFail("Test failed: " + e.getMessage());
            takeScreenshot("DTL-002_AddToCart_Failed");
            test.log(Status.FAIL, "DTL-002 FAILED: " + e.getMessage());
            throw e;
        }
    }

    @Test(description = "DTL-003: Verify image gallery functionality")
    public void testVerifyImageGalleryFunctionality() {
        test = ExtentManager.startTest("DTL-003: Verify image gallery functionality");

        try {
            logInfo("Starting test: Verify image and list image can click");

            // Prerequisite: Navigate to product detail page first
            testAccessToDetailProduct();

            // Verify we're on product detail page
            Assert.assertTrue(productDetailPage.isProductDetailPageLoaded(),
                "Should be on product detail page");
            logInfo("Confirmed on product detail page");

            // Step 1: Click to small image in image list under product image
            Assert.assertTrue(productDetailPage.areThumbnailImagesVisible(),
                "Thumbnail images should be visible");

            productDetailPage.clickFirstThumbnailImage();
            logInfo("Clicked on first thumbnail image");

            // Verify: A pop-up to make image bigger
            Assert.assertTrue(productDetailPage.isImagePopupDisplayed(),
                "Image popup should be displayed when clicking thumbnail");

            logPass("Image gallery functionality works correctly");
            test.log(Status.PASS, "DTL-003 PASSED: Image gallery shows popup when thumbnail is clicked");

        } catch (Exception e) {
            logFail("Test failed: " + e.getMessage());
            takeScreenshot("DTL-003_ImageGallery_Failed");
            test.log(Status.FAIL, "DTL-003 FAILED: " + e.getMessage());
            throw e;
        }
    }

    @Test(description = "DTL-004: Verify 'mua ngay' button functionality")
    public void testVerifyBuyNowButton() {
        test = ExtentManager.startTest("DTL-004: Verify 'mua ngay' button");

        try {
            logInfo("Starting test: Verify 'mua ngay' button works properly");

            // Prerequisite: Navigate to product detail page first
            testAccessToDetailProduct();

            // Verify we're on product detail page
            Assert.assertTrue(productDetailPage.isProductDetailPageLoaded(),
                "Should be on product detail page");
            logInfo("Confirmed on product detail page");

            String productTitle = productDetailPage.getProductTitle();
            logInfo("Current product: " + productTitle);

            // Step 1: Click to "mua ngay" button
            Assert.assertTrue(productDetailPage.isBuyNowButtonVisible(),
                "Buy now button should be visible");

            productDetailPage.clickBuyNowButton();
            logInfo("Clicked 'mua ngay' button");

            // Verify: Redirect to cart page with product from previous page
            Assert.assertTrue(productDetailPage.isRedirectedToCartPage(),
                "Should be redirected to cart page");

            String currentUrl = productDetailPage.getCurrentUrl();
            logInfo("Successfully redirected to cart page: " + currentUrl);

            // Additional verification - check if product is in cart
            Assert.assertTrue(productDetailPage.isProductInCart(),
                "Product should be added to cart");

            logPass("'Mua ngay' button functionality works correctly");
            test.log(Status.PASS, "DTL-004 PASSED: 'Mua ngay' button redirects to cart with product");

        } catch (Exception e) {
            logFail("Test failed: " + e.getMessage());
            takeScreenshot("DTL-004_BuyNow_Failed");
            test.log(Status.FAIL, "DTL-004 FAILED: " + e.getMessage());
            throw e;
        }
    }

    @Test(description = "DTL-005: Verify 'xem thêm' button in Technical specifications")
    public void testVerifyTechnicalSpecsViewMore() {
        test = ExtentManager.startTest("DTL-005: Verify 'xem thêm' in Technical specs");

        try {
            logInfo("Starting test: Verify 'xem thêm' button in Technical specifications section");

            // Prerequisite: Navigate to product detail page first
            testAccessToDetailProduct();

            // Verify we're on product detail page
            Assert.assertTrue(productDetailPage.isProductDetailPageLoaded(),
                "Should be on product detail page");
            logInfo("Confirmed on product detail page");

            // Navigate to technical specifications section
            productDetailPage.scrollToTechnicalSpecs();
            logInfo("Scrolled to technical specifications section");

            // Step 1: Click to "xem thêm" button
            Assert.assertTrue(productDetailPage.isViewMoreButtonVisible(),
                "View more button should be visible in technical specs section");

            productDetailPage.clickViewMoreButton();
            logInfo("Clicked 'xem thêm' button in technical specifications");

            // Verify: A pop-up list shows up with all technical information about product
            Assert.assertTrue(productDetailPage.isTechnicalSpecsPopupDisplayed(),
                "Technical specifications popup should be displayed");

            logPass("Technical specifications 'xem thêm' functionality works correctly");
            test.log(Status.PASS, "DTL-005 PASSED: 'Xem thêm' shows technical specifications popup");

        } catch (Exception e) {
            logFail("Test failed: " + e.getMessage());
            takeScreenshot("DTL-005_TechnicalSpecs_Failed");
            test.log(Status.FAIL, "DTL-005 FAILED: " + e.getMessage());
            throw e;
        }
    }

    // Helper test for product detail page verification
    @Test(description = "Helper: Verify product detail page elements")
    public void testProductDetailPageElements() {
        test = ExtentManager.startTest("Helper: Verify product detail page elements");

        try {
            logInfo("Testing product detail page elements visibility");

            // Navigate to product detail page
            testAccessToDetailProduct();

            // Verify essential elements are present
            Assert.assertTrue(productDetailPage.isAddToCartButtonVisible(),
                "Add to cart button should be visible");
            Assert.assertTrue(productDetailPage.isBuyNowButtonVisible(),
                "Buy now button should be visible");
            Assert.assertTrue(productDetailPage.areThumbnailImagesVisible(),
                "Thumbnail images should be visible");

            logPass("All essential product detail elements are present");

        } catch (Exception e) {
            logFail("Helper test failed: " + e.getMessage());
            takeScreenshot("ProductDetail_Helper_Failed");
            throw e;
        }
    }
}

