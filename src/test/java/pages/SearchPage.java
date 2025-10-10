package pages;

import io.qameta.allure.Allure;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import utils.JSUtils;
import utils.Reporter;

import java.util.List;

public class SearchPage extends BasePage {
    private JSUtils jsUtils;

    private final By productResults = By.xpath("//div[@id='js-product-list']//a[@class='product-name line-clamp-2']");
    private final By searchInput = By.xpath("//input[@id='js-global-seach']");
    private final By searchButton = By.xpath("//button[@class='submit-search']");
    private final By suggestionList = By.xpath("//div[@class='content-suggestions']");
    private final By suggestionItems = By.xpath("//div[@class='content-suggestions']//a");
    private final By noproductNoti = By.xpath("//h2[contains(text(),'Ôi')]");
    private final By loadMoreButton = By.cssSelector("a.more-all.show-more-product");

    public SearchPage(WebDriver driver) {
        super(driver);
        this.jsUtils = new JSUtils(driver);
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

    public String getNoProductNoti() {
        String notitext = waitAndGetText(noproductNoti);
        return notitext;
    }

    public boolean hasResults() {
        return Allure.step("Check if search has results", () -> {
            return getProductCount() > 0;
        });
    }

    public List<WebElement> getSuggestionItems() {
        try {
            List<WebElement> allitems = waitForAllElementsPresence(suggestionList);
            return allitems;

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

    public void verifyAllResultsContainKeyword(String keyword) {
        Allure.step("Verify all search results contain keyword: " + keyword, () -> {
            List<WebElement> products = findElements(productResults);

            if (products.isEmpty()) {
                Assert.fail("No search results found to verify.");
            }

            for (WebElement product : products) {
                String name = product.getText().trim();
                logger.info("Checking product: {}", name);
                Reporter.LogToReport("Checking product: " + name);

                Assert.assertTrue(
                        name.toLowerCase().contains(keyword.toLowerCase()),
                        "Product name '" + name + "' should contain keyword '" + keyword + "'"
                );
            }

            Reporter.LogToReport("✅ All search results contain the keyword: " + keyword);
        });
    }

    public List<String> getAllProductNames() {
        waitForElementToBeVisible(productResults);
        List<WebElement> productElements = findElements(productResults);
        return productElements.stream()
                .map(e -> e.getText().trim())
                .filter(text -> !text.isEmpty())
                .toList();
    }

    /**
     * Kiểm tra xem có nút "Xem thêm" không hiển thị.
     */
    public boolean isLoadMoreButtonVisible() {
        try {
            return isDisplayed(loadMoreButton);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Click nút "Xem thêm" và chờ thêm sản phẩm mới hiển thị.
     */
    public void clickLoadMoreAndWait() {
        if (!isLoadMoreButtonVisible()) {
            logger.warn("Không tìm thấy nút 'Xem thêm'. Có thể đã hiển thị hết sản phẩm.");
            return;
        }

        int beforeCount = getAllProductNames().size();
        WebElement loadMore = waitForElementToBeVisible(loadMoreButton);
        jsUtils.scrollToElement(loadMore);
        loadMore.click();

        // Đợi đến khi số sản phẩm tăng lên
        wait.until(driver -> getAllProductNames().size() > beforeCount);
        logger.info("Đã bấm 'Xem thêm' ({} → {})", beforeCount, getAllProductNames().size());
    }

    /**
     * Verify rằng nút 'Xem thêm' hoạt động đúng — số lượng sản phẩm tăng sau khi click.
     */
    public boolean verifyLoadMoreWorks() {
        if (!isLoadMoreButtonVisible()) {
            logger.warn("Không có nút 'Xem thêm' trên trang (có thể ít sản phẩm).");
            return false;
        }

        int initialCount = getAllProductNames().size();
        clickLoadMoreAndWait();
        int newCount = getAllProductNames().size();

        return newCount > initialCount;
    }


}
