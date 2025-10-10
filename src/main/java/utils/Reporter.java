package utils;

import io.qameta.allure.Allure;

/**
 * Reporter class — ghi log ra Allure Report
 * Dùng: Reporter.LogToReport("Step description");
 */
public class Reporter {

    /**
     * Ghi step vào Allure report
     * @param message nội dung log
     */
    public static void LogToReport(String message) {
        Allure.step(message);
    }

    /**
     * Log hành động test (ví dụ: click, search,...)
     */
    public static void Action(String message) {
        Allure.step(message);
    }

    /**
     * Log lỗi
     */
    public static void Error(String message) {
        Allure.step(message);
    }

    /**
     * Log cảnh báo
     */
    public static void Warn(String message) {
        Allure.step( message);
    }

    /**
     * Log thành công
     */
    public static void Success(String message) {
        Allure.step(message);
    }
}
