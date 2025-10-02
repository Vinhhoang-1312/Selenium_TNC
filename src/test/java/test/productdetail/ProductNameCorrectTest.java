package test.productdetail;


import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import commons.Driver_Factory;
import helpers.PageHelpers;
import pages.ProductDetailPage;

public class ProductNameCorrectTest {

    private WebDriver driver;
    private String baseUrl = "https://www.tncstore.vn/";

    @BeforeClass
    public void setUp() {
        driver = Driver_Factory.getDriver();
        driver.get(baseUrl);
    }


    @Test
    public void testProductNameCorrect() {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        By itemnameinmainpageLocator = ProductDetailPage.itemnameInMainPage;
        By iteminmainpageLocator = ProductDetailPage.itemInMainPage;
        By productnameLocator = ProductDetailPage.productName;

        driver.get(baseUrl);
        for (int i = 0; i < 10; i++) {
            js.executeScript("window.scrollBy(0, 600);");
            PageHelpers.waitForPresence(driver, By.cssSelector("body"), 10);
        }

        js.executeScript("window.scrollTo(0, document.body.scrollHeight);");
        WebElement itemnameElement = PageHelpers.waitForElementVisible(driver,itemnameinmainpageLocator, 10);
        String productNameOnMainPage = itemnameElement.getText();
        System.out.println("Tên sản phẩm trên trang chủ: " + productNameOnMainPage);
        WebElement itemElement = driver.findElement(iteminmainpageLocator);
        itemElement.click();

        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }

        WebElement productNameElement = PageHelpers.waitForElementVisible(driver,productnameLocator, 10);
        String productNameOnDetailPage = productNameElement.getText();
        Assert.assertEquals(productNameOnDetailPage, productNameOnMainPage, "Product names do not match!");

    }


    @AfterClass
    public void tearDown() {
        Driver_Factory.quitDriver();
    }
}
