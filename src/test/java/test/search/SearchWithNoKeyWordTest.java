package test.search;

import commons.Driver_Factory;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import pages.SearchPage;

import java.util.List;

public class SearchWithNoKeyWordTest {
    private WebDriver driver;
    private String baseUrl = "https://www.tncstore.vn/";

    @BeforeClass
    public void setUp() {

        driver = Driver_Factory.getDriver();
        driver.get(baseUrl);
    }

    @Test
    public void testSearchWithNoKeyword() {
        By searchinputLocator = SearchPage.searchInput;
        By suggestionlistLocator = SearchPage.suggestionList;

        driver.findElement(searchinputLocator).sendKeys("");
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        List<WebElement> suggestedItems = driver.findElements(suggestionlistLocator);
        for (WebElement item : suggestedItems) {
            String itemName = item.getText().toLowerCase();
            System.out.println("Checking item: " + itemName);
            Assert.assertTrue(itemName.contains("no information".toLowerCase()), "Suggested item contains information");
        }
    }

    @AfterClass
    public void tearDown() {
        Driver_Factory.quitDriver();
    }
}
