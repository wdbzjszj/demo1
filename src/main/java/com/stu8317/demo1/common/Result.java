package com.stu8317.demo1.common;

import lombok.Data;

/**
 * 统一API响应结果封装
 */
@Data
public class Result<T> {

    private Integer code; // 响应码
    private String message; // 响应消息
    private T data; // 响应数据

    /**
     * 成功响应（带数据）
     */
    public static <T> Result<T> success(T data) {
        Result<T> result = new Result<>();
        result.setCode(ResultCode.SUCCESS.getCode());
        result.setMessage(ResultCode.SUCCESS.getMessage());
        result.setData(data);
        return result;
    }

    /**
     * 成功响应（无数据）
     */
    public static <T> Result<T> success(String message) {
        Result<T> result = new Result<>();
        result.setCode(ResultCode.SUCCESS.getCode());
        result.setMessage(message);
        return result;
    }

    /**
     * 失败响应（自定义状态码）
     */
    public static <T> Result<T> error(ResultCode resultCode) {
        Result<T> result = new Result<>();
        result.setCode(resultCode.getCode());
        result.setMessage(resultCode.getMessage());
        return result;
    }

    /**
     * 失败响应（自定义消息）
     */
    public static <T> Result<T> error(String message) {
        Result<T> result = new Result<>();
        result.setCode(ResultCode.ERROR.getCode());
        result.setMessage(message);
        return result;
    }
}