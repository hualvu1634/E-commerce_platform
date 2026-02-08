package e_commerce.project.enumerate;


import lombok.Getter;

@Getter
public enum ErrorCode {

    INTERNAL_SERVER_ERROR(500, "Lỗi hệ thống"),
    //409
    USER_EXISTED(400, "Tài khoản đã tồn tại"),
    PRODUCT_EXISTED(400, "Sản phẩm đã tồn tại"),
    CATEGORY_EXISTED(400, "Danh mục đã tồn tại"),

    //404 NOT FOUND
    USER_NOT_FOUND(404, "Không tìm thấy người dùng"),
    PRODUCT_NOT_FOUND(404, "Sản phẩm không tồn tại"),
    CATEGORY_NOT_FOUND(404, "Danh mục không tồn tại"),



    //401 AUTH
    AUTH_FAILED(401, "Username hoặc Password không chính xác"),

    // Thêm vào ErrorCode enum
    ACCOUNT_LOCKED(403, "Tài khoản đã bị khóa"),
    //400 BAD REQUEST
    NOT_NULL(400,"không được để trống "),
    INVALID_SIZE(400, "độ dài ký tự không hợp lệ"),
    INVALID_MIN(400, "giá trị đầu vào không hợp lệ"),
    INVALID_PHONE(400, "định dạng số điện thoại không đúng"),
    NOT_ENOUGH(400,"k đủ số lượng"),
    CART_EMPTY(400,"Giỏ hàng trống");


    
    private int code;
    private String message;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }
}