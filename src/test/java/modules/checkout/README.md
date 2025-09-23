# Checkout Module

## Mô tả
Module Checkout chịu trách nhiệm test quy trình thanh toán và hoàn tất đơn hàng.

## Test Cases cần implement

### Checkout Process Tests
- **CK-PR-01**: Checkout với thông tin giao hàng hợp lệ
- **CK-PR-02**: Checkout khi chưa đăng nhập
- **CK-PR-03**: Checkout với giỏ hàng trống
- **CK-PR-04**: Checkout với sản phẩm hết hàng trong giỏ

### Shipping Information Tests
- **CK-SP-01**: Nhập thông tin giao hàng đầy đủ
- **CK-SP-02**: Thiếu thông tin bắt buộc
- **CK-SP-03**: Sử dụng địa chỉ đã lưu
- **CK-SP-04**: Thêm địa chỉ giao hàng mới

### Payment Method Tests
- **CK-PM-01**: Chọn thanh toán COD
- **CK-PM-02**: Chọn thanh toán chuyển khoản
- **CK-PM-03**: Chọn thanh toán online (VNPAY/MOMO)
- **CK-PM-04**: Thông tin thanh toán không hợp lệ

### Order Summary Tests
- **CK-OS-01**: Kiểm tra thông tin đơn hàng trước khi đặt
- **CK-OS-02**: Tính toán phí vận chuyển
- **CK-OS-03**: Áp dụng voucher trong checkout
- **CK-OS-04**: Tổng tiền cuối cùng chính xác

### Order Completion Tests
- **CK-OC-01**: Đặt hàng thành công
- **CK-OC-02**: Nhận email xác nhận đơn hàng
- **CK-OC-03**: Hiển thị order confirmation page
- **CK-OC-04**: Giỏ hàng được clear sau khi đặt hàng

### Guest Checkout Tests
- **CK-GS-01**: Guest checkout với thông tin hợp lệ
- **CK-GS-02**: Guest checkout thiếu thông tin
- **CK-GS-03**: Option tạo tài khoản sau khi checkout

## Files cần tạo
- `CheckoutPage.java` - Page Object Model
- `CheckoutTest.java` - Test cases
- `CheckoutTestData.java` - Test data

## Locators cần identify
- Shipping information form
- Payment method options
- Order summary section
- Apply coupon field
- Place order button
- Order confirmation elements
- Error message containers

## Test Data
- Valid shipping addresses
- Valid/Invalid payment information
- Coupon codes
- Different order scenarios

## Người phụ trách
- **Assigned to**: [TÊN THÀNH VIÊN]
- **Status**: ⏳ Chờ implement

## Notes
- Cần test end-to-end flow từ cart đến order completion
- Verify email notifications (có thể cần mail server test)
- Check integration với payment gateways
- Test với different browsers và devices
