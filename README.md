# 🏪 TNC Store Automation Testing Framework

## 📋 Project Overview
Automation testing framework for TNC Store website using Selenium WebDriver, TestNG, and ExtentReports.
**Enterprise-grade framework designed for team collaboration and scalable test automation.**

**Final project of nhomnhom team. Automation testing for https://www.tncstore.vn/**

## 🏗️ UPDATED Project Structure & File Explanations

### 📁 **src/main/java/commons/** - REUSABLE Core Components Only
```
├── DriverFactory.java       # 🚗 WebDriver management (Chrome, Firefox, Edge)
│                            # ✅ REUSABLE for any website/project
└── listener/                # 📡 TestNG Listeners for test events
    └── TestListener.java    # 👂 Global test execution listener (REUSABLE)
```

### 📁 **src/test/java/config/** - TEST-SPECIFIC Configuration
```
└── TNCStoreConfig.java      # ⚙️ TNC Store specific config (URLs, timeouts)
                             # 🎯 Only for tncstore.vn website
```

### 📁 **src/test/java/locators/** - TEST-SPECIFIC Web Elements
```
└── TNCStoreLocators.java    # 🎯 TNC Store web element locators
                             # 🏪 Only for tncstore.vn website
```

### 📁 **src/test/java/helpers/** - Test Utilities & Base Classes (9 files)
```
├── BaseTest.java           # 🏗️ Base class cho tất cả test classes
├── ConfigReader.java       # 📋 Đọc config từ properties files
├── ExcelReader.java        # 📊 Đọc test data từ Excel files
├── ExtentManager.java      # 📊 Quản lý ExtentReports initialization
├── ReportManager.java      # 📋 Quản lý logging và reporting per module
├── ScreenshotUtils.java    # 📸 Capture screenshots on test failures
├── TestDataProvider.java  # 🎲 Provide test data cho TestNG
├── TestDataReader.java     # 📖 Đọc test data từ multiple sources
└── WaitUtils.java          # ⏰ Wait strategies (explicit, fluent waits)
```

### 📁 **src/test/java/model/** - Test Data Models (4 files)
```
├── AuthenticationTestData.java  # 🔐 Login/Register test data model
├── CartTestData.java           # 🛒 Shopping cart test data model
├── ProductDetailTestData.java  # 📦 Product information test data
└── SearchTestData.java         # 🔍 Search functionality test data
```

### 📁 **src/test/java/pages/** - Page Object Model (3 files)
```
├── BasePage.java              # 🏗️ Base class cho tất cả page objects
├── AuthenticationPage.java    # 🔐 Login/Register page interactions
└── UserProfilePage.java       # 👤 User profile management page
```

### 📁 **src/test/java/test/** - Test Classes Organized by Features
```
├── authentication/            # 🔐 Authentication Module Tests
├── userprofile/              # 👤 User Profile Module Tests
├── cart/                     # 🛒 Shopping Cart Module Tests
├── search/                   # 🔍 Search Module Tests
├── productdetail/            # 📦 Product Detail Module Tests
└── checkout/                 # 💳 Checkout Module Tests
```

