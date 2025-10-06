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
import pages.ProductDetailPage;
import pages.BasePage;
public class SalePriceCorrectTest {
    private static final Logger log = LoggerFactory.getLogger(SalePriceCorrectTest.class);
    private WebDriver driver;
    private BasePage base;
    private String baseUrl = "https://www.tncstore.vn/man-hinh-gaming-asus-tuf-gaming-vg249q3a.html";

    @BeforeClass
    public void setUp() {
        driver = Driver_Factory.getDriver();
        base = new BasePage(driver);
        driver.get(baseUrl);
    }

    @Test
    public void testSalePriceCorrect() {
        By originpriceLocator = ProductDetailPage.originPrice;
        By priceLocator = ProductDetailPage.Price;
        By saleoffLocator = ProductDetailPage.SaleOff;

        WebElement originalPriceElement = driver.findElement(originpriceLocator);
        String originalPriceText = originalPriceElement.getText().replaceAll("[^0-9]", "");
        double originalPrice = Double.parseDouble(originalPriceText);
        System.out.println(originalPrice);

        WebElement salePriceElement = driver.findElement(priceLocator);
        String salePriceText = salePriceElement.getText().replaceAll("[^0-9]", "");
        double salePrice = Double.parseDouble(salePriceText);
        System.out.println(salePrice);

        WebElement discountElement = driver.findElement(saleoffLocator);
        String discountText = discountElement.getText().replaceAll("[^0-9]", "");
        double discountPercent = Double.parseDouble(discountText);
        System.out.println(discountPercent);

        double expectedPrice = originalPrice - (originalPrice * discountPercent / 100);
        Assert.assertEquals(salePrice, expectedPrice, "Giá sau khi giảm không đúng! Expected: " + expectedPrice + " | Actual: " + salePrice);
        System.out.println("Giá sau khi giảm đúng: " + salePrice);
    }

    @AfterClass
    public void tearDown() {
        Driver_Factory.quitDriver();
    }
}
