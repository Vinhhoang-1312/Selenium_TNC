# Authentication Module

## Mô tả
Module Authentication chịu trách nhiệm test các chức năng liên quan đến xác thực người dùng trên TNC Store.

## Test Cases đã implement

### Sign Up Tests (Đăng ký)
- **AUTH-SU-01**: Đăng ký với thông tin hợp lệ ✅
- **AUTH-SU-02**: Đăng ký với email đã tồn tại ✅
- **AUTH-SU-03**: Đăng ký với email không hợp lệ ✅
- **AUTH-SU-04**: Đăng ký với các trường bắt buộc để trống ✅
- **AUTH-SU-05**: Đăng ký với mật khẩu yếu/ngắn ✅

### Sign In Tests (Đăng nhập)
- **AUTH-SI-01**: Đăng nhập với email và mật khẩu hợp lệ ✅
- **AUTH-SI-02**: Đăng nhập với mật khẩu sai ✅
- **AUTH-SI-03**: Đăng nhập với email chưa đăng ký ✅
- **AUTH-SI-04**: Đăng nhập với định dạng email không hợp lệ ✅

### Forgot Password Tests (Quên mật khẩu)
- **AUTH-FP-01**: Reset mật khẩu với email đã đăng ký hợp lệ ✅
- **AUTH-FP-02**: Reset mật khẩu với email chưa đăng ký ✅
- **AUTH-FP-03**: Reset mật khẩu với định dạng email không hợp lệ ✅

## Files trong module
- `AuthenticationPage.java` - Page Object Model
- `AuthenticationTest.java` - Test cases
- `AuthenticationTestData.java` - Test data

## Locators cần cập nhật
⚠️ **QUAN TRỌNG**: Các locators trong `AuthenticationPage.java` hiện tại chỉ là template. Cần cập nhật với locators thực tế từ website TNC Store.

### Locators cần kiểm tra:
- Login email field
- Login password field  
- Login button
- Register name field
- Register email field
- Register password field
- Register button
- Error message elements
- Navigation links

## Chạy test Authentication
```bash
# Chạy tất cả authentication tests
mvn test -Dtest="modules.authentication.AuthenticationTest"

# Chạy test case cụ thể
mvn test -Dtest="modules.authentication.AuthenticationTest#testRegisterWithValidData"
```

## Test Data
Test data được định nghĩa trong `AuthenticationTestData.java` với các categories:
- Valid data
- Invalid email formats
- Weak passwords
- Non-existing emails
- Expected error messages

## Notes
- Module này đã sẵn sàng để test sau khi cập nhật locators
- Tất cả test cases theo đúng specification đã cung cấp
- Có screenshot tự động khi test fail
- Integrated với ExtentReports
