package modules.search;

import base.BaseTest;
import com.aventstack.extentreports.Status;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import utils.ExtentManager;
import data.ExcelReader;
import data.SearchTestData;

public class SearchTest extends BaseTest {
    private SearchPage searchPage;

    @BeforeMethod
    public void setUpSearchTest() {
        searchPage = new SearchPage(driver);
    }

    @Test(description = "SRH-001: Search with valid product - RTX 2050")
    public void testSearchWithValidProduct() {
        test = ExtentManager.startTest("SRH-001: Search with valid product");

        try {
            logInfo("Starting test: Search with valid product");

            // Get test data from Excel or fallback
            String keyword = ExcelReader.getSearchData("SRH-001", "keyword");
            if (keyword.isEmpty()) {
                keyword = SearchTestData.VALID_PRODUCT_KEYWORD;
            }

            // Step 1: Open the page (already done in BaseTest setup)
            searchPage.openMainPage();
            logInfo("Main page opened successfully");

            // Step 2: Navigate to search box
            searchPage.navigateToSearchBox();
            Assert.assertTrue(searchPage.isSearchBoxVisible(), "Search box should be visible");
            logInfo("Navigated to search box");

            // Step 3: Enter test data
            searchPage.enterSearchKeyword(keyword);
            logInfo("Entered keyword: " + keyword);

            // Step 4: Press Enter
            searchPage.pressEnter();
            logInfo("Pressed Enter to search");

            // Verify: A list shows up with related items
            Assert.assertTrue(searchPage.areSearchResultsDisplayed(),
                "Search results should be displayed for valid product");

            int resultCount = searchPage.getSearchResultCount();
            Assert.assertTrue(resultCount > 0, "Should have at least 1 search result");
            logInfo("Found " + resultCount + " search results");

            // Verify results contain the keyword
            Assert.assertTrue(searchPage.doResultsContainKeyword("rtx"),
                "Search results should contain RTX related items");

            logPass("Search with valid product completed successfully");
            test.log(Status.PASS, "SRH-001 PASSED: Valid product search returned " + resultCount + " results");

        } catch (Exception e) {
            logFail("Test failed: " + e.getMessage());
            takeScreenshot("SRH-001_SearchValidProduct_Failed");
            test.log(Status.FAIL, "SRH-001 FAILED: " + e.getMessage());
            throw e;
        }
    }

    @Test(description = "SRH-002: Search with invalid product - abcxyz123")
    public void testSearchWithInvalidProduct() {
        test = ExtentManager.startTest("SRH-002: Search with invalid product");

        try {
            logInfo("Starting test: Search with invalid product");

            // Get test data from Excel or fallback
            String keyword = ExcelReader.getSearchData("SRH-002", "keyword");
            if (keyword.isEmpty()) {
                keyword = SearchTestData.INVALID_PRODUCT_KEYWORD;
            }

            // Prerequisite: Page is already open
            Assert.assertTrue(searchPage.isSearchBoxVisible(), "Search box should be visible");

            // Step 1: Navigate to search box
            searchPage.navigateToSearchBox();
            logInfo("Navigated to search box");

            // Step 2: Enter test data
            searchPage.enterSearchKeyword(keyword);
            logInfo("Entered invalid keyword: " + keyword);

            // Step 3: Press Enter
            searchPage.pressEnter();
            logInfo("Pressed Enter to search");

            // Verify: Nothing shows up
            Assert.assertTrue(searchPage.areNoResultsShown(),
                "No results should be shown for invalid product");

            logInfo("No results displayed as expected for invalid keyword");

            logPass("Search with invalid product handled correctly");
            test.log(Status.PASS, "SRH-002 PASSED: Invalid product search showed no results");

        } catch (Exception e) {
            logFail("Test failed: " + e.getMessage());
            takeScreenshot("SRH-002_SearchInvalidProduct_Failed");
            test.log(Status.FAIL, "SRH-002 FAILED: " + e.getMessage());
            throw e;
        }
    }

    @Test(description = "SRH-003: Search with category keyword - màn hình máy tính")
    public void testSearchWithCategoryKeyword() {
        test = ExtentManager.startTest("SRH-003: Search with category keyword");

        try {
            logInfo("Starting test: Search with category keyword");

            // Get test data from Excel or fallback
            String keyword = ExcelReader.getSearchData("SRH-003", "keyword");
            if (keyword.isEmpty()) {
                keyword = SearchTestData.CATEGORY_KEYWORD;
            }

            // Prerequisite: Page is already open
            Assert.assertTrue(searchPage.isSearchBoxVisible(), "Search box should be visible");

            // Step 1: Navigate to search box
            searchPage.navigateToSearchBox();
            logInfo("Navigated to search box");

            // Step 2: Enter test data
            searchPage.enterSearchKeyword(keyword);
            logInfo("Entered category keyword: " + keyword);

            // Step 3: Press Enter
            searchPage.pressEnter();
            logInfo("Pressed Enter to search");

            // Verify: Main page redirects to monitor page
            Assert.assertTrue(searchPage.isRedirectedToCategoryPage(),
                "Should be redirected to monitor/category page");

            String currentUrl = searchPage.getCurrentUrl();
            logInfo("Current URL after search: " + currentUrl);

            // Additional verification for category page
            if (searchPage.isCategoryPageTitleDisplayed("màn hình")) {
                logInfo("Category page title contains monitor/màn hình as expected");
            }

            logPass("Search with category keyword redirected correctly");
            test.log(Status.PASS, "SRH-003 PASSED: Category keyword search redirected to monitor page");

        } catch (Exception e) {
            logFail("Test failed: " + e.getMessage());
            takeScreenshot("SRH-003_SearchCategoryKeyword_Failed");
            test.log(Status.FAIL, "SRH-003 FAILED: " + e.getMessage());
            throw e;
        }
    }

