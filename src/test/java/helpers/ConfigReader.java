package helpers;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 📋 Config Reader - Read configuration from properties file
 */
public class ConfigReader {
    private static Properties properties;
    private static final String CONFIG_FILE_PATH = "src/test/resources/config.properties";
    private static final Logger log = LoggerFactory.getLogger(ConfigReader.class);

    static {
        loadProperties();
    }

    /**
     * Load properties from config file
     */
    private static void loadProperties() {
        try {
            properties = new Properties();
            FileInputStream fis = new FileInputStream(CONFIG_FILE_PATH);
            properties.load(fis);
            fis.close();
            log.info("Configuration loaded successfully");
        } catch (IOException e) {
            log.error("Failed to load configuration file: {}", e.getMessage());
            // Initialize with default properties if file not found
            properties = new Properties();
            setDefaultProperties();
        }
    }

    /**
     * Set default properties if config file not found
     */
    private static void setDefaultProperties() {
        properties.setProperty("browser", "chrome");
        properties.setProperty("base.url", "https://tncstore.vn");
        properties.setProperty("implicit.wait", "15");
        properties.setProperty("explicit.wait", "20");
        properties.setProperty("page.load.timeout", "60");
        properties.setProperty("screenshot.on.failure", "true");
        properties.setProperty("headless", "false");
        log.info("Default configuration applied");
    }

    /**
     * Get property value
     *
     * @param key Property key
     * @return Property value
     */
    public static String getProperty(String key) {
        String value = properties.getProperty(key);
        if (value == null) {
            log.warn("Property not found: {}", key);
        }
        return value;
    }

    /**
     * Get property value with default
     *
     * @param key          Property key
     * @param defaultValue Default value if key not found
     * @return Property value or default
     */
    public static String getProperty(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }

    /**
     * Get integer property
     *
     * @param key          Property key
     * @param defaultValue Default value
     * @return Integer value
     */
    public static int getIntProperty(String key, int defaultValue) {
        try {
            String value = properties.getProperty(key);
            return value != null ? Integer.parseInt(value) : defaultValue;
        } catch (NumberFormatException e) {
            log.warn("Invalid number format for property: {}", key);
            return defaultValue;
        }
    }

    /**
     * Get boolean property
     *
     * @param key          Property key
     * @param defaultValue Default value
     * @return Boolean value
     */
    public static boolean getBooleanProperty(String key, boolean defaultValue) {
        try {
            String value = properties.getProperty(key);
            return value != null ? Boolean.parseBoolean(value) : defaultValue;
        } catch (Exception e) {
            log.warn("Invalid boolean format for property: {}", key);
            return defaultValue;
        }
    }

    // Convenience methods for common configurations
    public static String getBrowser() {
        return getProperty("browser", "chrome");
    }

    public static String getBaseUrl() {
        return getProperty("base.url", "https://tncstore.vn");
    }

    public static int getImplicitWait() {
        return getIntProperty("implicit.wait", 15);
    }

    public static int getExplicitWait() {
        return getIntProperty("explicit.wait", 20);
    }

    public static boolean isScreenshotOnFailure() {
        return getBooleanProperty("screenshot.on.failure", true);
    }

    public static boolean isHeadless() {
        return getBooleanProperty("headless", false);
    }
}
