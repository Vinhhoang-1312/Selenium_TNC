package utils;

import io.qameta.allure.Allure;
import org.testng.asserts.SoftAssert;

import java.io.ByteArrayInputStream;

public class AllureSoftAssert extends SoftAssert {

    @Override
    public void assertEquals(Object actual, Object expected, String message) {
        try {
            super.assertEquals(actual, expected, message);
            Allure.step("✓ PASSED: " + message, () -> {
                Allure.addAttachment("Expected", String.valueOf(expected));
                Allure.addAttachment("Actual", String.valueOf(actual));
            });
        } catch (AssertionError e) {
            Allure.step("✗ FAILED: " + message, () -> {
                Allure.addAttachment("Expected", String.valueOf(expected));
                Allure.addAttachment("Actual", String.valueOf(actual));
                Allure.addAttachment("Error", e.getMessage());
            });
            throw e;
        }
    }

    @Override
    public void assertEquals(Object actual, Object expected) {
        assertEquals(actual, expected, "Assertion");
    }

    @Override
    public void assertTrue(boolean condition, String message) {
        try {
            super.assertTrue(condition, message);
            Allure.step("✓ PASSED: " + message, () -> {
                Allure.addAttachment("Condition", "TRUE");
            });
        } catch (AssertionError e) {
            Allure.step("✗ FAILED: " + message, () -> {
                Allure.addAttachment("Expected", "TRUE");
                Allure.addAttachment("Actual", "FALSE");
                Allure.addAttachment("Error", e.getMessage());
            });
            throw e;
        }
    }

    @Override
    public void assertTrue(boolean condition) {
        assertTrue(condition, "Assertion should be true");
    }

    @Override
    public void assertFalse(boolean condition, String message) {
        try {
            super.assertFalse(condition, message);
            Allure.step("✓ PASSED: " + message, () -> {
                Allure.addAttachment("Condition", "FALSE");
            });
        } catch (AssertionError e) {
            Allure.step("✗ FAILED: " + message, () -> {
                Allure.addAttachment("Expected", "FALSE");
                Allure.addAttachment("Actual", "TRUE");
                Allure.addAttachment("Error", e.getMessage());
            });
            throw e;
        }
    }

    @Override
    public void assertNotNull(Object object, String message) {
        try {
            super.assertNotNull(object, message);
            Allure.step("✓ PASSED: " + message, () -> {
                Allure.addAttachment("Object", String.valueOf(object));
            });
        } catch (AssertionError e) {
            Allure.step("✗ FAILED: " + message, () -> {
                Allure.addAttachment("Expected", "NOT NULL");
                Allure.addAttachment("Actual", "NULL");
                Allure.addAttachment("Error", e.getMessage());
            });
            throw e;
        }
    }

    @Override
    public void assertNotNull(Object object) {
        assertNotNull(object, "Object should not be null");
    }

    @Override
    public void assertNull(Object object, String message) {
        try {
            super.assertNull(object, message);
            Allure.step("✓ PASSED: " + message);
        } catch (AssertionError e) {
            Allure.step("✗ FAILED: " + message, () -> {
                Allure.addAttachment("Expected", "NULL");
                Allure.addAttachment("Actual", String.valueOf(object));
                Allure.addAttachment("Error", e.getMessage());
            });
            throw e;
        }
    }

    @Override
    public void assertNull(Object object) {
        assertNull(object, "Object should be null");
    }

    @Override
    public void assertAll() {
        try {
            super.assertAll();
            Allure.step("✓ All soft assertions passed");
        } catch (AssertionError e) {
            Allure.step("✗ Soft assertions failed", () -> {
                Allure.addAttachment("Assertion Errors", "text/plain",
                    new ByteArrayInputStream(e.getMessage().getBytes()), ".txt");
            });
            throw e;
        }
    }
}
