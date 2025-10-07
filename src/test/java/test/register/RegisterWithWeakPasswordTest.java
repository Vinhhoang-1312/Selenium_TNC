package test.register;

import pages.AuthenticationPage;
import data.AuthenticationTestData;
import test.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RegisterWithWeakPasswordTest extends BaseTest {
    private static final Logger log = LoggerFactory.getLogger(RegisterWithWeakPasswordTest.class);

    @Test(groups = {"authentication", "negative", "signup"},
            description = "AUTH-SU-04: Register with weak password")
    public void testRegisterWithWeakPassword() {
        try {
            AuthenticationPage authPage = new AuthenticationPage(driver);
            boolean foundError = authPage.registerAndCheckPopupError(
                    AuthenticationTestData.VALID_NAME,
                    AuthenticationTestData.VALID_EMAIL_2,
                    AuthenticationTestData.WEAK_PASSWORD_1,
                    "Mật khẩu có tối thiểu 6 ký tự"
            );
            if (!foundError) {
                foundError = authPage.registerAndCheckConsoleError(
                        AuthenticationTestData.VALID_NAME,
                        AuthenticationTestData.VALID_EMAIL_2,
                        AuthenticationTestData.WEAK_PASSWORD_1,
                        "error"
                );
            }
            Assert.assertTrue(foundError, "Phải hiển thị hoặc log lỗi khi đăng ký với mật khẩu yếu");
        } catch (Exception e) {
            log.error("Register with weak password test failed: ", e);
            throw e;
        }
    }
}
