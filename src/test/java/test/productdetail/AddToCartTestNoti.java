package test.productdetail;

import commons.DriverFactory;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pages.ProductDetailPage;
import pages.BasePage;

public class AddToCartTestNoti {
    private static final Logger log = LoggerFactory.getLogger(AddToCartTestNoti.class);
    private WebDriver driver;
    BasePage base;
    private String baseUrl = "https://www.tncstore.vn/man-hinh-gaming-asus-tuf-gaming-vg249q3a.html";

    @BeforeClass
    public void setUp() {
        // Lấy driver từ DriverFactory, sẽ khởi tạo mới nếu cần
        driver = DriverFactory.getDriver();
        base = new BasePage(driver);
        driver.get(baseUrl);
    }

    @Test
    public void testAddToCart() {
        ProductDetailPage productDetailPage = new ProductDetailPage(driver);
        productDetailPage.addToCart();
       productDetailPage.verifyAddToCartSuccessMessage();
    }

    @AfterClass
    public void tearDown() {
        DriverFactory.quitDriver();
    }
}
