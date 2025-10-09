package data;

import utils.XlsxDataLoader;
import java.util.Map;


public class AuthenticationTestData {
    private static final String excelPath = "src/test/resources/testdata.xlsx";
    private static final String sheetName = "LoginData";
    private static final XlsxDataLoader loader = new XlsxDataLoader(excelPath);

    public static String get(String rowKey, String column) {
        Map<String, String> data = loader.getRowData(sheetName, rowKey);
        return data.getOrDefault(column, "");
    }

    public static String getValidEmail() {
        return get("valid", "Email");
    }

    public static String getValidPassword() {
        return get("valid", "Password");
    }

    public static String getInvalidEmail1() {
        return get("invalidEmail1", "Email");
    }

    public static String getEmptyEmail() {
        return get("emptyEmail", "Email");
    }

    public static String getEmptyPassword() {
        return get("emptyPassword", "Password");
    }

    public static String getBothEmptyEmail() {
        return get("bothEmpty", "Email");
    }

    public static String getBothEmptyPassword() {
        return get("bothEmpty", "Password");
    }
}
