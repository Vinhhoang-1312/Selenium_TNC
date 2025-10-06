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
import pages.SearchPage;

public class SearchRepeatTest {
    private static final Logger log = LoggerFactory.getLogger(SearchRepeatTest.class);
    private WebDriver driver;
    private String baseUrl = "https://www.tncstore.vn/";

    @BeforeClass
    public void setUp() {

        driver = DriverFactory.getDriver();
        driver.get(baseUrl);
    }

    @Test(invocationCount = 5)
    public void testSearchRepeat() {
        By searchInputLocator = SearchPage.searchInput;
        By searchButtonLocator = SearchPage.searchButton;

        WebElement searchInput = driver.findElement(searchInputLocator);
        String currentKeyword = searchInput.getAttribute("value");

        if (currentKeyword == null || currentKeyword.isEmpty()) {
            searchInput.sendKeys("rtx & 20500");
        }
        driver.findElement(searchButtonLocator).click();

        String result = driver.findElement(SearchPage.noproductNoti).getText();
        Assert.assertEquals(result, "Ôi! Rất tiếc không tìm thấy sản phẩm nào...!");

        driver.navigate().refresh();
    }

    @AfterClass
    public void tearDown() {
        DriverFactory.quitDriver();
    }
}
