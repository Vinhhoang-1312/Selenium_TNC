package pages;

import org.openqa.selenium.By;

public class ProductDetailPage {
    // Locator cho button "Thêm vào giỏ" sử dụng xpath
    public static By addtoCartButton = By.xpath("//a[contains(@class,'buy-go-cart')]");

    //localtor cho tên item trong trang chính để click vào xem chi tiết
    public static By itemnameInMainPage = By.xpath("//div[@id=\"js-product-cate-79\"]//div[@class=\"owl-item active\"][2]//a[contains(@class,'product-name')]");

    //locator để click item trong trang chính
    public static By itemInMainPage = By.xpath("//div[contains(@id,'79')]//div[@class='owl-item active'][2]");

    // tên sản phẩm ở trang chi tiết sản phẩm
    public static By productName = By.xpath("//h1[@class='name']");

    //giá gốc sản phẩm
    public static By originPrice = By.xpath("//div[@class='info-main-price']//del[@class='old-price']");
    
    //giá khuyến mãi sản phẩm
    public static By Price = By.xpath("//div[@class='info-main-price']//div[@class='price']");

    //saleoff
    public static By SaleOff = By.xpath("//div[@class='info-main-price']//div[@class='saleoff']");
}
