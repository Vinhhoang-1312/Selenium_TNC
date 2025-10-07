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

import io.qameta.allure.Allure;

public class TestListener implements ITestListener, ISuiteListener {

    private static ExtentReports extent;
    private static final ThreadLocal<ExtentTest> test = new ThreadLocal<>();
    private static final ThreadLocal<Boolean> screenshotTaken = ThreadLocal.withInitial(() -> Boolean.FALSE);
    private String reportPath;
    private String suiteNameForReport;

    @Override
    public void onStart(ISuite suite) {
        // Initialize ExtentReports once in a thread-safe way
        synchronized (TestListener.class) {
            if (extent == null) {
                String xmlFileName = "unknown_suite";
                try {
                    String filePath = suite.getXmlSuite().getFileName();
                    if (filePath != null) {
                        java.io.File f = new java.io.File(filePath);
                        xmlFileName = f.getName().replace(".xml", "");
                    }
                } catch (Exception e) {
                    System.err.println("[WARN] Could not determine suite xml file name: " + e.toString());
                }
                suiteNameForReport = xmlFileName;
                reportPath = "target/allure-results/ExtentReport.html";
                try {
                    Files.createDirectories(Paths.get("target/allure-results"));
                    System.out.println("Created directory: target/allure-results");
                } catch (Exception e) {
                    System.err.println("Cannot create target/allure-results directory: " + e.getMessage());
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
    }

    @Override
    public void onTestStart(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        String className = result.getTestClass().getName();

        // Include thread name to help distinguish parallel runs in the report
        String threadName = Thread.currentThread().getName();
        ExtentTest extentTest = extent.createTest(testName + " (" + className + ") [" + threadName + "]");
        test.set(extentTest);
        screenshotTaken.set(Boolean.FALSE);

        // Attach Allure labels if the test method contains common Allure annotations
        try {
            java.lang.reflect.Method m = result.getMethod().getConstructorOrMethod().getMethod();
            if (m != null) {
                // Product (we map Allure's Feature to Product in the report hierarchy)
                if (m.isAnnotationPresent(io.qameta.allure.Feature.class)) {
                    String product = m.getAnnotation(io.qameta.allure.Feature.class).value();
                    if (product != null && !product.isEmpty()) {
                        extentTest.assignCategory("Product: " + product);
                        try { Allure.label("product", product); } catch (Exception ignore) {}
                    }
                }
                // Epic
                if (m.isAnnotationPresent(io.qameta.allure.Epic.class)) {
                    String epic = m.getAnnotation(io.qameta.allure.Epic.class).value();
                    if (epic != null && !epic.isEmpty()) {
                        extentTest.assignCategory("Epic: " + epic);
                        try { Allure.label("epic", epic); } catch (Exception ignore) {}
                    }
                }
                // Story
                if (m.isAnnotationPresent(io.qameta.allure.Story.class)) {
                    String story = m.getAnnotation(io.qameta.allure.Story.class).value();
                    if (story != null && !story.isEmpty()) {
                        extentTest.assignCategory("Story: " + story);
                        try { Allure.label("story", story); } catch (Exception ignore) {}
                    }
                }
                // Severity
                if (m.isAnnotationPresent(io.qameta.allure.Severity.class)) {
                    io.qameta.allure.SeverityLevel sev = m.getAnnotation(io.qameta.allure.Severity.class).value();
                    if (sev != null) {
                        String sevStr = sev.name();
                        extentTest.assignCategory("Severity: " + sevStr);
                        try { Allure.label("severity", sevStr); } catch (Exception ignore) {}
                    }
                }
            }
        } catch (Exception ignore) {
            // non-fatal - don't break test start if annotations aren't present
        }

        extentTest.info("🚀 Starting test: " + testName + " on thread " + threadName);
        extentTest.info("Class: " + className);

        System.out.println("🚀 Starting test: " + testName + " [" + threadName + "]");
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

    private void captureScreenshotOnce(ITestResult result, ExtentTest extentTest) {
        try {
            if (screenshotTaken.get() != null && screenshotTaken.get()) {
                return; // already captured
            }
            Object testInstance = result.getInstance();
            java.lang.reflect.Field driverField;
            try {
                driverField = testInstance.getClass().getDeclaredField("driver");
            } catch (NoSuchFieldException nsf) {
                // try superclass (some BaseTest classes keep it on parent)
                try {
                    driverField = testInstance.getClass().getSuperclass().getDeclaredField("driver");
                } catch (Exception e) {
                    driverField = null;
                }
            }
            if (driverField != null) {
                driverField.setAccessible(true);
                Object driverObj = driverField.get(testInstance);
                if (driverObj instanceof TakesScreenshot) {
                    TakesScreenshot ts = (TakesScreenshot) driverObj;
                    byte[] screenshot = ts.getScreenshotAs(OutputType.BYTES);
                    String dateStr = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                    String screenshotDir = "target/allure-results-raw/screenshots";
                    Files.createDirectories(Paths.get(screenshotDir));
                    String screenshotPath = String.format(screenshotDir + "/%s_%s_%s.png", suiteNameForReport, result.getMethod().getMethodName(), dateStr);
                    Files.write(Paths.get(screenshotPath), screenshot);
                    // attach to Extent
                    try {
                        extentTest.addScreenCaptureFromPath(screenshotPath);
                    } catch (Exception e) {
                        extentTest.log(Status.WARNING, "Failed to attach screenshot to Extent: " + e.getMessage());
                    }
                    // attach to Allure once as well
                    try {
                        Allure.addAttachment("Screenshot - " + result.getMethod().getMethodName(), "image/png", new java.io.ByteArrayInputStream(screenshot), ".png");
                    } catch (Exception ex) {
                        // non-fatal
                    }
                    screenshotTaken.set(Boolean.TRUE);
                }
            }
        } catch (Exception ex) {
            if (extentTest != null) {
                extentTest.log(Status.WARNING, "Không thể chụp screenshot: " + ex.getMessage());
            }
        }
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
            // Capture screenshot only once per test and attach both to Extent + Allure
            captureScreenshotOnce(result, extentTest);
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
            // Write metadata and screenshots to the raw results folder so the public target/allure-results remains HTML-only
            String dateStr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
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
            }
        }
    }

    public static void cleanup() {
        test.remove();
        screenshotTaken.remove();
    }

    // Helper for other classes (e.g. custom asserts) to access the current ExtentTest in a thread-safe way
    public static ExtentTest getCurrentTest() {
        return test.get();
    }
}
