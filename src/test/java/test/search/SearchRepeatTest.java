package test.search;

import commons.Driver_Factory;
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

        driver = Driver_Factory.getDriver();
        driver.get(baseUrl);
    }

    @Test(invocationCount = 5)
    public void testSearchRepeat() {
        driver.findElement(SearchPage.searchInput).sendKeys("rtx 2050");
        driver.findElement(SearchPage.searchButton).click();
        String result = driver.findElement(SearchPage.noproductNoti).getText();
        Assert.assertEquals(result, "Ôi! Rất tiếc không tìm thấy sản phẩm nào...!");
        driver.navigate().refresh();
    }

    @AfterClass
    public void tearDown() {
        Driver_Factory.quitDriver();
    }
}
