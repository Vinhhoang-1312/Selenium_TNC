package data;

public class AuthenticationTestData {

    public static final String VALID_NAME = "John Doe";
    public static final String VALID_EMAIL = "john5@test.com";
    public static final String VALID_PASSWORD = "Abc12345";

    public static final String VALID_NAME_2 = "Jane Smith";
    public static final String VALID_EMAIL_2 = "jane@test.com";
    public static final String VALID_PASSWORD_2 = "Pass123456";

    public static final String VALID_NAME_3 = "Mike Johnson";
    public static final String VALID_EMAIL_3 = "mike@test.com";
    public static final String VALID_PASSWORD_3 = "SecurePass789";

    public static final String EXISTING_EMAIL = "john@test.com";
    public static final String EXISTING_EMAIL_2 = "admin@tncstore.vn";
    public static final String EXISTING_EMAIL_3 = "test@tncstore.vn";

    public static final String INVALID_EMAIL_1 = "test@";
    public static final String INVALID_EMAIL_2 = "abc.com";
    public static final String INVALID_EMAIL_3 = "1234";

    public static final String WEAK_PASSWORD_1 = "123";
    public static final String WEAK_PASSWORD_2 = "abc";
    public static final String WEAK_PASSWORD_3 = "11111";

    public static final String INVALID_PASSWORD = "invalid123";

    public static final String WRONG_PASSWORD_1 = "WrongPass123";
    public static final String WRONG_PASSWORD_2 = "IncorrectPwd";
    public static final String WRONG_PASSWORD_3 = "BadPassword!@#";

    public static final String NON_EXISTING_EMAIL_1 = "abc@fake.com";
    public static final String NON_EXISTING_EMAIL_2 = "xyz@test.com";
    public static final String NON_EXISTING_EMAIL_3 = "hello@none.com";

    public static final String FORGOT_NON_EXISTING_EMAIL_1 = "abc@fake.com";
    public static final String FORGOT_NON_EXISTING_EMAIL_2 = "xyz@123.com";
    public static final String FORGOT_NON_EXISTING_EMAIL_3 = "test@none.com";

    public static final String FORGOT_INVALID_EMAIL_1 = "abc";
    public static final String FORGOT_INVALID_EMAIL_2 = "123";
    public static final String FORGOT_INVALID_EMAIL_3 = "abc.com";

    public static final String EMAIL_EXISTS_ERROR = "Email đã được sử dụng Vui lòng đăng ký lại !";
    public static final String INVALID_EMAIL_ERROR = "Email không hợp lệ";
    public static final String REQUIRED_FIELD_ERROR = "This field is required";
    public static final String INVALID_PASSWORD_ERROR = "Invalid password";
    public static final String ACCOUNT_NOT_FOUND_ERROR = "Account not found";
    public static final String EMAIL_NOT_REGISTERED_ERROR = "Email not registered";
    public static final String INVALID_EMAIL_VALIDATION_ERROR = "Invalid email";

    public static class TestUser {
        public final String name;
        public final String email;
        public final String password;
        public TestUser(String name, String email, String password) {
            this.name = name;
            this.email = email;
            this.password = password;
        }
    }
    public static TestUser createUniqueUser(String prefix) {
        String timestamp = java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        String uniqueName = prefix + "_User_" + timestamp;
        String uniqueEmail = prefix.toLowerCase() + "_" + timestamp + "@test.com";
        String password = "Test123456";
        return new TestUser(uniqueName, uniqueEmail, password);
    }
}
