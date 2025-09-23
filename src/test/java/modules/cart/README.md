# Cart Module

## Mô tả
Module Cart chịu trách nhiệm test các chức năng quản lý giỏ hàng trên TNC Store.

## Test Cases cần implement

### Add to Cart Tests
- **CT-AC-01**: Thêm sản phẩm mới vào giỏ hàng trống
- **CT-AC-02**: Thêm sản phẩm đã có trong giỏ hàng (tăng số lượng)
- **CT-AC-03**: Thêm nhiều sản phẩm khác nhau
- **CT-AC-04**: Thêm sản phẩm khi chưa đăng nhập

### Cart Management Tests
- **CT-MG-01**: Cập nhật số lượng sản phẩm trong giỏ hàng
- **CT-MG-02**: Xóa sản phẩm khỏi giỏ hàng
- **CT-MG-03**: Xóa tất cả sản phẩm (clear cart)
- **CT-MG-04**: Kiểm tra tổng tiền tự động cập nhật

### Cart Validation Tests
- **CT-VL-01**: Kiểm tra số lượng không được âm hoặc 0
- **CT-VL-02**: Kiểm tra số lượng không vượt quá tồn kho
- **CT-VL-03**: Hiển thị thông báo khi giỏ hàng trống
- **CT-VL-04**: Kiểm tra giá sản phẩm trong giỏ hàng

### Cart Persistence Tests
- **CT-PS-01**: Giỏ hàng được lưu sau khi logout/login
- **CT-PS-02**: Giỏ hàng được đồng bộ giữa các devices
- **CT-PS-03**: Giỏ hàng tạm thời cho guest user

### Coupon/Discount Tests
- **CT-CP-01**: Áp dụng mã giảm giá hợp lệ
- **CT-CP-02**: Áp dụng mã giảm giá không hợp lệ
- **CT-CP-03**: Áp dụng mã giảm giá đã hết hạn
- **CT-CP-04**: Tính toán tổng tiền sau khi giảm giá

## Files cần tạo
- `CartPage.java` - Page Object Model
- `CartTest.java` - Test cases
- `CartTestData.java` - Test data

## Locators cần identify
- Cart icon/counter
- Product items in cart
- Quantity input/buttons
- Remove item buttons
- Clear cart button
- Subtotal/Total price
- Coupon input field
- Apply coupon button
- Proceed to checkout button

## Test Data
- Valid/Invalid coupon codes
- Different product types và quantities
- Price calculations

## Người phụ trách
- **Assigned to**: [TÊN THÀNH VIÊN]
- **Status**: ⏳ Chờ implement

## Notes
- Test cart functionality từ nhiều entry points
- Verify cart counter updates correctly
- Check price calculations accuracy
