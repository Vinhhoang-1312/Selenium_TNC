package pages;

import org.openqa.selenium.By;

public class SearchPage {

    public static By searchInput = By.xpath("//input[@id='js-global-seach']");

    public static By searchButton = By.xpath("//button[@class='submit-search']");

    public static By noproductNoti = By.xpath("//h2[contains(text(),'Ôi')]");

    public static By suggestionList = By.xpath("//div[@class='content-suggestions']");
}
