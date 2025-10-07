package data;

public class TestUser {
    public final String name;
    public final String email;
    public final String password;

    public TestUser(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    /**
     * Creates a unique test user with timestamp to avoid duplicates
     */
    public static TestUser createUniqueUser(String prefix) {
        String timestamp = java.time.LocalDateTime.now()
                .format(java.time.format.DateTimeFormatter.ofPattern("ddHHmmss"));
        String uniqueName = prefix + "_User_" + timestamp;
        String uniqueEmail = prefix.toLowerCase() + "_" + timestamp + "@test.com";
        String password = "Test123456";
        return new TestUser(uniqueName, uniqueEmail, password);
    }
}

