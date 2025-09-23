package tests;

import base.BaseTest;
import listener.TestListener;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.List;

@Listeners(listener.TestListener.class)
public class GoogleSearchTest extends BaseTest {

    @Test
    public void testGoogleFirstResult() {
        driver.get("https://www.google.com");

        WebElement searchBox = driver.findElement(By.name("q"));
        searchBox.sendKeys("Porsche Việt Nam");
        searchBox.submit();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("h3")));

        List<WebElement> results = driver.findElements(By.cssSelector("h3"));
        Assert.assertTrue(results.size() > 0, "Không tìm thấy kết quả Google");

        WebElement firstTitle = results.get(0);
        WebElement firstLink = firstTitle.findElement(By.xpath("./ancestor::a"));
        String titleText = firstTitle.getText();
        String linkHref = firstLink.getAttribute("href");

        System.out.println("Kết quả đầu tiên: " + titleText + " - " + linkHref);

        driver.get(linkHref);
    }

    @Test
    public void testSearchBoxOnFirstResult() {
        driver.get("https://www.google.com");

        WebElement searchBox = driver.findElement(By.name("q"));
        searchBox.sendKeys("Porsche Việt Nam");
        searchBox.submit();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("h3")));
        WebElement firstTitle = driver.findElements(By.cssSelector("h3")).get(0);
        WebElement firstLink = firstTitle.findElement(By.xpath("./ancestor::a"));
        String linkHref = firstLink.getAttribute("href");
        driver.get(linkHref);

        WebElement siteSearchField = wait.until(
                ExpectedConditions.presenceOfElementLocated(
                        By.xpath("//*[@id='m-01-site-search-field']")
                )
        );

        String payload = "SELECT * FROM san_pham WHERE ten_san_pham LIKE '%Porsche%';";
        siteSearchField.sendKeys(payload);

        WebElement searchButton = driver.findElement(
                By.xpath("//*[@id='m-01-site-search-form']//button")
        );
        searchButton.click();

        Assert.assertTrue(driver.getTitle().contains("Porsche"), "Kết quả tìm kiếm không hợp lệ!");
    }
}
