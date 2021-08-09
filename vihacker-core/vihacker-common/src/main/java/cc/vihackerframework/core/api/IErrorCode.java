package cc.vihackerframework.core.api;

/**
 * 封装API的错误码
 *
 * @Description
 * @Author: Ranger
 * @Date: 2021/1/15 10:27
 * @Email: wilton.icp@gmail.com
 */
public interface IErrorCode {
    long getCode();

    String getMessage();
}
