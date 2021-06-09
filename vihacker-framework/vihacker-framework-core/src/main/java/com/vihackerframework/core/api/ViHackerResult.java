package com.vihackerframework.core.api;

import lombok.Data;

/**
 * 通用api 返回对象
 *
 * @Description
 * @Author: Ranger
 * @Date: 2021/1/15 10:24
 * @Email: wilton.icp@gmail.com
 */
@Data
public class ViHackerResult<T> {
    /**
     * 状态码
     */
    private long code;
    /**
     * 提示信息
     */
    private String message;
    /**
     * 数据封装
     */
    private T data;

    protected ViHackerResult() {
    }

    protected ViHackerResult(long code, String message) {
        this.code = code;
        this.message = message;
    }

    protected ViHackerResult(long code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    /**
     * 成功
     *
     * @param <T>
     * @return
     */
    public static <T> ViHackerResult<T> success() {
        return new ViHackerResult<T>(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMessage());
    }

    /**
     * 成功
     *
     * @param <T>
     * @return
     */
    public static <T> ViHackerResult<T> success(String message) {
        return new ViHackerResult<T>(ResultCode.SUCCESS.getCode(), message);
    }

    /**
     * 成功返回结果
     *
     * @param data 获取的数据
     */
    public static <T> ViHackerResult<T> data(T data) {
        return new ViHackerResult<T>(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMessage(), data);
    }

    /**
     * 成功返回结果
     *
     * @param data    获取的数据
     * @param message 提示信息
     */
    public static <T> ViHackerResult<T> success(T data, String message) {
        return new ViHackerResult<T>(ResultCode.SUCCESS.getCode(), message, data);
    }

    /**
     * 失败返回结果
     *
     * @param errorCode 错误码
     */
    public static <T> ViHackerResult<T> failed(IErrorCode errorCode) {
        return new ViHackerResult<T>(errorCode.getCode(), errorCode.getMessage(), null);
    }

    /**
     * 失败返回结果
     *
     * @param errorCode 错误码
     * @param message   错误信息
     */
    public static <T> ViHackerResult<T> failed(IErrorCode errorCode, String message) {
        return new ViHackerResult<T>(errorCode.getCode(), message);
    }

    /**
     * 失败返回结果
     *
     * @param message 提示信息
     */
    public static <T> ViHackerResult<T> failed(String message) {
        return new ViHackerResult<T>(ResultCode.FAILED.getCode(), message);
    }

    /**
     * 失败返回结果
     */
    public static <T> ViHackerResult<T> failed() {
        return failed(ResultCode.FAILED);
    }

    /**
     * 参数验证失败返回结果
     */
    public static <T> ViHackerResult<T> validateFailed() {
        return failed(ResultCode.VALIDATE_FAILED);
    }

    /**
     * 参数验证失败返回结果
     *
     * @param message 提示信息
     */
    public static <T> ViHackerResult<T> validateFailed(String message) {
        return new ViHackerResult<T>(ResultCode.VALIDATE_FAILED.getCode(), message);
    }

    /**
     * 未登录返回结果
     */
    public static <T> ViHackerResult<T> unauthorized(T data) {
        return new ViHackerResult<T>(ResultCode.UNAUTHORIZED.getCode(), ResultCode.UNAUTHORIZED.getMessage(), data);
    }

    /**
     * 未授权返回结果
     */
    public static <T> ViHackerResult<T> forbidden(T data) {
        return new ViHackerResult<T>(ResultCode.FORBIDDEN.getCode(), ResultCode.FORBIDDEN.getMessage(), data);
    }
}
