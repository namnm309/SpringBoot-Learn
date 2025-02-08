package com.example.SpringBootTurialVip.exception;

public enum ErrorCode {
    USER_EXISTED(1001,"User đã tồn tại"),//Define 1 mã errorcode,
    UNCATEGORIZED_EXCEPTION(9999,"Lỗi không xác định"),
    PASSWORD_INVALID(1003,"Mật khẩu phải ít nhất 8 kí tự "),
    INVALIDID_KEY(1002,"Lỗi sai mã định danh lỗi . Hãy lưu lại và báo với BackEnd , Xin cảm ơn .")
    ;

    private int code;
    private String message;


    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
