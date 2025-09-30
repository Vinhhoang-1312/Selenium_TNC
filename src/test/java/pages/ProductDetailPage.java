package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

public class ProductDetailPage extends BasePage {
    private final By addToCartButton = By.xpath("//a[contains(text(),'Thêm vào giỏ hàng')]");
    private final By cartIcon = By.xpath("//a[@id='js-header-cart']");
    private final By viewCartLink = By.xpath("//a[@class='btn-goCart']");
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
        waitForElementToBeVisible(viewCartLink);
        WebElement cartElement = driver.findElement(viewCartLink);
        waitForElementToBeClickable(cartElement);
        cartElement.click();
        System.out.println("✅ Successfully navigated to cart page");
    }
}
