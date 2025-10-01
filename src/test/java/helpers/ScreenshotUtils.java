package helpers;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.io.FileHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ScreenshotUtils {

    private static final String SCREENSHOT_DIR = "target/screenshots/";
    private static final Logger log = LoggerFactory.getLogger(ScreenshotUtils.class);

    static {
        // Create screenshots directory if it doesn't exist
        createScreenshotDirectory();
    }

    private static void createScreenshotDirectory() {
        File directory = new File(SCREENSHOT_DIR);
        if (!directory.exists()) {
            directory.mkdirs();
        }
    }

    public static String captureScreenshot(WebDriver driver, String testName) {
        try {
            TakesScreenshot screenshot = (TakesScreenshot) driver;
            File sourceFile = screenshot.getScreenshotAs(OutputType.FILE);

            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            String fileName = testName + "_" + timestamp + ".png";
            String filePath = SCREENSHOT_DIR + fileName;

            File destFile = new File(filePath);
            FileHandler.copy(sourceFile, destFile);

            log.info("Screenshot captured: {}", filePath);
            return filePath;

        } catch (Exception e) {
            log.error("Failed to capture screenshot: {}", e.getMessage());
            return null;
        }
    }

    public static String captureScreenshotOnFailure(WebDriver driver, String testName) {
        String filePath = captureScreenshot(driver, testName + "_FAILED");
        if (filePath != null) {
            log.info("Failure screenshot saved: {}", filePath);
        }
        return filePath;
    }

    public static String captureFullPageScreenshot(WebDriver driver, String testName) {
        // This method can be enhanced to capture full page screenshots
        // For now, it uses the standard screenshot method
        return captureScreenshot(driver, testName + "_FullPage");
    }
}
