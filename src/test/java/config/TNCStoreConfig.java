package config;

/**
 * Configuration constants for TNC Store automation tests
 * This is TEST-SPECIFIC config for tncstore.vn website only
 */
public class TNCStoreConfig {

    // Timeout configurations
    public static final int ELEMENT_WAIT = 20;
    public static final int PAGE_LOAD_TIMEOUT = 60;
    public static final int IMPLICIT_WAIT = 15;

    // URLs
    public static final String BASE_URL = "https://www.tncstore.vn/";
    public static final String LOGIN_URL = BASE_URL + "login";
    public static final String REGISTER_URL = BASE_URL + "register";
    public static final String CART_URL = BASE_URL + "cart";

    // Test data
    public static final String DEFAULT_PASSWORD = "Test123456";
    public static final String DEFAULT_NAME = "Test User";

    // Browser configurations
    public static final boolean HEADLESS_MODE = false;
    public static final boolean MAXIMIZE_WINDOW = true;

    // Screenshot configurations
    public static final boolean SCREENSHOT_ON_FAILURE = true;
    public static final String SCREENSHOT_PATH = "target/screenshots/";
}
