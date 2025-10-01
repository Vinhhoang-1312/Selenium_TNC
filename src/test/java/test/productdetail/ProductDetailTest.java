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

public class ProductDetailTest {

    private WebDriver driver;
    private String baseUrl = "https://www.tncstore.vn/man-hinh-gaming-asus-tuf-gaming-vg249q3a.html";

    @BeforeClass
    public void setUp() {
        driver = Driver_Factory.getDriver();
        driver.get(baseUrl);
    }

    @Test
    public void testAddToCart() {
        driver.findElement(ProductDetailPage.addtoCartButton).click();
        WebElement successMessage = PageHelpers.waitForElementVisible(driver,ProductDetailPage.successNotification, 10);

        String expectedSuccessMessage = "Thêm sản phẩm vào giỏ hàng thành công !";
        String actualSuccessMessage = successMessage.getText();
        Assert.assertTrue(actualSuccessMessage.equals(expectedSuccessMessage), "Failed to verify success message");
    }

    @Test
    public void testScrollToBottomAndViewProductDetail() {
        JavascriptExecutor js = (JavascriptExecutor) driver;

        driver.get("https://www.tncstore.vn/");
        String homeUrl = driver.getCurrentUrl();

        long lastHeight = (long) js.executeScript("return document.body.scrollHeight");
        for (int i = 0; i < 10; i++) {
            js.executeScript("window.scrollBy(0, 600);");
            PageHelpers.waitForPresence(driver, By.cssSelector("body"), 10);
        }
        js.executeScript("window.scrollTo(0, document.body.scrollHeight);");
        WebElement itemnameElement = helpers.PageHelpers.waitForElementVisible(driver, ProductDetailPage.itemnameInMainPage, 10);
        String productNameOnMainPage = itemnameElement.getText();
        System.out.println("Tên sản phẩm trên trang chủ: " + productNameOnMainPage);
        WebElement itemElement = driver.findElement(ProductDetailPage.itemInMainPage);
        itemElement.click();

        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }
        WebElement productNameElement = helpers.PageHelpers.waitForElementVisible(driver, ProductDetailPage.productName, 10);
        String productNameOnDetailPage = productNameElement.getText();

        Assert.assertEquals(productNameOnDetailPage, productNameOnMainPage, "Product names do not match!");

    }

    @Test
    public void testSalePriceCorrect() {
        driver.get("https://www.tncstore.vn/man-hinh-gaming-asus-tuf-gaming-vg249q3a.html");

        WebElement originalPriceElement = driver.findElement(ProductDetailPage.originPrice);
        String originalPriceText = originalPriceElement.getText().replaceAll("[^0-9]", "");
        double originalPrice = Double.parseDouble(originalPriceText);
        System.out.println(originalPrice);

        WebElement salePriceElement = driver.findElement(ProductDetailPage.Price);
        String salePriceText = salePriceElement.getText().replaceAll("[^0-9]", "");
        double salePrice = Double.parseDouble(salePriceText);
        System.out.println(salePrice);

        WebElement discountElement = driver.findElement(ProductDetailPage.SaleOff);
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
