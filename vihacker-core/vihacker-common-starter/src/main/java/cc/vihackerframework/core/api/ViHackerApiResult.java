package cc.vihackerframework.core.api;

import lombok.Data;

import java.io.Serializable;

/**
 * 通用api 返回对象
 *
 * @author Ranger
 * @since 2021/1/15
 * @email wilton.icp@gmail.com
 */
@Data
public class ViHackerApiResult<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 状态码
     */
    private long code;
    /**
     * 提示信息
     */
    private String message;
    /**
     * 时间戳
     */
    private long time;
    /**
     * 数据封装
     */
    private T data;

    /**
     * 数据是否加密
     */
    private Boolean encrypt = Boolean.FALSE;

    protected ViHackerApiResult() {
    }

    protected ViHackerApiResult(long code, String message) {
        this.code = code;
        this.message = message;
        this.time = System.currentTimeMillis();
    }

    protected ViHackerApiResult(long code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
        this.time = System.currentTimeMillis();
    }

    /**
     * 成功
     *
     * @param <T>
     * @return
     */
    public static <T> ViHackerApiResult<T> success() {
        return new ViHackerApiResult<T>(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMessage());
    }

    /**
     * 成功
     *
     * @param <T>
     * @return
     */
    public static <T> ViHackerApiResult<T> success(String message) {
        return new ViHackerApiResult<T>(ResultCode.SUCCESS.getCode(), message);
    }

    /**
     * 成功返回结果
     *
     * @param data 获取的数据
     */
    public static <T> ViHackerApiResult<T> data(T data) {
        return new ViHackerApiResult<T>(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMessage(), data);
    }

    /**
     * 成功返回结果
     *
     * @param data    获取的数据
     * @param message 提示信息
     */
    public static <T> ViHackerApiResult<T> success(T data, String message) {
        return new ViHackerApiResult<T>(ResultCode.SUCCESS.getCode(), message, data);
    }

    /**
     * 成功返回结果
     * @param flag
     * @return
     * @param <T>
     */
    public static <T> ViHackerApiResult<T> success(boolean flag) {
        return flag ? success() : failed();
    }

    /**
     * 失败返回结果
     *
     * @param errorCode 错误码
     */
    public static <T> ViHackerApiResult<T> failed(IErrorCode errorCode) {
        return new ViHackerApiResult<T>(errorCode.getCode(), errorCode.getMessage(), null);
    }

    /**
     * 失败返回结果
     *
     * @param errorCode 错误码
     * @param message   错误信息
     */
    public static <T> ViHackerApiResult<T> failed(IErrorCode errorCode, String message) {
        return new ViHackerApiResult<T>(errorCode.getCode(), message);
    }

    /**
     * 失败返回结果
     *
     * @param message 提示信息
     */
    public static <T> ViHackerApiResult<T> failed(String message) {
        return new ViHackerApiResult<T>(ResultCode.FAILED.getCode(), message);
    }

    /**
     * 失败返回结果
     */
    public static <T> ViHackerApiResult<T> failed() {
        return failed(ResultCode.FAILED);
    }

    /**
     * 参数验证失败返回结果
     */
    public static <T> ViHackerApiResult<T> validateFailed() {
        return failed(ResultCode.VALIDATE_FAILED);
    }

    /**
     * 参数验证失败返回结果
     *
     * @param message 提示信息
     */
    public static <T> ViHackerApiResult<T> validateFailed(String message) {
        return new ViHackerApiResult<T>(ResultCode.VALIDATE_FAILED.getCode(), message);
    }

    /**
     * 未登录返回结果
     */
    public static <T> ViHackerApiResult<T> unauthorized(T data) {
        return new ViHackerApiResult<T>(ResultCode.UNAUTHORIZED.getCode(), ResultCode.UNAUTHORIZED.getMessage(), data);
    }

    /**
     * 未授权返回结果
     */
    public static <T> ViHackerApiResult<T> forbidden(T data) {
        return new ViHackerApiResult<T>(ResultCode.FORBIDDEN.getCode(), ResultCode.FORBIDDEN.getMessage(), data);
    }

    /**
     * 自定义返回结果
     */
    public static <T> ViHackerApiResult<T> customize(long code, String message) {
        return new ViHackerApiResult<T>(code, message);
    }
}
