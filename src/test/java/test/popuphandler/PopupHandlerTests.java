package test.popuphandler;

import helpers.PopupHandler;
import test.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PopupHandlerTests extends BaseTest {
    private static final Logger log = LoggerFactory.getLogger(PopupHandlerTests.class);

    @Test
    public void testDismissAllPopups() {
        PopupHandler popupHandler = new PopupHandler(driver);
        try {
            popupHandler.dismissAllPopups();
            Assert.assertTrue(true);
        } catch (Exception e) {
            log.error("Popup dismiss failed", e);
            Assert.fail("Popup dismiss failed: " + e.getMessage());
        }
    }
}
