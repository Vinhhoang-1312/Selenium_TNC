package test.productdetail;


import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import commons.Driver_Factory;
import pages.product_detail_page;

public class product_detail_test {

    private WebDriver driver;
    private String baseUrl = "https://www.tncstore.vn/man-hinh-gaming-asus-tuf-gaming-vg249q3a.html";

    @BeforeClass
    public void setUp() {
        // Lấy driver từ Driver_Factory, sẽ khởi tạo mới nếu cần
        driver = Driver_Factory.getDriver();
        driver.get(baseUrl); // Mở trang web cần test
    }

    @Test//kiểm thử chức năng thêm vào giỏ hàng
    public void testAddToCart() {
        // Assume you have a WebDriver instance initialized before running this test
        // and the product detail page is opened

        // Click the "Thêm vào giỏ" button
        driver.findElement(product_detail_page.ADDTO_CARTBUTTON).click();

        // Wait for the success message to appear
        WebDriverWait wait = new WebDriverWait(driver, java.time.Duration.ofSeconds(10));
        WebElement successMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='content-container']")));

        // Verify the success message appears after clicking the button
        String expectedSuccessMessage = "Thêm sản phẩm vào giỏ hàng thành công !";
        String actualSuccessMessage = successMessage.getText();
        Assert.assertTrue(actualSuccessMessage.equals(expectedSuccessMessage), "Failed to verify success message");
    }

    @Test // kiểm thử: từ trang chủ cuộn xuống cuối, click sản phẩm và so sánh tên
    public void testScrollToBottomAndViewProductDetail() {
        WebDriverWait wait = new WebDriverWait(driver,java.time.Duration.ofSeconds(10));
        JavascriptExecutor js = (JavascriptExecutor) driver;

        // 1. Bấm lại trang chủ
        driver.get("https://www.tncstore.vn/");
        String homeUrl = driver.getCurrentUrl();

        // 2. Cuộn chậm xuống cuối trang
        long lastHeight = (long) js.executeScript("return document.body.scrollHeight");
        for (int i = 0; i < 10; i++) { // cuộn 10 lần, mỗi lần 600px
            js.executeScript("window.scrollBy(0, 600);");
            // Explicit wait for page to load new items (wait for a known element to be present)
            wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("body")));
        }
        // đảm bảo cuộn hẳn xuống cuối
        js.executeScript("window.scrollTo(0, document.body.scrollHeight);");

        // 3. Tìm phần tử sản phẩm và lấy text của nó
        WebElement itemnameElement = wait.until(
            ExpectedConditions.visibilityOfElementLocated(product_detail_page.ITEMNAME_IN_MAINPAGE)
        );
        String productNameOnMainPage = itemnameElement.getText();
        System.out.println("Tên sản phẩm trên trang chủ: " + productNameOnMainPage);

        //4. Click vào sản phẩm để xem chi tiết
        WebElement itemElement = driver.findElement(product_detail_page.ITEM_IN_MAINPAGE);
        itemElement.click(); // click vào sản phẩm

        // Tạm dừng để chờ trang chi tiết tải (nên dùng explicit wait thay vì sleep trong thực tế)
        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }

        // 5. Chờ trang chi tiết sản phẩm tải xong và lấy tên sản phẩm trên trang chi tiết
        WebElement productNameElement = wait.until(
            ExpectedConditions.visibilityOfElementLocated(product_detail_page.PRODUCT_NAME)
        );
        String productNameOnDetailPage = productNameElement.getText();

        //6. So sánh tên sản phẩm trên trang chủ và trang chi tiết
        Assert.assertEquals(productNameOnDetailPage, productNameOnMainPage, "Product names do not match!");

    }

    @AfterClass
    public void tearDown() {
        // Đóng driver sau khi test xong để tránh rò rỉ session
        Driver_Factory.quitDriver();
    }
}
