package pages;

import org.openqa.selenium.By;

public class search_page {
    // Locator cho ô search sử dụng xpath
    public static By SEARCH_INPUT = By.xpath("//input[@id='js-global-seach']");

    // Locator cho h2 có nội dung 'Ôi' sử dụng xpath
    public static By NO_PRODUCT_NOTI = By.xpath("//h2[contains(text(),'Ôi')]");

    // Locator cho bảng hiện thị những item được gợi ý sử dụng xpath
    public static By SUGGESTION_LIST = By.xpath("//div[@class='content-suggestions']");
}
