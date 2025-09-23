package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class GooglePage {
    WebDriver driver;

    public GooglePage(WebDriver driver) {
        this.driver = driver;
    }

    public void search(String keyword) {
        driver.findElement(By.name("q")).sendKeys(keyword);
        driver.findElement(By.name("q")).submit();
    }

    public String getFirstResultTitle() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("h3")));

        List<WebElement> results = driver.findElements(By.cssSelector("h3"));
        if (results.isEmpty()) {
            return "No results found or CAPTCHA detected!";
        } else {
            return results.get(0).getText();
        }
    }

    public String getFirstResultLink() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div.yuRUbf > a")));

        List<WebElement> links = driver.findElements(By.cssSelector("div.yuRUbf > a"));
        if (links.isEmpty()) {
            return "No link found!";
        } else {
            return links.get(0).getAttribute("href");
        }
    }
}
