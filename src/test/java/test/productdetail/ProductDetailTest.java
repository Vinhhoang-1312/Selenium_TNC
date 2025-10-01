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
        // Lấy driver từ Driver_Factory, sẽ khởi tạo mới nếu cần
        driver = Driver_Factory.getDriver();
        driver.get(baseUrl); // Mở trang web cần test
    }

    @Test//kiểm thử chức năng thêm vào giỏ hàng
    public void testAddToCart() {
        // Assume you have a WebDriver instance initialized before running this test
        // and the product detail page is opened

        // Click the "Thêm vào giỏ" button
        driver.findElement(ProductDetailPage.addtoCartButton).click();

        // Wait for the success message to appear
        WebElement successMessage = PageHelpers.waitForElementVisible(driver, By.xpath("//div[@class='content-container']"), 10);

        // Verify the success message appears after clicking the button
        String expectedSuccessMessage = "Thêm sản phẩm vào giỏ hàng thành công !";
        String actualSuccessMessage = successMessage.getText();
        Assert.assertTrue(actualSuccessMessage.equals(expectedSuccessMessage), "Failed to verify success message");
    }

    @Test // kiểm thử: từ trang chủ cuộn xuống cuối, click sản phẩm và so sánh tên
    public void testScrollToBottomAndViewProductDetail() {
        JavascriptExecutor js = (JavascriptExecutor) driver;

        // 1. Bấm lại trang chủ
        driver.get("https://www.tncstore.vn/");
        String homeUrl = driver.getCurrentUrl();

        // 2. Cuộn chậm xuống cuối trang
        long lastHeight = (long) js.executeScript("return document.body.scrollHeight");
        for (int i = 0; i < 10; i++) { // cuộn 10 lần, mỗi lần 600px
            js.executeScript("window.scrollBy(0, 600);");
            // Explicit wait for page to load new items (wait for a known element to be present)
            PageHelpers.waitForPresence(driver, By.cssSelector("body"), 10);
        }
        // đảm bảo cuộn hẳn xuống cuối
        js.executeScript("window.scrollTo(0, document.body.scrollHeight);");

        // 3. Tìm phần tử sản phẩm và lấy text của nó
        WebElement itemnameElement = helpers.PageHelpers.waitForElementVisible(driver, ProductDetailPage.itemnameInMainPage, 10);
        // WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        // wait.until(
        //     ExpectedConditions.visibilityOfElementLocated(ProductDetailPage.itemnameInMainPage)
        // );
        String productNameOnMainPage = itemnameElement.getText();
        System.out.println("Tên sản phẩm trên trang chủ: " + productNameOnMainPage);

        //4. Click vào sản phẩm để xem chi tiết
        WebElement itemElement = driver.findElement(ProductDetailPage.itemInMainPage);
        itemElement.click(); // click vào sản phẩm

        // Tạm dừng để chờ trang chi tiết tải (nên dùng explicit wait thay vì sleep trong thực tế)
        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }

        // 5. Chờ trang chi tiết sản phẩm tải xong và lấy tên sản phẩm trên trang chi tiết
        WebElement productNameElement = helpers.PageHelpers.waitForElementVisible(driver, ProductDetailPage.productName, 10);
        // WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        // wait.until(
        //     ExpectedConditions.visibilityOfElementLocated(ProductDetailPage.productName)
        // );
        String productNameOnDetailPage = productNameElement.getText();

        //6. So sánh tên sản phẩm trên trang chủ và trang chi tiết
        Assert.assertEquals(productNameOnDetailPage, productNameOnMainPage, "Product names do not match!");

    }

    @Test//kiểm thử giá sale đã đúng chưa
    public void testSalePriceCorrect() {
        // 1. Truy cập trang sản phẩm
        driver.get("https://www.tncstore.vn/man-hinh-gaming-asus-tuf-gaming-vg249q3a.html");

        // 2. Lấy giá gốc (giả sử nằm trong thẻ có class "price-old")
        WebElement originalPriceElement = driver.findElement(ProductDetailPage.originPrice);
        String originalPriceText = originalPriceElement.getText().replaceAll("[^0-9]", "");
        double originalPrice = Double.parseDouble(originalPriceText);
        System.out.println(originalPrice);
        
        // 3. Lấy giá sau khi giảm (giả sử nằm trong thẻ có class "price-new")
        WebElement salePriceElement = driver.findElement(ProductDetailPage.Price);
        String salePriceText = salePriceElement.getText().replaceAll("[^0-9]", "");
        double salePrice = Double.parseDouble(salePriceText);
        System.out.println(salePrice);

        // 4. Lấy % giảm giá (giả sử nằm trong thẻ có class "discount-percent")
        WebElement discountElement = driver.findElement(ProductDetailPage.SaleOff);
        String discountText = discountElement.getText().replaceAll("[^0-9]", "");
        double discountPercent = Double.parseDouble(discountText);
        System.out.println(discountPercent);

        // 5. Tính giá mong đợi
        double expectedPrice = originalPrice - (originalPrice * discountPercent / 100);

        // 6. So sánh
        Assert.assertEquals(salePrice, expectedPrice, 
            "Giá sau khi giảm không đúng! Expected: " + expectedPrice + " | Actual: " + salePrice);
        
        System.out.println("Giá sau khi giảm đúng: " + salePrice);
    }


    @AfterClass
    public void tearDown() {
        // Đóng driver sau khi test xong để tránh rò rỉ session
        Driver_Factory.quitDriver();
    }
}
