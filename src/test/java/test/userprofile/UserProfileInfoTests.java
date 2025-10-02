package test.userprofile;

import helpers.BaseTest;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;

public class UserProfileInfoTests extends BaseTest {
    private final By fullnameInput = By.id("fullname");
    private final By emailInput = By.id("email");
    private final By mobileInput = By.id("mobile");
    private final By provinceSelect = By.id("js-shipto-province");
    private final By districtSelect = By.id("js-shipto-district");
    private final By wardSelect = By.id("js-shipto-ward");
    private final By addressInput = By.id("address");
    private final By saveButton = By.cssSelector("button.btn-submit");

    private void reloadPage() {
        driver.navigate().refresh();
        try { Thread.sleep(1500); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
    }

    @Test
    public void testUpdateFullname() {
        WebElement fullname = driver.findElement(fullnameInput);
        String oldValue = fullname.getAttribute("value");
        String newValue = oldValue + "_Test";
        fullname.clear();
        fullname.sendKeys(newValue);
        driver.findElement(saveButton).click();
        reloadPage();
        String after = driver.findElement(fullnameInput).getAttribute("value");
        Assert.assertEquals(after, newValue);
        // Test clear
        fullname = driver.findElement(fullnameInput);
        fullname.clear();
        driver.findElement(saveButton).click();
        reloadPage();
        String afterClear = driver.findElement(fullnameInput).getAttribute("value");
        Assert.assertEquals(afterClear, newValue);
    }

    @Test
    public void testUpdateEmail() {
        WebElement email = driver.findElement(emailInput);
        String oldValue = email.getAttribute("value");
        String newValue = "test" + System.currentTimeMillis() + "@mail.com";
        email.clear();
        email.sendKeys(newValue);
        driver.findElement(saveButton).click();
        reloadPage();
        String after = driver.findElement(emailInput).getAttribute("value");
        Assert.assertEquals(after, newValue);
        // Test clear
        email = driver.findElement(emailInput);
        email.clear();
        driver.findElement(saveButton).click();
        reloadPage();
        String afterClear = driver.findElement(emailInput).getAttribute("value");
        Assert.assertEquals(afterClear, newValue);
    }

    @Test
    public void testUpdateMobile() {
        WebElement mobile = driver.findElement(mobileInput);
        String oldValue = mobile.getAttribute("value");
        String newValue = "09" + (int)(Math.random()*100000000);
        mobile.clear();
        mobile.sendKeys(newValue);
        driver.findElement(saveButton).click();
        reloadPage();
        String after = driver.findElement(mobileInput).getAttribute("value");
        Assert.assertEquals(after, newValue);
        // Test clear
        mobile = driver.findElement(mobileInput);
        mobile.clear();
        driver.findElement(saveButton).click();
        reloadPage();
        String afterClear = driver.findElement(mobileInput).getAttribute("value");
        Assert.assertEquals(afterClear, newValue);
    }

    @Test
    public void testUpdateAddress() {
        WebElement address = driver.findElement(addressInput);
        String oldValue = address.getAttribute("value");
        String newValue = oldValue + "_Test";
        address.clear();
        address.sendKeys(newValue);
        driver.findElement(saveButton).click();
        reloadPage();
        String after = driver.findElement(addressInput).getAttribute("value");
        Assert.assertEquals(after, newValue);
        // Test clear
        address = driver.findElement(addressInput);
        address.clear();
        driver.findElement(saveButton).click();
        reloadPage();
        String afterClear = driver.findElement(addressInput).getAttribute("value");
        Assert.assertEquals(afterClear, newValue);
    }

    // Province, district, ward: only test change, not clear (usually select cannot be empty)
    @Test
    public void testUpdateProvince() {
        WebElement province = driver.findElement(provinceSelect);
        String oldValue = province.getAttribute("value");
        String newValue = "1".equals(oldValue) ? "2" : "1";
        province.sendKeys(newValue);
        driver.findElement(saveButton).click();
        reloadPage();
        String after = driver.findElement(provinceSelect).getAttribute("value");
        Assert.assertEquals(after, newValue);
    }

    @Test
    public void testUpdateDistrict() {
        WebElement district = driver.findElement(districtSelect);
        String oldValue = district.getAttribute("value");
        String newValue = "1".equals(oldValue) ? "2" : "1";
        district.sendKeys(newValue);
        driver.findElement(saveButton).click();
        reloadPage();
        String after = driver.findElement(districtSelect).getAttribute("value");
        Assert.assertEquals(after, newValue);
    }

    @Test
    public void testUpdateWard() {
        WebElement ward = driver.findElement(wardSelect);
        String oldValue = ward.getAttribute("value");
        String newValue = "1".equals(oldValue) ? "2" : "1";
        ward.sendKeys(newValue);
        driver.findElement(saveButton).click();
        reloadPage();
        String after = driver.findElement(wardSelect).getAttribute("value");
        Assert.assertEquals(after, newValue);
    }
}

