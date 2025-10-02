package locators;

public class TNCStoreLocators {

    public static final String ACCOUNT_BUTTON = "//span[contains(text(),'Tài khoản')]";
    public static final String LOGIN_POPUP = "#js-form-holder";
    public static final String CREATE_ACCOUNT_LINK = "//a[contains(text(),'o tài')]";
    public static final String REGISTER_NAME_FIELD = "//input[@id='js-popup-register-name']";
    public static final String REGISTER_EMAIL_FIELD = "//input[@id='js-popup-register-email']";
    public static final String REGISTER_PASSWORD_FIELD = "//input[@id='js-popup-register-password']";
    public static final String REGISTER_BUTTON = "//a[@class='btn-submit']";
    public static final String LOGIN_EMAIL_FIELD = "//input[@id='js-login-email']";
    public static final String LOGIN_PASSWORD_FIELD = "//input[@id='js-login-password']";
    public static final String LOGIN_BUTTON = "//a[@class='btn-submit']";
    public static final String ERROR_MESSAGE_GENERAL = "//div[contains(@class,'alert') or contains(@class,'error')]";
    public static final String SUCCESS_MESSAGE = "//div[contains(@class,'success')]";
    public static final String EMAIL_EXISTS_ERROR = "//span[contains(text(),'Email đã được sử dụng') or contains(text(),'Email already exists')]";
    public static final String INVALID_EMAIL_ERROR = "//span[contains(text(),'Email không hợp lệ') or contains(text(),'Invalid email')]";
    public static final String REQUIRED_FIELD_ERROR = "//span[contains(text(),'This field is required') or contains(text(),'Trường này là bắt buộc')]";
    public static final String PASSWORD_WEAK_ERROR = "//span[contains(text(),'Mật khẩu quá yếu') or contains(text(),'Password too weak')]";
    public static final String PROFILE_LINK = "//a[contains(@href,'profile') or contains(@href,'account')]";
    public static final String PROFILE_MENU_LINK = "//a[contains(text(),'Thông tin cá nhân')]";
    public static final String PROFILE_NAME_FIELD = "#profile-name";
    public static final String PROFILE_EMAIL_FIELD = "#profile-email";
    public static final String PROFILE_PHONE_FIELD = "#profile-phone";
    public static final String PROFILE_ADDRESS_FIELD = "#profile-address";
    public static final String SAVE_PROFILE_BUTTON = "//button[contains(text(),'Lưu thông tin')]";
    public static final String CURRENT_PASSWORD_FIELD = "#current-password";
    public static final String NEW_PASSWORD_FIELD = "#new-password";
    public static final String CONFIRM_PASSWORD_FIELD = "#confirm-password";
    public static final String CHANGE_PASSWORD_BUTTON = "//button[contains(text(),'Đổi mật khẩu')]";
    public static final String LOGOUT_LINK = "//a[contains(text(),'Đăng xuất') or contains(text(),'Logout')]";
    public static final String ACCOUNT_TEXT_ELEMENT = "/html/body/div[4]/div[2]/div/div/div[2]/a[1]/span";
    public static final String ACCOUNT_TEXT_ELEMENT_ALT = "//span[@class='hover-txt line-clamp-1']";
    public static final String NOT_LOGGED_IN_TEXT = "//span[contains(text(),'Tài khoản')]";
    public static final String LOGGED_IN_USER_NAME = "//span[@class='hover-txt line-clamp-1']";
    public static final String ACCOUNT_DROPDOWN_LOGGED_IN = "/html/body/div[4]/div[2]/div/div/div[2]/a[1]/span";
    public static final String LOGGED_IN_USER_NAME_ALT1 = "a[class='item account d-flex align-items'] span[class='hover-txt']";
    public static final String LOADING_SPINNER = "//div[@class='loading']";
    public static final String ACCOUNT_BUTTON_ALT = "//span[contains(text(),'Tài khoản') or contains(text(),'Account')]";
    public static final String LOGIN_EMAIL_ALT = "input[placeholder*='email' i], input[name*='email']";
    public static final String LOGIN_PASSWORD_ALT = "input[type='password'][id*='login']";
    public static final String REGISTER_NAME_ALT = "input[placeholder*='tên' i], input[name*='name']";
}
