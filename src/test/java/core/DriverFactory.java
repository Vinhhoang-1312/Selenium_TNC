package core;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

public final class DriverFactory {
    private DriverFactory() {
    }

    public static WebDriver initDriver(String browser) {
        return switch (browser.toLowerCase()) {
            case "chrome" -> configure(new ChromeDriver());
            case "edge" -> configure(new EdgeDriver());
            case "firefox" -> configure(new FirefoxDriver());
            default -> configure(new ChromeDriver());
        };
    }

    private static WebDriver configure(WebDriver driver) {
        driver.manage().window().maximize();
        return driver;
    }
}

