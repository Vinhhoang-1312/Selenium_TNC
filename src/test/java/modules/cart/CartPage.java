package modules.cart;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import base.WaitUtils;

import java.util.List;

public class CartPage {
    private WebDriver driver;

    public CartPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    // Cart elements
    @FindBy(xpath = "//button[contains(@class,'add-to-cart') or contains(text(),'Thêm vào giỏ hàng')]")
    private WebElement addToCartButton;

    @FindBy(xpath = "//a[contains(@href,'cart') or contains(@class,'cart-icon')]")
    private WebElement cartIcon;

    @FindBy(xpath = "//span[contains(@class,'cart-count') or contains(@class,'cart-counter')]")
    private WebElement cartCounter;

    @FindBy(xpath = "//input[contains(@class,'quantity') or contains(@name,'quantity')]")
    private List<WebElement> quantityInputs;

    @FindBy(xpath = "//button[contains(@class,'update') or contains(text(),'Cập nhật')]")
    private WebElement updateCartButton;

    @FindBy(xpath = "//button[contains(@class,'remove') or contains(@title,'Xóa')]")
    private List<WebElement> removeButtons;

    @FindBy(xpath = "//div[contains(@class,'cart-total') or contains(@class,'total-price')]")
    private WebElement totalPriceElement;

    @FindBy(xpath = "//div[contains(@class,'cart-item') or contains(@class,'product-item')]")
    private List<WebElement> cartItems;

    @FindBy(xpath = "//button[contains(@class,'checkout') or contains(text(),'Thanh toán')]")
    private WebElement checkoutButton;

    @FindBy(xpath = "//div[contains(@class,'empty-cart') or contains(text(),'Giỏ hàng trống')]")
    private WebElement emptyCartMessage;

    @FindBy(xpath = "//span[contains(@class,'item-price') or contains(@class,'product-price')]")
    private List<WebElement> itemPrices;

    // Cart action methods
    public void addProductToCart() {
        WaitUtils.waitForElementClickable(driver, addToCartButton);
        addToCartButton.click();
    }

    public void navigateToCart() {
        WaitUtils.waitForElementClickable(driver, cartIcon);
        cartIcon.click();
    }

    public boolean isProductAddedToCart() {
        try {
            WaitUtils.waitForElementVisible(driver, By.xpath("//span[contains(@class,'cart-count')]"));
            String countText = cartCounter.getText();
            return !countText.equals("0") && !countText.isEmpty();
        } catch (Exception e) {
            return false;
        }
    }

    // Quantity management methods
    public void updateProductQuantity(int itemIndex, int newQuantity) {
        if (itemIndex < quantityInputs.size()) {
            WebElement quantityInput = quantityInputs.get(itemIndex);
            WaitUtils.waitForElementVisible(driver, By.xpath("//input[contains(@class,'quantity')]"));
            quantityInput.clear();
            quantityInput.sendKeys(String.valueOf(newQuantity));

            try {
                if (updateCartButton.isDisplayed()) {
                    updateCartButton.click();
                }
            } catch (Exception e) {
                // Some carts auto-update without button
            }
        }
    }

    public int getProductQuantity(int itemIndex) {
        if (itemIndex < quantityInputs.size()) {
            return Integer.parseInt(quantityInputs.get(itemIndex).getAttribute("value"));
        }
        return 0;
    }

    // Remove product methods
    public void removeProductFromCart(int itemIndex) {
        if (itemIndex < removeButtons.size()) {
            WaitUtils.waitForElementClickable(driver, removeButtons.get(itemIndex));
            removeButtons.get(itemIndex).click();
        }
    }

    public boolean isProductRemovedFromCart(int originalItemCount) {
        WaitUtils.waitForPageLoad(driver);
        return getCartItemCount() < originalItemCount;
    }

    // Price calculation methods
    public String getTotalPrice() {
        WaitUtils.waitForElementVisible(driver, By.xpath("//div[contains(@class,'cart-total')]"));
        return totalPriceElement.getText().replaceAll("[^0-9]", "");
    }

    public double calculateExpectedTotal() {
        double total = 0.0;
        for (int i = 0; i < cartItems.size(); i++) {
            try {
                String priceText = itemPrices.get(i).getText().replaceAll("[^0-9]", "");
                double price = Double.parseDouble(priceText);
                int quantity = getProductQuantity(i);
                total += (price * quantity);
            } catch (Exception e) {
                // Skip if unable to calculate
            }
        }
        return total;
    }

    public boolean isTotalPriceCorrect() {
        try {
            double displayedTotal = Double.parseDouble(getTotalPrice());
            double calculatedTotal = calculateExpectedTotal();
            return Math.abs(displayedTotal - calculatedTotal) < 0.01;
        } catch (Exception e) {
            return false;
        }
    }

    // Checkout methods
    public void proceedToCheckout() {
        WaitUtils.waitForElementClickable(driver, checkoutButton);
        checkoutButton.click();
    }

    public boolean isCheckoutPageLoaded() {
        WaitUtils.waitForPageLoad(driver);
        return driver.getCurrentUrl().contains("checkout") ||
               driver.getCurrentUrl().contains("thanh-toan");
    }

    // Utility methods
    public int getCartItemCount() {
        return cartItems.size();
    }

    public boolean isCartEmpty() {
        try {
            return emptyCartMessage.isDisplayed() || getCartItemCount() == 0;
        } catch (Exception e) {
            return getCartItemCount() == 0;
        }
    }

    public String getCartCounter() {
        try {
            return cartCounter.getText();
        } catch (Exception e) {
            return "0";
        }
    }

    public boolean isAddToCartButtonVisible() {
        try {
            return addToCartButton.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public void waitForCartUpdate() {
        try {
            Thread.sleep(2000);
            WaitUtils.waitForPageLoad(driver);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
