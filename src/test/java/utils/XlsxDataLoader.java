package utils;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class XlsxDataLoader {
    private final String filePath;
    private final Map<String, Map<String, Map<String, String>>> sheetDataCache = new HashMap<>();

    public XlsxDataLoader(String filePath) {
        this.filePath = filePath;
    }

    public Map<String, String> getRowData(String sheetName, String rowKey) {
        if (!sheetDataCache.containsKey(sheetName)) {
            sheetDataCache.put(sheetName, loadSheet(sheetName));
        }
        return sheetDataCache.get(sheetName).getOrDefault(rowKey, new HashMap<String, String>());
    }

    private Map<String, Map<String, String>> loadSheet(String sheetName) {
        Map<String, Map<String, String>> data = new HashMap<>();
        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook workbook = new XSSFWorkbook(fis)) {
            Sheet sheet = workbook.getSheet(sheetName);
            if (sheet == null) return data;
            Row headerRow = sheet.getRow(0);
            if (headerRow == null) return data;
            int colCount = headerRow.getLastCellNum();
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;
                String key = getCellValue(row.getCell(0));
                if (key == null || key.isEmpty()) continue;
                Map<String, String> rowData = new HashMap<>();
                for (int j = 1; j < colCount; j++) {
                    String header = getCellValue(headerRow.getCell(j));
                    String value = getCellValue(row.getCell(j));
                    rowData.put(header, value);
                }
                data.put(key, rowData);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to read Excel file: " + filePath, e);
        }
        return data;
    }

    private String getCellValue(Cell cell) {
        if (cell == null) return "";
        if (cell.getCellType() == CellType.STRING) return cell.getStringCellValue();
        if (cell.getCellType() == CellType.NUMERIC) return String.valueOf((int)cell.getNumericCellValue());
        if (cell.getCellType() == CellType.BOOLEAN) return String.valueOf(cell.getBooleanCellValue());
        return "";
    }
}
