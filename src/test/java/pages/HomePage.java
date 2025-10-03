package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;

public class HomePage extends BasePage {
    private static final Logger log = LoggerFactory.getLogger(HomePage.class);

    private final By searchBox = By.xpath("//input[@id='js-global-seach']");
    private final By searchButton = By.xpath("//span[contains(text(),'Tìm kiếm')]");
    private final By productTitleLinks = By.xpath("//div[@id='js-product-list']//a[@class='product-name line-clamp-2']");
    private final By loadingSpinner = By.xpath("//div[contains(@class, 'loading-spinner')]");

    public HomePage(WebDriver driver) {
        super(driver);
    }

    public void searchProduct(String productName) {
        // Wait for search box to be visible and interactive
//        waitForPageLoad();
        waitForElementToBeVisible(searchBox);
        WebElement searchInput = driver.findElement(searchBox);
        searchInput.clear();
        searchInput.sendKeys(productName);

        // Click search button if it exists
        List<WebElement> searchButtons = driver.findElements(searchButton);
        if (!searchButtons.isEmpty()) {
            searchButtons.get(0).click();
        }

        // Wait for loading to complete
        waitForElementToDisappear(loadingSpinner);
        log.info("Successfully searched for product: {}", productName);
    }

    public void clickProduct() {
        // Wait for products to be visible after search
        waitForElementToBeVisible(productTitleLinks);
        List<WebElement> availableProducts = driver.findElements(productTitleLinks);

        if (!availableProducts.isEmpty()) {
            WebElement firstProduct = availableProducts.get(0);
            waitForElementToBeClickable(firstProduct);
            firstProduct.click();
            log.info("Successfully clicked product");
        } else {
            log.error("No products found on the page");
            log.error("Current URL: {}", driver.getCurrentUrl());
            throw new RuntimeException("No products found on homepage - Please check if products are loaded correctly");
        }
    }

    public void clickFirstNormalProduct() {
        List<WebElement> products = driver.findElements(By.cssSelector(".product-box"));
        for (WebElement product : products) {
            List<WebElement> saleTag = product.findElements(By.cssSelector(".old-price"));
            if (saleTag.isEmpty()) {
                product.click();
                return;
            }
        }
        throw new RuntimeException("No normal product found");
    }
}
