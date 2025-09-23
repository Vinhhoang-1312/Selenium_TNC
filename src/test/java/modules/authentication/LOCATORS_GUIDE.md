# 🔍 LOCATORS CẦN LẤY CHO MODULE AUTHENTICATION

## 📋 DANH SÁCH LOCATORS CẦN INSPECT TỪ WEBSITE TNC STORE:

### 1. TRANG ĐĂNG KÝ (REGISTER PAGE):
🌐 **URL**: https://www.tncstore.vn/account/register

**Cần inspect và lấy:**
- ✅ **Name field**: `<input>` để nhập họ tên
- ✅ **Email field**: `<input>` để nhập email  
- ✅ **Password field**: `<input>` để nhập mật khẩu
- ✅ **Register button**: `<button>` để submit form đăng ký

### 2. TRANG ĐĂNG NHẬP (LOGIN PAGE):
🌐 **URL**: https://www.tncstore.vn/account/login

**Cần inspect và lấy:**
- ✅ **Email field**: `<input>` để nhập email đăng nhập
- ✅ **Password field**: `<input>` để nhập mật khẩu đăng nhập
- ✅ **Login button**: `<button>` để submit form đăng nhập
- ✅ **Forgot password link**: `<a>` link "Quên mật khẩu"

### 3. NAVIGATION LINKS:
🌐 **URL**: https://www.tncstore.vn/ (homepage)

**Cần inspect và lấy:**
- ✅ **Register link**: Link dẫn đến trang đăng ký
- ✅ **Login link**: Link dẫn đến trang đăng nhập

### 4. ERROR MESSAGES:
**Khi test các trường hợp lỗi, cần inspect:**
- ✅ **General error message**: Div/span chứa thông báo lỗi chung
- ✅ **Email exists error**: Thông báo "Email đã được sử dụng"
- ✅ **Invalid email error**: Thông báo "Email không hợp lệ"
- ✅ **Required field error**: Thông báo "Trường này bắt buộc"

## 🛠️ CÁCH INSPECT VÀ LẤY LOCATORS:

### Bước 1: Mở Developer Tools
- `F12` hoặc `Right click → Inspect`

### Bước 2: Inspect từng element
- Click vào icon inspect (mũi tên)
- Click vào element trên website
- Copy locator từ HTML

### Bước 3: Ưu tiên các loại locators:
1. **ID** (tốt nhất): `id="email"`
2. **Name**: `name="email"`  
3. **Class**: `class="form-control"`
4. **CSS Selector**: `input[type="email"]`
5. **XPath** (cuối cùng): `//input[@placeholder='Email']`

## 📝 VÍ DỤ CÁCH LẤY LOCATORS:

### Ví dụ cho Email field:
```html
<!-- Nếu bạn thấy HTML như này: -->
<input id="user_email" name="email" type="email" placeholder="Nhập email">

<!-- Thì có thể dùng: -->
@FindBy(id = "user_email")  // Tốt nhất
// hoặc
@FindBy(name = "email")
// hoặc  
@FindBy(xpath = "//input[@placeholder='Nhập email']")
```

## 🎯 FILE CẦN UPDATE:

File: `src/test/java/modules/authentication/AuthenticationPage.java`

**Thay thế tất cả các locators có:**
- `REPLACE_WITH_REAL_NAME_FIELD_ID`
- `REPLACE_WITH_REAL_EMAIL_FIELD_ID`  
- `REPLACE_WITH_REAL_PASSWORD_FIELD_ID`
- `REPLACE_WITH_REGISTER_BUTTON_TEXT`
- `REPLACE_WITH_LOGIN_BUTTON_TEXT`
- v.v...

## 🚀 SAU KHI UPDATE LOCATORS:

Chạy test để kiểm tra:
```bash
mvn test -Dtest="modules.authentication.AuthenticationTest#testRegisterWithValidData"
```

## 📞 HỖ TRỢ:

Nếu gặp khó khăn với locator nào, hãy:
1. Share HTML code của element đó
2. Tôi sẽ giúp tạo locator phù hợp

---
**GHI CHÚ**: Website có thể có popup, captcha, hoặc validation đặc biệt. Hãy test thử thao tác thủ công trước khi viết automation.
