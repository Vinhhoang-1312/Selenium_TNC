# Allure Report - Hướng dẫn sử dụng

## 📊 Tổng quan
Project này sử dụng **Allure Report** - một framework reporting hiện đại và mạnh mẽ cho automation testing.

## 🚀 Chạy Tests và Generate Report

### 1. Chạy tất cả tests
```bash
mvn clean test
```

### 2. Chạy tests từ file XML cụ thể
```bash
# Login Tests
mvn clean test -DsuiteXmlFile=logintest.xml

# Smoke Tests
mvn clean test -DsuiteXmlFile=smoketest-quick.xml

# E2E Tests
mvn clean test -DsuiteXmlFile=e2etest.xml

# All Tests
mvn clean test -DsuiteXmlFile=testng.xml
```

### 3. Generate và xem Allure Report
```bash
# Generate report
mvn allure:report

# Serve report (mở browser tự động)
mvn allure:serve
```

## 📁 Cấu trúc Allure Results

Kết quả test được lưu tại:
- `allure-results/` - Thư mục chứa raw results
- `target/allure-results/` - Backup results
- `target/site/allure-maven-plugin/` - Generated HTML report

## ✨ Tính năng Allure Report

### 1. **Epic & Feature Organization**
Tests được tổ chức theo:
- **Epic**: Authentication, E-Commerce, User Management
- **Feature**: Login, Registration, Shopping Cart, Search, Checkout, Profile

### 2. **Story Grouping**
Mỗi test được gán vào Story cụ thể:
- User Login
- User Registration
- Add Products to Cart
- Search Functionality
- etc.

### 3. **Severity Levels**
- **BLOCKER**: Critical functionality (Login, Registration, Add to Cart)
- **CRITICAL**: Important features (Search, Checkout validation)
- **NORMAL**: Standard functionality
- **MINOR**: Edge cases

### 4. **Step-by-step Tracking**
Mỗi test có các steps chi tiết:
```java
Allure.step("Search for product");
Allure.step("Add product to cart");
Allure.step("Verify cart contains product");
```

### 5. **Parameters**
Các giá trị quan trọng được log:
```java
Allure.parameter("Product Name", productName);
Allure.parameter("Cart Size", cartSize);
```

### 6. **Screenshots on Failure**
Tự động capture screenshot khi test fail và attach vào report.

### 7. **Detailed Logging**
- Test execution time
- Pass/Fail status
- Error messages và stack traces
- Console logs

## 📊 Viewing Reports

### Option 1: Maven Serve (Recommended)
```bash
mvn allure:serve
```
- Tự động generate và mở browser
- Live server tại `http://localhost:port`

### Option 2: Generate Static Report
```bash
mvn allure:report
```
- Report tại: `target/site/allure-maven-plugin/index.html`
- Mở bằng browser thủ công

## 🎯 Report Sections

1. **Overview** - Tổng quan pass/fail rate, trends
2. **Categories** - Lỗi được group theo loại
3. **Suites** - Tests organized by test suites
4. **Graphs** - Visual charts và statistics
5. **Timeline** - Test execution timeline
6. **Behaviors** - Tests grouped by Epic/Feature/Story
7. **Packages** - Tests organized by package structure

## 🔍 Advanced Features

### History Trends
Allure tự động track history của tests qua các lần chạy:
```bash
# Copy previous results để track history
cp -r allure-results/history target/allure-results/history
```

### Environment Info
Thông tin môi trường được lưu tự động:
- Browser type
- Base URL
- Java version
- Selenium version

### Categories Configuration
Có thể customize error categories tại `src/test/resources/categories.json`

## 🛠️ Troubleshooting

### Report không hiển thị
```bash
# Clean và rebuild
mvn clean test allure:report
```

### Allure command not found
```bash
# Install Allure CLI
# Windows (Chocolatey)
choco install allure

# Mac (Homebrew)
brew install allure

# Linux
sudo apt-add-repository ppa:qameta/allure
sudo apt-get update
sudo apt-get install allure
```

## 📝 Best Practices

1. **Always use descriptive step names**
2. **Add parameters for important test data**
3. **Use appropriate severity levels**
4. **Group tests logically by Epic/Feature/Story**
5. **Clean allure-results before major test runs**

## 🔗 Useful Links

- [Allure Documentation](https://docs.qameta.io/allure/)
- [Allure TestNG Integration](https://docs.qameta.io/allure/#_testng)
- [Allure Maven Plugin](https://docs.qameta.io/allure/#_maven)

---

## 📧 Support

For issues or questions, contact the QA team.

