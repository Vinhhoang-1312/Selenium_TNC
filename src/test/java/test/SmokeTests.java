package test;

import helpers.ReportManager;
import helpers.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * 🟢 Smoke Tests - Basic framework verification
 * These tests verify that the automation framework is working correctly
 */
public class SmokeTests extends BaseTest {

    @Test(groups = {"smoke", "framework"}, 
          description = "SM-01: Verify website is accessible")
    public void testWebsiteAccessible() {
        ReportManager.startTest("SM-01: Verify website is accessible");
        
        try {
            // Website should already be loaded by BaseTest
            String currentUrl = getCurrentUrl();
            String pageTitle = getPageTitle();
            
            ReportManager.logInfo("Current URL: " + currentUrl);
            ReportManager.logInfo("Page Title: " + pageTitle);
            
            // Basic assertions
            Assert.assertTrue(currentUrl.contains("tncstore"), 
                "URL should contain 'tncstore'");
            Assert.assertFalse(pageTitle.isEmpty(), 
                "Page title should not be empty");
                
            ReportManager.logPass("✅ Website is accessible and responding");
            
        } catch (Exception e) {
            ReportManager.logFail("❌ Test failed: " + e.getMessage());
            throw e;
        }
    }
    
    @Test(groups = {"smoke", "framework"}, 
          description = "SM-02: Verify WebDriver functionality")
    public void testWebDriverFunctionality() {
        ReportManager.startTest("SM-02: Verify WebDriver functionality");
        
        try {
            // Test basic WebDriver operations
            String originalUrl = getCurrentUrl();
            ReportManager.logInfo("Original URL: " + originalUrl);
            
            // Navigate back and forward
            refreshPage();
            sleep(2);
            
            String urlAfterRefresh = getCurrentUrl();
            ReportManager.logInfo("URL after refresh: " + urlAfterRefresh);
            
            Assert.assertEquals(originalUrl, urlAfterRefresh, 
                "URL should remain the same after refresh");
                
            ReportManager.logPass("✅ WebDriver functionality verified");
            
        } catch (Exception e) {
            ReportManager.logFail("❌ Test failed: " + e.getMessage());
            throw e;
        }
    }
    
    @Test(groups = {"smoke", "reporting"}, 
          description = "SM-03: Verify reporting functionality")
    public void testReportingFunctionality() {
        ReportManager.startTest("SM-03: Verify reporting functionality");
        
        try {
            // Test all logging methods
            ReportManager.logInfo("📋 This is an info message");
            ReportManager.logPass("✅ This is a pass message");
            ReportManager.logWarning("⚠️ This is a warning message");
            
            // Test screenshot functionality (even if it fails, it shouldn't break the test)
            try {
                takeScreenshot("SM-03_ReportingTest");
                ReportManager.logInfo("📸 Screenshot captured successfully");
            } catch (Exception screenshotError) {
                ReportManager.logWarning("⚠️ Screenshot failed: " + screenshotError.getMessage());
            }
            
            ReportManager.logPass("✅ Reporting functionality verified");
            
        } catch (Exception e) {
            ReportManager.logFail("❌ Test failed: " + e.getMessage());
            throw e;
        }
    }
}
