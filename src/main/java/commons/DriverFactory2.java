package commons;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.util.concurrent.Semaphore;

public class DriverFactory2 {
    private static final ThreadLocal<WebDriver> driver = new ThreadLocal<>();

    // Giới hạn số lượng trình duyệt
    private static final Semaphore edgeSemaphore = new Semaphore(1);    // Tối đa 1 Edge
    private static final Semaphore chromeSemaphore = new Semaphore(1);  // Tối đa 1 Chrome
    private static final Semaphore firefoxSemaphore = new Semaphore(1); // Tối đa 1 Firefox

    public static WebDriver getDriver() {
        if (driver.get() == null) {
            String requestedBrowser = System.getProperty("browser", "edge").toLowerCase();
            String actualBrowser = requestedBrowser;

            try {
                boolean acquired = false;

                // Thử Edge
                if (actualBrowser.equals("edge")) {
                    acquired = edgeSemaphore.tryAcquire();
                    if (!acquired) {
                        System.out.println("⚠️ Thread " + Thread.currentThread().getId() + ": Edge bị chiếm, chuyển sang Chrome...");
                        actualBrowser = "chrome";
                    }
                }

                // Thử Chrome
                if (actualBrowser.equals("chrome")) {
                    acquired = chromeSemaphore.tryAcquire();
                    if (!acquired) {
                        System.out.println("⚠️ Thread " + Thread.currentThread().getId() + ": Chrome bị chiếm, chuyển sang Firefox...");
                        actualBrowser = "firefox";
                    }
                }

                // Thử Firefox
                if (actualBrowser.equals("firefox")) {
                    acquired = firefoxSemaphore.tryAcquire();
                    if (!acquired) {
                        System.out.println("❌ Thread " + Thread.currentThread().getId() + ": Firefox cũng bị chiếm, không còn trình duyệt khả dụng.");
                        throw new RuntimeException("🔥 Tất cả trình duyệt đều đang bị chiếm!");
                    }
                }

                // Khởi tạo trình duyệt
                switch (actualBrowser) {
                    case "edge":
                        driver.set(new EdgeDriver());
                        System.out.println("🚀 Thread " + Thread.currentThread().getId() + ": Khởi tạo Edge");
                        break;
                    case "chrome":
                        try {
                            driver.set(new ChromeDriver());
                            System.out.println("🚀 Thread " + Thread.currentThread().getId() + ": Khởi tạo Chrome");
                        } catch (Exception e) {
                            chromeSemaphore.release();
                            throw new RuntimeException("❌ Không thể khởi tạo Chrome", e);
                        }
                        break;
                    case "firefox":
                        try {
                            driver.set(new FirefoxDriver());
                            System.out.println("🚀 Thread " + Thread.currentThread().getId() + ": Khởi tạo Firefox");
                        } catch (Exception e) {
                            firefoxSemaphore.release();
                            throw new RuntimeException("❌ Không thể khởi tạo Firefox", e);
                        }
                        break;
                }

                driver.get().manage().window().maximize();

            } catch (Exception e) {
                throw new RuntimeException("🔥 Thread " + Thread.currentThread().getId() + ": Lỗi khởi tạo trình duyệt", e);
            }
        }

        return driver.get();
    }

    public static void quitDriver() {
        if (driver.get() != null) {
            WebDriver currentDriver = driver.get();

            if (currentDriver instanceof EdgeDriver) {
                edgeSemaphore.release();
                System.out.println("✅ Thread " + Thread.currentThread().getId() + ": Đóng Edge");
            } else if (currentDriver instanceof ChromeDriver) {
                chromeSemaphore.release();
                System.out.println("✅ Thread " + Thread.currentThread().getId() + ": Đóng Chrome");
            } else if (currentDriver instanceof FirefoxDriver) {
                firefoxSemaphore.release();
                System.out.println("✅ Thread " + Thread.currentThread().getId() + ": Đóng Firefox");
            }

            currentDriver.quit();
            driver.remove();
        }
    }
}
