package test.productdetail;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import commons.Driver_Factory;
import pages.HomePage;
import pages.ProductDetailPage;
import pages.BasePage;

public class MinusQuantityTest {
    private static final Logger log = LoggerFactory.getLogger(MinusQuantityTest.class);
    private WebDriver driver;
    BasePage base;
    private String baseUrl = "https://www.tncstore.vn/";

    @BeforeClass
    public void setUp() {
        driver = Driver_Factory.getDriver();
        base = new BasePage(driver);
        driver.get(baseUrl);
    }

    @Test
    public void testMinusQuantity_UsingAlert() {
        By quantityLocator = ProductDetailPage.quantityInput;
        By decreaseQuantityButtonLocator = ProductDetailPage.decreaseQuantityButton;
        By itemInMainPageLocator = ProductDetailPage.SecondLaptopInMainPage;

        HomePage.clickSpecificItem(driver,itemInMainPageLocator);
        WebElement minusButton = base.waitForElementToBeVisible(decreaseQuantityButtonLocator);
        WebElement quantityInput = driver.findElement(quantityLocator);
        quantityInput.clear();
        quantityInput.sendKeys("1");
        minusButton.click();
        String expectedAlertText = "Quý khách cần chọn số lượng sản phẩm lớn hơn 0";
        String actualAlertText = driver.switchTo().alert().getText().trim();

        Assert.assertEquals(actualAlertText, expectedAlertText, "Nội dung alert không đúng!");

        driver.switchTo().alert().accept();
    }

    @AfterClass
    public void tearDown() {
        Driver_Factory.quitDriver();
    }
}
