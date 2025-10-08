package helpers;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.List;

/**
 * Handles popup dismissal and click interception issues
 */
public class PopupHandler {
    private static final Logger logger = LoggerFactory.getLogger(PopupHandler.class);
    private final WebDriver driver;

    private static final By POPUP_CLOSE_BTN_1 = By.xpath("//div[@class='widget-header--inner widget-header--inner--collapsed']//span[@class='widget-header--button-close-icon']");
    private static final By POPUP_CLOSE_BTN_2 = By.xpath("//div[@class='widget-preview--btn-close']");
    private static final By POPUP_CLOSE_BTN_1_CSS = By.cssSelector("div.widget-header--inner.widget-header--inner--collapsed span.widget-header--button-close-icon");
    private static final By POPUP_CLOSE_BTN_2_CSS = By.cssSelector(".widget-preview--btn-close");

    public PopupHandler(WebDriver driver) {
        this.driver = driver;
    }

    /**
     * Attempts to dismiss all known popups on the page
     */
    public void dismissAllPopups() {
        tryClosePopup(POPUP_CLOSE_BTN_1);
        tryClosePopup(POPUP_CLOSE_BTN_2);
        tryClosePopup(POPUP_CLOSE_BTN_1_CSS);
        tryClosePopup(POPUP_CLOSE_BTN_2_CSS);
    }

    /**
     * Convenience static helper to dismiss all popups without creating a PopupHandler in-line.
     */
    public static void dismissAll(WebDriver driver) {
        try {
            new PopupHandler(driver).dismissAllPopups();
        } catch (Exception e) {
            logger.warn("Failed to dismiss popups via static helper", e);
        }
    }

    /**
     * Clicks an element with automatic popup handling if click is intercepted
     */
    public void clickWithPopupHandling(WebElement element) {
        try {
            element.click();
        } catch (ElementClickInterceptedException e) {
            logger.warn("Click intercepted, attempting to close popups...");
            dismissAllPopups();
            try {
                element.click();
            } catch (Exception ex) {
                logger.error("Click failed after popup handling.", ex);
                throw ex;
            }
        }
    }

    private void tryClosePopup(By by) {
        try {
            List<WebElement> closeBtns = driver.findElements(by);
            for (WebElement btn : closeBtns) {
                try {
                    if (btn.isDisplayed() && btn.isEnabled()) {
                        btn.click();
                        logger.info("Closed popup with selector: {}", by);
                    }
                } catch (WebDriverException clickEx) {
                    // element may be stale or window closed, continue
                    logger.warn("Could not click close button for selector {}: {}", by, clickEx.toString());
                }
            }
        } catch (NoSuchElementException ignore) {
            // No popup found, continue
        } catch (WebDriverException wde) {
            // Browser window may have been closed or detached; swallow to avoid test crash
            logger.warn("WebDriverException while searching for popup {}: {}", by, wde.toString());
        } catch (Exception e) {
            logger.warn("Failed to close popup with selector: {}", by, e);
        }
    }

    /**
     * Waits for JavaScript alert to be present and returns it
     * @param timeoutSeconds timeout in seconds
     * @return Alert object if present, null otherwise
     */
    public Alert waitForAlert(int timeoutSeconds) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
            return wait.until(ExpectedConditions.alertIsPresent());
        } catch (Exception e) {
            logger.warn("No alert present after waiting {} seconds", timeoutSeconds);
            return null;
        }
    }

    /**
     * Gets the text from JavaScript alert
     * @param timeoutSeconds timeout in seconds
     * @return alert text or null if no alert
     */
    public String getAlertText(int timeoutSeconds) {
        try {
            Alert alert = waitForAlert(timeoutSeconds);
            if (alert != null) {
                String text = alert.getText();
                logger.info("Alert text: {}", text);
                return text;
            }
        } catch (Exception e) {
            logger.error("Failed to get alert text", e);
        }
        return null;
    }

    /**
     * Accepts (clicks OK on) JavaScript alert
     * @param timeoutSeconds timeout in seconds
     */
    public void acceptAlert(int timeoutSeconds) {
        try {
            Alert alert = waitForAlert(timeoutSeconds);
            if (alert != null) {
                alert.accept();
                logger.info("Alert accepted");
            }
        } catch (Exception e) {
            logger.error("Failed to accept alert", e);
        }
    }

    /**
     * Dismisses (clicks Cancel on) JavaScript alert
     * @param timeoutSeconds timeout in seconds
     */
    public void dismissAlert(int timeoutSeconds) {
        try {
            Alert alert = waitForAlert(timeoutSeconds);
            if (alert != null) {
                alert.dismiss();
                logger.info("Alert dismissed");
            }
        } catch (Exception e) {
            logger.error("Failed to dismiss alert", e);
        }
    }

    /**
     * Gets alert text and accepts it in one call
     * @param timeoutSeconds timeout in seconds
     * @return alert text or null if no alert
     */
    public String getAlertTextAndAccept(int timeoutSeconds) {
        try {
            Alert alert = waitForAlert(timeoutSeconds);
            if (alert != null) {
                String text = alert.getText();
                logger.info("Alert text: {}", text);
                alert.accept();
                logger.info("Alert accepted");
                return text;
            }
        } catch (Exception e) {
            logger.error("Failed to get alert text and accept", e);
        }
        return null;
    }

    /**
     * Checks if alert is present without waiting
     * @return true if alert is present, false otherwise
     */
    public boolean isAlertPresent() {
        try {
            driver.switchTo().alert();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void waitForOverlaysToDisappear() {
        // Future implementation for waiting overlays
    }
}
