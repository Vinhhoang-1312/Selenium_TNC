package helpers;

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
     * DataProvider cho ProductDetail module tests
     */
    @DataProvider(name = "productDetailData")
    public static Object[][] getProductDetailData() {
        if (ExcelReader.isExcelFileExists("productdetail")) {
            Map<String, Map<String, String>> allData = ExcelReader.getAllTestData("productdetail");
            return convertToDataProvider(allData);
        }

        return getProductDetailFallbackData();
    }

    // Helper method to convert Map data to DataProvider format
    private static Object[][] convertToDataProvider(Map<String, Map<String, String>> allData) {
        List<Object[]> dataList = new ArrayList<>();

        for (Map.Entry<String, Map<String, String>> entry : allData.entrySet()) {
            String testCaseId = entry.getKey();
            Map<String, String> testData = entry.getValue();

            // Convert to array format expected by TestNG DataProvider
            Object[] rowData = {testCaseId, testData};
            dataList.add(rowData);
        }

        return dataList.toArray(new Object[dataList.size()][]);
    }

    // Fallback data methods
    private static Object[][] getAuthenticationFallbackData() {
        return new Object[][]{
                {"AUTH-SU-01", Map.of("name", "John Doe", "email", "john5@test.com", "password", "Abc12345")},
                {"AUTH-SU-02", Map.of("name", "Jane Smith", "email", "jane@test.com", "password", "Pass123456")},
                {"AUTH-LI-01", Map.of("email", "john5@test.com", "password", "Abc12345")},
                {"AUTH-LI-02", Map.of("email", "invalid@test.com", "password", "WrongPass123")}
        };
    }

    private static Object[][] getCartFallbackData() {
        return new Object[][]{
                {"CART-001", Map.of("productUrl", "/san-pham/laptop-gaming-asus", "quantity", "1")},
                {"CART-002", Map.of("productUrl", "/san-pham/pc-gaming-rtx-4070", "quantity", "2")},
                {"CART-003", Map.of("productUrl", "/san-pham/gaming-chair", "quantity", "5")}
        };
    }

    private static Object[][] getSearchFallbackData() {
        return new Object[][]{
                {"SRH-001", Map.of("keyword", "Rtx 2050", "expectedResults", "true")},
                {"SRH-002", Map.of("keyword", "abcxyz123", "expectedResults", "false")},
                {"SRH-003", Map.of("keyword", "màn hình máy tính", "expectedResults", "true")},
                {"SRH-004", Map.of("keyword", "rtx & 2050", "expectedResults", "false")}
        };
    }

    private static Object[][] getProductDetailFallbackData() {
        return new Object[][]{
                {"DTL-001", Map.of("productUrl", "/san-pham/laptop-gaming-asus")},
                {"DTL-002", Map.of("productUrl", "/san-pham/pc-gaming-rtx-4070")},
                {"DTL-003", Map.of("productUrl", "/san-pham/gaming-chair")},
                {"DTL-004", Map.of("productUrl", "/san-pham/monitor-gaming")}
        };
    }

    /**
     * Generic DataProvider for any module
     */
    @DataProvider(name = "genericTestData")
    public static Object[][] getGenericTestData(String moduleName) {
        switch (moduleName.toLowerCase()) {
            case "authentication":
                return getAuthenticationData();
            case "cart":
                return getCartData();
            case "search":
                return getSearchData();
            case "productdetail":
                return getProductDetailData();
            default:
                return new Object[0][0];
        }
    }
}
