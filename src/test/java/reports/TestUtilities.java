package reports;

import core.BaseTest;
import io.qameta.allure.Allure;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;

import java.io.ByteArrayInputStream;

public class TestUtilities {

    public static void captureScreenshotOnFailure(ITestResult result, String methodName) {
        Object instance = result.getInstance();
        if (instance instanceof BaseTest baseTest) {
            WebDriver driver = baseTest.getDriver();
            if (driver != null) {
                try {
                    byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
                    Allure.addAttachment("Screenshot - " + methodName, "image/png",
                            new ByteArrayInputStream(screenshot), ".png");
                } catch (Exception e) {
                    BaseTest.logger.warn("Failed to capture screenshot: {}", e.getMessage());
                }
            }
        }
    }
}

