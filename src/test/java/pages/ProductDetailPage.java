package pages;

import org.openqa.selenium.By;

public class ProductDetailPage {
    
    public static By addtoCartButton = By.xpath("//a[contains(@class,'buy-go-cart')]");

    public static By itemnameInMainPage = By.xpath("//div[@id=\"js-product-cate-79\"]//div[@class=\"owl-item active\"][2]//a[contains(@class,'product-name')]");

    public static By itemInMainPage = By.xpath("//div[contains(@id,'79')]//div[@class='owl-item active'][2]");

    public static By productName = By.xpath("//h1[@class='name']");

    public static By successNotification = By.xpath("//div[@class='content-container']");

    public static By originPrice = By.xpath("//div[@class='info-main-price']//del[@class='old-price']");
    
    public static By Price = By.xpath("//div[@class='info-main-price']//div[@class='price']");

    public static By SaleOff = By.xpath("//div[@class='info-main-price']//div[@class='saleoff']");
}
