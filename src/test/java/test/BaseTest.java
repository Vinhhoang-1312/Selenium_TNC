package test;

import commons.DriverFactory2;
import helpers.PopupHandler;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import pages.CartPage;
import pages.HomePage;
import pages.ProductDetailPage;

public class BaseTest {
    private static final Logger log = LoggerFactory.getLogger(BaseTest.class);
    protected WebDriver driver;
    protected HomePage homePage;
    protected ProductDetailPage productDetailPage;
    protected CartPage cartPage;
    protected PopupHandler popupHandler;

    @BeforeClass
    public void setDriver() {
        driver = DriverFactory2.getDriver();
        driver.get("https://www.tncstore.vn/");
    }

    @BeforeMethod
    public void setupPages() {
        homePage = new pages.HomePage(driver);
        productDetailPage = new pages.ProductDetailPage(driver);
        cartPage = new pages.CartPage(driver);
        popupHandler = new PopupHandler(driver);
    }

    @AfterClass
    public void tearDown() {
        DriverFactory2.quitDriver();
    }
}
