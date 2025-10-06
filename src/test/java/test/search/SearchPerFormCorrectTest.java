package test.search;

import commons.DriverFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import pages.BasePage;
import pages.SearchPage;

import java.util.List;

public class SearchPerFormCorrectTest {
    private static final Logger log = LoggerFactory.getLogger(SearchPerFormCorrectTest.class);
    private WebDriver driver;
    private BasePage base;
    private String baseUrl = "https://www.tncstore.vn/";

    @BeforeClass
    public void setUp() {
        driver = DriverFactory.getDriver();
        base = new BasePage(driver);
        driver.get(baseUrl);
    }

    @Test
    public void testSearchPerformCorrect() {
        By searchinputLocator = SearchPage.searchInput;
        By suggestionlistLocator = SearchPage.suggestionList;

        driver.findElement(searchinputLocator).sendKeys("rtx 2050");
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        List<WebElement> suggestedItems = base.waitForAllElementsPresence(suggestionlistLocator);
        for (WebElement item : suggestedItems) {
            String itemName = item.getText().toLowerCase();
            System.out.println("Đang kiểm tra gợi ý: " + itemName);
            Assert.assertTrue(itemName.contains("rtx 2050".toLowerCase()), "Suggested item contains the search query");
        }
    }

    @AfterClass
    public void tearDown() {
        DriverFactory.quitDriver();
    }
}
