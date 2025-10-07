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
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeSuite;

import java.time.Duration;

public class BaseTest {
    private static final Logger log = LoggerFactory.getLogger(BaseTest.class);
    protected WebDriver driver;

    @BeforeClass(alwaysRun = true)
    public void setDriver(){
        try {
            driver = DriverFactory.getDriver();
            if (driver == null) {
                throw new RuntimeException("DriverFactory returned null driver");
            }
            driver.get("https://www.tncstore.vn/");
            log.info("Driver initialized successfully for test class: {}", this.getClass().getSimpleName());
        } catch (Exception e) {
            log.error("Failed to initialize driver in BaseTest setup", e);
            throw new RuntimeException("Driver initialization failed", e);
        }
    }

    @AfterClass(alwaysRun = true)
    public void tearDown() {
        try {
            DriverFactory.quitDriver();
            driver = null;
            log.info("Driver quit successfully for test class: {}", this.getClass().getSimpleName());
        } catch (Exception e) {
            log.error("Error during driver teardown", e);
        }
    }

    public boolean clickIfPresent(By locator) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(2));
            WebElement element = wait.until(
                    ExpectedConditions.elementToBeClickable(locator)
            );

            element.click();
            log.info("Clicked on element: {}", locator);
            return true;
        } catch (Exception e) {
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
