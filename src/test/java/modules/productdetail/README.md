# Product Detail Module

## Mô tả
Module Product Detail chịu trách nhiệm test các chức năng hiển thị và tương tác với chi tiết sản phẩm.

## Test Cases cần implement

### Product Display Tests
- **PD-DS-01**: Hiển thị thông tin sản phẩm đầy đủ
- **PD-DS-02**: Hiển thị hình ảnh sản phẩm
- **PD-DS-03**: Hiển thị giá sản phẩm chính xác
- **PD-DS-04**: Hiển thị trạng thái còn hàng/hết hàng

### Product Variants Tests
- **PD-VR-01**: Chọn phiên bản sản phẩm khác nhau
- **PD-VR-02**: Cập nhật giá khi chọn variant
- **PD-VR-03**: Kiểm tra tồn kho theo variant

### Product Reviews Tests
- **PD-RV-01**: Hiển thị đánh giá sản phẩm
- **PD-RV-02**: Thêm đánh giá mới (khi đã đăng nhập)
- **PD-RV-03**: Không thể đánh giá khi chưa đăng nhập

### Add to Cart Tests
- **PD-AC-01**: Thêm sản phẩm vào giỏ hàng
- **PD-AC-02**: Thêm sản phẩm với số lượng tùy chỉnh
- **PD-AC-03**: Không thể thêm sản phẩm hết hàng
- **PD-AC-04**: Thêm sản phẩm vượt quá số lượng tồn kho

### Related Products Tests
- **PD-RP-01**: Hiển thị sản phẩm liên quan
- **PD-RP-02**: Click vào sản phẩm liên quan

## Files cần tạo
- `ProductDetailPage.java` - Page Object Model
- `ProductDetailTest.java` - Test cases
- `ProductDetailTestData.java` - Test data

## Locators cần identify
- Product title
- Product price
- Product images
- Variant selectors (color, size, configuration)
- Quantity input
- Add to cart button
- Stock status
- Product description
- Reviews section
- Related products section

## Test Data
- Product URLs với các loại sản phẩm khác nhau
- Quantities: 1, 5, 10, 999
- Valid/Invalid product IDs

## Người phụ trách
- **Assigned to**: [TÊN THÀNH VIÊN]
- **Status**: ⏳ Chờ implement

## Notes
- Cần test với nhiều loại sản phẩm khác nhau
- Verify responsive design trên mobile
- Check loading time của images
