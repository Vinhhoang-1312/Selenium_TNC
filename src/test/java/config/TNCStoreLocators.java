package config;

/**
 * XPath locators for TNC Store website (https://www.tncstore.vn/)
 * Organized by page/functionality
 */
public class TNCStoreLocators {

    // ========== NAVIGATION & GENERAL ==========
    public static final String ACCOUNT_BUTTON = "/html/body/div[4]/div[2]/div/div/div[2]/a[1]/span";
    public static final String LOGIN_POPUP = "#js-form-holder";
    public static final String MAIN_LOGO = "//a[@class='logo']";
    public static final String SEARCH_BOX = "//input[@name='query']";
    public static final String CART_ICON = "//a[contains(@href,'cart')]";

    // ========== LOGIN/AUTHENTICATION ==========
    public static final String LOGIN_EMAIL_FIELD = "#js-login-email";
    public static final String LOGIN_PASSWORD_FIELD = "#js-login-password";
    public static final String LOGIN_BUTTON = "//*[@id='js-form-login']//button[@type='submit']";
    public static final String CREATE_ACCOUNT_LINK = "//*[@id='js-form-login']/div[2]/div[4]/a";
    public static final String FORGOT_PASSWORD_LINK = "//a[contains(text(),'Quên mật khẩu')]";

    // ========== REGISTRATION ==========
    public static final String REGISTER_NAME_FIELD = "#js-popup-register-name";
    public static final String REGISTER_EMAIL_FIELD = "#js-popup-register-email";
    public static final String REGISTER_PASSWORD_FIELD = "#js-popup-register-password";
    public static final String REGISTER_BUTTON = "//*[@id='js-form-register']//button[@type='submit']";
    public static final String REGISTER_TERMS_CHECKBOX = "//input[@name='terms']";

    // ========== ERROR/SUCCESS MESSAGES ==========
    public static final String ERROR_MESSAGE_GENERAL = "//div[contains(@class,'alert')]";
    public static final String SUCCESS_MESSAGE = "//div[contains(@class,'success-message')]";
    public static final String EMAIL_EXISTS_ERROR = "//span[contains(text(),'Email đã được sử dụng')]";
    public static final String INVALID_EMAIL_ERROR = "//span[contains(text(),'Email không hợp lệ')]";
    public static final String REQUIRED_FIELD_ERROR = "//span[contains(text(),'This field is required')]";
    public static final String PASSWORD_WEAK_ERROR = "//span[contains(text(),'Mật khẩu quá yếu')]";

    // ========== USER PROFILE ==========
    public static final String PROFILE_LINK = "//a[contains(@href,'profile') or contains(@href,'account')]";
    public static final String PROFILE_MENU_LINK = "//a[contains(text(),'Thông tin cá nhân')]";
    public static final String PROFILE_NAME_FIELD = "#profile-name";
    public static final String PROFILE_EMAIL_FIELD = "#profile-email";
    public static final String PROFILE_PHONE_FIELD = "#profile-phone";
    public static final String PROFILE_ADDRESS_FIELD = "#profile-address";
    public static final String SAVE_PROFILE_BUTTON = "//button[contains(text(),'Lưu thông tin')]";

    // ========== PASSWORD CHANGE ==========
    public static final String CURRENT_PASSWORD_FIELD = "#current-password";
    public static final String NEW_PASSWORD_FIELD = "#new-password";
    public static final String CONFIRM_PASSWORD_FIELD = "#confirm-password";
    public static final String CHANGE_PASSWORD_BUTTON = "//button[contains(text(),'Đổi mật khẩu')]";

    // ========== LOGOUT ==========
    public static final String LOGOUT_LINK = "//a[contains(text(),'Đăng xuất')]";
    public static final String USER_MENU_DROPDOWN = "//div[@class='user-menu']";

    // ========== PRODUCT & SHOPPING ==========
    public static final String PRODUCT_ITEM = "//div[@class='product-item']";
    public static final String ADD_TO_CART_BUTTON = "//button[contains(text(),'Thêm vào giỏ')]";
    public static final String CART_ITEMS_COUNT = "//span[@class='cart-count']";
    public static final String CHECKOUT_BUTTON = "//a[contains(@href,'checkout')]";

    // ========== LOADING & STATES ==========
    public static final String LOADING_SPINNER = "//div[@class='loading']";
    public static final String PAGE_LOADER = "//div[@id='page-loader']";
}
