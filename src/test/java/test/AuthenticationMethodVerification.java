package test;

import pages.AuthenticationPage;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Verification class to ensure AuthenticationPage methods are accessible
 */
public class AuthenticationMethodVerification {
    private static final Logger log = LoggerFactory.getLogger(AuthenticationMethodVerification.class);

    public void verifyMethods(WebDriver driver) {
        AuthenticationPage authPage = new AuthenticationPage(driver);

        // These method calls confirm that isUserLoggedIn and getLoggedInUserName exist
        boolean userLoggedIn = authPage.isUserLoggedIn();
        String userName = authPage.getLoggedInUserName();

        log.info("Methods verified:");
        log.info("- isUserLoggedIn(): {}", userLoggedIn);
        log.info("- getLoggedInUserName(): {}", userName);
    }
}