    @Test(description = "SRH-004: Search with special characters - rtx & 2050")
    public void testSearchWithSpecialCharacters() {
        test = ExtentManager.startTest("SRH-004: Search with special characters");

        try {
            logInfo("Starting test: Search with special characters");

            // Get test data from Excel or fallback
            String keyword = ExcelReader.getSearchData("SRH-004", "keyword");
            if (keyword.isEmpty()) {
                keyword = SearchTestData.SPECIAL_CHAR_KEYWORD;
            }

            // Prerequisite: Page is already open
            Assert.assertTrue(searchPage.isSearchBoxVisible(), "Search box should be visible");

            // Step 1: Navigate to search box
            searchPage.navigateToSearchBox();
            logInfo("Navigated to search box");

            // Step 2: Enter test data
            searchPage.enterSearchKeyword(keyword);
            logInfo("Entered special character keyword: " + keyword);

            // Step 3: Press Enter
            searchPage.pressEnter();
            logInfo("Pressed Enter to search");

            // Verify: Nothing shows up (special characters should be handled gracefully)
            Assert.assertTrue(searchPage.isSpecialCharacterSearchHandled(),
                "Special character search should be handled properly");

            logInfo("Special character search handled as expected");

            logPass("Search with special characters handled correctly");
            test.log(Status.PASS, "SRH-004 PASSED: Special character search handled gracefully");

        } catch (Exception e) {
            logFail("Test failed: " + e.getMessage());
            takeScreenshot("SRH-004_SearchSpecialChars_Failed");
            test.log(Status.FAIL, "SRH-004 FAILED: " + e.getMessage());
            throw e;
        }
    }

    @Test(description = "SRH-005: Search continuously many times - RTX 2050")
    public void testSearchContinuouslyManyTimes() {
        test = ExtentManager.startTest("SRH-005: Search continuously many times");

        try {
            // Get test data from Excel or fallback
            String keyword = ExcelReader.getSearchData("SRH-005", "keyword");
            String timesStr = ExcelReader.getSearchData("SRH-005", "times");

            if (keyword.isEmpty()) keyword = SearchTestData.CONTINUOUS_SEARCH_KEYWORD;
            int times = timesStr.isEmpty() ? SearchTestData.CONTINUOUS_SEARCH_TIMES : Integer.parseInt(timesStr);

            logInfo("Starting test: Search continuously " + times + " times");

            // Prerequisite: Page is already open
            Assert.assertTrue(searchPage.isSearchBoxVisible(), "Search box should be visible");

            // Perform continuous search
            logInfo("Performing continuous search with keyword: " + keyword);

            Assert.assertTrue(searchPage.isContinuousSearchWorking(keyword, times),
                "Continuous search should work " + times + " times without issues");

            logInfo("Completed " + times + " continuous searches successfully");

            // Final verification that search still works
            searchPage.performSearch(keyword);
            Assert.assertTrue(searchPage.areSearchResultsDisplayed() || searchPage.areNoResultsShown(),
                "Search should still work after continuous testing");

            logPass("Continuous search completed successfully");
            test.log(Status.PASS, "SRH-005 PASSED: Continuous search worked " + times + " times without issues");

        } catch (Exception e) {
            logFail("Test failed: " + e.getMessage());
            takeScreenshot("SRH-005_ContinuousSearch_Failed");
            test.log(Status.FAIL, "SRH-005 FAILED: " + e.getMessage());
            throw e;
        }
    }

    // Helper test for search functionality
    @Test(description = "Helper: Test search box functionality")
    public void testSearchBoxFunctionality() {
        test = ExtentManager.startTest("Helper: Test search box functionality");

        try {
            logInfo("Testing basic search box functionality");

            // Test search box visibility
            Assert.assertTrue(searchPage.isSearchBoxVisible(), "Search box should be visible");

            // Test entering and clearing text
            searchPage.enterSearchKeyword("test");
            Assert.assertEquals(searchPage.getSearchBoxValue(), "test", "Search box should contain entered text");

            searchPage.clearSearchBox();
            Assert.assertEquals(searchPage.getSearchBoxValue(), "", "Search box should be empty after clearing");

            logPass("Search box functionality verified");

        } catch (Exception e) {
            logFail("Helper test failed: " + e.getMessage());
            takeScreenshot("SearchBox_Helper_Failed");
            throw e;
        }
    }
}
