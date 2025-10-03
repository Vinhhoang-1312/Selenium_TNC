package test.productdetail;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import commons.Driver_Factory;
import helpers.PageHelpers;
import pages.ProductDetailPage;
public class CorrectProductNameTest {
    private static final Logger log = LoggerFactory.getLogger(CorrectProductNameTest.class);
    private WebDriver driver;
    private String baseUrl = "https://www.tncstore.vn/man-hinh-gaming-asus-tuf-gaming-vg249q3a.html";

    @BeforeClass
    public void setUp() {
        // Lấy driver từ Driver_Factory, sẽ khởi tạo mới nếu cần
        driver = Driver_Factory.getDriver();
        driver.get(baseUrl);
    }

    @Test
    public void testProductNameCorrect() {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        By itemnameinmainpageLocator = ProductDetailPage.itemNameInMainPage;
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
