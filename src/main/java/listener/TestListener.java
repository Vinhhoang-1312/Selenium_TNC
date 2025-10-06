package listener;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.ISuite;
import org.testng.ISuiteListener;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.OutputType;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.io.FileWriter;
import java.io.IOException;

/**
 * TestNG Listener for handling test execution events
 * This is a REUSABLE component that can be used across different projects
 * Simple implementation without dependencies on test-specific helper classes
 */
public class TestListener implements ITestListener, ISuiteListener {

    private static ExtentReports extent;
    private static ThreadLocal<ExtentTest> test = new ThreadLocal<>();
    private String reportPath;
    private String suiteNameForReport;

    @Override
    public void onStart(ISuite suite) {
        if (extent == null) {
            String xmlFileName = "unknown_suite";
            try {
                String filePath = suite.getXmlSuite().getFileName();
                if (filePath != null) {
                    java.io.File f = new java.io.File(filePath);
                    xmlFileName = f.getName().replace(".xml", "");
                }
            } catch (Exception e) {
            }
            suiteNameForReport = xmlFileName;
            String dateStr = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            reportPath = "target/allure-results/ExtentReport.html";
            try {
                Files.createDirectories(Paths.get("target/allure-results"));
                System.out.println("Created directory: target/allure-results");
            } catch (Exception e) {
                System.err.println("Cannot create target/allure-results directory: " + e.getMessage());
                e.printStackTrace();
            }
            ExtentSparkReporter sparkReporter = new ExtentSparkReporter(reportPath);
            sparkReporter.config().setDocumentTitle("TNC Store Automation Test Report");
            sparkReporter.config().setReportName("TNC Store Test Execution Report");
            extent = new ExtentReports();
            extent.attachReporter(sparkReporter);
            extent.setSystemInfo("Environment", "QA");
            extent.setSystemInfo("Tester", "Automation Team");
            extent.setSystemInfo("OS", System.getProperty("os.name"));
            extent.setSystemInfo("Java Version", System.getProperty("java.version"));
            System.out.println("📊 ExtentReport initialized: " + reportPath);
        }
    }

    @Override
    public void onTestStart(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        String className = result.getTestClass().getName();

        ExtentTest extentTest = extent.createTest(testName);
        test.set(extentTest);

        extentTest.info("🚀 Starting test: " + testName);
        extentTest.info("Class: " + className);

        System.out.println("🚀 Starting test: " + testName);
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        String testName = result.getMethod().getMethodName();

        ExtentTest extentTest = test.get();
        if (extentTest != null) {
            extentTest.log(Status.PASS, "✅ Test passed: " + testName);
        }

        System.out.println("✅ Test passed: " + testName);
    }

    @Override
    public void onTestFailure(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        String errorMessage = result.getThrowable() != null ? result.getThrowable().getMessage() : "Unknown error";
        ExtentTest extentTest = test.get();
        if (extentTest != null) {
            extentTest.log(Status.FAIL, "❌ Test failed: " + testName);
            extentTest.log(Status.FAIL, "Error: " + errorMessage);
            if (result.getThrowable() != null) {
                extentTest.log(Status.FAIL, result.getThrowable());
            }
            Object testInstance = result.getInstance();
            try {
                java.lang.reflect.Field driverField = testInstance.getClass().getDeclaredField("driver");
                driverField.setAccessible(true);
                Object driverObj = driverField.get(testInstance);
                if (driverObj instanceof TakesScreenshot) {
                    TakesScreenshot ts = (TakesScreenshot) driverObj;
                    byte[] screenshot = ts.getScreenshotAs(OutputType.BYTES);
                    String dateStr = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                    String screenshotDir = "target/allure-results-raw/screenshots";
                    Files.createDirectories(Paths.get(screenshotDir));
                    String screenshotPath = String.format(screenshotDir + "/%s_%s_%s.png", suiteNameForReport, testName, dateStr);
                    Files.write(Paths.get(screenshotPath), screenshot);
                    // For compactness we don't copy images elsewhere; keep them under target/allure-results/screenshots
                    extentTest.addScreenCaptureFromPath(screenshotPath);
                }
            } catch (Exception ex) {
                extentTest.log(Status.WARNING, "Không thể chụp screenshot: " + ex.getMessage());
            }
        }
        System.out.println("❌ Test failed: " + testName);
        System.out.println("Error: " + errorMessage);
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        String skipReason = result.getThrowable() != null ? result.getThrowable().getMessage() : "Unknown reason";

        ExtentTest extentTest = test.get();
        if (extentTest != null) {
            extentTest.log(Status.SKIP, "⚠️ Test skipped: " + testName);
            extentTest.log(Status.SKIP, "Reason: " + skipReason);
        }

        System.out.println("⚠️ Test skipped: " + testName);
    }

    @Override
    public void onFinish(ISuite suite) {
        try {
            String allureResultsDir = "target/allure-results";
            Files.createDirectories(Paths.get(allureResultsDir));
            // We keep only essential HTML files and an environment.properties metadata file in target/allure-results
            String envFile = allureResultsDir + "/environment.properties";
            String dateStr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            // Write metadata and screenshots to the raw results folder so the public target/allure-results remains HTML-only
            String browser = System.getProperty("browser", "chrome");
            String os = System.getProperty("os.name");
            String javaVersion = System.getProperty("java.version");
            String suiteName = suite.getName();
            String tester = System.getProperty("user.name");
            // place the environment.properties into the raw results folder
            String rawDir = "target/allure-results-raw";
            try {
                Files.createDirectories(Paths.get(rawDir));
            } catch (IOException ioe) {
                System.err.println("[ERROR] Cannot create raw results dir: " + ioe.getMessage());
            }
            String rawEnvFile = rawDir + "/environment.properties";
            try (FileWriter fw = new FileWriter(rawEnvFile)) {
                fw.write("Suite=" + suiteName + "\n");
                fw.write("Run Date=" + dateStr + "\n");
                fw.write("Browser=" + browser + "\n");
                fw.write("OS=" + os + "\n");
                fw.write("Java Version=" + javaVersion + "\n");
                fw.write("Tester=" + tester + "\n");
                fw.write("Project=TNC Store Automation\n");
            }
            System.out.println("[INFO] Allure environment.properties generated (raw): " + rawEnvFile);
        } catch (IOException e) {
            System.err.println("[ERROR] Cannot write Allure environment.properties: " + e.getMessage());
        }
        if (extent != null) {
            try {
                System.out.println("[DEBUG] Flushing ExtentReports to file...");
                extent.flush();
                System.out.println("[DEBUG] Flushed ExtentReports. Checking file existence...");
                if (Files.exists(Paths.get(reportPath))) {
                    System.out.println("[SUCCESS] Extent report generated at: " + reportPath);
                } else {
                    System.err.println("[ERROR] Extent report was NOT created at: " + reportPath);
                }
            } catch (Exception e) {
                System.err.println("[ERROR] Exception while flushing ExtentReports: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    public static void cleanup() {
        test.remove();
    }
}
