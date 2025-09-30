# Sample Excel Files Structure for TNC Store Automation

## 📊 Cách tạo Excel files:

### 1. authentication_testdata.xlsx
```
TestCaseID    | name      | email           | password    | expectedResult
AUTH-SU-01    | John Doe  | john5@test.com   | Abc12345    | success
AUTH-SU-02    | Jane Doe  | existing@tnc.vn | Abc12345    | email_exists_error
AUTH-SI-01    |           | john@test.com   | Abc12345    | login_success
AUTH-FP-01    |           | john@test.com   |             | reset_email_sent
```

### 2. cart_testdata.xlsx
```
TestCaseID | productUrl               | quantity | action     | expectedResult
TC001      | /san-pham/laptop-gaming  | 1        | add        | added_to_cart
TC002      | /san-pham/laptop-gaming  | 2        | update     | quantity_updated
TC003      | /san-pham/laptop-gaming  | 0        | remove     | removed_from_cart
TC004      | /san-pham/laptop-gaming  | 1        | verify     | total_correct
TC005      | /san-pham/laptop-gaming  | 1        | checkout   | redirect_checkout
```

### 3. search_testdata.xlsx
```
TestCaseID | keyword              | times | expectedResult
SRH-001    | Rtx 2050            | 1     | results_found
SRH-002    | abcxyz123           | 1     | no_results
SRH-003    | màn hình máy tính   | 1     | category_redirect
SRH-004    | rtx & 2050          | 1     | handled_gracefully
SRH-005    | Rtx 2050            | 10    | continuous_success
```

### 4. productdetail_testdata.xlsx
```
TestCaseID | action           | expectedResult
DTL-001    | access           | detail_page_loaded
DTL-002    | add_to_cart      | notification_shown
DTL-003    | image_gallery    | popup_displayed
DTL-004    | buy_now          | cart_redirect
DTL-005    | technical_specs  | specs_popup
```

## 🎯 Hướng dẫn sử dụng:

1. **Tạo Excel files** trong folder `src/test/resources/testdata/`
2. **Đặt tên files** theo format: `{module}_testdata.xlsx`
3. **Header row** (dòng đầu) chứa tên columns
4. **Data rows** chứa test data cho từng test case
5. **TestCaseID column** là column đầu tiên và bắt buộc

## 🔧 Cách tests sử dụng Excel data:

```java
// Trong test class:
String email = ExcelReader.getAuthData("AUTH-SU-01", "email");
String password = ExcelReader.getAuthData("AUTH-SU-01", "password");

// Nếu Excel không có data, fallback to hardcoded:
if (email.isEmpty()) email = AuthenticationTestData.VALID_EMAIL;
```

## ✅ Lợi ích:

- **Data Management tập trung**: Không cần modify code để thay đổi test data
- **Team Collaboration**: Mỗi người có thể maintain Excel files riêng
- **Flexible Testing**: Dễ dàng test với nhiều data sets khác nhau
- **Backward Compatible**: Vẫn chạy được khi không có Excel files
