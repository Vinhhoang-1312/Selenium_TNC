package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import java.util.List;

public class CartPage extends BasePage {
    private final By quantityInputs = By.xpath("//input[contains(@class, 'js-buy-quantity')]");
    private final By firstPlusSign = By.xpath("(//a[@class='js-quantity-change'])[1]");
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

    public void clickFirstPlusSign() {
        waitForPageLoad();
        WebElement plusSign = driver.findElement(firstPlusSign);
        waitForElementToBeClickable(plusSign);
        plusSign.click();
    }

    public void setFirstItemQuantity(int quantity) {
        waitForPageLoad();
        waitForElementToBeVisible(quantityInputs);
        List<WebElement> inputs = driver.findElements(quantityInputs);

        if (!inputs.isEmpty()) {
            WebElement qtyInput = inputs.get(0);
            waitForElementToBeClickable(qtyInput);
            qtyInput.click();
            qtyInput.sendKeys(Keys.chord(Keys.CONTROL, "a")); // Select all text
            qtyInput.sendKeys(Keys.DELETE); // Delete selected text
            qtyInput.sendKeys(String.valueOf(quantity));
            qtyInput.sendKeys(Keys.ENTER);
            System.out.println("✅ Successfully updated quantity to: " + quantity);
        } else {
            System.out.println("❌ No items found in cart");
            throw new RuntimeException("No items found in cart - Please check if items were added correctly");
        }
    }
}
