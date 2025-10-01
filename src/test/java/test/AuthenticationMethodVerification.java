package test;

import pages.authenticationPage;
import org.openqa.selenium.WebDriver;

/**
 * Verification class to ensure authenticationPage methods are accessible
 */
public class AuthenticationMethodVerification {

    public void verifyMethods(WebDriver driver) {
        authenticationPage authPage = new authenticationPage(driver);

        // These method calls confirm that isUserLoggedIn and getLoggedInUserName exist
        boolean userLoggedIn = authPage.isUserLoggedIn();
        String userName = authPage.getLoggedInUserName();

        System.out.println("Methods verified:");
        System.out.println("- isUserLoggedIn(): " + userLoggedIn);
        System.out.println("- getLoggedInUserName(): " + userName);
    }
}
