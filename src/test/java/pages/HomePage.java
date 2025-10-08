package pages;

import io.qameta.allure.Allure;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public class HomePage extends BasePage {

    private final By searchBox = By.xpath("//input[@id='js-global-seach']");
    private final By searchButton = By.xpath("//button[@class='submit-search']");
    private final By productTitleLinks = By.xpath("//div[@id='js-product-list']//a[@class='product-name line-clamp-2']");private final By firstProductLink = By.xpath("(//div[@id='js-product-list']//a[@class='product-name line-clamp-2'])[1]");
    private final By loadingSpinner = By.xpath("//div[contains(@class, 'success-form')]");

    public HomePage(WebDriver driver) {
        super(driver);
    }

    public void searchProduct(String productName) {
        Allure.step("Search for product: " + productName, () -> {
            waitForElementToBeVisible(searchBox);
            clearAndType(searchBox, productName);

            if (!driver.findElements(loadingSpinner).isEmpty()) {
                waitForElementToDisappear(loadingSpinner);
            }
            click(searchButton);

            // Wait for search results to load
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            logger.info("Successfully searched for product: {}", productName);
        });
    }

    /**
     * Types the search text into the search box without submitting (useful for suggestions)
     */
    public void typeSearch(String productName) {
        waitForElementToBeVisible(searchBox);
        clearAndType(searchBox, productName);
    }

    public void clickFirstProduct() {
        Allure.step("Click first product from search results", () -> {
            waitForElementToBeVisible(productTitleLinks);
            List<WebElement> availableProducts = driver.findElements(productTitleLinks);

            if (!availableProducts.isEmpty()) {
                click(firstProductLink);
                logger.info("Successfully clicked first product");
            } else {
                logger.error("No products found on the page");
                throw new RuntimeException("No products found on homepage");
            }
        });
    }

    /**
     * Navigate back to previous page with wait for page to stabilize
     */
    public void navigateBack() {
        driver.navigate().back();

        // Wait for page to load after navigation
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        logger.info("Navigated back to previous page");
    }
}
