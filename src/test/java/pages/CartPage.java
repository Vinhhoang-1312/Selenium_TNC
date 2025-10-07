package pages;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

import java.util.List;

public class CartPage extends BasePage {
    private static final Logger logger = LoggerFactory.getLogger(CartPage.class);

    private final By quantityInputs = By.xpath("//input[contains(@class, 'js-buy-quantity')]");
    private final By firstPlusSign = By.xpath("(//a[@class='js-quantity-change'])[1]");
    private final By firstMinusSign = By.xpath("(//a[@class='js-quantity-change'])[2]");
    private final By makePaymentButton = By.xpath("//a[@class='button-send-cart']");
    private final By confirmPurchaseButton = By.xpath("//button[span[text()='Xác nhận mua hàng']]");
    private final By firstProductName = By.xpath("//div[@class='name-price']/child::a");
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
                logger.error("Quantity value is empty or null");
                throw new RuntimeException("Quantity value is empty or null - Check if quantity field is properly loaded");
            }
            try {
                return Integer.parseInt(value);
            } catch (NumberFormatException e) {
                logger.error("Invalid quantity value: {}", value);
                throw new RuntimeException("Invalid quantity value: " + value);
            }
        }
        logger.error("No items found in cart");
        throw new RuntimeException("No items found in cart - Please check if items were added correctly");
    }

    public void clickFirstPlusButton() {
        waitForPageLoad();
        waitForElementToBeClickable(firstPlusSign).click();
    }

    public void clickFirstMinusButton() {
        waitForPageLoad();
        waitForElementToBeClickable(firstMinusSign).click();
    }

    /**
     * Verifies that first item quantity is 1
     */
    public void verifyFirstItemQuantity() {
        int quantity = getFirstItemQuantity();
        Assert.assertEquals(quantity, 1, "Item should be added to cart with quantity 1");
        logger.info("Verified first item quantity is 1");
    }

    /**
     * Checks that item quantity increases when plus button is clicked
     */
    public void checkItemQuantityIncrease() {
        int initialQuantity = getFirstItemQuantity();
        int expectedQuantity = initialQuantity + 1;
        clickFirstPlusButton();
        customWait(1000);
        int updatedQuantity = getFirstItemQuantity();
        Assert.assertEquals(updatedQuantity, expectedQuantity,
            "Item quantity should be increased by 1");
        logger.info("Item quantity increased from {} to {}", initialQuantity, updatedQuantity);
    }

    /**
     * Checks that item quantity decreases when minus button is clicked
     */
    public void checkItemQuantityDecrease() {
        int initialQuantity = getFirstItemQuantity();
        int expectedQuantity = initialQuantity - 1;
        clickFirstMinusButton();
        customWait(1000);
        int updatedQuantity = getFirstItemQuantity();
        Assert.assertEquals(updatedQuantity, expectedQuantity,
            "Item quantity should be decreased by 1");
        logger.info("Item quantity decreased from {} to {}", initialQuantity, updatedQuantity);
    }

    public void proceedToCheckout() {
        waitForElementToBeClickable(makePaymentButton).click();
        logger.info("Clicked on Proceed to Checkout button");
    }

    public void clickOnConfirmPurchase() {
        waitForElementToBeVisible(confirmPurchaseButton);
        waitForElementToBeClickable(confirmPurchaseButton).click();
        logger.info("Clicked on confirm purchase");
    }

    public void acceptAlert() {
        try {
            Alert alert = wait.until(ExpectedConditions.alertIsPresent());
            alert.accept();
            logger.info("Alert accepted");
        } catch (Exception e) {
            logger.warn("No alert present to accept");
        }
    }

    /**
     * Verifies missing phone number error appears
     */
    public void verifyMissingPhoneNumberError() {
        clickOnConfirmPurchase();
        try {
            acceptAlert();
            waitForElementToBeVisible(missingPhoneErrorLocator);
            WebElement missingPhoneError = driver.findElement(missingPhoneErrorLocator);
            String actualText = missingPhoneError.getText();

            Assert.assertTrue(actualText.contains("Bạn chưa nhập SĐT"),
                "Error message for missing phone number should be displayed");
            logger.info("Verified missing phone number error message: {}", actualText);
        } catch (TimeoutException e) {
            logger.error("Alert did not appear after clicking confirm purchase");
            Assert.fail("Alert did not appear after clicking confirm purchase");
        }
    }

    /**
     * Verifies multiple products are in cart
     */
    public void verifyMultipleProducts() {
        int cartSize = getCartSize();
        Assert.assertEquals(cartSize, 2, "Cart should contain 2 products");
        logger.info("Verified multiple products added to cart successfully, cart size: {}", cartSize);
    }

    /**
     * Verifies expected number of products in cart
     */
    public void verifyCartSize(int expectedSize) {
        int actualSize = getCartSize();
        Assert.assertEquals(actualSize, expectedSize,
            "Cart should contain " + expectedSize + " products");
        logger.info("Verified cart size: {}", actualSize);
    }

    public String getFirstProductName() {
        return waitAndGetText(firstProductName);
    }
}
