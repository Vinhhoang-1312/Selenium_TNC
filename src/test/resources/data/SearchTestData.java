package data;

public class SearchTestData {
    
    // Test keywords for different scenarios
    
    // SRH-001: Valid product search
    public static final String VALID_PRODUCT_KEYWORD = "Rtx 2050";
    public static final String[] VALID_PRODUCTS = {
        "Rtx 2050",
        "RTX 4070",
        "Laptop Gaming",
        "Gaming Chair",
        "PC Gaming"
    };
    
    // SRH-002: Invalid product search
    public static final String INVALID_PRODUCT_KEYWORD = "abcxyz123";
    public static final String[] INVALID_KEYWORDS = {
        "abcxyz123",
        "nonexistentproduct999",
        "invalidkeyword456",
        "fakebrand999"
    };
    
    // SRH-003: Category keyword search
    public static final String CATEGORY_KEYWORD = "màn hình máy tính";
    public static final String[] CATEGORY_KEYWORDS = {
        "màn hình máy tính",
        "monitor",
        "laptop gaming",
        "pc gaming",
        "gaming gear"
    };
    
    // SRH-004: Special character search
    public static final String SPECIAL_CHAR_KEYWORD = "rtx & 2050";
    public static final String[] SPECIAL_CHAR_KEYWORDS = {
        "rtx & 2050",
        "laptop@gaming",
        "pc#gaming",
        "monitor$4k",
        "gaming%chair"
    };
    
    // SRH-005: Continuous search
    public static final String CONTINUOUS_SEARCH_KEYWORD = "Rtx 2050";
    public static final int CONTINUOUS_SEARCH_TIMES = 10;
    
    // Expected results and messages
    public static final String NO_RESULTS_MESSAGE = "Không tìm thấy sản phẩm";
    public static final String CATEGORY_PAGE_TITLE = "Màn hình máy tính";
    
    // URLs and navigation
    public static final String MAIN_PAGE_URL = "https://www.tncstore.vn/";
    public static final String SEARCH_RESULTS_URL_PATTERN = "/search";
    public static final String MONITOR_CATEGORY_URL_PATTERN = "/man-hinh";
    
    // Search performance thresholds
    public static final int MIN_SEARCH_RESULTS = 1;
    public static final int MAX_SEARCH_TIME_SECONDS = 5;
    
    // Test data for verification
    public static class SearchTestCase {
        public String testId;
        public String keyword;
        public String expectedResult;
        public boolean shouldHaveResults;
        
        public SearchTestCase(String testId, String keyword, String expectedResult, boolean shouldHaveResults) {
            this.testId = testId;
            this.keyword = keyword;
            this.expectedResult = expectedResult;
            this.shouldHaveResults = shouldHaveResults;
        }
    }
    
    public static final SearchTestCase[] ALL_TEST_CASES = {
        new SearchTestCase("SRH-001", VALID_PRODUCT_KEYWORD, "Should show RTX 2050 related items", true),
        new SearchTestCase("SRH-002", INVALID_PRODUCT_KEYWORD, "Should show no results", false),
        new SearchTestCase("SRH-003", CATEGORY_KEYWORD, "Should redirect to monitor category", true),
        new SearchTestCase("SRH-004", SPECIAL_CHAR_KEYWORD, "Should handle special characters", false),
        new SearchTestCase("SRH-005", CONTINUOUS_SEARCH_KEYWORD, "Should work 10 times continuously", true)
    };
}
