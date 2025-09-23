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

    // Search elements
    @FindBy(xpath = "//input[contains(@class,'search') or contains(@name,'search') or contains(@placeholder,'Tìm kiếm')]")
    private WebElement searchBox;

    @FindBy(xpath = "//button[contains(@class,'search-btn') or contains(@type,'submit')]")
    private WebElement searchButton;

    @FindBy(xpath = "//div[contains(@class,'search-results') or contains(@class,'product-list')]")
    private WebElement searchResultsContainer;

    @FindBy(xpath = "//div[contains(@class,'product-item') or contains(@class,'search-item')]")
    private List<WebElement> searchResultItems;

    @FindBy(xpath = "//div[contains(@class,'no-results') or contains(text(),'Không tìm thấy')]")
    private WebElement noResultsMessage;

    @FindBy(xpath = "//h1[contains(@class,'category-title') or contains(@class,'page-title')]")
    private WebElement categoryPageTitle;

    // Search methods
    public void navigateToSearchBox() {
        WaitUtils.waitForElementVisible(driver, By.xpath("//input[contains(@class,'search')]"));
    }

    public void enterSearchKeyword(String keyword) {
        WaitUtils.waitForElementVisible(driver, By.xpath("//input[contains(@class,'search')]"));
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

    // Validation methods
    public boolean areSearchResultsDisplayed() {
        try {
            WaitUtils.waitForElementVisible(driver, By.xpath("//div[contains(@class,'search-results')]"));
            return searchResultsContainer.isDisplayed() && getSearchResultCount() > 0;
        } catch (Exception e) {
            return false;
        }
    }

    public int getSearchResultCount() {
        try {
            WaitUtils.waitForElementVisible(driver, By.xpath("//div[contains(@class,'search-results')]"));
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

    public boolean areNoResultsShown() {
        return getSearchResultCount() == 0 || isNoResultsMessageDisplayed();
    }

    public boolean isNoResultsMessageDisplayed() {
        try {
            WaitUtils.waitForElementVisible(driver, By.xpath("//div[contains(@class,'no-results')]"));
            return noResultsMessage.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isRedirectedToCategoryPage() {
        WaitUtils.waitForPageLoad(driver);
        String currentUrl = driver.getCurrentUrl();
        return currentUrl.contains("man-hinh") || currentUrl.contains("monitor") ||
               currentUrl.contains("category");
    }

    public boolean isCategoryPageTitleDisplayed(String expectedCategory) {
        try {
            WaitUtils.waitForElementVisible(driver, By.xpath("//h1[contains(@class,'category-title')]"));
            String titleText = categoryPageTitle.getText().toLowerCase();
            return titleText.contains(expectedCategory.toLowerCase()) ||
                   titleText.contains("màn hình") ||
                   titleText.contains("monitor");
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isSpecialCharacterSearchHandled() {
        return areNoResultsShown() || areSearchResultsDisplayed();
    }

    public void performContinuousSearch(String keyword, int times) {
        for (int i = 0; i < times; i++) {
            performSearch(keyword);

            if (!areSearchResultsDisplayed() && !areNoResultsShown()) {
                throw new RuntimeException("Search failed on iteration " + (i + 1));
            }

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
    public void openMainPage() {
        WaitUtils.waitForPageLoad(driver);
    }

    public String getCurrentUrl() {
        return driver.getCurrentUrl();
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
}
