package core;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

public final class DriverFactory {
    private DriverFactory() {
    }

    public static WebDriver initDriver(String browser) {
        return switch (browser.toLowerCase()) {
            case "chrome" -> configure(new ChromeDriver(getChromeOptions()));
            case "edge" -> configure(new EdgeDriver());
            case "firefox" -> configure(new FirefoxDriver());
            default -> configure(new ChromeDriver(getChromeOptions()));
        };
    }

    private static ChromeOptions getChromeOptions() {
        ChromeOptions options = new ChromeOptions();
        // Essential for CI/CD environments to avoid user-data-dir conflicts
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-gpu");
        options.addArguments("--disable-extensions");
        options.addArguments("--disable-popup-blocking");
        options.addArguments("--disable-notifications");
        options.addArguments("--remote-allow-origins=*");

        // For CI/CD environments - use headless mode
        String ciEnv = System.getenv("CI");
        if ("true".equalsIgnoreCase(ciEnv)) {
            options.addArguments("--headless=new");
            options.addArguments("--window-size=1920,1080");
        }

        // DO NOT set --user-data-dir to avoid conflicts in CI
        // Chrome will use a temporary profile automatically

        return options;
    }

    private static WebDriver configure(WebDriver driver) {
        driver.manage().window().maximize();
        return driver;
    }
}
