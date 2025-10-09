package pages;

import helpers.PopupHandler;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.List;

public abstract class BasePage {
    protected static final Logger logger = LoggerFactory.getLogger(BasePage.class);
    protected final WebDriver driver;
    protected final WebDriverWait wait;
    protected final PopupHandler popupHandler;

    protected BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, java.time.Duration.ofSeconds(10));
        this.popupHandler = new PopupHandler(driver);
    }

    protected WebElement waitAndFind(By locator) {
//        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));

        WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(locator));
        String text = element.getAttribute("innerText");
        System.out.println("Notification text: \"" + text + "\"");
        return element;
    }

    protected void clearAndType(By locator, String value) {
        WebElement element = waitAndFind(locator);
        element.clear();
        element.sendKeys(value);
    }

    protected void click(By locator) {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(locator)).click();
        } catch (ElementNotInteractableException e) {
            // Retry immediately without sleep - element will become interactable or fail fast
            click(locator);
        } catch (org.openqa.selenium.UnhandledAlertException alertEx) {
            // Accept unexpected JS alert and retry
            try {
                popupHandler.getAlertTextAndAccept(3);
            } catch (Exception ex) {
                // ignore
            }
            // retry click once
            wait.until(ExpectedConditions.elementToBeClickable(locator)).click();
        }
    }

    protected boolean isDisplayed(By locator) {
        try {
            waitAndFind(locator);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    protected List<WebElement> findElements(By locator) {
        return driver.findElements(locator);
    }

    public String waitAndGetText(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator)).getText();
    }

    // DEPRECATED: Use explicit waits instead of Thread.sleep
    // This method is kept for backward compatibility but should not be used
    public void customWait(int milliseconds) {
        logger.warn("customWait() is deprecated - use explicit wait methods instead");
    }

    public void waitForElementToDisappear(By locator) {
        try {
            wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
        } catch (TimeoutException e) {
        }
    }

    public WebElement waitForElementToBeClickable(By locator) {
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    public WebElement waitForElementToBeVisible(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public List<WebElement> waitForAllElementsPresence(By locator) {
        return wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(locator));
    }

    protected void clickElementWithRetry(By by, String elementName) {
        int maxAttempts = 3;
        for (int attempt = 1; attempt <= maxAttempts; attempt++) {
            try {
                popupHandler.dismissAllPopups();
                wait.until(ExpectedConditions.elementToBeClickable(by));
                driver.findElement(by).click();
                return;
            } catch (ElementClickInterceptedException e) {
                popupHandler.dismissAllPopups();
                WebElement el = driver.findElement(by);
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", el);
                return;
            } catch (org.openqa.selenium.UnhandledAlertException alertEx) {
                popupHandler.getAlertTextAndAccept(3);
                if (attempt == maxAttempts) {
                    throw new RuntimeException("Failed to click " + elementName + " after handling unexpected alert", alertEx);
                }
            } catch (Exception e) {
                if (attempt == maxAttempts) {
                    throw new RuntimeException("Failed to click " + elementName + " after " + maxAttempts + " attempts", e);
                }
                try {
                    wait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector(".loading, .spinner")));
                } catch (Exception ignored) {
                }
            }
        }
    }

    protected void waitForPageStability() {
        try {
            wait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector(".loading, .spinner")));
        } catch (Exception e) {
            // No loading spinner found or timeout, page is stable
        }
        try {
            wait.until(webDriver -> ((JavascriptExecutor) webDriver)
                    .executeScript("return document.readyState").equals("complete"));
        } catch (Exception e) {
            // Fallback if JS check fails
        }
    }
}
