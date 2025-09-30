package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import java.util.List;

public class CartPage extends BasePage {
    private final By quantityInputs = By.xpath("//input[contains(@class, 'js-buy-quantity')]");
    private final By updateCartButton = By.xpath("//button[contains(@class, 'update-cart-button')]");

    public CartPage(WebDriver driver) {
        super(driver);
    }

    public int getFirstItemQuantity() {
        waitForPageLoad();
        waitForElementToBeVisible(quantityInputs);
        List<WebElement> inputs = driver.findElements(quantityInputs);

        if (!inputs.isEmpty()) {
            WebElement firstInput = inputs.getFirst();
            String value = firstInput.getAttribute("value");
            if (value == null || value.isEmpty()) {
                System.out.println("❌ Quantity value is empty or null");
                throw new RuntimeException("Quantity value is empty or null - Check if quantity field is properly loaded");
            }
            try {
                return Integer.parseInt(value);
            } catch (NumberFormatException e) {
                System.out.println("❌ Invalid quantity value: " + value);
                throw new RuntimeException("Invalid quantity value: " + value);
            }
        }
        System.out.println("❌ No items found in cart");
        throw new RuntimeException("No items found in cart - Please check if items were added correctly");
    }

    public void setFirstItemQuantity(int quantity) {
        waitForPageLoad();
        waitForElementToBeVisible(quantityInputs);
        List<WebElement> inputs = driver.findElements(quantityInputs);

        if (!inputs.isEmpty()) {
            WebElement qtyInput = inputs.getFirst();
            waitForElementToBeClickable(qtyInput);
            qtyInput.clear();
            qtyInput.sendKeys(String.valueOf(quantity));

            WebElement updateButton = driver.findElement(updateCartButton);
            waitForElementToBeClickable(updateButton);
            updateButton.click();
            System.out.println("✅ Successfully updated quantity to: " + quantity);
        } else {
            System.out.println("❌ No items found in cart");
            throw new RuntimeException("No items found in cart - Please check if items were added correctly");
        }
    }
}
