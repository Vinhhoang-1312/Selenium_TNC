package helpers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUtils {
    public static String extractField(String json, String fieldName) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode node = mapper.readTree(json);
            JsonNode field = node.get(fieldName);
            return field != null ? field.asText() : null;
        } catch (Exception e) {
            return null;
        }
    }
}

