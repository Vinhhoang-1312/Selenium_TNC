package test.search;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import commons.Driver_Factory;
import pages.SearchPage;

public class SearchWithSpecCharTest {
    private WebDriver driver;
    private String baseUrl = "https://www.tncstore.vn/";

    @BeforeClass
    public void setUp() {

        driver = Driver_Factory.getDriver();
        driver.get(baseUrl);
    }

    @Test
    public void testSearchWithSpecChar() {
        By searchinputLocator = SearchPage.searchInput;
        By searchbuttonLocator = SearchPage.searchButton;
        By noproductLocator = SearchPage.noproductNoti;

        driver.findElement(searchinputLocator).sendKeys("rtx & 2050");
        driver.findElement(searchbuttonLocator).click();
        String result = driver.findElement(noproductLocator).getText();
        Assert.assertEquals(result, "Ôi! Rất tiếc không tìm thấy sản phẩm nào...!");
    }

    @AfterClass
    public void tearDown() {
        Driver_Factory.quitDriver();
    }
}
