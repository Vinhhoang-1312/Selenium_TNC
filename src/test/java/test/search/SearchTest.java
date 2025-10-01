package test.search;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import commons.Driver_Factory;
import pages.SearchPage;

public class SearchTest {
    private WebDriver driver;
    private String baseUrl = "https://www.tncstore.vn/";

    @BeforeClass
    public void setUp() {
        // Lấy driver từ Driver_Factory, sẽ khởi tạo mới nếu cần
        driver = Driver_Factory.getDriver();
        driver.get(baseUrl); // Mở trang web cần test
    }

    @Test
    public void testSearchFunctionality() {
        // Thêm mã kiểm thử chức năng tìm kiếm tại đây
        assert driver.findElement(By.xpath("//input[@id='js-global-seach']")).isDisplayed();
    }

    @Test
    public void testSearchResults() {
        // Thêm mã kiểm thử kết quả tìm kiếm tại đây
        driver.findElement(By.xpath("//input[@id='js-global-seach']")).sendKeys("Laptop");
        driver.findElement(By.xpath("//button[@class='submit-search']")).click();
    }

    @Test//tìm với keyword bình thường và ký tự đặc biệt
    public void testSearchForRTX2050() {
        // Send search query for RTX 2050
        driver.findElement(SearchPage.searchInput).sendKeys("rtx & 2050");

        // Submit the search form
        driver.findElement(By.xpath("//button[@class='submit-search']")).click();

        // Check if the result matches the expected locator
        String result = driver.findElement(SearchPage.noproductNoti).getText();
        Assert.assertEquals(result, "Ôi! Rất tiếc không tìm thấy sản phẩm nào...!"); // Replace with your expected outcome
    }

    @Test(invocationCount = 5)//tìm liên tục 5 lần
    public void testSearchForRTX2050Repeat() {
        // Send search query for RTX 2050
        driver.findElement(SearchPage.searchInput).sendKeys("rtx & 2050");

        // Submit the search form
        driver.findElement(By.xpath("//button[@class='submit-search']")).click();

        // Check if the result matches the expected locator
        String result = driver.findElement(SearchPage.noproductNoti).getText();
        Assert.assertEquals(result, "Ôi! Rất tiếc không tìm thấy sản phẩm nào...!"); // Replace with your expected outcome

        //reload page
        driver.navigate().refresh();
    }

    @Test//tìm với keyword hợp lệ
    public void testSearchPerformCorrect() {
        // Send search query for RTX 2050
        driver.findElement(SearchPage.searchInput).sendKeys("rtx 2050");
        // Wait for 4 seconds before checking if the suggestion list is displayed
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Get the suggested items from the suggestion list
        List<WebElement> suggestedItems = helpers.PageHelpers.waitForAllElementsPresence(driver, SearchPage.suggestionList, 4);

        // Iterate through the suggested items and check if any of them contain the search query
        for (WebElement item : suggestedItems) {
            String itemName = item.getText().toLowerCase();
            // In ra nội dung phần tử đang kiểm tra
            System.out.println("Đang kiểm tra gợi ý: " + itemName);
//            System.out.println("HTML: " + item.getAttribute("innerHTML"));
//            System.out.println("Class: " + item.getAttribute("class"));
//            System.out.println("Link: " + item.getAttribute("href")); // nếu là thẻ <a>

            Assert.assertTrue(itemName.contains("rtx 2050".toLowerCase()), "Suggested item contains the search query");
        }
    }
    
    @Test//test khi nhập keyword và reload lại trang web
    public void testSearchReloadPage() {
        // Send search query for RTX 2050
        driver.findElement(SearchPage.searchInput).sendKeys("rtx 2050");

        // Reload the page to simulate a user reloading the page after searching
        driver.navigate().refresh();

        // Check if the keyword is still present in the search input field
        WebElement searchInput = driver.findElement(SearchPage.searchInput);
        Assert.assertTrue(searchInput.getAttribute("value").contains("rtx 2050"), "Keyword not found after reloading page");
    }

    @Test//tìm với keyword rỗng
    public void testSearchWithNoKeyword() {
        // Send search query with no keyword
        driver.findElement(SearchPage.searchInput).sendKeys("");

        // Wait for 3 seconds before checking if the suggestion list is displayed
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Get the suggested items from the suggestion list
        List<WebElement> suggestedItems = driver.findElements(SearchPage.suggestionList);

        // Check if any of the suggested items contain no information
        for (WebElement item : suggestedItems) {
            String itemName = item.getText().toLowerCase();
            System.out.println("Checking item: " + itemName);
            Assert.assertTrue(itemName.contains("no information".toLowerCase()), "Suggested item contains information");
        }
    }

    @AfterClass
    public void tearDown() {
        // Đóng driver sau khi test xong để tránh rò rỉ session
        Driver_Factory.quitDriver();
    }

}
