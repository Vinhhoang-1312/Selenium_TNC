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

public class SearchWithReloadPageTest {
    private WebDriver driver;
    private String baseUrl = "https://www.tncstore.vn/";

    @BeforeClass
    public void setUp() {

        driver = Driver_Factory.getDriver();
        driver.get(baseUrl);
    }

    @Test
    public void testSearchReloadPage() {
        By searchinputLocator = SearchPage.searchInput;

        driver.findElement(searchinputLocator).sendKeys("rtx 2050");
        driver.navigate().refresh();
        WebElement searchInput = driver.findElement(searchinputLocator);
        Assert.assertTrue(searchInput.getAttribute("value").contains("rtx 2050"), "Keyword not found after reloading page");
    }

    @AfterClass
    public void tearDown() {
        Driver_Factory.quitDriver();
    }
}
