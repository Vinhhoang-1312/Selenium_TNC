package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import java.util.List;

public class CartPage extends BasePage {
    @FindBy(css = "input.qty-input")
    private List<WebElement> quantityInputs;

    @FindBy(css = "button.update-cart-button")
    private WebElement updateCartButton;

    public CartPage(WebDriver driver) {
        super(driver);
    }

    public int getFirstItemQuantity() {
        waitForPageLoad();
        if (!quantityInputs.isEmpty()) {
            return Integer.parseInt(quantityInputs.get(0).getAttribute("value"));
        }
        throw new RuntimeException("No items in cart");
    }

    public void setFirstItemQuantity(int quantity) {
        waitForPageLoad();
        if (!quantityInputs.isEmpty()) {
            WebElement qtyInput = quantityInputs.get(0);
            qtyInput.clear();
            qtyInput.sendKeys(String.valueOf(quantity));
            updateCartButton.click();
        } else {
            throw new RuntimeException("No items in cart");
        }
    }
}
