package pages;
import org.openqa.selenium.NoAlertPresentException;
import  org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;

public class CartPage extends BasePage {
    private static final Logger log = LoggerFactory.getLogger(CartPage.class);

    private final By quantityInputs = By.xpath("//input[contains(@class, 'js-buy-quantity')]");
    private final By firstPlusSign = By.xpath("(//a[@class='js-quantity-change'])[1]");
    private final By firstMinusSign = By.xpath("(//a[@class='js-quantity-change'])[2]");
    private final By checkoutButton = By.xpath("//a[@class='button-send-cart']");
    private final By phoneErrorLocator = By.xpath("//div[contains(@class, 'error') and contains(text(), 'Số điện thoại không được để trống')]");

    public CartPage(WebDriver driver) {
        super(driver);
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

    public void setFirstItemQuantity(int quantity) {
        waitForPageLoad();
        waitForElementToBeVisible(quantityInputs);
        List<WebElement> inputs = driver.findElements(quantityInputs);

        if (!inputs.isEmpty()) {
            WebElement qtyInput = inputs.get(0);
            waitForElementToBeClickable(qtyInput);
            qtyInput.click();
            qtyInput.sendKeys(Keys.chord(Keys.CONTROL, "a"));
            qtyInput.sendKeys(Keys.DELETE);
            qtyInput.sendKeys(String.valueOf(quantity));
            qtyInput.sendKeys(Keys.ENTER);
            log.info("Successfully updated quantity to: {}", quantity);
        } else {
            log.error("No items found in cart");
            throw new RuntimeException("No items found in cart - Please check if items were added correctly");
        }
    }

    public void checkItemQuantityIncrease() {
        int initialQuantity = getFirstItemQuantity();
        int newQuantity = initialQuantity + 1;
        clickFirstPlusSign();
        int updatedQuantity = getFirstItemQuantity();
        org.testng.Assert.assertEquals(updatedQuantity, newQuantity, "Item quantity should be increased by 1");
    }

    public void checkItemQuantityDecrease() {
        int initialQuantity = getFirstItemQuantity();
        int newQuantity = initialQuantity - 1;
        clickFirstMinusSign();
        int updatedQuantity = getFirstItemQuantity();
        org.testng.Assert.assertEquals(updatedQuantity, newQuantity, "Item quantity should be decreased by 1");
    }

    /**
     * Returns the number of products currently in the cart.
     * This is determined by counting the quantity input fields for each product row.
     */
    public int getCartSize() {
        waitForPageLoad();
        waitForElementToBeVisible(quantityInputs);
        List<WebElement> inputs = driver.findElements(quantityInputs);
        return inputs.size();
    }
    public void proceedToCheckout() {
        WebElement checkoutBtn = driver.findElement(checkoutButton);
        waitForElementToBeClickable(checkoutBtn);
        checkoutBtn.click();
        log.info("Clicked on Proceed to Checkout button");
    }

    public void verifyMissingPhoneNumberError() {
        // Chờ alert xuất hiện
        Alert alert = waitForAlert();
        String actualText = alert.getText();
        org.testng.Assert.assertTrue(actualText.contains("Vui lòng kiểm tra lại thông tin đơn hàng"), "Error message for missing phone number should be displayed in alert");
        log.info("Verified missing phone number error message in alert: {}", actualText);
        alert.accept();
    }

    private Alert waitForAlert() {
        int timeoutSeconds = 10;
        for (int i = 0; i < timeoutSeconds * 2; i++) {
            try {
                return driver.switchTo().alert();
            } catch (NoAlertPresentException e) {
                try { Thread.sleep(500); } catch (InterruptedException ignored) {}
            }
        }
        throw new RuntimeException("Alert not present after waiting");
    }

    public void verifyCheckOut() {
        waitForElementToBeVisible(checkoutButton);
        WebElement checkoutBtn = driver.findElement(checkoutButton);
        String actualText = checkoutBtn.getText();
        org.testng.Assert.assertTrue(actualText.contains("Xác nhận mua hàng"), "The text 'Xác nhận mua hàng' must appear on the purchase confirmation page");
        log.info("Verified checkout confirmation text: {}", actualText);
    }

}
