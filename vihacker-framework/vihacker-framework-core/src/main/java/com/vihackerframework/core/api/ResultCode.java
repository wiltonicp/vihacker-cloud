package com.vihackerframework.core.api;

/**
 * 枚举了一些常用API操作码
 *
 * @Description
 * @Author: Ranger
 * @Date: 2021/1/15 10:27
 * @Email: wilton.icp@gmail.com
 */
public enum ResultCode implements IErrorCode {
    SUCCESS(200, "操作成功"),
    FAILED(500, "操作失败"),
    BODY_NOT_MATCH(400, "请求的数据格式不符!"),
    UNAUTHORIZED(401, "暂未登录或token已经过期"),
    FORBIDDEN(403, "没有相关权限"),
    VALIDATE_FAILED(404, "未找到该资源!"),
    SERVER_BUSY(503, "服务器正忙，请稍后再试!"),
    REQUEST_METHOD_SUPPORT_ERROR(40001, "当前请求方法不支持");;

    /**
     * 错误码
     */
    private long code;
    /**
     * 错误描述
     */
    private String message;

    ResultCode(long code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public long getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
