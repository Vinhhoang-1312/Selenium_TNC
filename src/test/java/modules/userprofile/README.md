# User Profile Module

## Mô tả
Module User Profile chịu trách nhiệm test các chức năng quản lý thông tin cá nhân của người dùng.

## Test Cases cần implement

### Profile View Tests
- **UP-VW-01**: Xem thông tin profile khi đã đăng nhập
- **UP-VW-02**: Redirect đến login khi chưa đăng nhập

### Profile Update Tests  
- **UP-UD-01**: Cập nhật thông tin cá nhân hợp lệ
- **UP-UD-02**: Cập nhật với email đã tồn tại
- **UP-UD-03**: Cập nhật với dữ liệu không hợp lệ
- **UP-UD-04**: Cập nhật với trường bắt buộc để trống

### Password Change Tests
- **UP-PC-01**: Đổi mật khẩu với mật khẩu cũ đúng
- **UP-PC-02**: Đổi mật khẩu với mật khẩu cũ sai
- **UP-PC-03**: Đổi mật khẩu với mật khẩu mới yếu

## Files cần tạo
- `UserProfilePage.java` - Page Object Model
- `UserProfileTest.java` - Test cases
- `UserProfileTestData.java` - Test data

## Locators cần identify
- Profile form fields (name, email, phone, address)
- Save/Update buttons
- Change password form
- Success/Error messages

## Người phụ trách
- **Assigned to**: [TÊN THÀNH VIÊN]
- **Status**: ⏳ Chờ implement

## Notes
- Cần login trước khi test các chức năng profile
- Test data nên sử dụng random data để tránh conflict
