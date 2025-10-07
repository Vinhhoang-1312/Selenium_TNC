package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class UtilsPage extends BasePage {
    private static final Logger logger = LoggerFactory.getLogger(UtilsPage.class);

    private static final By POPUP_CLOSE_BTN_1 = By.xpath("//div[@class='widget-header--inner widget-header--inner--collapsed']//span[@class='widget-header--button-close-icon']");
    private static final By POPUP_CLOSE_BTN_2 = By.xpath("//div[@class='widget-preview--btn-close']");
    private static final By POPUP_CLOSE_BTN_1_CSS = By.cssSelector("div.widget-header--inner.widget-header--inner--collapsed span.widget-header--button-close-icon");
    private static final By POPUP_CLOSE_BTN_2_CSS = By.cssSelector(".widget-preview--btn-close");

    public UtilsPage(WebDriver driver) {
        super(driver);
    }

    public void handlePopup() {
        tryClosePopup(POPUP_CLOSE_BTN_1);
        tryClosePopup(POPUP_CLOSE_BTN_2);
        tryClosePopup(POPUP_CLOSE_BTN_1_CSS);
        tryClosePopup(POPUP_CLOSE_BTN_2_CSS);
    }

    private void tryClosePopup(By by) {
        try {
            List<WebElement> closeBtns = driver.findElements(by);
            for (WebElement btn : closeBtns) {
                if (btn.isDisplayed() && btn.isEnabled()) {
                    btn.click();
                    logger.info("Closed popup with selector: {}", by);
                }
            }
        } catch (NoSuchElementException ignore) {
        } catch (Exception e) {
            logger.warn("Failed to close popup with selector: {}", by, e);
        }
    }
}

