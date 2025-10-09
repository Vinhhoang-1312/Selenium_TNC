package data;

import utils.ExcelDataReader;
import java.util.Map;
import java.util.HashMap;

public class AuthenticationTestData {
    private static final String excelPath = "src/test/resources/testdata.xlsx";
    private static final String sheetName = "LoginData";
    private static final ExcelDataReader reader = new ExcelDataReader(excelPath);

    public static String get(String rowKey, String column) {
        Object[][] allData = reader.getSheetData(sheetName);

        for (Object[] row : allData) {
            if (row.length > 0 && rowKey.equals(String.valueOf(row[0]))) {
                int columnIndex = getColumnIndex(column);
                if (columnIndex >= 0 && columnIndex < row.length) {
                    return String.valueOf(row[columnIndex]);
                }
            }
        }
        return "";
    }

    private static int getColumnIndex(String columnName) {
        Map<String, Integer> columns = new HashMap<>();
        columns.put("Email", 1);
        columns.put("Password", 2);
        return columns.getOrDefault(columnName, -1);
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
