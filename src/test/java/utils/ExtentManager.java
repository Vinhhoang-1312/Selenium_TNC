package utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

public class ExtentManager {
    private static ExtentReports extent;
    private static ThreadLocal<ExtentTest> test = new ThreadLocal<>();

    public static void initReports() {
        if (extent == null) {
            createInstance();
        }
    }

    private static void createInstance() {
        String reportPath = ConfigReader.getProperty("report.path", "target/ExtentReport.html");
        ExtentSparkReporter sparkReporter = new ExtentSparkReporter(reportPath);

        // Configure the report
        sparkReporter.config().setTheme(Theme.STANDARD);
        sparkReporter.config().setDocumentTitle("TNC Store Automation Test Report");
        sparkReporter.config().setReportName("TNC Store Test Execution Report");
        sparkReporter.config().setTimeStampFormat("dd/MM/yyyy HH:mm:ss");

        extent = new ExtentReports();
        extent.attachReporter(sparkReporter);

        // Add system information
        extent.setSystemInfo("OS", System.getProperty("os.name"));
        extent.setSystemInfo("Java Version", System.getProperty("java.version"));
        extent.setSystemInfo("User", System.getProperty("user.name"));
        extent.setSystemInfo("Environment", "QA");
        extent.setSystemInfo("Browser", ConfigReader.getProperty("browser", "chrome"));
        extent.setSystemInfo("Base URL", ConfigReader.getProperty("base.url"));
        extent.setSystemInfo("Project", "TNC Store Automation");
    }

    public static ExtentTest startTest(String testName) {
        if (extent == null) {
            initReports();
        }
        ExtentTest extentTest = extent.createTest(testName);
        test.set(extentTest);
        return extentTest;
    }

    public static ExtentTest startTest(String testName, String description) {
        if (extent == null) {
            initReports();
        }
        ExtentTest extentTest = extent.createTest(testName, description);
        test.set(extentTest);
        return extentTest;
    }

    public static ExtentTest getTest() {
        return test.get();
    }

    public static void flush() {
        if (extent != null) {
            extent.flush();
        }
    }

    public static ExtentReports getExtentReports() {
        return extent;
    }
}
