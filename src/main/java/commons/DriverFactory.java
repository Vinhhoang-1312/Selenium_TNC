package commons;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DriverFactory {
    private static final Logger log = LoggerFactory.getLogger(DriverFactory.class);
    private static final ThreadLocal<WebDriver> driverThreadLocal = new ThreadLocal<>();

    public static WebDriver getDriver() {
        WebDriver driver = driverThreadLocal.get();
        if (driver == null) {
            try {
                driver = new EdgeDriver();
            } catch (Exception e1) {
                log.warn("Không thể khởi tạo EdgeDriver, thử ChromeDriver...", e1);
                try {
                    driver = new ChromeDriver();
                } catch (Exception e2) {
                    log.warn("Không thể khởi tạo ChromeDriver, thử FirefoxDriver...", e2);
                    try {
                        driver = new FirefoxDriver();
                    } catch (Exception e3) {
                        log.error("Không thể khởi tạo bất kỳ trình điều khiển nào.", e3);
                        throw new RuntimeException("Không thể khởi tạo WebDriver", e3);
                    }
                }
            }
            driver.manage().window().maximize();
            driverThreadLocal.set(driver);
        }
        return driver;
    }

    public static void quitDriver() {

            WebDriver driver = driverThreadLocal.get();
            if (driver != null) {
                try {
                    driver.quit();
                } catch (Exception e) {
                    log.error("Error quitting driver: {}", e.getMessage(), e);
                } finally {
                    driverThreadLocal.remove();
                }
            }
    }
}
