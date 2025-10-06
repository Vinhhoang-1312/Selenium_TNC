package test.productdetail;

import commons.DriverFactory;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pages.BasePage;
import pages.ProductDetailPage;
public class CorrectProductNameTest {
    private static final Logger log = LoggerFactory.getLogger(CorrectProductNameTest.class);
    private WebDriver driver;
    private BasePage base;
    private String baseUrl = "https://www.tncstore.vn/";

    @BeforeClass
    public void setUp() {
        driver = DriverFactory.getDriver();
        base = new BasePage(driver);
        driver.get(baseUrl);
    }

    @Test
    public void testProductNameCorrect() {
        ProductDetailPage.verifyProductNameConsistency(driver, base, baseUrl);
    }


    @AfterClass
    public void tearDown() {
        DriverFactory.quitDriver();
    }
}
