package com.stu8317.demo1.common;

import lombok.Getter;

/**
 * 响应状态码枚举
 */
@Getter
public enum ResultCode {

    SUCCESS(200, "操作成功"),
    ERROR(500, "服务器内部错误"),
    USER_HAS_EXISTED(400, "用户名已存在"),
    USER_NOT_EXIST(400, "用户不存在"),
    PASSWORD_ERROR(400, "密码错误");

    private final Integer code;
    private final String message;

    ResultCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}