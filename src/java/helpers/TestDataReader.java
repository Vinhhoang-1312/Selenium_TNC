package helpers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;

public class TestDataReader {
    private static JsonNode testData;
    private static final String TEST_DATA_PATH = "src/test/resources/testdata.json";

    static {
        loadTestData();
    }

    private static void loadTestData() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            testData = mapper.readTree(new File(TEST_DATA_PATH));
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to load test data from: " + TEST_DATA_PATH);
        }
    }

    public static String getString(String jsonPath) {
        String[] keys = jsonPath.split("\\.");
        JsonNode node = testData;

        for (String key : keys) {
            node = node.get(key);
            if (node == null) {
                throw new RuntimeException("Key not found: " + jsonPath);
            }
        }

        return node.asText();
    }

    public static int getInt(String jsonPath) {
        String[] keys = jsonPath.split("\\.");
        JsonNode node = testData;

        for (String key : keys) {
            node = node.get(key);
            if (node == null) {
                throw new RuntimeException("Key not found: " + jsonPath);
            }
        }

        return node.asInt();
    }

    public static boolean getBoolean(String jsonPath) {
        String[] keys = jsonPath.split("\\.");
        JsonNode node = testData;

        for (String key : keys) {
            node = node.get(key);
            if (node == null) {
                throw new RuntimeException("Key not found: " + jsonPath);
            }
        }

        return node.asBoolean();
    }

    public static JsonNode getJsonNode(String jsonPath) {
        String[] keys = jsonPath.split("\\.");
        JsonNode node = testData;

        for (String key : keys) {
            node = node.get(key);
            if (node == null) {
                throw new RuntimeException("Key not found: " + jsonPath);
            }
        }

        return node;
    }

    // Convenience methods for common test data
    public static String getValidEmail() {
        return getString("authentication.validData.email");
    }

    public static String getValidPassword() {
        return getString("authentication.validData.password");
    }

    public static String getValidName() {
        return getString("authentication.validData.name");
    }

    public static String getInvalidEmail() {
        return getString("authentication.invalidData.email");
    }
}
