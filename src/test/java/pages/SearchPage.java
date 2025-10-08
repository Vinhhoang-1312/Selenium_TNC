package pages;

import io.qameta.allure.Allure;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public class SearchPage extends BasePage {

    private final By productResults = By.xpath("//div[@id='js-product-list']//a[@class='product-name line-clamp-2']");
    private final By noResultsMessage = By.xpath("//div[contains(@class,'no-results')]");
    private final By searchInput = By.xpath("//input[@id='js-global-seach']");
    private final By searchButton = By.xpath("//button[@class='submit-search']");
    private final By suggestionList = By.xpath("//div[@class='content-suggestions']");
    private final By suggestionItems = By.xpath("//div[@class='content-suggestions']//a");
    private final By noproductNoti = By.xpath("//h2[contains(text(),'Ôi')]");

    public SearchPage(WebDriver driver) {
        super(driver);
    }

    public int getProductCount() {
        return Allure.step("Get search results count", () -> {
            try {
                return findElements(productResults).size();
            } catch (Exception e) {
                return 0;
            }
        });
    }

    public boolean hasResults() {
        return Allure.step("Check if search has results", () -> {
            return getProductCount() > 0;
        });
    }

    public List<WebElement> getSuggestionItems() {
        try {
            waitForElementToBeVisible(suggestionList);
            return findElements(suggestionItems);
        } catch (Exception e) {
            logger.warn("No suggestion items found", e);
            return List.of();
        }
    }

    /**
     * Waits for search suggestions to load
     */
    public void waitForSearchSuggestions() {
        try {
            waitForElementToBeVisible(suggestionList);
            logger.info("Search suggestions loaded");
        } catch (Exception e) {
            logger.warn("Search suggestions did not load", e);
        }
    }

    /**
     * Gets suggestion items with explicit wait
     */
    public List<WebElement> getSuggestionItemsWithWait() {
        return Allure.step("Get search suggestion items", () -> {
            waitForSearchSuggestions();
            return getSuggestionItems();
        });
    }

    /**
     * Checks if suggestion list is displayed
     */
    public boolean isSuggestionListDisplayed() {
        try {
            return isDisplayed(suggestionList);
        } catch (Exception e) {
            return false;
        }
    }


}
