package utils;

import org.apache.poi.ss.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class ExcelDataReader {
    private static final Logger logger = LoggerFactory.getLogger(ExcelDataReader.class);
    private static final int DATA_START_ROW = 1;

    private final String excelFilePath;

    public ExcelDataReader() {
        this.excelFilePath = ConfigReader.getProperty("testdata.excel.path");
        validateConfiguration();
    }

    public ExcelDataReader(String customPath) {
        this.excelFilePath = customPath;
        validateConfiguration();
    }

    private void validateConfiguration() {
        if (excelFilePath == null || excelFilePath.trim().isEmpty()) {
            throw new RuntimeException("Excel file path not configured in application.properties");
        }
        logger.info("ExcelDataReader initialized with path: {}", excelFilePath);
    }

    public Object[][] getSheetData(String sheetName) {
        File excelFile = new File(excelFilePath);

        if (!excelFile.exists()) {
            logger.error("Excel file not found: {}", excelFilePath);
            throw new RuntimeException("Excel file not found: " + excelFilePath);
        }

        logger.info("Reading test data from sheet: {} in file: {}", sheetName, excelFilePath);

        try (FileInputStream fileInput = new FileInputStream(excelFile);
             Workbook workbook = WorkbookFactory.create(fileInput)) {

            Sheet targetSheet = workbook.getSheet(sheetName);
            if (targetSheet == null) {
                logger.error("Sheet '{}' not found in Excel file", sheetName);
                throw new RuntimeException("Sheet '" + sheetName + "' not found");
            }

            return extractDataFromSheet(targetSheet);

        } catch (IOException e) {
            logger.error("Error reading Excel file: {}", excelFilePath, e);
            throw new RuntimeException("Failed to read Excel file", e);
        }
    }

    private Object[][] extractDataFromSheet(Sheet sheet) {
        if (sheet == null || sheet.getPhysicalNumberOfRows() == 0) {
            logger.warn("Sheet is empty, returning empty array");
            return new Object[0][0];
        }

        int totalColumns = calculateColumnCount(sheet);
        int dataRowCount = countValidDataRows(sheet);

        if (dataRowCount == 0) {
            logger.warn("No valid data rows found (excluding header)");
            return new Object[0][0];
        }

        Object[][] testData = new Object[dataRowCount][totalColumns];

        for (int rowIdx = 0; rowIdx < dataRowCount; rowIdx++) {
            Row currentRow = sheet.getRow(DATA_START_ROW + rowIdx);

            for (int colIdx = 0; colIdx < totalColumns; colIdx++) {
                testData[rowIdx][colIdx] = extractCellValue(currentRow, colIdx);
            }
        }

        logger.info("Successfully extracted {} rows x {} columns from sheet '{}'",
                    dataRowCount, totalColumns, sheet.getSheetName());

        return testData;
    }

    private int calculateColumnCount(Sheet sheet) {
        Row firstDataRow = sheet.getRow(DATA_START_ROW);

        if (firstDataRow == null) {
            logger.warn("First data row is null, defaulting to 0 columns");
            return 0;
        }

        int maxColumnIndex = 0;
        int lastCellNum = firstDataRow.getLastCellNum();

        for (int colIdx = 0; colIdx < lastCellNum; colIdx++) {
            Cell cell = firstDataRow.getCell(colIdx, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
            if (cell != null) {
                maxColumnIndex = colIdx + 1;
            }
        }

        return maxColumnIndex;
    }

    private int countValidDataRows(Sheet sheet) {
        int validRowCount = 0;
        int lastRowNum = sheet.getLastRowNum();

        for (int rowIdx = DATA_START_ROW; rowIdx <= lastRowNum; rowIdx++) {
            Row currentRow = sheet.getRow(rowIdx);
            String firstColumnValue = extractCellValue(currentRow, 0);

            if (firstColumnValue.trim().isEmpty()) {
                logger.debug("Found empty first column at row {}, stopping count", rowIdx);
                break;
            }

            validRowCount++;
        }

        return validRowCount;
    }

    private String extractCellValue(Row row, int columnIndex) {
        if (row == null) {
            return "";
        }

        Cell cell = row.getCell(columnIndex);
        if (cell == null) {
            return "";
        }

        try {
            String cellValue = cell.toString();
            return cellValue != null ? cellValue.trim() : "";
        } catch (Exception e) {
            logger.warn("Error reading cell at row={}, col={}: {}",
                       row.getRowNum(), columnIndex, e.getMessage());
            return "";
        }
    }
}
