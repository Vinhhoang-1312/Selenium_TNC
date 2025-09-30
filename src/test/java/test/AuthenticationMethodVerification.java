package test;

import pages.AuthenticationPage;
import org.openqa.selenium.WebDriver;

/**
 * Verification class to ensure AuthenticationPage methods are accessible
 * This file helps IDEs recognize the methods by explicitly referencing them
 */
public class AuthenticationMethodVerification {

    public void verifyMethodsExist(WebDriver driver) {
        AuthenticationPage authPage = new AuthenticationPage(driver);

        // These method calls confirm that isUserLoggedIn and getLoggedInUserName exist
        boolean userLoggedIn = authPage.isUserLoggedIn();
        String userName = authPage.getLoggedInUserName();

        System.out.println("Methods verified:");
        System.out.println("- isUserLoggedIn(): " + userLoggedIn);
        System.out.println("- getLoggedInUserName(): " + userName);
    }
}
