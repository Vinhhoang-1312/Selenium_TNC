package modules.productdetail;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
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

    // Product elements
    @FindBy(xpath = "//div[contains(@class,'product-item') or contains(@class,'product-card')]")
    private List<WebElement> productItems;

    @FindBy(xpath = "//button[contains(@class,'add-to-cart') or contains(text(),'Thêm vào giỏ hàng')]")
    private WebElement addToCartButton;

    @FindBy(xpath = "//div[contains(@class,'success-notification') or contains(@class,'cart-notification')]")
    private WebElement successNotification;

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

    // Navigation methods
    public void scrollDownAndClickProduct() {
        ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight/2);");
        WaitUtils.waitForPageLoad(driver);

        WaitUtils.waitForElementVisible(driver, By.xpath("//div[contains(@class,'product-item')]"));

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
            return driver.getCurrentUrl().contains("san-pham") ||
                   driver.getCurrentUrl().contains("product");
        }
    }

    // Add to cart methods
    public void clickAddToCartButton() {
        WaitUtils.waitForElementClickable(driver, addToCartButton);
        addToCartButton.click();
    }

    public boolean isSuccessNotificationDisplayed() {
        try {
            WaitUtils.waitForElementVisible(driver, By.xpath("//div[contains(@class,'success-notification')]"));
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

    // Image gallery methods
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
            WaitUtils.waitForElementVisible(driver, By.xpath("//div[contains(@class,'image-popup')]"));
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

    // Buy now methods
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
        return isRedirectedToCartPage();
    }

    // Technical specs methods
    public void scrollToTechnicalSpecs() {
        try {
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", technicalSpecsSection);
            WaitUtils.waitForElementVisible(driver, By.xpath("//div[contains(@class,'technical-specs')]"));
        } catch (Exception e) {
            ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight);");
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
            WaitUtils.waitForElementVisible(driver, By.xpath("//div[contains(@class,'specs-popup')]"));
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
            WaitUtils.waitForElementVisible(driver, By.xpath("//h1[contains(@class,'product-title')]"));
            return productTitle.getText();
        } catch (Exception e) {
            return "";
        }
    }

    public String getProductPrice() {
        try {
            WaitUtils.waitForElementVisible(driver, By.xpath("//div[contains(@class,'product-price')]"));
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
