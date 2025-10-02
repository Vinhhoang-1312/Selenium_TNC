package pages;

import org.openqa.selenium.By;

public class ProductDetailPage {
    
    public static By addtoCartButton = By.xpath("//a[contains(@class,'buy-go-cart')]");

<<<<<<< HEAD
    public static By itemnameInMainPage = By.xpath("//div[@id=\"js-product-cate-79\"]//div[@class=\"owl-item active\"][2]//a[contains(@class,'product-name')]");
=======
public class ProductDetailPage extends BasePage {
    private static final Logger log = LoggerFactory.getLogger(ProductDetailPage.class);
    public static By addToCartButton = By.xpath("//a[contains(text(),'Thêm vào giỏ hàng')]");
    private final By cartIcon = By.xpath("//a[@id='js-header-cart']");
    private final By viewCartLink = By.xpath("//a[@class='btn-goCart']");
    private final By loadingSpinner = By.xpath("//div[contains(@class, 'loading-spinner')]");
    private final Actions actions;
    public static By itemnameInMainPage = org.openqa.selenium.By.xpath("//div[@id=\"js-product-cate-79\"]//div[@class=\"owl-item active\"][2]//a[contains(@class,'product-name')]");
    public static By itemInMainPage = org.openqa.selenium.By.xpath("//div[contains(@id,'79')]//div[@class='owl-item active'][2]");
    public static By productName = org.openqa.selenium.By.xpath("//h1[@class='name']");
    public static By successNotification = By.xpath("//div[@class='content-container']");
    public static By originPrice = org.openqa.selenium.By.xpath("//div[@class='info-main-price']//del[@class='old-price']");
    public static By Price = org.openqa.selenium.By.xpath("//div[@class='info-main-price']//div[@class='price']");
    public static By SaleOff = org.openqa.selenium.By.xpath("//div[@class='info-main-price']//div[@class='saleoff']");
>>>>>>> 2b511788fd639212808e02d46f60d65effaea549

    public static By itemInMainPage = By.xpath("//div[contains(@id,'79')]//div[@class='owl-item active'][2]");

    public static By productName = By.xpath("//h1[@class='name']");

    public static By successNotification = By.xpath("//div[@class='content-container']");

    public static By originPrice = By.xpath("//div[@class='info-main-price']//del[@class='old-price']");
    
    public static By Price = By.xpath("//div[@class='info-main-price']//div[@class='price']");

    public static By SaleOff = By.xpath("//div[@class='info-main-price']//div[@class='saleoff']");
}
