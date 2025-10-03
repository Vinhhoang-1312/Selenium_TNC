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

public class MinusQuantityTest {
    private static final Logger log = LoggerFactory.getLogger(MinusQuantityTest.class);
    private WebDriver driver;
    BasePage base;
    private String baseUrl = "https://www.tncstore.vn/man-hinh-gaming-asus-tuf-gaming-vg249q3a.html";

    @BeforeClass
    public void setUp() {
        // Lấy driver từ Driver_Factory, sẽ khởi tạo mới nếu cần
        driver = Driver_Factory.getDriver();
        base = new BasePage(driver);
        driver.get(baseUrl);
    }

    @Test
    public void testMinusQuantity_UsingAlert() {
        By quantityLocator = ProductDetailPage.quantity;
        By decreaseQuantityButtonLocator = ProductDetailPage.decreaseQuantityButton;

        // Mở trang chi tiết sản phẩm
        driver.get("https://www.tncstore.vn/man-hinh-gaming-asus-tuf-gaming-vg249q3a.html");

        // Chờ nút giảm số lượng hiển thị
        WebElement minusButton = base.waitForElementToBeVisible(decreaseQuantityButtonLocator);

        // Đảm bảo số lượng đang là 1
        WebElement quantityInput = driver.findElement(quantityLocator);
        quantityInput.clear();
        quantityInput.sendKeys("1");

        // Click nút giảm
        minusButton.click();

        // Chuyển sang alert và kiểm tra nội dung
        String expectedAlertText = "Quý khách cần chọn số lượng sản phẩm lớn hơn 0";
        String actualAlertText = driver.switchTo().alert().getText().trim();

        Assert.assertEquals(actualAlertText, expectedAlertText, "Nội dung alert không đúng!");

        // Đóng alert (bấm OK)
        driver.switchTo().alert().accept();
    }

    @AfterClass
    public void tearDown() {
        Driver_Factory.quitDriver();
    }
}
