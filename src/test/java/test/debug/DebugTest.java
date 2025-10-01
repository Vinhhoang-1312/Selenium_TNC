package test.debug;

import helpers.BaseTest;
import org.testng.annotations.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DebugTest extends BaseTest {
    private static final Logger log = LoggerFactory.getLogger(DebugTest.class);

    @Test
    public void testDriverSetup() {
        try {
            log.info("=== DEBUG TEST START ===");

            if (driver == null) {
                log.error("❌ Driver is NULL in test method!");
                throw new RuntimeException("Driver is null - BaseTest.setUp() failed");
            }

            log.info("✅ Driver is available: {}", driver.getClass().getSimpleName());
            log.info("✅ Current URL: {}", driver.getCurrentUrl());
            log.info("✅ Page title: {}", driver.getTitle());

            log.info("=== DEBUG TEST PASSED ===");

        } catch (Exception e) {
            log.error("❌ Debug test failed: {}", e.getMessage(), e);
            throw e;
        }
    }
}
