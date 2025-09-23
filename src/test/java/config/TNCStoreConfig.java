package config;

/**
 * Configuration constants for TNC Store website (https://www.tncstore.vn/)
 * Contains URLs, timeouts, and other configuration variables
 */
public class TNCStoreConfig {

    // ========== BASE URLs ==========
    public static final String BASE_URL = "https://www.tncstore.vn/";
    public static final String LOGIN_URL = "https://www.tncstore.vn/account/login";
    public static final String REGISTER_URL = "https://www.tncstore.vn/account/register";
    public static final String PROFILE_URL = "https://www.tncstore.vn/account/profile";
    public static final String CART_URL = "https://www.tncstore.vn/cart";
    public static final String CHECKOUT_URL = "https://www.tncstore.vn/checkout";
    public static final String SEARCH_URL = "https://www.tncstore.vn/search";

    // ========== API Endpoints ==========
    public static final String API_LOGIN = "/api/auth/login";
    public static final String API_REGISTER = "/api/auth/register";
    public static final String API_FORGOT_PASSWORD = "/api/auth/forgot-password";
    public static final String API_PROFILE = "/api/user/profile";
    public static final String API_CHANGE_PASSWORD = "/api/user/change-password";

    // ========== TIMEOUTS (in seconds) ==========
    public static final int IMPLICIT_WAIT = 15;
    public static final int EXPLICIT_WAIT = 30;
    public static final int PAGE_LOAD_TIMEOUT = 60;
    public static final int SCRIPT_TIMEOUT = 30;
    public static final int ELEMENT_WAIT = 10;
    public static final int SHORT_WAIT = 5;
    public static final int LONG_WAIT = 60;

    // ========== TEST DATA ==========
    public static final String DEFAULT_TEST_EMAIL = "john@test.com";
    public static final String DEFAULT_TEST_PASSWORD = "Abc12345";
    public static final String INVALID_EMAIL = "invalid.email.format";
    public static final String WEAK_PASSWORD = "123";
    public static final String STRONG_PASSWORD = "StrongPassword123!";

    // ========== BROWSER SETTINGS ==========
    public static final String DEFAULT_BROWSER = "chrome";
    public static final boolean HEADLESS_MODE = false;
    public static final String BROWSER_WINDOW_SIZE = "1920,1080";
    public static final boolean MAXIMIZE_WINDOW = true;

    // ========== REPORTING ==========
    public static final String REPORT_PATH = "target/reports/";
    public static final String SCREENSHOT_PATH = "target/screenshots/";
    public static final String EXTENT_REPORT_NAME = "TNC_Store_Test_Report.html";
    public static final String EXTENT_REPORT_TITLE = "TNC Store Automation Test Report";

    // ========== RETRY SETTINGS ==========
    public static final int MAX_RETRY_COUNT = 3;
    public static final int RETRY_DELAY_MS = 2000;

    // ========== ENVIRONMENT SETTINGS ==========
    public static final String TEST_ENVIRONMENT = "QA";
    public static final String STAGING_URL = "https://staging.tncstore.vn/";
    public static final String PRODUCTION_URL = "https://www.tncstore.vn/";

    // ========== VALIDATION MESSAGES ==========
    public static final String LOGIN_SUCCESS_MESSAGE = "Đăng nhập thành công";
    public static final String REGISTER_SUCCESS_MESSAGE = "Đăng ký thành công";
    public static final String PROFILE_UPDATE_SUCCESS = "Cập nhật thông tin thành công";
    public static final String PASSWORD_CHANGE_SUCCESS = "Đổi mật khẩu thành công";
    public static final String EMAIL_EXISTS_MESSAGE = "Email đã được sử dụng";
    public static final String INVALID_CREDENTIALS_MESSAGE = "Thông tin đăng nhập không đúng";

    // ========== COMMON TEST DATA ==========
    public static final String[] VALID_NAMES = {
        "Nguyen Van A", "Tran Thi B", "Le Van C", "Pham Thi D"
    };

    public static final String[] VALID_EMAILS = {
        "user1@test.com", "user2@test.com", "user3@test.com"
    };

    public static final String[] INVALID_EMAILS = {
        "invalid.email", "@test.com", "user@", "user space@test.com"
    };

    public static final String[] WEAK_PASSWORDS = {
        "123", "abc", "password", "12345678"
    };

    public static final String[] STRONG_PASSWORDS = {
        "StrongPass123!", "SecurePassword456#", "ComplexPass789$"
    };

    // ========== PRODUCT CATEGORIES ==========
    public static final String CATEGORY_ELECTRONICS = "electronics";
    public static final String CATEGORY_CLOTHING = "clothing";
    public static final String CATEGORY_BOOKS = "books";
    public static final String CATEGORY_HOME = "home";

    // ========== PAYMENT METHODS ==========
    public static final String PAYMENT_COD = "cod";
    public static final String PAYMENT_BANKING = "banking";
    public static final String PAYMENT_MOMO = "momo";
    public static final String PAYMENT_VNPAY = "vnpay";
}
