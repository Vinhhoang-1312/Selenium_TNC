package commons;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import java.io.IOException;
import java.time.Duration;


public class DriverFactory {

    private static final ThreadLocal<WebDriver> driverThreadLocal = new ThreadLocal<>();


    public static void initializeDriver(String browserName) {
        if (isDriverInitialized()) {
            quitDriver(); // cleanup old driver
        }

        WebDriver driver;
        browserName = browserName == null ? "chrome" : browserName.toLowerCase();

        switch (browserName) {
            case "chrome":
                WebDriverManager.chromedriver().setup();
                ChromeOptions chromeOptions = new ChromeOptions();
                chromeOptions.addArguments("--disable-extensions", "--disable-dev-shm-usage",
                        "--no-sandbox", "--remote-allow-origins=*");
                driver = new ChromeDriver(chromeOptions);
                break;

            case "firefox":
                WebDriverManager.firefoxdriver().setup();
                FirefoxOptions firefoxOptions = new FirefoxOptions();
                driver = new FirefoxDriver(firefoxOptions);
                break;

            case "edge":
                WebDriverManager.edgedriver().setup();
                EdgeOptions edgeOptions = new EdgeOptions();
                edgeOptions.addArguments("--remote-allow-origins=*");
                driver = new EdgeDriver(edgeOptions);
                break;

            default:
                throw new IllegalArgumentException("❌ Unsupported browser: " + browserName);
        }

        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

        driverThreadLocal.set(driver);
        System.out.println("✅ " + browserName + " driver initialized.");
    }


    public static WebDriver getDriver() {
        WebDriver driver = driverThreadLocal.get();
        if (driver == null) {
//            throw new IllegalStateException("❌ WebDriver is not initialized. Call initializeDriver() first.");
            driver = new EdgeDriver();
        }
        return driver;
    }


    public static void quitDriver() {
        WebDriver driver = driverThreadLocal.get();
        if (driver != null) {
            try {
                driver.quit();
                System.out.println("✅ WebDriver quit successfully.");
            } catch (Exception e) {
                System.err.println("⚠️ Error quitting driver: " + e.getMessage());
                forceKillBrowser();
            } finally {
                driverThreadLocal.remove();
            }
        }
    }


    public static boolean isDriverInitialized() {
        return driverThreadLocal.get() != null;
    }


    private static void forceKillBrowser() {
        String os = System.getProperty("os.name").toLowerCase();
        try {
            if (os.contains("windows")) {
                Runtime.getRuntime().exec("taskkill /F /IM chrome.exe /T");
                Runtime.getRuntime().exec("taskkill /F /IM chromedriver.exe /T");
                Runtime.getRuntime().exec("taskkill /F /IM msedge.exe /T");
                Runtime.getRuntime().exec("taskkill /F /IM msedgedriver.exe /T");
                Runtime.getRuntime().exec("taskkill /F /IM firefox.exe /T");
                Runtime.getRuntime().exec("taskkill /F /IM geckodriver.exe /T");
            } else if (os.contains("linux") || os.contains("mac")) {
                Runtime.getRuntime().exec("pkill -f chrome");
                Runtime.getRuntime().exec("pkill -f chromedriver");
                Runtime.getRuntime().exec("pkill -f firefox");
                Runtime.getRuntime().exec("pkill -f geckodriver");
                Runtime.getRuntime().exec("pkill -f msedge");
                Runtime.getRuntime().exec("pkill -f msedgedriver");
            }
            System.out.println("🔧 Force killed browser processes.");
        } catch (IOException e) {
            System.err.println("⚠️ Failed to force kill browser: " + e.getMessage());
        }
    }


    public static void forceCleanup() {
        quitDriver();
        forceKillBrowser();
    }
}