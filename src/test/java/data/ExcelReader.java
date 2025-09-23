package data;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import utils.ConfigReader;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
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
        String fileName = moduleName.toLowerCase() + "_testdata.xlsx";

        // Kiểm tra cache trước
        if (dataCache.containsKey(fileName) &&
            dataCache.get(fileName).containsKey(testCaseId)) {
            return dataCache.get(fileName).get(testCaseId);
        }

        // Đọc từ Excel nếu chưa có trong cache
        loadExcelData(fileName);

        return dataCache.getOrDefault(fileName, new HashMap<>())
                      .getOrDefault(testCaseId, new HashMap<>());
    }

    /**
     * Đọc tất cả data từ một Excel file và lưu vào cache
     */
    private static void loadExcelData(String fileName) {
        try {
            String filePath = EXCEL_DATA_PATH + fileName;
            File file = new File(filePath);

            if (!file.exists()) {
                System.err.println("Excel file not found: " + filePath);
                return;
            }

            FileInputStream fis = new FileInputStream(file);
            Workbook workbook = new XSSFWorkbook(fis);

            Map<String, Map<String, String>> fileData = new HashMap<>();

            // Đọc từng sheet trong Excel file
            for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
                Sheet sheet = workbook.getSheetAt(i);
                Map<String, Map<String, String>> sheetData = readSheetData(sheet);
                fileData.putAll(sheetData);
            }

            dataCache.put(fileName, fileData);

            workbook.close();
            fis.close();

        } catch (IOException e) {
            System.err.println("Error reading Excel file: " + fileName);
            e.printStackTrace();
        }
    }

    /**
     * Đọc data từ một sheet
     */
    private static Map<String, Map<String, String>> readSheetData(Sheet sheet) {
        Map<String, Map<String, String>> sheetData = new HashMap<>();

        if (sheet.getPhysicalNumberOfRows() < 2) {
            return sheetData; // Không có data
        }

        // Đọc header row để lấy column names
        Row headerRow = sheet.getRow(0);
        List<String> headers = new ArrayList<>();

        for (Cell cell : headerRow) {
            headers.add(getCellValueAsString(cell));
        }

        // Đọc data rows
        for (int rowIndex = 1; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
            Row row = sheet.getRow(rowIndex);
            if (row == null) continue;

            Map<String, String> rowData = new HashMap<>();
            String testCaseId = "";

            for (int cellIndex = 0; cellIndex < headers.size(); cellIndex++) {
                Cell cell = row.getCell(cellIndex);
                String cellValue = getCellValueAsString(cell);
                String header = headers.get(cellIndex);

                rowData.put(header, cellValue);

                // Lấy test case ID từ cột đầu tiên
                if (cellIndex == 0) {
                    testCaseId = cellValue;
                }
            }

            if (!testCaseId.isEmpty()) {
                sheetData.put(testCaseId, rowData);
            }
        }

        return sheetData;
    }

    /**
     * Convert cell value thành string
     */
    private static String getCellValueAsString(Cell cell) {
        if (cell == null) {
            return "";
        }

        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue().trim();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue().toString();
                } else {
                    return String.valueOf((long) cell.getNumericCellValue());
                }
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                return cell.getCellFormula();
            default:
                return "";
        }
    }

    /**
     * Convenience methods cho các modules cụ thể
     */

    // Authentication module
    public static String getAuthData(String testCaseId, String field) {
        Map<String, String> data = getTestData("authentication", testCaseId);
        return data.getOrDefault(field, "");
    }

    // Cart module
    public static String getCartData(String testCaseId, String field) {
        Map<String, String> data = getTestData("cart", testCaseId);
        return data.getOrDefault(field, "");
    }

    // Search module
    public static String getSearchData(String testCaseId, String field) {
        Map<String, String> data = getTestData("search", testCaseId);
        return data.getOrDefault(field, "");
    }

    // Product Detail module
    public static String getProductDetailData(String testCaseId, String field) {
        Map<String, String> data = getTestData("productdetail", testCaseId);
        return data.getOrDefault(field, "");
    }

    /**
     * Lấy tất cả test data cho một module
     */
    public static Map<String, Map<String, String>> getAllTestData(String moduleName) {
        String fileName = moduleName.toLowerCase() + "_testdata.xlsx";

        if (!dataCache.containsKey(fileName)) {
            loadExcelData(fileName);
        }

        return dataCache.getOrDefault(fileName, new HashMap<>());
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
        String fileName = moduleName.toLowerCase() + "_testdata.xlsx";
        String filePath = EXCEL_DATA_PATH + fileName;
        return new File(filePath).exists();
    }
}
