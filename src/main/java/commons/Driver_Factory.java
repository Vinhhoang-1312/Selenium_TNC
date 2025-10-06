package commons;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

public class Driver_Factory {
    private static WebDriver driver;

    public static WebDriver getDriver() {
        if (driver == null) {
            try {
                driver = new EdgeDriver();
            } catch (Exception e1) {
                System.out.println("Không thể khởi tạo EdgeDriver, thử ChromeDriver...");
                try {
                    driver = new ChromeDriver();
                } catch (Exception e2) {
                    System.out.println("Không thể khởi tạo ChromeDriver, thử FirefoxDriver...");
                    try {
                        driver = new FirefoxDriver();
                    } catch (Exception e3) {
                        System.out.println("Không thể khởi tạo bất kỳ trình điều khiển nào.");
                        throw new RuntimeException("Không thể khởi tạo WebDriver", e3);
                    }
                }
            }
            driver.manage().window().maximize();
        }
        return driver;
    }

    public static void quitDriver() {
        if (driver != null) {
            driver.quit();
            driver = null;
        }
    }
}
