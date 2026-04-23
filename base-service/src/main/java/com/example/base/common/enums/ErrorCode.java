package com.example.base.common.enums;

public enum ErrorCode {

    /* ========== 通用 ========== */
    // 自定义业务错误码推荐使用 0 表示成功
    SUCCESS(0, "success"),

    PARAM_ERROR(4000, "参数错误"),
    NOT_FOUND(4004, "资源不存在"),
    METHOD_NOT_ALLOWED(4005, "请求方法不支持"),

    /* ========== 认证 / 授权 ========== */
    UNAUTHORIZED(4010, "未登录或登录失效"),
    FORBIDDEN(4030, "无访问权限"),

    /* ========== 业务相关（示例） ========== */
    USER_NOT_EXISTS(4101, "用户不存在"),
    USER_ALREADY_EXISTS(4102, "用户已存在"),

    /* ========== 系统异常 ========== */
    SYSTEM_ERROR(5000, "系统异常"),
    DATABASE_ERROR(5001, "数据库异常"),
    NETWORK_ERROR(5002, "网络异常");

    private final int code;
    private final String message;

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
