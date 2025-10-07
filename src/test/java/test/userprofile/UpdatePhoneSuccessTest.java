package test.userprofile;

import org.testng.Assert;
import org.testng.annotations.Test;
import pages.UserProfilePage;
import test.BaseTest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UpdatePhoneSuccessTest extends BaseTest {
    private static final Logger log = LoggerFactory.getLogger(UpdatePhoneSuccessTest.class);

    @Test(groups = "userprofile")
    public void testUpdatePhoneSuccess() {
        UserProfilePage profilePage = new UserProfilePage(driver);
        String newPhone = "0912345678";
        profilePage.updatePhone(newPhone);
        driver.navigate().refresh();
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        String after = profilePage.getPhone();
        Assert.assertEquals(after, newPhone);
    }
}
