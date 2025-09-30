package commons;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

/**
 *  Driver Factory - Quản lý WebDriver instances
 * Supports: Chrome, Firefox, Edge
 */
public class DriverFactory {
    private static final ThreadLocal<WebDriver> driverThreadLocal = new ThreadLocal<>();

    /**
     * Initialize WebDriver based on browser type
     * @param browserName Browser name (chrome, firefox, edge)
     */
    public static void initializeDriver(String browserName) {
        // Check if driver is already initialized
        if (isDriverInitialized()) {
            System.out.println("⚠️ Driver already initialized, skipping...");
            return;
        }

        WebDriver driver = null;

        switch (browserName.toLowerCase()) {
            case "chrome":
                WebDriverManager.chromedriver().setup();
                ChromeOptions chromeOptions = new ChromeOptions();
                chromeOptions.addArguments("--disable-blink-features=AutomationControlled");
                chromeOptions.addArguments("--disable-extensions");
                chromeOptions.addArguments("--disable-dev-shm-usage");
                chromeOptions.addArguments("--no-sandbox");
                chromeOptions.addArguments("--remote-allow-origins=*");
                // Add argument to prevent multiple instances
                chromeOptions.addArguments("--disable-background-timer-throttling");
                chromeOptions.addArguments("--disable-renderer-backgrounding");
                driver = new ChromeDriver(chromeOptions);
                break;

            case "firefox":
                WebDriverManager.firefoxdriver().setup();
                FirefoxOptions firefoxOptions = new FirefoxOptions();
                firefoxOptions.addArguments("--disable-blink-features=AutomationControlled");
                driver = new FirefoxDriver(firefoxOptions);
                break;

            case "edge":
                WebDriverManager.edgedriver().setup();
                EdgeOptions edgeOptions = new EdgeOptions();
                edgeOptions.addArguments("--disable-blink-features=AutomationControlled");
                edgeOptions.addArguments("--remote-allow-origins=*");
                driver = new EdgeDriver(edgeOptions);
                break;

            default:
                throw new IllegalArgumentException("❌ Browser not supported: " + browserName);
        }

        driverThreadLocal.set(driver);
        System.out.println("✅ " + browserName + " driver initialized successfully");
    }

    /**
     * Get current WebDriver instance
     * @return WebDriver instance
     */
    public static WebDriver getDriver() {
        return driverThreadLocal.get();
    }

    /**
     * Quit WebDriver and remove from ThreadLocal
     */
    public static void quitDriver() {
        WebDriver driver = driverThreadLocal.get();
        if (driver != null) {
            try {
                driver.quit();
                System.out.println("✅ WebDriver quit successfully");
            } catch (Exception e) {
                System.err.println("⚠️ Warning: Error quitting driver: " + e.getMessage());
                // Force kill Chrome processes if quit() fails
                try {
                    if (System.getProperty("os.name").toLowerCase().contains("windows")) {
                        Runtime.getRuntime().exec("taskkill /F /IM chrome.exe /T");
                        Runtime.getRuntime().exec("taskkill /F /IM chromedriver.exe /T");
                        System.out.println("🔧 Force killed Chrome processes");
                    }
                } catch (Exception killEx) {
                    System.err.println("⚠️ Could not force kill Chrome: " + killEx.getMessage());
                }
            } finally {
                driverThreadLocal.remove();
                System.out.println("🧹 ThreadLocal cleaned up");
            }
        }
    }

    /**
     * Check if driver is already initialized
     */
    public static boolean isDriverInitialized() {
        return driverThreadLocal.get() != null;
    }

    /**
     * Force cleanup all resources
     */
    public static void forceCleanup() {
        quitDriver();
        try {
            if (System.getProperty("os.name").toLowerCase().contains("windows")) {
                Runtime.getRuntime().exec("taskkill /F /IM chrome.exe /T");
                Runtime.getRuntime().exec("taskkill /F /IM chromedriver.exe /T");
                System.out.println("🔧 Force cleanup completed");
            }
        } catch (Exception e) {
            System.err.println("⚠️ Force cleanup failed: " + e.getMessage());
        }
    }
}
