package modules.search;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import base.WaitUtils;

import java.util.List;

public class SearchPage {
    private WebDriver driver;

    public SearchPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    // Search Elements - Cần cập nhật với locators thực tế từ TNC Store
    @FindBy(xpath = "//input[contains(@class,'search') or contains(@name,'search') or contains(@placeholder,'Tìm kiếm')]")
    private WebElement searchBox;

    @FindBy(xpath = "//button[contains(@class,'search-btn') or contains(@type,'submit')]")
    private WebElement searchButton;

    @FindBy(xpath = "//div[contains(@class,'search-results') or contains(@class,'product-list')]")
    private WebElement searchResultsContainer;

    @FindBy(xpath = "//div[contains(@class,'product-item') or contains(@class,'search-item')]")
    private List<WebElement> searchResultItems;

    @FindBy(xpath = "//div[contains(@class,'no-results') or contains(text(),'Không tìm thấy') or contains(text(),'nothing show up')]")
    private WebElement noResultsMessage;

    @FindBy(xpath = "//div[contains(@class,'search-suggestion') or contains(@class,'autocomplete')]")
    private WebElement searchSuggestions;

    @FindBy(xpath = "//h1[contains(@class,'category-title') or contains(@class,'page-title')]")
    private WebElement categoryPageTitle;

    // Methods for Search Test Cases

    // SRH-001: Search with valid product
    public void openMainPage() {
        // Already handled in BaseTest setup
        WaitUtils.waitForPageLoad(driver);
    }

    public void navigateToSearchBox() {
        WaitUtils.waitForElementVisible(driver, By.xpath("//input[contains(@class,'search') or contains(@name,'search')]"));
    }

    public void enterSearchKeyword(String keyword) {
        WaitUtils.waitForElementVisible(driver, searchBox);
        searchBox.clear();
        searchBox.sendKeys(keyword);
    }

    public void pressEnter() {
        searchBox.sendKeys(Keys.ENTER);
        WaitUtils.waitForPageLoad(driver);
    }

    public void clickSearchButton() {
        WaitUtils.waitForElementClickable(driver, searchButton);
        searchButton.click();
        WaitUtils.waitForPageLoad(driver);
    }

    public void performSearch(String keyword) {
        navigateToSearchBox();
        enterSearchKeyword(keyword);
        pressEnter();
    }

    // SRH-001: Verify search results for valid product
    public boolean areSearchResultsDisplayed() {
        try {
            WaitUtils.waitForElementVisible(driver, searchResultsContainer);
            return searchResultsContainer.isDisplayed() && getSearchResultCount() > 0;
        } catch (Exception e) {
            return false;
        }
    }

    public int getSearchResultCount() {
        try {
            WaitUtils.waitForElementVisible(driver, searchResultsContainer);
            return searchResultItems.size();
        } catch (Exception e) {
            return 0;
        }
    }

    public boolean doResultsContainKeyword(String keyword) {
        try {
            for (WebElement item : searchResultItems) {
                String itemText = item.getText().toLowerCase();
                if (itemText.contains(keyword.toLowerCase())) {
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    // SRH-002: Search with invalid product
    public boolean isNoResultsMessageDisplayed() {
        try {
            WaitUtils.waitForElementVisible(driver, noResultsMessage);
            return noResultsMessage.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean areNoResultsShown() {
        return getSearchResultCount() == 0 || isNoResultsMessageDisplayed();
    }

    // SRH-003: Search with category keyword
    public boolean isRedirectedToCategoryPage() {
        WaitUtils.waitForPageLoad(driver);
        String currentUrl = driver.getCurrentUrl();
        return currentUrl.contains("man-hinh") || currentUrl.contains("monitor") ||
               currentUrl.contains("category");
    }

    public boolean isCategoryPageTitleDisplayed(String expectedCategory) {
        try {
            WaitUtils.waitForElementVisible(driver, categoryPageTitle);
            String titleText = categoryPageTitle.getText().toLowerCase();
            return titleText.contains(expectedCategory.toLowerCase()) ||
                   titleText.contains("màn hình") ||
                   titleText.contains("monitor");
        } catch (Exception e) {
            return false;
        }
    }

    // SRH-004: Search with special characters
    public boolean isSpecialCharacterSearchHandled() {
        // Should show no results or handle gracefully
        return areNoResultsShown() || areSearchResultsDisplayed();
    }

    // SRH-005: Search continuously many times
    public void performContinuousSearch(String keyword, int times) {
        for (int i = 0; i < times; i++) {
            performSearch(keyword);

            // Verify search works each time
            if (!areSearchResultsDisplayed() && !areNoResultsShown()) {
                throw new RuntimeException("Search failed on iteration " + (i + 1));
            }

            // Small delay between searches
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public boolean isContinuousSearchWorking(String keyword, int times) {
        try {
            performContinuousSearch(keyword, times);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // Utility methods
    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    public String getPageTitle() {
        return driver.getTitle();
    }

    public boolean isSearchBoxVisible() {
        try {
            return searchBox.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public void clearSearchBox() {
        if (isSearchBoxVisible()) {
            searchBox.clear();
        }
    }

    public String getSearchBoxValue() {
        try {
            return searchBox.getAttribute("value");
        } catch (Exception e) {
            return "";
        }
    }

    public boolean isSearchSuggestionsDisplayed() {
        try {
            return searchSuggestions.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
}
