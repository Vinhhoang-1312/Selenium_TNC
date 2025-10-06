package helpers;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    private static final Logger log = LoggerFactory.getLogger(ReportManager.class);

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
        } else if (testClassName.contains("TNCStoreTests")) {
            // For group-based testing, use general module name
            setModule("tnc-store");
        } else {
            setModule("general");
        }
    }

    // Set module based on TestNG groups being executed
    public static void setModuleFromGroups(String[] groups) {
        if (groups != null && groups.length > 0) {
            String primaryGroup = groups[0].toLowerCase();

            // Map groups to module names for reporting
            if (primaryGroup.contains("smoke")) {
                setModule("smoke-tests");
            } else if (primaryGroup.contains("authentication") || primaryGroup.contains("signup") ||
                    primaryGroup.contains("login") || primaryGroup.contains("forgot-password")) {
                setModule("authentication");
            } else if (primaryGroup.contains("userprofile") || primaryGroup.contains("profile")) {
                setModule("userprofile");
            } else if (primaryGroup.contains("cart")) {
                setModule("cart");
            } else if (primaryGroup.contains("search")) {
                setModule("search");
            } else if (primaryGroup.contains("productdetail")) {
                setModule("productdetail");
            } else if (primaryGroup.contains("checkout")) {
                setModule("checkout");
            } else if (primaryGroup.contains("regression")) {
                setModule("regression-tests");
            } else {
                setModule("tnc-store");
            }
        } else {
            setModule("tnc-store");
        }
    }

    public static void initReports() {
        if (extent == null) {
            // Ensure report/results directory exists
            java.io.File resultsDir = new java.io.File("report/results");
            if (!resultsDir.exists()) {
                resultsDir.mkdirs();
            }
            // Create main report in report/results folder
            String reportPath = "report/results/TNC_Store_Test_Report.html";
            ExtentSparkReporter sparkReporter = new ExtentSparkReporter(reportPath);

            // Enhanced report configuration for PM viewing
            sparkReporter.config().setDocumentTitle("TNC Store - Automation Test Execution Report");
            sparkReporter.config().setReportName("TNC Store Test Results Dashboard");
            sparkReporter.config().setTheme(Theme.STANDARD);

            // Add custom CSS for better presentation
            sparkReporter.config().setCss(".brand-logo { display: none; } .nav-wrapper { background-color: #2196F3; }");

            extent = new ExtentReports();
            extent.attachReporter(sparkReporter);

            // System information for PM
            extent.setSystemInfo("Project", "TNC Store E-commerce Website");
            extent.setSystemInfo("Website URL", "https://www.tncstore.vn/");
            extent.setSystemInfo("Test Environment", "QA Environment");
            extent.setSystemInfo("Test Type", "Functional Automation Testing");
            extent.setSystemInfo("Browser", "Chrome (Latest)");
            extent.setSystemInfo("Operating System", "Windows 11");
            extent.setSystemInfo("Java Version", "21");
            extent.setSystemInfo("Test Framework", "TestNG + Selenium WebDriver");
            extent.setSystemInfo("Report Generated", LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")));
            extent.setSystemInfo("Module Tested", currentModule.toUpperCase());

            log.info("Main Test Report initialized: {}", reportPath);
        }
    }

    public static ExtentTest startTest(String testName) {
        initReports();
        ExtentTest extentTest = extent.createTest(testName);
        test.set(extentTest);
        return extentTest;
    }

    public static void logInfo(String message) {
        if (test.get() != null) {
            test.get().info(message);
        }
    }

    public static void logPass(String message) {
        if (test.get() != null) {
            test.get().pass(message);
        }
    }

    public static void logFail(String message) {
        if (test.get() != null) {
            test.get().fail(message);
        }
    }

    public static void logWarning(String message) {
        if (test.get() != null) {
            test.get().warning(message);
        }
    }

    public static void logTestResult(String testName, String status, String duration, String error) {
        testResults.add(new TestResult(testName, status, duration, error));
    }

    public static void generateExcelReport() {
        try {
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            String fileName = "target/" + currentModule + "_TestResults_" + timestamp + ".xlsx";

            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("Test Results");

            // Create header
            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("Test Name");
            headerRow.createCell(1).setCellValue("Status");
            headerRow.createCell(2).setCellValue("Duration");
            headerRow.createCell(3).setCellValue("Error");
            headerRow.createCell(4).setCellValue("Timestamp");

            // Add test results
            int rowNum = 1;
            for (TestResult result : testResults) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(result.testName);
                row.createCell(1).setCellValue(result.status);
                row.createCell(2).setCellValue(result.duration);
                row.createCell(3).setCellValue(result.error != null ? result.error : "");
                row.createCell(4).setCellValue(result.timestamp);
            }

            // Auto-size columns
            for (int i = 0; i < 5; i++) {
                sheet.autoSizeColumn(i);
            }

            FileOutputStream fileOut = new FileOutputStream(fileName);
            workbook.write(fileOut);
            fileOut.close();
            workbook.close();

            log.info("Excel report generated: {}", fileName);
        } catch (IOException e) {
            log.error("Failed to generate Excel report: {}", e.getMessage());
        }
    }

    public static void flushReports() {
        if (extent != null) {
            extent.flush();
            generateExcelReport();
        }
    }

    public static ExtentTest getTest() {
        return test.get();
    }
}
