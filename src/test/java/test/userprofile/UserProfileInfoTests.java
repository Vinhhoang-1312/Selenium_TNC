//package test.userprofile;
//
//import org.openqa.selenium.By;
//import org.openqa.selenium.WebElement;
//import org.openqa.selenium.support.ui.ExpectedConditions;
//import org.openqa.selenium.support.ui.WebDriverWait;
//import org.testng.Assert;
//import org.testng.annotations.*;
//import pages.HomePage;
//import pages.UserProfilePage;
//import test.BaseTest;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import java.time.Duration;
//
//public class UserProfileInfoTests extends BaseTest {
//    private static final Logger log = LoggerFactory.getLogger(UserProfileInfoTests.class);
//    private final By fullnameInput = By.id("fullname");
//    private final By saveButton = By.cssSelector("button.btn-submit");
//    private final By emailInput = By.id("email");
//    private final By phoneInput = By.id("phone");
//    private final By provinceSelect = By.id("province");
//    private final By districtSelect = By.id("district");
//    private final By wardSelect = By.id("ward");
//    private final By addressInput = By.id("address");
//    private final By errorMessage = By.cssSelector(".error-message, .invalid-feedback, .form-error");
//
//
//    private void reloadPage() {
//        driver.navigate().refresh();
//        try {
//            Thread.sleep(1500);
//        } catch (InterruptedException e) {
//            Thread.currentThread().interrupt();
//        }
//    }
//
//    @Test(groups = "userprofile")
//    public void testUpdateFullname() {
//        clickIfPresent(By.xpath("//span[contains(text(),'Tài khoản')]"));
//        HomePage homePage = new HomePage(driver);
//        homePage.logIn();
//        homePage.goToProfilePage();
//        log.info("[TEST] Bắt đầu testUpdateFullname - chờ trường fullname xuất hiện...");
//        UserProfilePage profilePage = new UserProfilePage(driver);
//        try {
//            String oldValue = profilePage.getFullname();
//            String newValue = oldValue + "_Test";
//            profilePage.updateFullname(newValue);
//            reloadPage();
//            String after = profilePage.getFullname();
//            Assert.assertEquals(after, newValue);
//        } catch (Exception e) {
//            log.error("[TEST] Không tìm thấy trường fullname hoặc lỗi thao tác: {}", e.getMessage(), e);
//            throw e;
//        }
//    }
//
//    @Test(groups = "userprofile")
//    public void testUpdatePhoneSuccess() {
//        clickIfPresent(By.xpath("//span[contains(text(),'Tài khoản')]"));
//        HomePage homePage = new HomePage(driver);
//        homePage.logIn();
//        homePage.goToProfilePage();
//        UserProfilePage profilePage = new UserProfilePage(driver);
//        String newPhone = "0912345678";
//        profilePage.updatePhone(newPhone);
//        reloadPage();
//        String after = profilePage.getPhone();
//        Assert.assertEquals(after, newPhone);
//    }
//
//    @Test(groups = "userprofile")
//    public void testUpdateAddressSuccess() {
//        clickIfPresent(By.xpath("//span[contains(text(),'Tài khoản')]"));
//        HomePage homePage = new HomePage(driver);
//        homePage.logIn();
//        homePage.goToProfilePage();
//        UserProfilePage profilePage = new UserProfilePage(driver);
//        String newAddress = "123 Đường ABC, Phường XYZ";
//        profilePage.updateAddress(newAddress);
//        reloadPage();
//        String after = profilePage.getAddress();
//        Assert.assertEquals(after, newAddress);
//    }
//
//    @Test(groups = "userprofile")
//    public void testEmptyFullnameNotAllowed() {
//        clickIfPresent(By.xpath("//span[contains(text(),'Tài khoản')]"));
//        HomePage homePage = new HomePage(driver);
//        homePage.logIn();
//        homePage.goToProfilePage();
//        UserProfilePage profilePage = new UserProfilePage(driver);
//        String oldFullname = profilePage.getFullname();
//        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
//        WebElement fullname = wait.until(ExpectedConditions.visibilityOfElementLocated(fullnameInput));
//        fullname.clear();
//        driver.findElement(saveButton).click();
//        reloadPage();
//        String after = profilePage.getFullname();
//        Assert.assertFalse(after.isEmpty(), "Fullname bị để trống sau khi reload, đây là lỗi!");
//        Assert.assertEquals(after, oldFullname, "Fullname đã bị thay đổi thành rỗng, đây là lỗi!");
//    }
//
//    @Test(groups = "userprofile")
//    public void testInvalidPhoneNotAllowed() {
//        clickIfPresent(By.xpath("//span[contains(text(),'Tài khoản')]"));
//        HomePage homePage = new HomePage(driver);
//        homePage.logIn();
//        homePage.goToProfilePage();
//        UserProfilePage profilePage = new UserProfilePage(driver);
//        String oldPhone = profilePage.getPhone();
//        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
//        WebElement phone = wait.until(ExpectedConditions.visibilityOfElementLocated(phoneInput));
//        phone.clear();
//        String invalidPhone = "abc123";
//        phone.sendKeys(invalidPhone);
//        driver.findElement(saveButton).click();
//        reloadPage();
//        String after = profilePage.getPhone();
//        Assert.assertNotEquals(after, invalidPhone, "Số điện thoại sai định dạng đã được lưu, đây là lỗi!");
//        Assert.assertEquals(after, oldPhone, "Số điện thoại đã bị thay đổi thành giá trị sai, đây là lỗi!");
//    }
//
//    @Test(groups = "userprofile")
//    public void testEmailInvalidNotSaved() {
//        clickIfPresent(By.xpath("//span[contains(text(),'Tài khoản')]"));
//        HomePage homePage = new HomePage(driver);
//        homePage.logIn();
//        homePage.goToProfilePage();
//        UserProfilePage profilePage = new UserProfilePage(driver);
//        String oldEmail = profilePage.getProfileEmail();
//        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
//        WebElement email = wait.until(ExpectedConditions.visibilityOfElementLocated(emailInput));
//        email.clear();
//        String invalidEmail = "invalid@@@!!!.com";
//        email.sendKeys(invalidEmail);
//        driver.findElement(saveButton).click();
//        reloadPage();
//        String after = profilePage.getProfileEmail();
//        Assert.assertNotEquals(after, invalidEmail, "Email sai định dạng đã được lưu, đây là lỗi!");
//        Assert.assertEquals(after, oldEmail, "Email đã bị thay đổi thành giá trị sai, đây là lỗi!");
//    }
//
//    @Test(groups = "userprofile")
//    public void testSelectProvinceDistrictWard() {
//        clickIfPresent(By.xpath("//span[contains(text(),'Tài khoản')]"));
//        HomePage homePage = new HomePage(driver);
//        homePage.logIn();
//        homePage.goToProfilePage();
//        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
//        WebElement province = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("js-shipto-province")));
//        province.click();
//        province.findElement(By.xpath(".//option[contains(text(),'Đà Nẵng')]"))
//                .click();
//        WebElement district = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("js-district-holder")));
//        district.click();
//        district.findElement(By.xpath(".//option[contains(text(),'Quận Sơn Trà')]"))
//                .click();
//        WebElement ward = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("js-ward-holder")));
//        ward.click();
//        ward.findElement(By.xpath(".//option[contains(text(),'Phường An Hải Bắc')]"))
//                .click();
//        driver.findElement(saveButton).click();
//        reloadPage();
//        WebElement provinceAfter = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("js-shipto-province")));
//        String selectedProvince = provinceAfter.findElement(By.xpath(".//option[@selected]"))
//                .getText();
//        Assert.assertTrue(selectedProvince.contains("Đà Nẵng"), "Tỉnh/Thành phố không được lưu đúng!");
//    }
//
//    @Test(groups = "userprofile")
//    public void testProfileInfoPersistsAfterReload() {
//        clickIfPresent(By.xpath("//span[contains(text(),'Tài khoản')]"));
//        HomePage homePage = new HomePage(driver);
//        homePage.logIn();
//        homePage.goToProfilePage();
//        UserProfilePage profilePage = new UserProfilePage(driver);
//        String newFullname = "John Doe_Test_Test";
//        String newPhone = "0987654321";
//        String newAddress = "456 Đường DEF, Phường UVW";
//        profilePage.updateFullname(newFullname);
//        profilePage.updatePhone(newPhone);
//        profilePage.updateAddress(newAddress);
//        driver.findElement(saveButton).click();
//        reloadPage();
//        Assert.assertEquals(profilePage.getFullname(), newFullname);
//        Assert.assertEquals(profilePage.getPhone(), newPhone);
//        Assert.assertEquals(profilePage.getAddress(), newAddress);
//    }
//
//    @Test(groups = "userprofile")
//    public void testUpdatePhoneWithValidValue() {
//        clickIfPresent(By.xpath("//span[contains(text(),'Tài khoản')]"));
//        HomePage homePage = new HomePage(driver);
//        homePage.logIn();
//        homePage.goToProfilePage();
//        UserProfilePage profilePage = new UserProfilePage(driver);
//        String validPhone = "0912345678";
//        profilePage.updatePhone(validPhone);
//        reloadPage();
//        String after = profilePage.getPhone();
//        Assert.assertEquals(after, validPhone, "Số điện thoại hợp lệ không được lưu đúng!");
//    }
//
//}
