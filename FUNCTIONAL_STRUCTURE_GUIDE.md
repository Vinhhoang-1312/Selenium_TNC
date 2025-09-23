# 🎯 TNC Store Automation - Functional Test Structure

## 📋 **Project Restructure Summary**

✅ **COMPLETED**: Successfully divided TNCStoreTests.java into **individual functional test classes** with centralized configuration for https://www.tncstore.vn/

### **New Functional Structure:**

```
src/test/java/
├── config/                    # 🔧 Configuration Files (NEW)
│   ├── TNCStoreLocators.java  # XPath/CSS locators for TNC Store
│   └── TNCStoreConfig.java    # URLs, timeouts, test data constants
├── pages/                     # 📄 Page Objects (Updated)
│   ├── AuthenticationPage.java # Uses centralized config
│   └── UserProfilePage.java    # Uses centralized config
├── tests/                     # 🧪 Functional Test Classes (NEW)
│   ├── authentication/        
│   │   ├── LoginTests.java           # Login functionality only
│   │   ├── RegisterTests.java        # Registration functionality only
│   │   └── ForgotPasswordTests.java  # Password recovery only
│   └── userprofile/
│       ├── ProfileViewTests.java     # Profile viewing only
│       ├── ProfileUpdateTests.java   # Profile editing only
│       └── PasswordChangeTests.java  # Password change only
└── base/, data/, listener/, utils/   # Existing structure
```

---

## 🏗️ **Configuration Files Details**

### **1. TNCStoreLocators.java** - XPath/CSS Locators
```java
// Organized by functionality
public static final String LOGIN_EMAIL_FIELD = "#js-login-email";
public static final String REGISTER_NAME_FIELD = "#js-popup-register-name";
public static final String PROFILE_LINK = "//a[contains(@href,'profile')]";
// ... 30+ locators for TNC Store website
```

### **2. TNCStoreConfig.java** - Configuration Constants
```java
// URLs for TNC Store
public static final String BASE_URL = "https://www.tncstore.vn/";
public static final String LOGIN_URL = "https://www.tncstore.vn/account/login";

// Timeouts
public static final int ELEMENT_WAIT = 10;
public static final int PAGE_LOAD_TIMEOUT = 60;

// Test Data
public static final String DEFAULT_TEST_EMAIL = "john@test.com";
```

---

## 🎯 **Functional Test Classes**

### **Authentication Tests (3 Classes):**
1. **LoginTests.java** - 4 test methods
   - Valid login, wrong password, unregistered email, invalid format
2. **RegisterTests.java** - 5 test methods  
   - Valid registration, existing email, invalid email, blank fields, weak password
3. **ForgotPasswordTests.java** - 3 test methods
   - Valid email, non-existing email, invalid format

### **User Profile Tests (3 Classes):**
1. **ProfileViewTests.java** - 2 test methods
   - View when logged in, redirect when not logged in
2. **ProfileUpdateTests.java** - 2 test methods
   - Update with valid data, update with existing email
3. **PasswordChangeTests.java** - 2 test methods
   - Change with correct password, change with wrong password

---

## 🚀 **How to Run Functional Tests**

### **1. Interactive Menu (Recommended):**
```cmd
run-functional-tests.bat
```
**Menu Options:**
- [1] 🔥 Smoke Tests (critical only)
- [2] 🔐 Login Tests  
- [3] 📝 Register Tests
- [4] 🔒 Forgot Password Tests
- [5] 👁️ Profile View Tests
- [6] ✏️ Profile Update Tests
- [7] 🔑 Password Change Tests
- [8] 🧪 All Authentication
- [9] 👤 All User Profile
- [10] 🚀 All Functional Tests

### **2. Maven Commands:**
```cmd
# Individual Functions
mvn test -Plogin              # Login tests only
mvn test -Pregister           # Register tests only
mvn test -Pforgot-password    # Forgot password tests only
mvn test -Pprofile-view       # Profile view tests only
mvn test -Pprofile-update     # Profile update tests only
mvn test -Ppassword-change    # Password change tests only

# Grouped Functions
mvn test -Pauth               # All authentication tests
mvn test -Pprofile            # All user profile tests
mvn test -Pfunctions          # All functional tests

# Quick Tests
mvn test -Psmoke-functional   # Critical tests only
```

### **3. TestNG XML Files:**
- `testng-functions.xml` - Functional test structure
- `testng-groups.xml` - Original group structure (still available)

---

## 📊 **Reporting System**

### **Function-Specific Reports:**
Each functional test generates its own report:
- `target/authentication-login_report.html`
- `target/authentication-register_report.html`
- `target/userprofile-view_report.html`
- `target/userprofile-update_report.html`
- `target/userprofile-password_report.html`

### **Excel Reports:**
- `target/authentication-login_TestResults_*.xlsx`
- `target/userprofile-view_TestResults_*.xlsx`
- etc.

---

## 👥 **Team Collaboration Benefits**

### **For Individual Developers:**
```cmd
# Developer A working on login functionality
mvn test -Plogin

# Developer B working on registration
mvn test -Pregister

# Developer C working on profile features
mvn test -Pprofile-update
```

### **For Feature Teams:**
```cmd
# Authentication team
mvn test -Pauth

# User management team  
mvn test -Pprofile
```

### **For CI/CD Pipeline:**
```yaml
# GitHub Actions example
- name: Login Tests
  run: mvn test -Plogin
  
- name: Registration Tests
  run: mvn test -Pregister
  
- name: Smoke Tests
  run: mvn test -Psmoke-functional
```

---

## 🔧 **Configuration Advantages**

### **Centralized Locators:**
- All XPath/CSS selectors for TNC Store in one place
- Easy maintenance when website changes
- Consistent locator naming convention

### **Centralized Configuration:**
- All URLs, timeouts, test data in TNCStoreConfig.java
- Environment-specific settings
- Easy to switch between staging/production

### **Page Objects Enhancement:**
```java
// Before
@FindBy(xpath = "/html/body/div[4]/div[2]/div/div/div[2]/a[1]/span")

// After  
@FindBy(xpath = TNCStoreLocators.ACCOUNT_BUTTON)
```

---

## 🎉 **Migration Complete!**

### **✅ What's Been Achieved:**

1. **Functional Separation**: Each test class focuses on one specific functionality
2. **Centralized Configuration**: All TNC Store-specific settings in config package
3. **Enhanced Maintainability**: Easy to find and modify specific test functions
4. **Team-Friendly**: Multiple developers can work on different functions simultaneously
5. **Flexible Execution**: Run specific functions or combinations as needed
6. **Better Reporting**: Function-specific reports for targeted analysis

### **📁 Key Files Created:**
- `config/TNCStoreLocators.java` - 30+ locators for TNC Store
- `config/TNCStoreConfig.java` - Complete configuration constants
- `tests/authentication/` - 3 test classes (Login, Register, ForgotPassword)
- `tests/userprofile/` - 3 test classes (View, Update, PasswordChange)
- `testng-functions.xml` - TestNG configuration for functional structure
- `run-functional-tests.bat` - Interactive test runner

### **🚀 Ready for Team Development:**
Your TNC Store automation framework is now optimized for:
- **Function-specific development**
- **Independent team collaboration** 
- **Granular test execution**
- **Centralized maintenance**
- **Professional reporting**

**Start testing immediately with: `run-functional-tests.bat`** 🎯
