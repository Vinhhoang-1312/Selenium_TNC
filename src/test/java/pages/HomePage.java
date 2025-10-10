package pages;

import io.qameta.allure.Allure;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import java.util.List;

public class HomePage extends BasePage {
    private final Actions actions;

    private final By searchBox = By.xpath("//input[@id='js-global-seach']");
    private final By searchButton = By.xpath("//button[@class='submit-search']");
    private final By productTitleLinks = By.xpath("//div[@id='js-product-list']//a[@class='product-name line-clamp-2']");
    private final By firstProductLink = By.xpath("(//div[@id='js-product-list']//a[@class='product-name line-clamp-2'])[1]");
    private final By loadingSpinner = By.xpath("//div[contains(@class, 'success-form')]");

    public HomePage(WebDriver driver) {
        super(driver);
        this.actions = new Actions(driver);
    }

    public void searchProduct(String productName) {
        Allure.step("Search for product: " + productName, () -> {
            waitForElementToBeVisible(searchBox);
            clearAndType(searchBox, productName);

            if (!driver.findElements(loadingSpinner).isEmpty()) {
                waitForElementToDisappear(loadingSpinner);
            }
            click(searchButton);
            waitForPageStability();
            logger.info("Successfully searched for product: {}", productName);
        });
    }

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

    public void navigateBack() {
        driver.navigate().back();
        waitForPageStability();
        logger.info("Navigated back to previous page");
    }
}
