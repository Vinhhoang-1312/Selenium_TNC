package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class BasePage {
    protected WebDriver driver;
    protected WebDriverWait wait;

    public BasePage(WebDriver driver) {
        this.driver = driver;
        driver.manage().window().maximize();
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    protected void waitForPageLoad() {
        wait.until(webDriver -> {
            String readyState = (String) ((JavascriptExecutor) webDriver)
                    .executeScript("return document.readyState");
            return readyState != null && readyState.equals("complete");
        });
    }

    protected void waitForElementToDisappear(By locator) {
        try {
            wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
        } catch (TimeoutException e) {
            // Element might not exist at all, which is fine
            System.out.println("Note: Loading element was not present");
        }
    }

    protected void waitForElementToBeClickable(WebElement element) {
        wait.until(ExpectedConditions.elementToBeClickable(element));
    }

    protected void waitForElementToBeVisible(By locator) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }
}
