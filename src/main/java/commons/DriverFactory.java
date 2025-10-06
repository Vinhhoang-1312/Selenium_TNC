package commons;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

public class DriverFactory {
    private static final ThreadLocal<WebDriver> driverThreadLocal = new ThreadLocal<>();

    public static WebDriver getDriver() {
        if (driverThreadLocal.get() == null) {
            WebDriver driver = initDriver();
            driverThreadLocal.set(driver);
        }
        return driverThreadLocal.get();
    }

    private static WebDriver initDriver() {
        WebDriver driver;
        try {
            EdgeOptions options = new EdgeOptions();
            driver = new EdgeDriver(options);
            System.out.println("✅ EdgeDriver khởi tạo thành công!");
        } catch (Exception e1) {
            System.out.println("⚠️ Không thể khởi tạo EdgeDriver, thử ChromeDriver...");
            try {
                ChromeOptions options = new ChromeOptions();
                driver = new ChromeDriver(options);
                System.out.println("✅ ChromeDriver khởi tạo thành công!");
            } catch (Exception e2) {
                System.out.println("⚠️ Không thể khởi tạo ChromeDriver, thử FirefoxDriver...");
                try {
                    FirefoxOptions options = new FirefoxOptions();
                    driver = new FirefoxDriver(options);
                    System.out.println("✅ FirefoxDriver khởi tạo thành công!");
                } catch (Exception e3) {
                    throw new RuntimeException("❌ Không thể khởi tạo bất kỳ trình điều khiển nào.", e3);
                }
            }
        }
        driver.manage().window().maximize();
        return driver;
    }

    public static void quitDriver() {
        WebDriver driver = driverThreadLocal.get();
        if (driver != null) {
            driver.quit();
            driverThreadLocal.remove();
            System.out.println("🧹 Đã quit và dọn dẹp driver cho thread: " + Thread.currentThread().getId());
        }
    }
}
