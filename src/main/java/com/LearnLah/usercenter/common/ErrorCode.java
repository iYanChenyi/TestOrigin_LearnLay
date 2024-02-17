package com.LearnLah.usercenter.common;

import lombok.Data;

/**
 * Error Code
 *
 * @author YanChenyi
 */
public enum ErrorCode {
    SUCCESS(20000, "success", ""),

    PARAMS_ERROR(40000, "Request Value Erroe", ""),
    PARAMS_NULL_ERROR(40001, "Request Data NULL", ""),
    NOT_LOGIN(40100, "Unlogin", ""),
    NOT_AUTH(40101, "No Right", ""),
    SYSTEM_ERROR(50000, "System error", ""),
    INVALID_PASSWORD_ERROR(40102, "Invaild Password", "");
    private final int code;
    /**
     * Status Code Message
     */
    private final String message;
    /**
     * Status Code Description
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
