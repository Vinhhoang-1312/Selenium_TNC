package test;


import commons.DriverFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

import java.time.Duration;

public class BaseTest {
    private static final Logger log = LoggerFactory.getLogger(BaseTest.class);
    protected WebDriver driver;


    @BeforeClass(alwaysRun = true)
    public void setup() {
        driver = DriverFactory.getDriver();
        driver.get("https://www.tncstore.vn/");
    }

    @AfterClass(alwaysRun = true)
    public void tearDown() {
        DriverFactory.quitDriver();
    }

    public boolean clickIfPresent(By locator) {
        try {
            // Chờ tối đa 2 giây để tìm phần tử hiển thị và có thể nhấp
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(2));
            WebElement element = wait.until(
                    ExpectedConditions.elementToBeClickable(locator)
            );

            // Nhấp vào phần tử
            element.click();
            log.info("Clicked on element: {}", locator);
            return true;
        } catch (Exception e) {
            // Chỉ bắt các ngoại lệ liên quan
            log.info("Element not present or not clickable, skipping: {}. Reason: {}",
                    locator, e.getMessage());
            return false;
        }
    }

    public void dismissPopupsIfPresent() {
        clickIfPresent(By.cssSelector(".widget-header--button-close"));
        clickIfPresent(By.cssSelector(".widget-preview--btn-close"));
    }
}
