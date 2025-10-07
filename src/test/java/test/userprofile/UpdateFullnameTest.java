package test.userprofile;

import org.testng.Assert;
import org.testng.annotations.Test;
import pages.UserProfilePage;
import test.BaseTest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UpdateFullnameTest extends BaseTest {
    private static final Logger log = LoggerFactory.getLogger(UpdateFullnameTest.class);

    @Test(groups = "userprofile")
    public void testUpdateFullname() {
        log.info("[TEST] Starting testUpdateFullname - waiting for fullname field to appear...");
        UserProfilePage profilePage = new UserProfilePage(driver);
        try {
            String oldValue = profilePage.getFullname();
            String newValue = oldValue + "_Test";
            profilePage.updateFullname(newValue);
            driver.navigate().refresh();
            Thread.sleep(1500);
            String after = profilePage.getFullname();
            Assert.assertEquals(after, newValue);
        } catch (Exception e) {
            log.error("[TEST] Could not find fullname field or encountered an error: {}", e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }
}
