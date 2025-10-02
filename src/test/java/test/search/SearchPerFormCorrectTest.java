package test.search;

import commons.Driver_Factory;
import helpers.PageHelpers;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import pages.SearchPage;

import java.util.List;

public class SearchPerFormCorrectTest {
    private static final Logger log = LoggerFactory.getLogger(SearchPerFormCorrectTest.class);
    private WebDriver driver;
    private String baseUrl = "https://www.tncstore.vn/";

    @BeforeClass
    public void setUp() {

        driver = Driver_Factory.getDriver();
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

        List<WebElement> suggestedItems = PageHelpers.waitForAllElementsPresence(driver, suggestionlistLocator, 4);
        for (WebElement item : suggestedItems) {
            String itemName = item.getText().toLowerCase();
            System.out.println("Đang kiểm tra gợi ý: " + itemName);
            Assert.assertTrue(itemName.contains("rtx 2050".toLowerCase()), "Suggested item contains the search query");
        }
    }

    @AfterClass
    public void tearDown() {
        Driver_Factory.quitDriver();
    }
}
