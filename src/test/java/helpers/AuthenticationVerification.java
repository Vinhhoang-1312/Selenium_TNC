package helpers;

import pages.AuthenticationPage;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Helper class to verify AuthenticationPage methods are accessible and functional.
 */
public class AuthenticationVerification {
    private static final Logger log = LoggerFactory.getLogger(AuthenticationVerification.class);

    public void verifyMethods(WebDriver driver) {
        AuthenticationPage authPage = new AuthenticationPage(driver);
        boolean userLoggedIn = authPage.isUserLoggedIn();
        String userName = authPage.getLoggedInUserName();
        log.info("Methods verified:");
        log.info("- isUserLoggedIn(): {}", userLoggedIn);
        log.info("- getLoggedInUserName(): {}", userName);
    }
}

