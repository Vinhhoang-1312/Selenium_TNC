package modules.productdetail;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import base.WaitUtils;

import java.util.List;

public class ProductDetailPage {
    private WebDriver driver;

    public ProductDetailPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    // Product Detail Elements - Cần cập nhật với locators thực tế từ TNC Store
    @FindBy(xpath = "//div[contains(@class,'product-item') or contains(@class,'product-card')]")
    private List<WebElement> productItems;

    @FindBy(xpath = "//button[contains(@class,'add-to-cart') or contains(text(),'Thêm vào giỏ hàng')]")
    private WebElement addToCartButton;

    @FindBy(xpath = "//div[contains(@class,'success-notification') or contains(@class,'cart-notification')]")
    private WebElement successNotification;

    @FindBy(xpath = "//div[contains(@class,'product-images') or contains(@class,'image-gallery')]")
    private WebElement productMainImage;

    @FindBy(xpath = "//div[contains(@class,'image-list') or contains(@class,'thumbnail-list')]//img")
    private List<WebElement> productThumbnailImages;

    @FindBy(xpath = "//div[contains(@class,'image-popup') or contains(@class,'lightbox')]")
    private WebElement imagePopup;

    @FindBy(xpath = "//button[contains(@class,'mua-ngay') or contains(text(),'Mua ngay')]")
    private WebElement buyNowButton;

    @FindBy(xpath = "//div[contains(@class,'technical-specs') or contains(@class,'specifications')]")
    private WebElement technicalSpecsSection;

    @FindBy(xpath = "//button[contains(@class,'xem-them') or contains(text(),'Xem thêm')]")
    private WebElement viewMoreButton;

    @FindBy(xpath = "//div[contains(@class,'specs-popup') or contains(@class,'technical-popup')]")
    private WebElement technicalSpecsPopup;

    @FindBy(xpath = "//h1[contains(@class,'product-title') or contains(@class,'product-name')]")
    private WebElement productTitle;

    @FindBy(xpath = "//div[contains(@class,'product-price')]")
    private WebElement productPrice;

    // Methods for Product Detail Test Cases

    // DTL-001: Access to detail product
    public void scrollDownAndClickProduct() {
        // Scroll down to see products
        driver.executeScript("window.scrollTo(0, document.body.scrollHeight/2);");
        WaitUtils.waitForPageLoad(driver);

        // Wait for products to be visible
        WaitUtils.waitForElementVisible(driver, By.xpath("//div[contains(@class,'product-item')]"));

        // Click on any available product
        if (!productItems.isEmpty()) {
            WaitUtils.waitForElementClickable(driver, productItems.get(0));
            productItems.get(0).click();
        }
    }

    public boolean isProductDetailPageLoaded() {
        WaitUtils.waitForPageLoad(driver);
        try {
            return productTitle.isDisplayed() && productPrice.isDisplayed();
        } catch (Exception e) {
            // Alternative check using URL
            return driver.getCurrentUrl().contains("san-pham") ||
                   driver.getCurrentUrl().contains("product");
        }
    }

    // DTL-002: Verify add to cart button
    public void clickAddToCartButton() {
        WaitUtils.waitForElementClickable(driver, addToCartButton);
        addToCartButton.click();
    }

    public boolean isSuccessNotificationDisplayed() {
        try {
            WaitUtils.waitForElementVisible(driver, successNotification);
            return successNotification.isDisplayed() &&
                   (successNotification.getText().contains("thành công") ||
                    successNotification.getText().contains("success") ||
                    successNotification.getText().contains("đã thêm"));
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isAddToCartButtonVisible() {
        try {
            return addToCartButton.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    // DTL-003: Verify image and list image can click
    public void clickThumbnailImage(int index) {
        if (index < productThumbnailImages.size()) {
            WaitUtils.waitForElementClickable(driver, productThumbnailImages.get(index));
            productThumbnailImages.get(index).click();
        }
    }

    public void clickFirstThumbnailImage() {
        clickThumbnailImage(0);
    }

    public boolean isImagePopupDisplayed() {
        try {
            WaitUtils.waitForElementVisible(driver, imagePopup);
            return imagePopup.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean areThumbnailImagesVisible() {
        try {
            return !productThumbnailImages.isEmpty() &&
                   productThumbnailImages.get(0).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    // DTL-004: Verify "mua ngay" button works properly
    public void clickBuyNowButton() {
        WaitUtils.waitForElementClickable(driver, buyNowButton);
        buyNowButton.click();
        WaitUtils.waitForPageLoad(driver);
    }

    public boolean isRedirectedToCartPage() {
        WaitUtils.waitForPageLoad(driver);
        String currentUrl = driver.getCurrentUrl();
        return currentUrl.contains("cart") || currentUrl.contains("gio-hang");
    }

    public boolean isBuyNowButtonVisible() {
        try {
            return buyNowButton.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isProductInCart() {
        // This would need to be verified on the cart page
        // For now, we'll check if we're on cart page
        return isRedirectedToCartPage();
    }

    // DTL-005: Verify "xem thêm" button in Technical specifications
    public void scrollToTechnicalSpecs() {
        try {
            driver.executeScript("arguments[0].scrollIntoView(true);", technicalSpecsSection);
            WaitUtils.waitForElementVisible(driver, technicalSpecsSection);
        } catch (Exception e) {
            // If specs section not found, scroll down to find it
            driver.executeScript("window.scrollTo(0, document.body.scrollHeight);");
        }
    }

    public void clickViewMoreButton() {
        scrollToTechnicalSpecs();
        WaitUtils.waitForElementClickable(driver, viewMoreButton);
        viewMoreButton.click();
    }

    public boolean isViewMoreButtonVisible() {
        try {
            scrollToTechnicalSpecs();
            return viewMoreButton.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isTechnicalSpecsPopupDisplayed() {
        try {
            WaitUtils.waitForElementVisible(driver, technicalSpecsPopup);
            return technicalSpecsPopup.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isTechnicalSpecsSectionVisible() {
        try {
            return technicalSpecsSection.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    // Utility methods
    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    public String getProductTitle() {
        try {
            WaitUtils.waitForElementVisible(driver, productTitle);
            return productTitle.getText();
        } catch (Exception e) {
            return "";
        }
    }

    public String getProductPrice() {
        try {
            WaitUtils.waitForElementVisible(driver, productPrice);
            return productPrice.getText();
        } catch (Exception e) {
            return "";
        }
    }

    public boolean isOnMainPage() {
        String currentUrl = getCurrentUrl();
        return currentUrl.equals("https://www.tncstore.vn/") ||
               currentUrl.contains("tncstore.vn") && !currentUrl.contains("san-pham");
    }

    public void navigateToMainPage() {
        driver.get("https://www.tncstore.vn/");
        WaitUtils.waitForPageLoad(driver);
    }

    public int getAvailableProductCount() {
        try {
            return productItems.size();
        } catch (Exception e) {
            return 0;
        }
    }

    public void waitForPageLoad() {
        WaitUtils.waitForPageLoad(driver);
    }
}
