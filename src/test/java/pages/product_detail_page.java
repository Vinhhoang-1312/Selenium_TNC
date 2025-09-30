package pages;

import org.openqa.selenium.By;

public class product_detail_page {
    // Locator cho button "Thêm vào giỏ" sử dụng xpath
    public static By ADDTO_CARTBUTTON = By.xpath("//a[contains(@class,'buy-go-cart')]");

    //localtor cho tên item trong trang chính để click vào xem chi tiết
    public static By ITEMNAME_IN_MAINPAGE = By.xpath("//div[@id=\"js-product-cate-79\"]//div[@class=\"owl-item active\"][2]//a[contains(@class,'product-name')]");

    //locator để click item trong trang chính
    public static By ITEM_IN_MAINPAGE = By.xpath("//div[contains(@id,'79')]//div[@class='owl-item active'][2]");

    // tên sản phẩm ở trang chi tiết sản phẩm
    public static By PRODUCT_NAME = By.xpath("//h1[@class='name']");
}
