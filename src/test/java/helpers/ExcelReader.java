package helpers;

import java.util.*;

public class ExcelReader {
    private static final String EXCEL_DATA_PATH = "src/test/resources/testdata/";

    // Cache để lưu trữ data đã đọc
    private static Map<String, Map<String, Map<String, String>>> dataCache = new HashMap<>();

    /**
     * Đọc data từ Excel file theo module và test case ID
     * @param moduleName Tên module (authentication, cart, search, productdetail)
     * @param testCaseId ID của test case (AUTH-SU-01, SRH-001, etc.)
     * @return Map chứa test data cho test case
     */
    public static Map<String, String> getTestData(String moduleName, String testCaseId) {
        return new HashMap<>(); // Always return empty to use fallback data
    }

    /**
     * Convenience methods cho các modules cụ thể
     */

    // Authentication module
    public static String getAuthData(String testCaseId, String field) {
        return ""; // Always return empty to use fallback data
    }

    // Cart module
    public static String getCartData(String testCaseId, String field) {
        return ""; // Always return empty to use fallback data
    }

    // Search module
    public static String getSearchData(String testCaseId, String field) {
        return ""; // Always return empty to use fallback data
    }

    // Product Detail module
    public static String getProductDetailData(String testCaseId, String field) {
        return ""; // Always return empty to use fallback data
    }

    /**
     * Lấy tất cả test data cho một module
     */
    public static Map<String, Map<String, String>> getAllTestData(String moduleName) {
        return new HashMap<>(); // Always return empty to use fallback data
    }

    /**
     * Clear cache - useful khi Excel files được update
     */
    public static void clearCache() {
        dataCache.clear();
    }

    /**
     * Check xem Excel file có tồn tại không
     */
    public static boolean isExcelFileExists(String moduleName) {
        return false; // Always return false to use fallback data
    }
}
