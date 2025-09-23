package utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ReportManager {
    private static ExtentReports extent;
    private static ThreadLocal<ExtentTest> test = new ThreadLocal<>();
    private static List<TestResult> testResults = new ArrayList<>();
    private static String currentModule = "general"; // Default module name

    // Test result data structure
    public static class TestResult {
        public String testName;
        public String status;
        public String duration;
        public String error;
        public String timestamp;

        public TestResult(String testName, String status, String duration, String error) {
            this.testName = testName;
            this.status = status;
            this.duration = duration;
            this.error = error;
            this.timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
        }
    }

    // Set module name for report naming
    public static void setModule(String moduleName) {
        currentModule = moduleName.toLowerCase();
        // Reset extent to create new report for new module
        extent = null;
        testResults.clear();
    }

    // Auto-detect module from test class name
    public static void setModuleFromTestClass(String testClassName) {
        if (testClassName.contains("Authentication")) {
            setModule("authentication");
        } else if (testClassName.contains("UserProfile")) {
            setModule("userprofile");
        } else if (testClassName.contains("Cart")) {
            setModule("cart");
        } else if (testClassName.contains("Search")) {
            setModule("search");
        } else if (testClassName.contains("ProductDetail")) {
            setModule("productdetail");
        } else if (testClassName.contains("Checkout")) {
            setModule("checkout");
        } else {
            setModule("general");
        }
    }

    public static void initReports() {
        if (extent == null) {
            createInstance();
        }
    }

    private static void createInstance() {
        // Create module-specific report path
        String reportPath = "target/" + currentModule + "_report.html";
        ExtentSparkReporter sparkReporter = new ExtentSparkReporter(reportPath);

        // Enhanced report configuration with module-specific titles
        sparkReporter.config().setTheme(Theme.STANDARD);
        sparkReporter.config().setDocumentTitle("🏪 TNC Store - " + capitalizeModule(currentModule) + " Module Report");
        sparkReporter.config().setReportName("TNC Store " + capitalizeModule(currentModule) + " Test Dashboard");
        sparkReporter.config().setTimeStampFormat("dd/MM/yyyy HH:mm:ss");
        sparkReporter.config().setEncoding("utf-8");

        // Custom CSS for better styling
        String css = """
            .test-content { font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; }
            .badge-primary { background-color: #007bff; }
            .badge-success { background-color: #28a745; }
            .badge-danger { background-color: #dc3545; }
            .badge-warning { background-color: #ffc107; color: #212529; }
            .module-header { background: linear-gradient(90deg, #667eea 0%, #764ba2 100%); color: white; padding: 10px; border-radius: 5px; }
            """;
        sparkReporter.config().setCss(css);

        extent = new ExtentReports();
        extent.attachReporter(sparkReporter);

        // Enhanced system information with module info
        extent.setSystemInfo("📋 Module", capitalizeModule(currentModule));
        extent.setSystemInfo("🖥️ Operating System", System.getProperty("os.name"));
        extent.setSystemInfo("☕ Java Version", System.getProperty("java.version"));
        extent.setSystemInfo("👤 User", System.getProperty("user.name"));
        extent.setSystemInfo("🌍 Environment", ConfigReader.getProperty("environment", "QA"));
        extent.setSystemInfo("🌐 Browser", ConfigReader.getProperty("browser", "chrome"));
        extent.setSystemInfo("🔗 Base URL", ConfigReader.getProperty("base.url"));
        extent.setSystemInfo("📁 Project", "TNC Store Automation Framework");
        extent.setSystemInfo("⏰ Test Execution Time", LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")));
    }

    private static String capitalizeModule(String module) {
        return module.substring(0, 1).toUpperCase() + module.substring(1);
    }

    public static ExtentTest startTest(String testName) {
        if (extent == null) {
            initReports();
        }
        ExtentTest extentTest = extent.createTest("🧪 " + testName);
        test.set(extentTest);
        return extentTest;
    }

    public static ExtentTest startTest(String testName, String description) {
        if (extent == null) {
            initReports();
        }
        ExtentTest extentTest = extent.createTest("🧪 " + testName, description);
        test.set(extentTest);
        return extentTest;
    }

    public static ExtentTest getTest() {
        return test.get();
    }

    public static void logTestResult(String testName, String status, String duration, String error) {
        testResults.add(new TestResult(testName, status, duration, error));
    }

    public static void generateExcelReport() {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet(capitalizeModule(currentModule) + " Test Results");

            // Create header
            Row headerRow = sheet.createRow(0);
            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerStyle.setFont(headerFont);

            String[] headers = {"Test Name", "Status", "Duration", "Timestamp", "Error Message"};
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }

            // Add test results
            for (int i = 0; i < testResults.size(); i++) {
                Row row = sheet.createRow(i + 1);
                TestResult result = testResults.get(i);

                row.createCell(0).setCellValue(result.testName);
                row.createCell(1).setCellValue(result.status);
                row.createCell(2).setCellValue(result.duration);
                row.createCell(3).setCellValue(result.timestamp);
                row.createCell(4).setCellValue(result.error != null ? result.error : "");
            }

            // Auto-size columns
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            // Save Excel file with module name
            String excelPath = "target/" + currentModule + "_TestResults_" +
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".xlsx";
            try (FileOutputStream fos = new FileOutputStream(excelPath)) {
                workbook.write(fos);
                System.out.println("📊 Excel report generated: " + excelPath);
            }

        } catch (IOException e) {
            System.err.println("❌ Error generating Excel report: " + e.getMessage());
        }
    }

    public static void endReporting() {
        if (extent != null) {
            extent.flush();
            generateExcelReport();
            String reportPath = "target/" + currentModule + "_report.html";
            System.out.println("📋 " + capitalizeModule(currentModule) + " module reports generated successfully!");
            System.out.println("🔗 HTML Report: " + reportPath);
        }
    }

    public static void logInfo(String message) {
        if (test.get() != null) {
            test.get().log(Status.INFO, "ℹ️ " + message);
        }
    }

    public static void logPass(String message) {
        if (test.get() != null) {
            test.get().log(Status.PASS, "✅ " + message);
        }
    }

    public static void logFail(String message) {
        if (test.get() != null) {
            test.get().log(Status.FAIL, "❌ " + message);
        }
    }

    public static void logWarning(String message) {
        if (test.get() != null) {
            test.get().log(Status.WARNING, "⚠️ " + message);
        }
    }

    public static void logSkip(String message) {
        if (test.get() != null) {
            test.get().log(Status.SKIP, "⏭️ " + message);
        }
    }
}
