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
import helpers.PageHelpers;
import pages.ProductDetailPage;

public class AddToCartTest {
    private static final Logger log = LoggerFactory.getLogger(AddToCartTest.class);
    private WebDriver driver;
    private String baseUrl = "https://www.tncstore.vn/man-hinh-gaming-asus-tuf-gaming-vg249q3a.html";

    @BeforeClass
    public void setUp() {
        // Lấy driver từ Driver_Factory, sẽ khởi tạo mới nếu cần
        driver = Driver_Factory.getDriver();
        driver.get(baseUrl);
    }

    @Test
    public void testAddToCart() {
        By addtocartbuttonlocator = ProductDetailPage.addToCartButton;
        By successNotilocator = ProductDetailPage.successNotification;

        driver.findElement(addtocartbuttonlocator).click();
        WebElement successMessage = PageHelpers.waitForElementVisible(driver,successNotilocator, 10);
        String expectedSuccessMessage = "Thêm sản phẩm vào giỏ hàng thành công !";
        String actualSuccessMessage = successMessage.getText();
        Assert.assertTrue(actualSuccessMessage.equals(expectedSuccessMessage), "Failed to verify success message");
    }

    @AfterClass
    public void tearDown() {
        Driver_Factory.quitDriver();
    }
}
