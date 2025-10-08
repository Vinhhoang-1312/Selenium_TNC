package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class SearchPage extends BasePage {
    private static final Logger logger = LoggerFactory.getLogger(SearchPage.class);

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
        try {
            return findElements(productResults).size();
        } catch (Exception e) {
            return 0;
        }
    }

    public boolean hasResults() {
        return getProductCount() > 0;
    }

    public String getNoResultsMessage() {
        try {
            return waitAndGetText(noResultsMessage);
        } catch (Exception e) {
            return "";
        }
    }

    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    /**
     * Gets all suggestion items from search dropdown
     */
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
     * Checks if suggestion list is displayed
     */
    public boolean isSuggestionListDisplayed() {
        try {
            return isDisplayed(suggestionList);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Verifies all suggestion items contain the search keyword
     */
    public boolean allSuggestionsContainKeyword(String keyword) {
        List<WebElement> suggestions = getSuggestionItems();
        if (suggestions.isEmpty()) {
            return false;
        }

        for (WebElement item : suggestions) {
            String itemText = item.getText().toLowerCase();
            if (!itemText.contains(keyword.toLowerCase())) {
                logger.warn("Suggestion '{}' does not contain keyword '{}'", itemText, keyword);
                return false;
            }
        }
        return true;
    }

    /**
     * Gets the count of suggestion items
     */
    public int getSuggestionCount() {
        return getSuggestionItems().size();
    }

    /**
     * Checks if "no product" notification is displayed
     */
    public boolean isNoProductNotificationDisplayed() {
        try {
            return isDisplayed(noproductNoti);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Gets search input element locator (for direct access in tests)
     */
    public By getSearchInputLocator() {
        return searchInput;
    }

    /**
     * Gets suggestion list locator (for direct access in tests)
     */
    public By getSuggestionListLocator() {
        return suggestionList;
    }

    /**
     * Gets all suggestion items as WebElements with visible wait
     * This method encapsulates the direct WebDriver access pattern
     */
    public List<WebElement> getSuggestionItemsWithWait() {
        try {
            waitForElementToBeVisible(suggestionList);
            customWait(1000);
            return driver.findElements(By.xpath("//div[@class='content-suggestions']//a"));
        } catch (Exception e) {
            logger.warn("No suggestion items found after wait", e);
            return List.of();
        }
    }
}
