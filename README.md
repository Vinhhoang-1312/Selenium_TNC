# 🏪 TNC Store Automation Testing Framework

## 📋 Project Overview
Automation testing framework for TNC Store website using Selenium WebDriver, TestNG, and ExtentReports.

## 🏗️ Project Structure
```
📁 src/main/java/team/three/automation/
├── 📁 commons/          # Utility classes & common functions (TNCStoreConfig, TNCStoreLocators)
├── 📁 helpers/          # Helper classes for complex tasks
├── 📁 listener/         # Listeners (moved to test folder)
├── 📁 reports/          # Custom report generation classes
└── 📁 ui/              # UI interaction classes (Page Object components)

📁 src/test/java/
├── 📁 helpers/          # Test-specific helper classes (BaseTest, DriverFactory, etc.)
├── 📁 model/           # Data models for test data structure
├── 📁 pages/           # Page Object Model implementation
└── 📁 test/            # Actual test cases
    ├── 📁 authentication/  # Login, Register, Forgot Password tests
    ├── 📁 userprofile/     # User profile management tests
    └── SmokeTests.java     # Framework verification tests

📁 src/test/resources/
├── config.properties    # Configuration settings
├── testdata.json       # Test data in JSON format
└── 📁 data/            # Test data files (.json, .csv, .excel)
```

## 🚀 Technologies Used
- **Java 17**
- **Selenium WebDriver 4.25.0**
- **TestNG 7.8.0**
- **ExtentReports 5.0.9**
- **Maven 3.9.x**
- **WebDriverManager** (automatic driver management)

## ⚙️ Configuration
Edit `src/test/resources/config.properties`:
```properties
# Browser Settings
browser=chrome
headless=false

# URLs
base.url=https://tncstore.vn

# Timeouts
implicit.wait=15
explicit.wait=20
page.load.timeout=60
```

## 🔧 Setup & Installation
1. **Prerequisites:**
   - Java 17+
   - Maven 3.6+
   - Chrome/Firefox/Edge browser

2. **Clone & Setup:**
   ```bash
   git clone https://github.com/gitdung/nhomnhom-final-project.git
   cd nhomnhom-final-project
   mvn clean compile
   ```

## 🧪 Running Tests

### Run All Tests
```bash
mvn test
```

### Run by Groups
```bash
# Smoke tests
mvn test -Dgroups=smoke

# Authentication tests
mvn test -Dgroups=authentication

# User Profile tests  
mvn test -Dgroups=userprofile
```

### Run Specific Test Classes
```bash
# Run smoke tests
mvn test -Dtest=SmokeTests

# Run login tests
mvn test -Dtest=LoginTests

# Run registration tests
mvn test -Dtest=RegisterTests
```

### Run with Different Browsers
```bash
# Chrome (default)
mvn test -Dbrowser=chrome

# Firefox
mvn test -Dbrowser=firefox

# Edge
mvn test -Dbrowser=edge
```

## 📊 Reports
- **ExtentReports:** `target/general_report.html`
- **TestNG Reports:** `test-output/emailable-report.html`
- **Screenshots:** `target/screenshots/`

## 🌟 Features
- ✅ **Multi-browser support** (Chrome, Firefox, Edge)
- ✅ **Page Object Model** pattern
- ✅ **Data-driven testing** support
- ✅ **Screenshot capture** on test failures
- ✅ **Detailed HTML reports** with ExtentReports
- ✅ **Parallel execution** support
- ✅ **CI/CD ready**

## 📝 Test Cases Coverage

### Authentication Module
- **Registration:** Valid data, existing email, invalid email format, weak password
- **Login:** Valid credentials, invalid email, wrong password, empty fields
- **Forgot Password:** Valid email, non-existing email, invalid format

### User Profile Module
- **Profile View:** Display user information
- **Profile Update:** Update name, phone, address
- **Password Change:** Valid change, wrong current password

## 🔄 Branching Strategy
Follow the established branching rules:

- `test/<feature-name>` - New feature tests
- `fix/test/<description>` - Fix broken/flaky tests
- `refactor/<area>` - Code refactoring
- `test/coverage/<feature>` - Add more test coverage
- `hotfix/test/<critical-issue>` - Critical fixes

## 👥 Team Structure
- **PM Rules Compliance:** Full adherence to project management guidelines
- **Code Organization:** Clean architecture with separation of concerns
- **Best Practices:** Following automation testing best practices

## 🔗 Links
- **Repository:** https://github.com/gitdung/nhomnhom-final-project
- **Website Under Test:** https://tncstore.vn
- **Documentation:** See `AUTOMATION_TESTING_BEST_PRACTICES.md`

---
🎯 **Ready for Development & CI/CD Integration**
