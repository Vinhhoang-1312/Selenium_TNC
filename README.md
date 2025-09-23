# TNC Store Automation Testing Project

## Tổng quan dự án
Dự án automation testing cho website TNC Store (https://www.tncstore.vn/) được thiết kế theo module architecture để hỗ trợ làm việc nhóm hiệu quả.

## Cấu trúc Modules
Dự án được chia thành 6 modules chính:

1. **Authentication** - Đăng nhập, đăng ký, quên mật khẩu
2. **User Profile** - Quản lý thông tin cá nhân
3. **Search** - Tìm kiếm sản phẩm
4. **Product Detail** - Chi tiết sản phẩm
5. **Cart** - Giỏ hàng
6. **Checkout** - Thanh toán

## Cấu trúc thư mục
```
src/test/java/
├── base/                 # Base classes cho tất cả modules
├── modules/              # Chứa 6 modules
│   ├── authentication/   # Module Authentication (HOÀN THÀNH)
│   ├── userprofile/     # Module User Profile
│   ├── search/          # Module Search
│   ├── productdetail/   # Module Product Detail
│   ├── cart/            # Module Cart
│   └── checkout/        # Module Checkout
├── utils/               # Utility classes
└── listener/            # TestNG listeners
```

## Hướng dẫn làm việc nhóm

### Quy tắc đặt tên
- Test class: `[ModuleName]Test.java`
- Page class: `[ModuleName]Page.java`
- Test data: `[ModuleName]TestData.java`
- Test case ID format: `[MODULE]-[FEATURE]-[NUMBER]`

### Quy trình phát triển
1. Mỗi thành viên chọn 1 module
2. Tạo Page Object Model cho module đó
3. Tạo Test Data class
4. Implement test cases
5. Cập nhật TestNG XML

## Chạy test
```bash
# Chạy tất cả test
mvn test

# Chạy module cụ thể
mvn test -Dtest="modules.authentication.AuthenticationTest"

# Chạy với browser khác
mvn test -Dbrowser=firefox
```

## Report
- ExtentReport: `target/ExtentReport.html`
- TestNG Report: `target/surefire-reports/index.html`
- Screenshots: `target/screenshots/`

## Module đã hoàn thành
- ✅ Authentication Module (11 test cases)

## Module cần implement
- ⏳ User Profile Module
- ⏳ Search Module  
- ⏳ Product Detail Module
- ⏳ Cart Module
- ⏳ Checkout Module
