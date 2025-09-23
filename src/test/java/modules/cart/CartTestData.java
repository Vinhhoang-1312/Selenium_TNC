package modules.cart;

public class CartTestData {

    // Test quantities for updating cart
    public static final int INITIAL_QUANTITY = 1;
    public static final int UPDATED_QUANTITY = 2;
    public static final int LARGE_QUANTITY = 5;
    public static final int MAX_QUANTITY = 10;

    // Product information for testing
    public static final String SAMPLE_PRODUCT_NAME = "Laptop Gaming ASUS";
    public static final String SAMPLE_PRODUCT_URL = "/san-pham/laptop-gaming-asus";
    public static final double SAMPLE_PRODUCT_PRICE = 25000000.0;

    // Test scenarios
    public static final String[] PRODUCT_URLS = {
        "/san-pham/laptop-gaming-asus",
        "/san-pham/pc-gaming-rtx-4070",
        "/san-pham/gaming-chair"
    };

    public static final String[] PRODUCT_NAMES = {
        "Laptop Gaming ASUS",
        "PC Gaming RTX 4070",
        "Gaming Chair"
    };

    public static final double[] PRODUCT_PRICES = {
        25000000.0,
        35000000.0,
        5000000.0
    };

    // Expected messages
    public static final String EMPTY_CART_MESSAGE = "Giỏ hàng của bạn đang trống";
    public static final String PRODUCT_ADDED_MESSAGE = "Sản phẩm đã được thêm vào giỏ hàng";
    public static final String PRODUCT_REMOVED_MESSAGE = "Sản phẩm đã được xóa khỏi giỏ hàng";

    // URLs for navigation
    public static final String CART_URL = "https://www.tncstore.vn/cart";
    public static final String CHECKOUT_URL = "https://www.tncstore.vn/checkout";

    // Test data for edge cases
    public static final int ZERO_QUANTITY = 0;
    public static final int NEGATIVE_QUANTITY = -1;
    public static final int EXCESSIVE_QUANTITY = 999;

    // Price calculation test data
    public static class PriceTestData {
        public String productName;
        public double unitPrice;
        public int quantity;
        public double expectedTotal;

        public PriceTestData(String productName, double unitPrice, int quantity) {
            this.productName = productName;
            this.unitPrice = unitPrice;
            this.quantity = quantity;
            this.expectedTotal = unitPrice * quantity;
        }
    }

    public static final PriceTestData[] PRICE_TEST_SCENARIOS = {
        new PriceTestData("Laptop Gaming", 25000000.0, 1),
        new PriceTestData("Gaming Chair", 5000000.0, 2),
        new PriceTestData("PC Gaming", 35000000.0, 1)
    };
}
