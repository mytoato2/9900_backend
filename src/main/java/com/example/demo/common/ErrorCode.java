package com.example.demo.common;


/**
 * 错误码
 */
public enum ErrorCode {

    SUCCESS(200, "ok", ""),
    PARAMS_ERROR(40000, "params error", ""),
    NULL_ERROR(40001, "params null", ""),
    NOT_LOGIN(40100, "not login", ""),
    NO_AUTH(40101, "no auth", ""),
    SYSTEM_ERROR(50000, "sysyem error", ""),
    UPDATE_FAILED(50010, "update failed", ""),
    INSERT_FAILED(50011, "insert failed", ""),
    DELETE_FAILED(50012, "delete failed", ""),
    QUERY_FAILED(50013, "query failed", ""),
    DUPLICATE_KEY(50014, "duplicate data", ""),

    USER_NOT_FOUND(40400, "user not found", "");

    private final int code;

    /**
     * 状态码信息
     */
    private final String message;

    /**
     * 状态码描述（详情）
     */
    private final String description;

    ErrorCode(int code, String message, String description) {
        this.code = code;
        this.message = message;
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }


    public String getDescription() {
        return description;
    }
}
