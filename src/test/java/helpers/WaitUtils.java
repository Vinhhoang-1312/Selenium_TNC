package helpers;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;

import java.time.Duration;
import java.util.List;

/**
 * ⏰ Wait Utils - Centralized waiting strategies
 */
public class WaitUtils {
    private static final int DEFAULT_TIMEOUT = 15;
    private static final int DEFAULT_POLLING = 2;

    private WebDriver driver;
    private WebDriverWait wait;

    // Constructor for instance usage (used in BasePage)
    public WaitUtils(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(DEFAULT_TIMEOUT));
    }

    // ========== INSTANCE METHODS (for BasePage usage) ==========

    /**
     * Wait for element to be visible (instance method)
     */
    public WebElement waitForElementToBeVisible(WebElement element) {
        try {
            return wait.until(ExpectedConditions.visibilityOf(element));
        } catch (TimeoutException e) {
            throw new TimeoutException("Element not visible after " + DEFAULT_TIMEOUT + " seconds: " + element);
        }
    }

    /**
     * Wait for element to be clickable (instance method)
     */
    public WebElement waitForElementToBeClickable(WebElement element) {
        try {
            return wait.until(ExpectedConditions.elementToBeClickable(element));
        } catch (TimeoutException e) {
            throw new TimeoutException("Element not clickable after " + DEFAULT_TIMEOUT + " seconds: " + element);
        }
    }

    /**
     * Wait for element to be present (instance method)
     */
    public boolean waitForElementToBePresent(WebElement element) {
        try {
            return element.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    // ========== STATIC METHODS (for direct usage) ==========

    /**
     * Wait for element to be visible
     */
    public static WebElement waitForElementVisible(WebDriver driver, By locator, int timeoutSeconds) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
            return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        } catch (TimeoutException e) {
            throw new TimeoutException("Element not visible after " + timeoutSeconds + " seconds: " + locator);
        }
    }

    /**
     * Wait for element to be visible with default timeout
     */
    public static WebElement waitForElementVisible(WebDriver driver, By locator) {
        return waitForElementVisible(driver, locator, DEFAULT_TIMEOUT);
    }

    /**
     * Wait for element to be clickable
     */
    public static WebElement waitForElementClickable(WebDriver driver, By locator, int timeoutSeconds) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
            return wait.until(ExpectedConditions.elementToBeClickable(locator));
        } catch (TimeoutException e) {
            throw new TimeoutException("Element not clickable after " + timeoutSeconds + " seconds: " + locator);
        }
    }

    /**
     * Wait for element to be clickable with default timeout
     */
    public static WebElement waitForElementClickable(WebDriver driver, By locator) {
        return waitForElementClickable(driver, locator, DEFAULT_TIMEOUT);
    }

    /**
     * Wait for element presence
     */
    public static WebElement waitForElementPresent(WebDriver driver, By locator, int timeoutSeconds) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
            return wait.until(ExpectedConditions.presenceOfElementLocated(locator));
        } catch (TimeoutException e) {
            throw new TimeoutException("Element not present after " + timeoutSeconds + " seconds: " + locator);
        }
    }

    /**
     * Wait for element presence with default timeout
     */
    public static WebElement waitForElementPresent(WebDriver driver, By locator) {
        return waitForElementPresent(driver, locator, DEFAULT_TIMEOUT);
    }

    /**
     * Wait for text to be present in element
     */
    public static boolean waitForTextInElement(WebDriver driver, By locator, String text, int timeoutSeconds) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
            return wait.until(ExpectedConditions.textToBePresentInElementLocated(locator, text));
        } catch (TimeoutException e) {
            return false;
        }
    }

    /**
     * Wait for element to disappear
     */
    public static boolean waitForElementToDisappear(WebDriver driver, By locator, int timeoutSeconds) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
            return wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
        } catch (TimeoutException e) {
            return false;
        }
    }

    /**
     * Fluent wait for element with custom conditions
     */
    public static WebElement fluentWaitForElement(WebDriver driver, By locator, int timeoutSeconds, int pollingSeconds) {
        FluentWait<WebDriver> fluentWait = new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(timeoutSeconds))
                .pollingEvery(Duration.ofSeconds(pollingSeconds))
                .ignoring(NoSuchElementException.class);

        return fluentWait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    /**
     * Wait for page to load completely
     */
    public static void waitForPageLoad(WebDriver driver, int timeoutSeconds) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
        wait.until(webDriver -> ((org.openqa.selenium.JavascriptExecutor) webDriver)
                .executeScript("return document.readyState").equals("complete"));
    }

    /**
     * Wait for page to load with default timeout
     */
    public static void waitForPageLoad(WebDriver driver) {
        waitForPageLoad(driver, DEFAULT_TIMEOUT);
    }

    /**
     * Wait for all elements to be visible
     */
    public static List<WebElement> waitForAllElementsVisible(WebDriver driver, By locator, int timeoutSeconds) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
            return wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(locator));
        } catch (TimeoutException e) {
            throw new TimeoutException("Elements not visible after " + timeoutSeconds + " seconds: " + locator);
        }
    }

    /**
     * Simple sleep method
     */
    public static void sleep(int seconds) {
        try {
            Thread.sleep(seconds * 1000L);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
