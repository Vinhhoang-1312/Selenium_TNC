package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

public class ProductDetailPage extends BasePage {
    // Locators with meaningful names using xpath
    private final By addToCartButton = By.xpath("//a[contains(text(),'Thêm vào giỏ hàng')]");
    private final By cartIcon = By.xpath("//a[@id='js-header-cart']");
    private final By cartLink = By.xpath("//a[contains(@href, 'cart') or contains(@href, 'gio-hang')]");
    private final By loadingSpinner = By.xpath("//div[contains(@class, 'loading-spinner')]");

    private final Actions actions;

    public ProductDetailPage(WebDriver driver) {
        super(driver);
        this.actions = new Actions(driver);
    }

    public void addToCart() {
        waitForPageLoad();
        waitForElementToDisappear(loadingSpinner);
        waitForElementToBeVisible(addToCartButton);
        WebElement addToCartElement = driver.findElement(addToCartButton);
        waitForElementToBeClickable(addToCartElement);
        addToCartElement.click();
        System.out.println("✅ Successfully added product to cart");
    }

    public void goToCart() {
        waitForPageLoad();

        // First hover over the cart icon
        WebElement cartIconElement = driver.findElement(cartIcon);
        waitForElementToBeVisible(cartIcon);
        actions.moveToElement(cartIconElement).perform();
        System.out.println("✅ Successfully hovered over cart icon");

        // Then wait for and click the view cart link
        waitForElementToBeVisible(cartLink);
        WebElement cartElement = driver.findElement(cartLink);
        waitForElementToBeClickable(cartElement);
        cartElement.click();
        System.out.println("✅ Successfully navigated to cart page");
    }
}
