package tests;

import core.BaseTest;
import io.qameta.allure.*;
import listeners.BaseListener;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.HomePage;
import pages.SearchPage;

import java.util.List;

@Listeners({BaseListener.class})
@Epic("E-Commerce")
@Feature("Product Search")
public class SearchTests extends BaseTest {

    @Test(groups = {"search", "smoke"},
            description = "SEARCH-01: Search with valid keyword returns results")
    @Story("Product Search")
    @Severity(SeverityLevel.BLOCKER)
    @Description("Verify that searching with a valid keyword returns relevant product results")
    public void testSearchWithValidKeyword() {
        Allure.step("Search for 'laptop'");
        HomePage homePage = new HomePage(getDriver());
        SearchPage searchPage = new SearchPage(getDriver());

        homePage.searchProduct("laptop");

        Allure.step("Verify search results are displayed");
        Assert.assertTrue(searchPage.hasResults(), "Search should return results for valid keyword");
        int resultCount = searchPage.getProductCount();
        Allure.parameter("Results Count", resultCount);
        logger.info("Search returned {} results", resultCount);
    }

    @Test(groups = {"search"},
            description = "SEARCH-02: Search with special characters")
    @Story("Product Search - Edge Cases")
    @Severity(SeverityLevel.MINOR)
    @Description("Verify system behavior when searching with special characters")
    public void testSearchWithSpecialCharacters() {
        Allure.step("Search with special characters: @#$%");
        HomePage homePage = new HomePage(getDriver());
        homePage.searchProduct("@#$%");

        logger.info("Search with special characters completed");
    }

    @Test(groups = {"search"},
            description = "SEARCH-03: Search with empty keyword")
    @Story("Product Search - Edge Cases")
    @Severity(SeverityLevel.MINOR)
    @Description("Verify system behavior when searching with empty keyword")
    public void testSearchWithEmptyKeyword() {
        Allure.step("Submit search with empty keyword");
        HomePage homePage = new HomePage(getDriver());
        homePage.searchProduct("");

        logger.info("Search with empty keyword completed");
    }

    @Test(groups = {"search"},
            description = "SEARCH-04: Search suggestions contain correct keyword")
    @Story("Search Autocomplete")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify that search suggestions contain the entered keyword")
    public void testSearchSuggestionsContainKeyword() {
        HomePage homePage = new HomePage(getDriver());
        SearchPage searchPage = new SearchPage(getDriver());

        Allure.step("Enter search keyword: rtx 2050");
        homePage.typeSearch("rtx 2050");
        searchPage.waitForSearchSuggestions();

        Allure.step("Verify all suggestions contain the keyword");
        List<WebElement> suggestedItems = searchPage.getSuggestionItemsWithWait();
        Assert.assertFalse(suggestedItems.isEmpty(), "Suggestion list should not be empty");

        for (WebElement item : suggestedItems) {
            String itemName = item.getText().toLowerCase();
            logger.info("Checking suggestion: {}", itemName);
            Assert.assertTrue(itemName.contains("rtx 2050".toLowerCase()),
                "Suggested item should contain the search query");
        }
        Allure.parameter("Suggestions Count", suggestedItems.size());
        logger.info("All {} suggestions contain the search keyword", suggestedItems.size());
    }

    @Test(groups = {"search"},
            description = "SEARCH-05: Search and reload page")
    @Story("Search Persistence")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify that search results persist after page reload")
    public void testSearchReloadPage() {
        Allure.step("Perform initial search");
        HomePage homePage = new HomePage(getDriver());
        homePage.searchProduct("laptop");

        SearchPage searchPage = new SearchPage(getDriver());
        int initialCount = searchPage.getProductCount();
        Allure.parameter("Initial Results Count", initialCount);

        Allure.step("Reload page");
        getDriver().navigate().refresh();

        Allure.step("Verify results count remains the same");
        int afterReloadCount = searchPage.getProductCount();
        Allure.parameter("After Reload Count", afterReloadCount);
        Assert.assertEquals(afterReloadCount, initialCount,
            "Product count should remain the same after reload");
        logger.info("Search results consistent after reload: {} products", afterReloadCount);
    }

    @Test(groups = {"search"},
            description = "SEARCH-06: Repeated search returns consistent results")
    @Story("Search Consistency")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify that repeated searches return consistent results")
    public void testRepeatedSearch() {
        HomePage homePage = new HomePage(getDriver());
        SearchPage searchPage = new SearchPage(getDriver());

        Allure.step("Perform first search");
        homePage.searchProduct("laptop");
        int firstCount = searchPage.getProductCount();
        Allure.parameter("First Search Count", firstCount);

        Allure.step("Navigate back and search again");
        getDriver().navigate().to(baseUrl);
        homePage.searchProduct("laptop");
        int secondCount = searchPage.getProductCount();
        Allure.parameter("Second Search Count", secondCount);

        Allure.step("Verify results are consistent");
        Assert.assertEquals(secondCount, firstCount,
            "Repeated search should return consistent results");
        logger.info("Repeated search consistent: {} products", secondCount);
    }

    @Test(groups = {"search"},
            description = "SEARCH-07: Verify suggestion list is displayed")
    @Story("Search Autocomplete")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify that search suggestions list is displayed when typing")
    public void testSuggestionListDisplayed() {
        HomePage homePage = new HomePage(getDriver());
        SearchPage searchPage = new SearchPage(getDriver());

        Allure.step("Type search keyword to trigger suggestions");
        homePage.typeSearch("laptop");
        searchPage.waitForSearchSuggestions();

        Allure.step("Verify suggestion list is displayed");
        Assert.assertTrue(searchPage.isSuggestionListDisplayed(),
            "Suggestion list should be displayed");
        Assert.assertTrue(searchPage.getSuggestionCount() > 0,
            "Should have at least one suggestion");
        Allure.parameter("Suggestion Count", searchPage.getSuggestionCount());
        logger.info("Suggestion list displayed with {} items", searchPage.getSuggestionCount());
    }
}
