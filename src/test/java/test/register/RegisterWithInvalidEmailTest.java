package test.register;

import pages.AuthenticationPage;
import data.AuthenticationTestData;
import test.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class RegisterWithInvalidEmailTest extends BaseTest {
    private static final Logger log = LoggerFactory.getLogger(RegisterWithInvalidEmailTest.class);

    @Test(groups = {"authentication", "negative", "signup"},
            description = "AUTH-SU-03: Register with invalid email format")
    public void testRegisterWithInvalidEmail() {
        try {
            AuthenticationPage authPage = new AuthenticationPage(driver);
            authPage.performRegistration(
                    AuthenticationTestData.VALID_NAME_3,
                    AuthenticationTestData.INVALID_EMAIL_1,
                    AuthenticationTestData.VALID_PASSWORD_3
            );

            String expectedText = "Email không hợp lệ";
            log.error("bat tai div");
            By divBy = By.xpath("//div[text()='" + expectedText + "']");
            WebElement el = helpers.WaitUtils.waitForElementVisible(driver, divBy, 5);

            if (el == null) {
                By spanBy = By.xpath("//span[normalize-space(text())='" + expectedText + "']");
                el = helpers.WaitUtils.waitForElementVisible(driver, spanBy, 5);
            }

            Assert.assertNotNull(el, "Expected validation message element '" + expectedText + "' not found (div/span).");
            String actual = el.getText().trim();
            Assert.assertEquals(actual, expectedText, "Validation message text does not match expected text.");

        } catch (Exception e) {
            log.error("Register with invalid email test failed: ", e);
            throw e;
        }
    }
}
