package base;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import utils.ConfigReader;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

public class DriverFactory {
    private static ThreadLocal<WebDriver> driver = new ThreadLocal<>();

    public static void initializeDriver(String browserName) {
        WebDriver webDriver = null;

        switch (browserName.toLowerCase()) {
            case "chrome":
                WebDriverManager.chromedriver().setup();
                ChromeOptions chromeOptions = new ChromeOptions();
                chromeOptions.addArguments("--disable-notifications");
                chromeOptions.addArguments("--disable-popup-blocking");
                chromeOptions.addArguments("--disable-blink-features=AutomationControlled");

                // Fix experimental options
                Map<String, Object> prefs = new HashMap<String, Object>();
                prefs.put("useAutomationExtension", false);
                chromeOptions.setExperimentalOption("prefs", prefs);
                chromeOptions.addArguments("--disable-extensions");

                // Headless mode option
                if (Boolean.parseBoolean(ConfigReader.getProperty("headless.mode", "false"))) {
                    chromeOptions.addArguments("--headless");
                }

                webDriver = new ChromeDriver(chromeOptions);
                break;

            case "firefox":
                WebDriverManager.firefoxdriver().setup();
                FirefoxOptions firefoxOptions = new FirefoxOptions();

                // Headless mode option
                if (Boolean.parseBoolean(ConfigReader.getProperty("headless.mode", "false"))) {
                    firefoxOptions.addArguments("--headless");
                }

                webDriver = new FirefoxDriver(firefoxOptions);
                break;

            case "edge":
                WebDriverManager.edgedriver().setup();
                EdgeOptions edgeOptions = new EdgeOptions();
                edgeOptions.addArguments("--disable-notifications");
                edgeOptions.addArguments("--disable-popup-blocking");

                // Headless mode option
                if (Boolean.parseBoolean(ConfigReader.getProperty("headless.mode", "false"))) {
                    edgeOptions.addArguments("--headless");
                }

                webDriver = new EdgeDriver(edgeOptions);
                break;

            default:
                throw new IllegalArgumentException("Browser not supported: " + browserName);
        }

        // Configure driver settings
        webDriver.manage().window().maximize();
        webDriver.manage().deleteAllCookies();
        webDriver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        webDriver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));

        driver.set(webDriver);
    }

    public static WebDriver getDriver() {
        return driver.get();
    }

    public static void closeDriver() {
        if (driver.get() != null) {
            driver.get().quit();
            driver.remove();
        }
    }

    // Additional utility methods
    public static void switchToWindow(String windowHandle) {
        if (driver.get() != null) {
            driver.get().switchTo().window(windowHandle);
        }
    }

    public static void switchToFrame(String frameNameOrId) {
        if (driver.get() != null) {
            driver.get().switchTo().frame(frameNameOrId);
        }
    }

    public static void switchToDefaultContent() {
        if (driver.get() != null) {
            driver.get().switchTo().defaultContent();
        }
    }
}
