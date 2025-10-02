package helpers;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.ElementClickInterceptedException;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PopupHandler {
    private static final Logger log = LoggerFactory.getLogger(PopupHandler.class);
    private final WebDriver driver;

    // Popup close selectors
    private static final By POPUP_CLOSE_BTN_1 = By.xpath("//div[@class='widget-header--inner widget-header--inner--collapsed']//span[@class='widget-header--button-close-icon']");
    private static final By POPUP_CLOSE_BTN_2 = By.xpath("//div[@class='widget-preview--btn-close']");
    private static final By POPUP_CLOSE_BTN_1_CSS = By.cssSelector("div.widget-header--inner.widget-header--inner--collapsed span.widget-header--button-close-icon");
    private static final By POPUP_CLOSE_BTN_2_CSS = By.cssSelector(".widget-preview--btn-close");

    public PopupHandler(WebDriver driver) {
        this.driver = driver;
    }

    /**
     * Attempt to dismiss all known popups if present.
     */
    public void dismissAllPopups() {
        tryClosePopup(POPUP_CLOSE_BTN_1);
        tryClosePopup(POPUP_CLOSE_BTN_2);
        tryClosePopup(POPUP_CLOSE_BTN_1_CSS);
        tryClosePopup(POPUP_CLOSE_BTN_2_CSS);
    }

    /**
     * Try to click an element, if click is intercepted by popup, dismiss popups and retry once.
     */
    public void clickWithPopupHandling(WebElement element) {
        try {
            element.click();
        } catch (ElementClickInterceptedException e) {
            log.warn("Click intercepted, attempting to close popups...");
            dismissAllPopups();
            try {
                element.click();
            } catch (Exception ex) {
                log.error("Click failed after popup handling.", ex);
                throw ex;
            }
        }
    }

    /**
     * Try to close popup by selector if present.
     */
    private void tryClosePopup(By by) {
        try {
            List<WebElement> closeBtns = driver.findElements(by);
            for (WebElement btn : closeBtns) {
                if (btn.isDisplayed() && btn.isEnabled()) {
                    btn.click();
                    log.info("Closed popup with selector: {}", by);
                }
            }
        } catch (NoSuchElementException ignore) {
            // No popup found, ignore
        } catch (Exception e) {
            log.warn("Failed to close popup with selector: {}", by, e);
        }
    }

    /**
     * Wait for overlays/popups to disappear (if needed, can be extended).
     */
    public void waitForOverlaysToDisappear() {
        // Implement if needed, e.g. WebDriverWait for overlays
    }
}
