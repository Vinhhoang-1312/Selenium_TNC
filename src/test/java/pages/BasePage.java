package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.FluentWait;

import java.time.Duration;
import java.util.List;

public abstract class BasePage {
    protected final WebDriver driver;
    protected final WebDriverWait wait;

    protected BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    protected WebElement waitAndFind(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    protected List<WebElement> waitAndFindList(By locator) {
        return wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(locator));
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
            customWait(200);
            click(locator);
        }
    }

    protected void clickWithJs(By locator) {
        WebElement element = waitAndFind(locator);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
    }

    protected void pressEnter(By locator) {
        WebElement element = waitAndFind(locator);
        element.sendKeys(Keys.ENTER);
    }

    protected boolean isDisplayed(By locator) {
        try {
            return waitAndFind(locator).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    protected List<WebElement> findElements(By locator) {
        return driver.findElements(locator);
    }

    public String getTitle() {
        return driver.getTitle();
    }

    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    public String waitAndGetText(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator)).getText();
    }

    public boolean checkCurrentUrl(String url) {
        return wait.until(ExpectedConditions.urlContains(url));
    }

    public String waitAndGetAttribute(By locator, String nameAttribute) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator)).getAttribute(nameAttribute);
    }

    public boolean waitAndCheckEnabled(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator)).isEnabled();
    }

    public void customWait(int milliseconds) {
        new FluentWait<>(driver)
                .withTimeout(Duration.ofMillis(milliseconds))
                .pollingEvery(Duration.ofMillis(100))
                .ignoring(Exception.class)
                .until(driver -> true);
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

    public void waitForPageLoad() {
        wait.until(webDriver -> {
            String readyState = (String) ((JavascriptExecutor) webDriver)
                    .executeScript("return document.readyState");
            return readyState != null && readyState.equals("complete");
        });
    }

    public void waitForPresence(By locator) {
        wait.until(ExpectedConditions.presenceOfElementLocated(locator));
    }

    public List<WebElement> waitForAllElementsPresence(By locator) {
        return wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(locator));
    }
}

