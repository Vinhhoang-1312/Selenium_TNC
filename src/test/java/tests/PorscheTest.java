package tests;
import org.testng.Assert;
import org.testng.annotations.Test;
import base.BaseTest;

public class PorscheTest extends BaseTest {
    @Test
    public void testPorscheHomePage() {
        driver.get("https://porsche-vietnam.vn/");
        String title = driver.getTitle();
        Assert.assertTrue(title.contains("Porsche"), "Trang không có tiêu đề đúng");
    }
}
