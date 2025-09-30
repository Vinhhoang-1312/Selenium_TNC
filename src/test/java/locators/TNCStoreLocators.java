package locators;

/**
 * XPath locators for TNC Store website (https://www.tncstore.vn/)
 * Organized by page/functionality
 * Based on actual website flow analysis - UPDATED 2024
 */
public class TNCStoreLocators {

    // ========== NAVIGATION & GENERAL ==========
    // Account button - click to open login popup
    public static final String ACCOUNT_BUTTON = "//span[contains(text(),'Tài khoản')]";

    // Login popup frame that appears after clicking "Account"
    public static final String LOGIN_POPUP = "#js-form-holder";

    // ========== REGISTRATION FLOW ==========
    // From login popup, click "Create Account" to switch to register form
    public static final String CREATE_ACCOUNT_LINK = "//*[@id=\"js-form-login\"]/div[2]/div[4]/a";

    // 3 input fields that appear after clicking "Create Account"
    public static final String REGISTER_NAME_FIELD = "//input[@id='js-popup-register-name']";     // 1. Full name
    public static final String REGISTER_EMAIL_FIELD = "//input[@id='js-popup-register-email']";   // 2. Email
    public static final String REGISTER_PASSWORD_FIELD = "//input[@id='js-popup-register-password']"; // 3. Password
    public static final String REGISTER_BUTTON = "//a[@class='btn-submit']";

    // ========== LOGIN FLOW ==========
    // From popup #js-form-holder, enter login information directly
    public static final String LOGIN_EMAIL_FIELD = "//input[@id='js-login-email']";
    public static final String LOGIN_PASSWORD_FIELD = "//input[@id='js-login-password']";
    public static final String LOGIN_BUTTON = "//a[@class='btn-submit']";

    // ========== ERROR/SUCCESS MESSAGES ==========
    public static final String ERROR_MESSAGE_GENERAL = "//div[contains(@class,'alert') or contains(@class,'error')]";
    public static final String SUCCESS_MESSAGE = "//div[contains(@class,'success')]";
    public static final String EMAIL_EXISTS_ERROR = "//span[contains(text(),'Email already exists') or contains(text(),'Email đã được sử dụng')]";
    public static final String INVALID_EMAIL_ERROR = "//span[contains(text(),'Invalid email') or contains(text(),'Email không hợp lệ')]";
    public static final String REQUIRED_FIELD_ERROR = "//span[contains(text(),'This field is required') or contains(text(),'Trường này là bắt buộc')]";
    public static final String PASSWORD_WEAK_ERROR = "//span[contains(text(),'Password too weak') or contains(text(),'Mật khẩu quá yếu')]";

    // ========== USER PROFILE ==========
    public static final String PROFILE_LINK = "//a[contains(@href,'profile') or contains(@href,'account')]";
    public static final String PROFILE_MENU_LINK = "//a[contains(text(),'Profile Information') or contains(text(),'Thông tin cá nhân')]";
    public static final String PROFILE_NAME_FIELD = "#profile-name";
    public static final String PROFILE_EMAIL_FIELD = "#profile-email";
    public static final String PROFILE_PHONE_FIELD = "#profile-phone";
    public static final String PROFILE_ADDRESS_FIELD = "#profile-address";
    public static final String SAVE_PROFILE_BUTTON = "//button[contains(text(),'Save Information') or contains(text(),'Lưu thông tin')]";

    // ========== PASSWORD CHANGE ==========
    public static final String CURRENT_PASSWORD_FIELD = "#current-password";
    public static final String NEW_PASSWORD_FIELD = "#new-password";
    public static final String CONFIRM_PASSWORD_FIELD = "#confirm-password";
    public static final String CHANGE_PASSWORD_BUTTON = "//button[contains(text(),'Change Password') or contains(text(),'Đổi mật khẩu')]";

    // ========== LOGOUT ==========
    public static final String LOGOUT_LINK = "//a[contains(text(),'Logout') or contains(text(),'Đăng xuất')]";

    // ========== LOGGED IN USER VERIFICATION ==========
    // When user is logged in, the account button shows user's name instead of "Tài khoản"
    public static final String LOGGED_IN_USER_NAME = "//span[@class='hover-txt line-clamp-1']";
    public static final String ACCOUNT_DROPDOWN_LOGGED_IN = "//div[contains(@class,'account-info')]//span[@class='hover-txt line-clamp-1']";

    // ========== LOADING & STATES ==========
    public static final String LOADING_SPINNER = "//div[@class='loading']";

    // ========== ALTERNATIVES - More stable locators ==========
    // Backup locators using better strategies
    public static final String ACCOUNT_BUTTON_ALT = "//span[contains(text(),'Account') or contains(text(),'Tài khoản')]";
    public static final String LOGIN_EMAIL_ALT = "input[placeholder*='email' i], input[name*='email']";
    public static final String LOGIN_PASSWORD_ALT = "input[type='password'][id*='login']";
    public static final String REGISTER_NAME_ALT = "input[placeholder*='name' i], input[name*='name']";
}
