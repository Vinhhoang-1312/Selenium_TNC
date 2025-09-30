package commons;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.edge.EdgeDriver;

public class Driver_Factory {
    // Biến static để lưu WebDriver dùng chung
    private static WebDriver driver;

    // Hàm trả về WebDriver hiện tại hoặc khởi tạo mới nếu cần
    public static WebDriver getDriver() {
        // Nếu driver chưa được khởi tạo hoặc đã bị đóng session thì khởi tạo lại
        if (driver == null || isSessionClosed(driver)) {
            driver = new EdgeDriver(); // Khởi tạo trình duyệt Edge mới
        }
        return driver; // Trả về driver hiện tại
    }

    // Hàm kiểm tra xem session của driver có còn hoạt động không
    private static boolean isSessionClosed(WebDriver driver) {
        try {
            driver.getCurrentUrl(); // Gọi lệnh đơn giản để kiểm tra session
            return false; // Nếu không lỗi, session vẫn còn
        } catch (Exception e) {
            return true; // Nếu lỗi, session đã bị đóng
        }
    }

    // Hàm đóng driver và reset về null để lần sau có thể khởi tạo lại
    public static void quitDriver() {
        if (driver != null) {
            driver.quit(); // Đóng trình duyệt và kết thúc session
            driver = null; // Reset biến driver để tránh dùng lại session cũ
        }
    }
}

