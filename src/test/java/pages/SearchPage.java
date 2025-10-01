package pages;

import org.openqa.selenium.By;

public class SearchPage {
    // Locator cho ô search sử dụng xpath
    public static By searchInput = By.xpath("//input[@id='js-global-seach']");

    // Locator cho h2 có nội dung 'Ôi' sử dụng xpath
    public static By noproductNoti = By.xpath("//h2[contains(text(),'Ôi')]");

    // Locator cho bảng hiện thị những item được gợi ý sử dụng xpath
    public static By suggestionList = By.xpath("//div[@class='content-suggestions']");
}
