package tests;

import core.BaseTest;
import listeners.BaseListener;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.HomePage;
import pages.SearchPage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Listeners({BaseListener.class})
public class SearchTests extends BaseTest {
    private static final Logger log = LoggerFactory.getLogger(SearchTests.class);

    @Test(groups = {"search", "smoke"},
            description = "SEARCH-01: Search with valid keyword returns results")
    public void testSearchWithValidKeyword() {
        HomePage homePage = new HomePage(getDriver());
        homePage.searchProduct("laptop");

        SearchPage searchPage = new SearchPage(getDriver());
        Assert.assertTrue(searchPage.hasResults(), "Search should return results for valid keyword");
        log.info("Search returned {} results", searchPage.getProductCount());
    }

    @Test(groups = {"search"},
            description = "SEARCH-02: Search with special characters")
    public void testSearchWithSpecialCharacters() {
        HomePage homePage = new HomePage(getDriver());
        homePage.searchProduct("@#$%");

        customWait(1000);
        log.info("Search with special characters completed");
    }

    @Test(groups = {"search"},
            description = "SEARCH-03: Search with empty keyword")
    public void testSearchWithEmptyKeyword() {
        HomePage homePage = new HomePage(getDriver());
        homePage.searchProduct("");

        customWait(1000);
        log.info("Search with empty keyword completed");
    }

    @Test(groups = {"search"},
            description = "SEARCH-04: Search suggestions contain correct keyword")
    public void testSearchSuggestionsContainKeyword() {
        SearchPage searchPage = new SearchPage(getDriver());
        By searchInputLocator = searchPage.getSearchInputLocator();
        By suggestionListLocator = searchPage.getSuggestionListLocator();

        getDriver().findElement(searchInputLocator).sendKeys("rtx 2050");
        customWait(3000);

        List<WebElement> suggestedItems = getDriver().findElements(By.xpath("//div[@class='content-suggestions']//a"));
        Assert.assertFalse(suggestedItems.isEmpty(), "Suggestion list should not be empty");

        for (WebElement item : suggestedItems) {
            String itemName = item.getText().toLowerCase();
            log.info("Checking suggestion: {}", itemName);
            Assert.assertTrue(itemName.contains("rtx 2050".toLowerCase()),
                "Suggested item should contain the search query");
        }
        log.info("All {} suggestions contain the search keyword", suggestedItems.size());
    }

    @Test(groups = {"search"},
            description = "SEARCH-05: Search and reload page")
    public void testSearchReloadPage() {
        HomePage homePage = new HomePage(getDriver());
        homePage.searchProduct("laptop");

        SearchPage searchPage = new SearchPage(getDriver());
        int initialCount = searchPage.getProductCount();

        getDriver().navigate().refresh();
        customWait(2000);

        int afterReloadCount = searchPage.getProductCount();
        Assert.assertEquals(afterReloadCount, initialCount,
            "Product count should remain the same after reload");
        log.info("Search results consistent after reload: {} products", afterReloadCount);
    }

    @Test(groups = {"search"},
            description = "SEARCH-06: Repeated search returns consistent results")
    public void testRepeatedSearch() {
        HomePage homePage = new HomePage(getDriver());
        SearchPage searchPage = new SearchPage(getDriver());

        // First search
        homePage.searchProduct("laptop");
        customWait(2000);
        int firstCount = searchPage.getProductCount();

        // Navigate back and search again
        getDriver().navigate().to(baseUrl);
        customWait(1000);
        homePage.searchProduct("laptop");
        customWait(2000);
        int secondCount = searchPage.getProductCount();

        Assert.assertEquals(secondCount, firstCount,
            "Repeated search should return consistent results");
        log.info("Repeated search consistent: {} products", secondCount);
    }

    @Test(groups = {"search"},
            description = "SEARCH-07: Verify suggestion list is displayed")
    public void testSuggestionListDisplayed() {
        SearchPage searchPage = new SearchPage(getDriver());
        By searchInputLocator = searchPage.getSearchInputLocator();

        getDriver().findElement(searchInputLocator).sendKeys("laptop");
        customWait(2000);

        Assert.assertTrue(searchPage.isSuggestionListDisplayed(),
            "Suggestion list should be displayed");
        Assert.assertTrue(searchPage.getSuggestionCount() > 0,
            "Should have at least one suggestion");
        log.info("Suggestion list displayed with {} items", searchPage.getSuggestionCount());
    }
}
