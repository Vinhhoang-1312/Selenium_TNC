package test.search;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import commons.Driver_Factory;
import pages.SearchPage;

public class SearchTest {
    private static final Logger log = LoggerFactory.getLogger(SearchTest.class);
    private WebDriver driver;
    private String baseUrl = "https://www.tncstore.vn/";

    @BeforeClass
    public void setUp() {

        driver = Driver_Factory.getDriver();
        driver.get(baseUrl);
    }

    @Test
    public void testSearchFunctionality() {
        assert driver.findElement(SearchPage.searchInput).isDisplayed();
    }

    @Test
    public void testSearchResults() {
        driver.findElement(SearchPage.searchInput).sendKeys("Laptop");
        driver.findElement(SearchPage.searchButton).click();
    }

    @Test
    public void testSearchForRTX2050() {
        driver.findElement(SearchPage.searchInput).sendKeys("rtx & 2050");
        driver.findElement(SearchPage.searchButton).click();
        String result = driver.findElement(SearchPage.noproductNoti).getText();
        Assert.assertEquals(result, "Ôi! Rất tiếc không tìm thấy sản phẩm nào...!");
    }

    @Test(invocationCount = 5)
    public void testSearchForRTX2050Repeat() {
        driver.findElement(SearchPage.searchInput).sendKeys("rtx & 2050");
        driver.findElement(SearchPage.searchButton).click();
        String result = driver.findElement(SearchPage.noproductNoti).getText();
        Assert.assertEquals(result, "Ôi! Rất tiếc không tìm thấy sản phẩm nào...!");
        driver.navigate().refresh();
    }

    @Test
    public void testSearchPerformCorrect() {
        driver.findElement(SearchPage.searchInput).sendKeys("rtx 2050");
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        List<WebElement> suggestedItems = helpers.PageHelpers.waitForAllElementsPresence(driver, SearchPage.suggestionList, 4);
        for (WebElement item : suggestedItems) {
            String itemName = item.getText().toLowerCase();
            log.info("Đang kiểm tra gợi ý: {}", itemName);
//            System.out.println("HTML: " + item.getAttribute("innerHTML"));
//            System.out.println("Class: " + item.getAttribute("class"));
//            System.out.println("Link: " + item.getAttribute("href")); // nếu là thẻ <a>

            System.out.println("Đang kiểm tra gợi ý: " + itemName);
            Assert.assertTrue(itemName.contains("rtx 2050".toLowerCase()), "Suggested item contains the search query");
        }
    }
    
    @Test
    public void testSearchReloadPage() {
        driver.findElement(SearchPage.searchInput).sendKeys("rtx 2050");
        driver.navigate().refresh();

        WebElement searchInput = driver.findElement(SearchPage.searchInput);
        Assert.assertTrue(searchInput.getAttribute("value").contains("rtx 2050"), "Keyword not found after reloading page");
    }

    @Test
    public void testSearchWithNoKeyword() {
        driver.findElement(SearchPage.searchInput).sendKeys("");
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            log.error("Thread interrupted", e);
        }
        List<WebElement> suggestedItems = driver.findElements(SearchPage.suggestionList);
        for (WebElement item : suggestedItems) {
            String itemName = item.getText().toLowerCase();
            log.info("Checking item: {}", itemName);
            Assert.assertTrue(itemName.contains("no information".toLowerCase()), "Suggested item contains information");
        }
    }

    @AfterClass
    public void tearDown() {
        Driver_Factory.quitDriver();
    }

}
