# Search Module

## Mô tả
Module Search chịu trách nhiệm test các chức năng tìm kiếm sản phẩm trên TNC Store.

## Test Cases cần implement

### Basic Search Tests
- **SR-BS-01**: Tìm kiếm với từ khóa hợp lệ có kết quả
- **SR-BS-02**: Tìm kiếm với từ khóa không có kết quả
- **SR-BS-03**: Tìm kiếm với từ khóa trống
- **SR-BS-04**: Tìm kiếm với ký tự đặc biệt

### Advanced Search Tests
- **SR-AS-01**: Tìm kiếm theo danh mục sản phẩm
- **SR-AS-02**: Tìm kiếm với bộ lọc giá
- **SR-AS-03**: Tìm kiếm với bộ lọc thương hiệu
- **SR-AS-04**: Kết hợp nhiều bộ lọc

### Search Results Tests
- **SR-RS-01**: Sắp xếp kết quả theo giá tăng dần
- **SR-RS-02**: Sắp xếp kết quả theo giá giảm dần
- **SR-RS-03**: Sắp xếp theo tên sản phẩm
- **SR-RS-04**: Phân trang kết quả tìm kiếm

### Auto-suggestion Tests
- **SR-AS-01**: Hiển thị gợi ý khi gõ từ khóa
- **SR-AS-02**: Click vào gợi ý để tìm kiếm

## Files cần tạo
- `SearchPage.java` - Page Object Model
- `SearchTest.java` - Test cases
- `SearchTestData.java` - Test data

## Locators cần identify
- Search input field
- Search button
- Search results container
- Filter options (category, price, brand)
- Sort dropdown
- Pagination elements
- Auto-suggestion dropdown

## Test Data
- Valid search keywords: "laptop gaming", "PC", "RTX 4070"
- Invalid search keywords: "xyz123", "!@#$%"
- Price ranges: 10000000-20000000, 20000000-50000000
- Categories: Laptop, PC Gaming, Linh kiện

## Người phụ trách
- **Assigned to**: [TÊN THÀNH VIÊN]
- **Status**: ⏳ Chờ implement

## Notes
- Test search từ homepage và từ các trang khác
- Verify số lượng kết quả hiển thị
- Check pagination khi có nhiều kết quả
