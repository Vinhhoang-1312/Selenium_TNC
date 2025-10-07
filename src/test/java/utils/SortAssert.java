package utils;

import listener.TestListener;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import io.qameta.allure.Allure;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * Lightweight assertion helper that reports actual vs expected to ExtentReports and Allure.
 * Use this in tests when you want richer reporting of the comparison.
 */
public class SortAssert {

    public static void assertEquals(Object actual, Object expected, String message) {
        boolean eq = Objects.equals(actual, expected);
        ExtentTest t = TestListener.getCurrentTest();
        String actualStr = String.valueOf(actual);
        String expectedStr = String.valueOf(expected);
        String shortMsg = (message == null ? "Assertion" : message);

        if (eq) {
            // success - log a small note to extent if available and a step in Allure
            if (t != null) {
                t.log(Status.PASS, "Assertion passed: " + shortMsg);
            }
            try {
                Allure.step("Assertion passed: " + shortMsg, () -> {
                    // keep step lightweight for readability
                });
            } catch (Exception ignore) {
            }
            return;
        }
        // Failure: prepare details
        String failMsg = shortMsg + "\nExpected: " + expectedStr + "\nActual: " + actualStr;

        // Log to Extent with highlighted block for easier scanning in the HTML report
        if (t != null) {
            try {
                // Use simple HTML to highlight expected vs actual
                String html = "<div style=\"padding:8px;border-radius:6px;background:#fff0f0;border:1px solid #ffcccc;\">"
                        + "<b>" + escapeHtml(shortMsg) + "</b><br/>"
                        + "<div style=\"margin-top:6px;\"><b>Expected:</b><pre style=\"white-space:pre-wrap;\">" + escapeHtml(expectedStr) + "</pre></div>"
                        + "<div style=\"margin-top:6px;\"><b>Actual:</b><pre style=\"white-space:pre-wrap;\">" + escapeHtml(actualStr) + "</pre></div>"
                        + "</div>";
                t.fail(html);
                t.log(Status.FAIL, "Assertion failed: " + shortMsg);
            } catch (Exception e) {
                // fallback to plain text logging
                t.log(Status.FAIL, failMsg);
            }
        }

        // Add step-by-step information into Allure for readability
        try {
            Allure.step("Assertion failed: " + shortMsg, () -> {
                Allure.addAttachment("Expected", "text/plain", new ByteArrayInputStream(expectedStr.getBytes(StandardCharsets.UTF_8)), ".txt");
                Allure.addAttachment("Actual", "text/plain", new ByteArrayInputStream(actualStr.getBytes(StandardCharsets.UTF_8)), ".txt");
            });
        } catch (Exception ignore) {
        }

        // Throw an AssertionError to fail the test after reporting
        throw new AssertionError(failMsg);
    }

    public static void assertTrue(boolean condition, String message) {
        ExtentTest t = TestListener.getCurrentTest();
        String shortMsg = (message == null ? "Expected condition to be true" : message);
        if (condition) {
            if (t != null) {
                t.log(Status.PASS, "Assertion passed: " + shortMsg);
            }
            try {
                Allure.step("Assertion passed: " + shortMsg, () -> {});
            } catch (Exception ignore) {
            }
            return;
        }
        String failMsg = shortMsg;
        if (t != null) {
            t.log(Status.FAIL, failMsg);
        }
        try {
            Allure.step("Assertion failed: " + shortMsg, () -> {
                Allure.addAttachment("AssertionFailure", "text/plain", new ByteArrayInputStream(failMsg.getBytes(StandardCharsets.UTF_8)), ".txt");
            });
        } catch (Exception ignore) {
        }
        throw new AssertionError(failMsg);
    }

    // Minimal HTML escaping so logging doesn't break the report markup
    private static String escapeHtml(String s) {
        if (s == null) return "null";
        return s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;").replace("\"", "&quot;").replace("'", "&#39;");
    }

}
