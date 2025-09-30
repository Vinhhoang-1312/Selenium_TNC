package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class ProductDetailPage extends BasePage {
    @FindBy(css = "button.add-to-cart-button")
    private WebElement addToCartButton;

    @FindBy(css = "a[href*='cart']")
    private WebElement cartLink;

    public ProductDetailPage(WebDriver driver) {
        super(driver);
    }

    public void addToCart() {
        waitForPageLoad();
        addToCartButton.click();
    }

    public void goToCart() {
        waitForPageLoad();
        cartLink.click();
    }
}
