package utils;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class JSUtils {

    private WebDriver driver;

    public JSUtils(WebDriver driver) {
        this.driver = driver;
    }

    /**
     * Cuộn đến phần tử (canh giữa màn hình)
     */
    public void scrollToElement(WebElement element) {
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", element);
    }

    /**
     * Cuộn đến cuối trang
     */
    public void scrollToBottom() {
        ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight);");
    }

    /**
     * Click phần tử bằng JavaScript (phòng khi Selenium click lỗi)
     */
    public void clickElementByJS(WebElement element) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
    }

    /**
     * Highlight phần tử (debug hoặc Allure report screenshot)
     */
    public void highlightElement(WebElement element) {
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].style.border='2px solid red'; arguments[0].style.backgroundColor='yellow';", element);
    }

    /**
     * Lấy text qua JS (tránh lỗi stale hoặc element hidden)
     */
    public String getInnerText(WebElement element) {
        return (String) ((JavascriptExecutor) driver).executeScript("return arguments[0].innerText;", element);
    }

    /**
     * Cuộn lên đầu trang
     */
    public void scrollToTop() {
        ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, 0);");
    }
}
