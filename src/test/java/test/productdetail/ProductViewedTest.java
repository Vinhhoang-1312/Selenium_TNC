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

import java.util.List;

public class ProductViewedTest {
    private static final Logger log = LoggerFactory.getLogger(ProductViewedTest.class);
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
    public void testViewedProduct() {
        By similarProductLocator = ProductDetailPage.similarProduct;
        By productNameLocator = ProductDetailPage.productName;
        By viewedProductLocator = ProductDetailPage.viewedProduct;
        String initialTitle = base.waitForElementToBeVisible(productNameLocator).getText();
        System.out.println(initialTitle);

        WebElement otherProduct = base.waitForElementToBeClickable(similarProductLocator);
        otherProduct.click();

        WebElement viewedSection = base.waitForElementToBeVisible(viewedProductLocator);

        List<WebElement> viewedTitles = viewedSection.findElements(By.cssSelector("a.product-name"));
        System.out.println("📋 Danh sách sản phẩm đã xem:");
        for (WebElement titleElement : viewedTitles) {
            System.out.println("- " + titleElement.getText());
        }
        boolean found = viewedTitles.stream().anyMatch(e -> e.getText().contains(initialTitle));

        Assert.assertTrue(found, "❌ Sản phẩm ban đầu không có trong danh sách đã xem!");
        System.out.println("✅ Sản phẩm đã được thêm vào danh sách đã xem.");
    }

    @AfterClass
    public void tearDown() {
        Driver_Factory.quitDriver();
    }

}
