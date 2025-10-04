# 🎯 Automation Testing Best Practices Guide

## ❓ **Automation Tester chuyên nghiệp làm gì khi tìm locator?**

### **❌ Cách KHÔNG nên làm:**

1. F12 → Right click → Copy XPath
2. Paste trực tiếp vào code
3. Dùng absolute XPath dài

**Tại sao không nên:**

- Absolute XPath dễ bị break khi website thay đổi structure
- Không readable và khó maintain
- Performance chậm

### **✅ Cách automation tester chuyên nghiệp làm:**

#### **Bước 1: Phân tích website structure**

```html
<!-- Ví dụ HTML của TNC Store -->
<div id="js-form-holder">                    <!-- Popup container -->
    <form id="js-form-login">               <!-- Login form -->
        <input id="js-login-email" type="email">     <!-- Email field -->
        <input id="js-login-password" type="password"> <!-- Password field -->
        <button type="submit">Đăng nhập</button>
    </form>
</div>
```

#### **Bước 2: Chọn locator strategy theo thứ tự ưu tiên**

**1. ID (Ưu tiên cao nhất)**

```java
@FindBy(id = "js-login-email")           // ✅ Perfect!
@FindBy(css = "#js-login-email")         // ✅ Tương đương
```

**2. Name attribute**

```java
@FindBy(name = "email")                  // ✅ Tốt nếu có
```

**3. CSS Selector (linh hoạt)**

```java
@FindBy(css = "input[type='email']")     // ✅ Theo attribute
@FindBy(css = ".login-form input[type='email']") // ✅ Theo parent class
@FindBy(css = "#js-form-login input[type='email']") // ✅ Theo parent ID
```

**4. XPath thông minh (relative)**

```java
@FindBy(xpath = "//input[@id='js-login-email']")     // ✅ Theo ID
@FindBy(xpath = "//input[@type='email']")            // ✅ Theo attribute
@FindBy(xpath = "//form[@id='js-form-login']//input[@type='email']") // ✅ Theo parent
```

**5. XPath theo text (cho buttons/links)**

```java
@FindBy(xpath = "//button[contains(text(),'Đăng nhập')]")  // ✅ Theo text
@FindBy(xpath = "//a[contains(text(),'Tạo tài khoản')]")  // ✅ Theo text
```

#### **Bước 3: Viết XPath có backup strategy**

```java
// ✅ Multiple strategies trong 1 XPath
"//input[@id='js-login-email'] | //input[contains(@placeholder,'email')]"

// ✅ Dùng contains() cho partial match
"//div[contains(@class,'login-form')]//input[contains(@id,'email')]"
```

---

## 🎯 **Ví dụ thực tế cho TNC Store**

### **Flow bạn mô tả:**

**1. Click nút "Tài khoản"**

```java
// ❌ Absolute XPath (dễ break)
@FindBy(xpath = "/html/body/div[4]/div[2]/div/div/div[2]/a[1]/span")

// ✅ Better XPath  
@FindBy(xpath = "//span[contains(text(),'Tài khoản')]")
@FindBy(xpath = "//a[contains(@class,'account')]//span")
```

**2. Popup "#js-form-holder" hiện ra**

```java
@FindBy(css = "#js-form-holder")         // ✅ Perfect! Dùng ID
@FindBy(xpath = "//div[@id='js-form-holder']") // ✅ Tương đương
```

**3A. Đăng nhập: Nhập trực tiếp email/password**

```java
@FindBy(css = "#js-login-email")         // ✅ Email field
@FindBy(css = "#js-login-password")      // ✅ Password field
```

**3B. Đăng ký: Click "Tạo tài khoản" → 3 fields**

```java
// Link "Tạo tài khoản"
@FindBy(xpath = "//a[contains(text(),'Tạo tài khoản')]")

// 3 input fields sau khi click
@FindBy(css = "#js-popup-register-name")     // 1. Họ và tên
@FindBy(css = "#js-popup-register-email")    // 2. Email
@FindBy(css = "#js-popup-register-password") // 3. Mật khẩu
```

---

## 🛠️ **Tools automation tester chuyên nghiệp dùng:**

### **1. Browser DevTools (F12) - Nhưng KHÔNG copy XPath!**

- Inspect element để hiểu structure
- Test CSS selector trong Console: `$('#js-login-email')`
- Test XPath trong Console: `$x("//input[@id='js-login-email']")`

### **2. Browser Extensions:**

- **SelectorsHub** - Generate smart selectors
- **ChroPath** - Test XPath/CSS trong browser
- **XPath Helper** - Highlight elements

### **3. IDE Tools:**

- **Selenium IDE** - Record and replay (học cách viết locator)
- **Page Object Pattern** - Organize locators properly

### **4. Best Practice Workflow:**

```
1. Inspect element (F12)
2. Identify unique attributes (id, class, name)
3. Write CSS selector first: #js-login-email
4. If CSS không đủ → Write smart XPath
5. Test locator trong Console
6. Add to Page Object với meaningful name
```

---

## 🎯 **Tổng kết cho TNC Store:**

### **Đã sửa theo flow đúng của bạn:**

**Login Flow:**

```
Click "Tài khoản" → Popup hiện (#js-form-holder) → Nhập email/password trực tiếp
```

**Register Flow:**

```
Click "Tài khoản" → Popup hiện → Click "Tạo tài khoản" → 3 fields hiện ra → Nhập thông tin
```

### **Locators đã được cải thiện:**

- Sử dụng ID selectors: `#js-login-email`, `#js-popup-register-name`
- Backup XPath strategies cho các trường hợp element thay đổi
- Comments tiếng Việt để dễ hiểu

### **Tips quan trọng:**

1. **Ưu tiên ID/CSS** hơn XPath khi có thể
2. **Dùng contains()** cho text có thể thay đổi
3. **Viết comments** giải thích từng locator
4. **Test locators** trong browser console trước khi code
5. **Có backup strategy** khi main locator fail

**Nhớ:** Automation testing tốt = Locators ổn định + Code dễ maintain + Flow logic rõ ràng! 🎯
