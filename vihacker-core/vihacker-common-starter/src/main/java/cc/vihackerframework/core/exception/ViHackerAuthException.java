package cc.vihackerframework.core.exception;

/**
 * 认证处理异常
 *
 * @author Ranger
 * @email wilton.icp@gmail.com
 * @since 2020/6/4
 */
public class ViHackerAuthException extends ViHackerRuntimeException {

    private static final long serialVersionUID = -6916154462432027437L;

    public ViHackerAuthException(String message) {
        super(message);
    }

}
