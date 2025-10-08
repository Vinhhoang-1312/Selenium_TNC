package utils;

import org.openqa.selenium.WebDriver;

public class DriverHelper {
    protected static final ThreadLocal<WebDriver> driverThreadLocal = new ThreadLocal<>();
    protected static final ThreadLocal<AllureSoftAssert> softAssertThreadLocal = new ThreadLocal<>();

    public static WebDriver getDriver() {
        return driverThreadLocal.get();
    }

    public static AllureSoftAssert getSoftAssert() {
        return softAssertThreadLocal.get();
    }

    public static void setDriverThreadLocal(WebDriver webDriver) {
        driverThreadLocal.set(webDriver);
    }

    public static void setSoftAssertThreadLocal(AllureSoftAssert softAssert) {
        softAssertThreadLocal.set(softAssert);
    }

    public static void quitDriverThreadLocal() {
        driverThreadLocal.remove();
    }

    public static void quitSoftAssertThreadLocal() {
        softAssertThreadLocal.remove();
    }
}
