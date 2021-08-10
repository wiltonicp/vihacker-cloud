package cc.vihackerframework.core.exception;

/**
 * 系统通用业务异常
 *
 * @Description
 * @Author: Ranger
 * @Date: 2021/1/15 14:03
 * @Email: wilton.icp@gmail.com
 */
public class ViHackerException extends Exception {

    private static final long serialVersionUID = -6916154462432027437L;

    public ViHackerException(String message) {
        super(message);
    }
}
