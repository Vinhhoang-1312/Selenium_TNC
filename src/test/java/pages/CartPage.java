package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

import java.time.Duration;
import java.util.List;

public class CartPage extends BasePage {
    private static final Logger log = LoggerFactory.getLogger(CartPage.class);

    private final By quantityInputs = By.xpath("//input[contains(@class, 'js-buy-quantity')]");
    private final By firstPlusSign = By.xpath("(//a[@class='js-quantity-change'])[1]");
    private final By firstMinusSign = By.xpath("(//a[@class='js-quantity-change'])[2]");
    private final By makePaymentButton = By.xpath("//a[@class='button-send-cart']");
    private final By confirmPurchaseButton = By.xpath("//button[span[text()='Xác nhận mua hàng']]");
    private final By missingPhoneErrorLocator = By.xpath("(//div[@class='note-error'])[2]");

    public CartPage(WebDriver driver) {
        super(driver);
    }

    public int getCartSize() {
        waitForPageLoad();
        waitForElementToBeVisible(quantityInputs);
        List<WebElement> inputs = driver.findElements(quantityInputs);
        return inputs.size();
    }

    public int getFirstItemQuantity() {
        waitForPageLoad();
        waitForElementToBeVisible(quantityInputs);
        List<WebElement> inputs = driver.findElements(quantityInputs);

        if (!inputs.isEmpty()) {
            WebElement firstInput = inputs.get(0);
            waitForElementToBeVisible(quantityInputs);
            String value = firstInput.getAttribute("value");
            if (value == null || value.isEmpty()) {
                log.error("Quantity value is empty or null");
                throw new RuntimeException("Quantity value is empty or null - Check if quantity field is properly loaded");
            }
            try {
                return Integer.parseInt(value);
            } catch (NumberFormatException e) {
                log.error("Invalid quantity value: {}", value);
                throw new RuntimeException("Invalid quantity value: " + value);
            }
        }
        log.error("No items found in cart");
        throw new RuntimeException("No items found in cart - Please check if items were added correctly");
    }

    public void clickFirstPlusSign() {
        waitForPageLoad();
        WebElement plusSign = driver.findElement(firstPlusSign);
        waitForElementToBeClickable(plusSign);
        plusSign.click();
    }

    public void clickFirstMinusSign() {
        waitForPageLoad();
        WebElement minusSign = driver.findElement(firstMinusSign);
        waitForElementToBeClickable(minusSign);
        minusSign.click();
    }

    public void checkFirstItemQuantity() {
        int quantity = getFirstItemQuantity();
        Assert.assertEquals(quantity, 1, "Item should be added to cart with quantity 1");
        log.info("Verified first item quantity is 1");
    }

    public void checkItemQuantityIncrease() {
        int initialQuantity = getFirstItemQuantity();
        int newQuantity = initialQuantity + 1;
        clickFirstPlusSign();
        int updatedQuantity = getFirstItemQuantity();
        Assert.assertEquals(updatedQuantity, newQuantity, "Item quantity should be increased by 1");
    }

    public void checkItemQuantityDecrease() {
        int initialQuantity = getFirstItemQuantity();
        int newQuantity = initialQuantity - 1;
        clickFirstMinusSign();
        int updatedQuantity = getFirstItemQuantity();
        Assert.assertEquals(updatedQuantity, newQuantity, "Item quantity should be decreased by 1");
    }

    public void proceedToCheckout() {
        WebElement checkoutBtn = driver.findElement(makePaymentButton);
        waitForElementToBeClickable(checkoutBtn);
        checkoutBtn.click();
        log.info("Clicked on Proceed to Checkout button");
    }

    public void clickOnConfirmPurchase(){
        waitForElementToBeVisible(confirmPurchaseButton);
        WebElement confirmPurchaseBtn = driver.findElement(confirmPurchaseButton);
        waitForElementToBeClickable(confirmPurchaseBtn);

        confirmPurchaseBtn.click();
        log.info("Clicked on confirm purchase");
    }

    public void acceptAlert(){
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        Alert alert = wait.until(ExpectedConditions.alertIsPresent());
        alert.accept();
    }

    public void verifyMissingPhoneNumberError() {
        clickOnConfirmPurchase();
        try {
            acceptAlert();

            waitForElementToBeVisible(missingPhoneErrorLocator);
            WebElement missingPhoneError = driver.findElement(missingPhoneErrorLocator);
            String actualText = missingPhoneError.getText();

            Assert.assertTrue(actualText.contains("Bạn chưa nhập SĐT"),"Error message for missing phone number should be displayed" );
            log.info("Verified missing phone number error message");
        } catch (TimeoutException e) {
            Assert.fail("Alert did not appear after clicking confirm purchase");
        }
    }

    public void checkCartSize() {
        int cartSize = getCartSize();
        Assert.assertEquals(cartSize, 2, "Cart should contain 2 products");
        log.info("Verified multiple products added to cart successfully, cart size: {}", cartSize);
    }
}
