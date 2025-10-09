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
        // Initialize page objects
        HomePage homePage = new HomePage(getDriver());
        SearchPage searchPage = new SearchPage(getDriver());

        homePage.searchProduct("rtx 2050");

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
        // Initialize page objects
        HomePage homePage = new HomePage(getDriver());
        SearchPage searchPage = new SearchPage(getDriver());

        homePage.searchProduct("@#$%");
        String text = searchPage.getNoProductNoti();
        Assert.assertEquals(text,"Ôi! Rất tiếc không tìm thấy sản phẩm nào...!");
        logger.info("Search with special characters completed");
    }

    @Test(groups = {"search"},
            description = "SEARCH-03: Search with empty keyword")
    @Story("Product Search - Edge Cases")
    @Severity(SeverityLevel.MINOR)
    @Description("Verify system behavior when searching with empty keyword")
    public void testSearchWithEmptyKeyword() {
        // Initialize page objects
        HomePage homePage = new HomePage(getDriver());
        SearchPage searchPage = new SearchPage(getDriver());

        homePage.searchProduct("");
        String text = searchPage.getNoProductNoti();
        Assert.assertEquals(text,"Ôi! Rất tiếc không tìm thấy sản phẩm nào...!");
        logger.info("Search with empty keyword completed");
    }
    @Test(groups = {"search"},
            description = "SEARCH-04: Search suggestions contain correct keyword")
    @Story("Search Autocomplete")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify that search suggestions contain the entered keyword")
    public void testSearchSuggestionsContainKeyword() {
        // Initialize page objects
        HomePage homePage = new HomePage(getDriver());
        SearchPage searchPage = new SearchPage(getDriver());

        homePage.typeSearch("rtx 2050");
        searchPage.waitForSearchSuggestions();

        List<WebElement> suggestedItems = searchPage.getSuggestionItemsWithWait();
        Assert.assertFalse(suggestedItems.isEmpty(), "Suggestion list should not be empty");

        for (WebElement item : suggestedItems) {
            String itemName = item.getText().toLowerCase();
            logger.info("Checking suggestion: {}", itemName);
            Assert.assertTrue(itemName.contains("rtx 2050".toLowerCase()),
                "Suggested item should contain the search query");
        }
        Allure.parameter("Suggestions Count", suggestedItems.size());
        logger.info("All each suggestions contain the search keyword");
    }

    @Test(groups = {"search"},
            description = "SEARCH-05: Search and reload page")
    @Story("Search Persistence")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify that search results persist after page reload")
    public void testSearchReloadPage() {
        // Initialize page objects
        HomePage homePage = new HomePage(getDriver());
        SearchPage searchPage = new SearchPage(getDriver());

        homePage.searchProduct("laptop");

        int initialCount = searchPage.getProductCount();
        Allure.parameter("Initial Results Count", initialCount);

        getDriver().navigate().refresh();

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
        // Initialize page objects
        HomePage homePage = new HomePage(getDriver());
        SearchPage searchPage = new SearchPage(getDriver());

        homePage.searchProduct("laptop");
        int firstCount = searchPage.getProductCount();
        Allure.parameter("First Search Count", firstCount);

        getDriver().navigate().to(baseUrl);
        homePage.searchProduct("laptop");
        int secondCount = searchPage.getProductCount();
        Allure.parameter("Second Search Count", secondCount);

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
        // Initialize page objects
        HomePage homePage = new HomePage(getDriver());
        SearchPage searchPage = new SearchPage(getDriver());

        homePage.typeSearch("laptop");

        Assert.assertTrue(searchPage.isSuggestionListDisplayed(),
            "Suggestion list should be displayed when typing");
        logger.info("Suggestion list displayed successfully");
    }
}
