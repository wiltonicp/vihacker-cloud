package cc.vihackerframework.core.exception;

import cc.vihackerframework.core.api.IErrorCode;

/**
 * 断言处理类，用于抛出各种API异常
 *
 * @author Ranger
 * @email wilton.icp@gmail.com
 * @since 2021/4/22
 */
public class Asserts {
    public static void fail(String message) {
        throw new ViHackerRuntimeException(message);
    }

    public static void fail(IErrorCode errorCode) {
        throw new ViHackerRuntimeException(errorCode);
    }
}
