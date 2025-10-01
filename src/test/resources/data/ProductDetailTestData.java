package data;

public class ProductDetailTestData {

    // Test data for Product Detail scenarios

    // DTL-001: Access to detail product
    public static final String MAIN_PAGE_URL = "https://www.tncstore.vn/";
    public static final String[] SAMPLE_PRODUCT_URLS = {
        "/san-pham/laptop-gaming-asus",
        "/san-pham/pc-gaming-rtx-4070",
        "/san-pham/gaming-chair",
        "/san-pham/monitor-gaming"
    };

    // DTL-002: Add to cart functionality
    public static final String SUCCESS_ADD_TO_CART_MESSAGE = "Sản phẩm đã được thêm vào giỏ hàng thành công";
    public static final String[] SUCCESS_MESSAGES = {
        "thành công",
        "success",
        "đã thêm",
        "added to cart"
    };

    // DTL-003: Image gallery functionality
    public static final int DEFAULT_THUMBNAIL_INDEX = 0;
    public static final int SECOND_THUMBNAIL_INDEX = 1;
    public static final String IMAGE_POPUP_XPATH = "//div[contains(@class,'image-popup') or contains(@class,'lightbox')]";

    // DTL-004: Buy now functionality
    public static final String CART_PAGE_URL_PATTERN = "/cart";
    public static final String CART_PAGE_URL_PATTERN_VN = "/gio-hang";
    public static final String BUY_NOW_BUTTON_TEXT = "Mua ngay";

    // DTL-005: Technical specifications
    public static final String VIEW_MORE_BUTTON_TEXT = "Xem thêm";
    public static final String TECHNICAL_SPECS_SECTION_TITLE = "Thông số kỹ thuật";
    public static final String[] TECHNICAL_SPECS_KEYWORDS = {
        "thông số",
        "kỹ thuật",
        "specifications",
        "technical"
    };

    // Expected page elements and behaviors
    public static final String PRODUCT_TITLE_SELECTOR = "h1[contains(@class,'product-title')]";
    public static final String PRODUCT_PRICE_SELECTOR = "div[contains(@class,'product-price')]";
    public static final String ADD_TO_CART_SELECTOR = "button[contains(@class,'add-to-cart')]";

    // Test timeouts and waits
    public static final int PAGE_LOAD_TIMEOUT = 10;
    public static final int POPUP_DISPLAY_TIMEOUT = 5;
    public static final int NOTIFICATION_TIMEOUT = 3;

    // Test scenarios data
    public static class ProductDetailTestCase {
        public String testId;
        public String description;
        public String precondition;
        public String expectedResult;

        public ProductDetailTestCase(String testId, String description, String precondition, String expectedResult) {
            this.testId = testId;
            this.description = description;
            this.precondition = precondition;
            this.expectedResult = expectedResult;
        }
    }

    public static final ProductDetailTestCase[] ALL_TEST_CASES = {
        new ProductDetailTestCase(
            "DTL-001",
            "Access to detail product",
            "Stay at main page",
            "Redirect to detail product page"
        ),
        new ProductDetailTestCase(
            "DTL-002",
            "Verify add to cart button",
            "Already in detail product page",
            "Success notification shows up"
        ),
        new ProductDetailTestCase(
            "DTL-003",
            "Verify image gallery functionality",
            "Already in detail product page",
            "Image popup appears when clicking thumbnails"
        ),
        new ProductDetailTestCase(
            "DTL-004",
            "Verify 'mua ngay' button",
            "Already in detail product page",
            "Redirect to cart page with product"
        ),
        new ProductDetailTestCase(
            "DTL-005",
            "Verify 'xem thêm' in Technical specs",
            "Already in detail product page",
            "Technical information popup shows up"
        )
    };

    // Navigation and interaction data
    public static final String SCROLL_TO_PRODUCTS_SCRIPT = "window.scrollTo(0, document.body.scrollHeight/2);";
    public static final String SCROLL_TO_BOTTOM_SCRIPT = "window.scrollTo(0, document.body.scrollHeight);";
    public static final String SCROLL_TO_ELEMENT_SCRIPT = "arguments[0].scrollIntoView(true);";
}
