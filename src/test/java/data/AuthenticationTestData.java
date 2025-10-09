package data;

import utils.XlsxDataLoader;
import java.util.Map;

/**
 * Loads authentication test data from Excel using XlsxDataLoader.
 */
public class AuthenticationTestData {
    private static final String excelPath = "src/test/resources/testdata.xlsx";
    private static final String sheetName = "LoginData";
    private static final XlsxDataLoader loader = new XlsxDataLoader(excelPath);

    public static String getValidEmail() {
        Map<String, String> data = loader.getRowData(sheetName, "valid");
        return data.getOrDefault("Email", "");
    }

    public static String getValidPassword() {
        Map<String, String> data = loader.getRowData(sheetName, "valid");
        return data.getOrDefault("Password", "");
    }

    public static String getInvalidEmail1() {
        Map<String, String> data = loader.getRowData(sheetName, "invalidEmail1");
        return data.getOrDefault("Email", "");
    }

    public static String getEmptyEmail() {
        Map<String, String> data = loader.getRowData(sheetName, "emptyEmail");
        return data.getOrDefault("Email", "");
    }

    public static String getEmptyPassword() {
        Map<String, String> data = loader.getRowData(sheetName, "emptyPassword");
        return data.getOrDefault("Password", "");
    }

    public static String getBothEmptyEmailPassword() {
        Map<String, String> data = loader.getRowData(sheetName, "bothEmpty");
        return data.getOrDefault("Email", "") + "," + data.getOrDefault("Password", "");
    }
}
