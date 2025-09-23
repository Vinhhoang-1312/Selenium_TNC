package data;

import org.testng.annotations.DataProvider;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TestDataProvider {

    /**
     * DataProvider cho Authentication module tests
     */
    @DataProvider(name = "authenticationData")
    public static Object[][] getAuthenticationData() {
        if (ExcelReader.isExcelFileExists("authentication")) {
            Map<String, Map<String, String>> allData = ExcelReader.getAllTestData("authentication");
            return convertToDataProvider(allData);
        }

        // Fallback to hardcoded data if Excel not available
        return getAuthenticationFallbackData();
    }

    /**
     * DataProvider cho Cart module tests
     */
    @DataProvider(name = "cartData")
    public static Object[][] getCartData() {
        if (ExcelReader.isExcelFileExists("cart")) {
            Map<String, Map<String, String>> allData = ExcelReader.getAllTestData("cart");
            return convertToDataProvider(allData);
        }

        return getCartFallbackData();
    }

    /**
     * DataProvider cho Search module tests
     */
    @DataProvider(name = "searchData")
    public static Object[][] getSearchData() {
        if (ExcelReader.isExcelFileExists("search")) {
            Map<String, Map<String, String>> allData = ExcelReader.getAllTestData("search");
            return convertToDataProvider(allData);
        }

        return getSearchFallbackData();
    }

    /**
     * DataProvider cho Product Detail module tests
     */
    @DataProvider(name = "productDetailData")
    public static Object[][] getProductDetailData() {
        if (ExcelReader.isExcelFileExists("productdetail")) {
            Map<String, Map<String, String>> allData = ExcelReader.getAllTestData("productdetail");
            return convertToDataProvider(allData);
        }

        return getProductDetailFallbackData();
    }

    /**
     * Convert Map data thành Object[][] format cho TestNG DataProvider
     */
    private static Object[][] convertToDataProvider(Map<String, Map<String, String>> data) {
        List<Object[]> result = new ArrayList<>();

        for (Map.Entry<String, Map<String, String>> entry : data.entrySet()) {
            String testCaseId = entry.getKey();
            Map<String, String> testData = entry.getValue();
            result.add(new Object[]{testCaseId, testData});
        }

        return result.toArray(new Object[0][]);
    }

    /**
     * Fallback data methods khi Excel files không tồn tại
     */
    private static Object[][] getAuthenticationFallbackData() {
        return new Object[][] {
            {"AUTH-SU-01", Map.of("name", "John Doe", "email", "john@test.com", "password", "Abc12345")},
            {"AUTH-SI-01", Map.of("email", "john@test.com", "password", "Abc12345")},
            {"AUTH-FP-01", Map.of("email", "john@test.com")}
        };
    }

    private static Object[][] getCartFallbackData() {
        return new Object[][] {
            {"TC001", Map.of("action", "add", "quantity", "1")},
            {"TC002", Map.of("action", "update", "quantity", "2")},
            {"TC003", Map.of("action", "remove", "quantity", "0")}
        };
    }

    private static Object[][] getSearchFallbackData() {
        return new Object[][] {
            {"SRH-001", Map.of("keyword", "Rtx 2050", "expectedResult", "results")},
            {"SRH-002", Map.of("keyword", "abcxyz123", "expectedResult", "no_results")},
            {"SRH-003", Map.of("keyword", "màn hình máy tính", "expectedResult", "category")}
        };
    }

    private static Object[][] getProductDetailFallbackData() {
        return new Object[][] {
            {"DTL-001", Map.of("action", "access", "expectedResult", "detail_page")},
            {"DTL-002", Map.of("action", "add_to_cart", "expectedResult", "notification")},
            {"DTL-003", Map.of("action", "image_gallery", "expectedResult", "popup")}
        };
    }

    /**
     * Convenience methods để lấy data cho từng test case cụ thể
     */
    public static String getTestData(String module, String testCaseId, String field) {
        return ExcelReader.getTestData(module, testCaseId).getOrDefault(field, "");
    }

    // Authentication specific methods
    public static String getAuthEmail(String testCaseId) {
        return ExcelReader.getAuthData(testCaseId, "email");
    }

    public static String getAuthPassword(String testCaseId) {
        return ExcelReader.getAuthData(testCaseId, "password");
    }

    public static String getAuthName(String testCaseId) {
        return ExcelReader.getAuthData(testCaseId, "name");
    }

    // Search specific methods
    public static String getSearchKeyword(String testCaseId) {
        return ExcelReader.getSearchData(testCaseId, "keyword");
    }

    public static String getSearchExpectedResult(String testCaseId) {
        return ExcelReader.getSearchData(testCaseId, "expectedResult");
    }

    // Cart specific methods
    public static String getCartQuantity(String testCaseId) {
        return ExcelReader.getCartData(testCaseId, "quantity");
    }

    public static String getCartAction(String testCaseId) {
        return ExcelReader.getCartData(testCaseId, "action");
    }

    // Product Detail specific methods
    public static String getProductDetailAction(String testCaseId) {
        return ExcelReader.getProductDetailData(testCaseId, "action");
    }

    public static String getProductDetailExpectedResult(String testCaseId) {
        return ExcelReader.getProductDetailData(testCaseId, "expectedResult");
    }
}
