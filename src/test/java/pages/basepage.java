package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;
import helpers.WaitUtils;
import java.time.Duration;

/**
 * Base Page class containing common functionality for all page objects
 */
public class BasePage {
    protected WebDriver driver;
    protected WebDriverWait wait;
    protected WaitUtils waitUtils;

    public BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        this.waitUtils = new WaitUtils(driver);
        PageFactory.initElements(driver, this);
    }

    /**
     * Get current page title
     */
    public String getPageTitle() {
        return driver.getTitle();
    }

    /**
     * Get current page URL
     */
    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    /**
     * Navigate to a specific URL
     */
    public void navigateTo(String url) {
        driver.get(url);
    }

    /**
     * Check if element is displayed
     */
    public boolean isElementDisplayed(WebElement element) {
        try {
            return element.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Check if element is enabled
     */
    public boolean isElementEnabled(WebElement element) {
        try {
            return element.isEnabled();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Click element with wait
     */
    public void clickElement(WebElement element) {
        waitUtils.waitForElementToBeClickable(element);
        element.click();
    }

    /**
     * Send keys to element with wait
     */
    public void sendKeysToElement(WebElement element, String text) {
        waitUtils.waitForElementToBeVisible(element);
        element.clear();
        element.sendKeys(text);
    }

    /**
     * Get text from element
     */
    public String getElementText(WebElement element) {
        waitUtils.waitForElementToBeVisible(element);
        return element.getText();
    }
}
