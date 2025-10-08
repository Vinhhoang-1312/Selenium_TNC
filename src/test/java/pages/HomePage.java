package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class HomePage extends BasePage {
    private static final Logger logger = LoggerFactory.getLogger(HomePage.class);

    private final By searchBox = By.xpath("//input[@id='js-global-seach']");
    private final By searchButton = By.xpath("//button[@class='submit-search']");
    private final By productTitleLinks = By.xpath("//div[@id='js-product-list']//a[@class='product-name line-clamp-2']");
    private final By loadingSpinner = By.xpath("//div[contains(@class, 'success-form')]");

    public HomePage(WebDriver driver) {
        super(driver);
    }

    public void searchProduct(String productName) {
        waitForElementToBeVisible(searchBox);
        clearAndType(searchBox, productName);

        if (!driver.findElements(loadingSpinner).isEmpty()) {
            waitForElementToDisappear(loadingSpinner);
        }
        waitForElementToBeClickable(searchButton).click();
        logger.info("Successfully searched for product: {}", productName);
    }

    /**
     * Types the search text into the search box without submitting (useful for suggestions)
     */
    public void typeSearch(String productName) {
        waitForElementToBeVisible(searchBox);
        clearAndType(searchBox, productName);
    }

    public void clickFirstProduct() {
        waitForElementToBeVisible(productTitleLinks);
        List<WebElement> availableProducts = driver.findElements(productTitleLinks);

        if (!availableProducts.isEmpty()) {
            WebElement firstProduct = availableProducts.get(0);
            waitForElementToBeClickable(By.xpath("(//div[@id='js-product-list']//a[@class='product-name line-clamp-2'])[1]")).click();
            logger.info("Successfully clicked first product");
        } else {
            logger.error("No products found on the page");
            throw new RuntimeException("No products found on homepage");
        }
    }
}
