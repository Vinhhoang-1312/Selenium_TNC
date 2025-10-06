package test.search;

import commons.DriverFactory;
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

public class SearchWithNoKeywordTest {
    private static final Logger log = LoggerFactory.getLogger(SearchWithNoKeywordTest.class);
    private WebDriver driver;
    private String baseUrl = "https://www.tncstore.vn/";

    @BeforeClass
    public void setUp() {

        driver = DriverFactory.getDriver();
        driver.get(baseUrl);
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
        DriverFactory.quitDriver();
    }
}
