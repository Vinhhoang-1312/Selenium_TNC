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
        try {
            System.out.println("🔄 Starting driver initialization for: " + browserName);

            // Check if driver is already initialized
            if (isDriverInitialized()) {
                System.out.println("⚠️ Driver already initialized, cleaning up first...");
                quitDriver();
            }

            WebDriver driver = null;

            switch (browserName.toLowerCase()) {
                case "chrome":
                    System.out.println("📦 Setting up ChromeDriver with WebDriverManager...");
                    WebDriverManager.chromedriver().setup();
                    System.out.println("✅ WebDriverManager setup completed");

                    ChromeOptions chromeOptions = new ChromeOptions();
                    chromeOptions.addArguments("--disable-blink-features=AutomationControlled");
                    chromeOptions.addArguments("--disable-extensions");
                    chromeOptions.addArguments("--disable-dev-shm-usage");
                    chromeOptions.addArguments("--no-sandbox");
                    chromeOptions.addArguments("--remote-allow-origins=*");
                    chromeOptions.addArguments("--disable-background-timer-throttling");
                    chromeOptions.addArguments("--disable-renderer-backgrounding");

                    System.out.println("🚀 Creating ChromeDriver instance...");
                    driver = new ChromeDriver(chromeOptions);
                    System.out.println("✅ ChromeDriver created successfully");
                    break;

                case "firefox":
                    System.out.println("📦 Setting up GeckoDriver with WebDriverManager...");
                    WebDriverManager.firefoxdriver().setup();
                    System.out.println("✅ WebDriverManager setup completed");

                    FirefoxOptions firefoxOptions = new FirefoxOptions();
                    firefoxOptions.addArguments("--disable-blink-features=AutomationControlled");

                    System.out.println("🚀 Creating FirefoxDriver instance...");
                    driver = new FirefoxDriver(firefoxOptions);
                    System.out.println("✅ FirefoxDriver created successfully");
                    break;

                case "edge":
                    System.out.println("📦 Setting up EdgeDriver with WebDriverManager...");
                    WebDriverManager.edgedriver().setup();
                    System.out.println("✅ WebDriverManager setup completed");

                    EdgeOptions edgeOptions = new EdgeOptions();
                    edgeOptions.addArguments("--disable-blink-features=AutomationControlled");
                    edgeOptions.addArguments("--remote-allow-origins=*");

                    System.out.println("🚀 Creating EdgeDriver instance...");
                    driver = new EdgeDriver(edgeOptions);
                    System.out.println("✅ EdgeDriver created successfully");
                    break;

                default:
                    throw new IllegalArgumentException("❌ Browser not supported: " + browserName);
            }

            if (driver == null) {
                throw new RuntimeException("❌ Driver creation failed - driver is null");
            }

            System.out.println("📝 Setting driver in ThreadLocal...");
            driverThreadLocal.set(driver);

            // Verify driver is set correctly
            WebDriver verifyDriver = driverThreadLocal.get();
            if (verifyDriver == null) {
                throw new RuntimeException("❌ Failed to set driver in ThreadLocal");
            }

            System.out.println("✅ " + browserName + " driver initialized successfully");
            System.out.println("🔍 Driver class: " + driver.getClass().getName());
            System.out.println("🔍 ThreadLocal driver: " + verifyDriver.getClass().getName());

        } catch (Exception e) {
            System.err.println("❌ Driver initialization failed: " + e.getMessage());
            e.printStackTrace();

            // Cleanup on failure
            try {
                quitDriver();
            } catch (Exception cleanupError) {
                System.err.println("❌ Cleanup after failure also failed: " + cleanupError.getMessage());
            }

            throw new RuntimeException("Driver initialization failed: " + e.getMessage(), e);
        }
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
