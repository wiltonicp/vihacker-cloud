package cc.vihackerframework.core.exception;

import cc.vihackerframework.core.api.IErrorCode;

/**
 * @Description
 * @Author: Ranger
 * @Date: 2021/3/4 15:32
 * @Email: wilton.icp@gmail.com
 */
public class ViHackerRuntimeException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * 错误码
     */
    protected long errorCode;
    /**
     * 错误信息
     */
    protected String errorMsg;

    public ViHackerRuntimeException() {
        super();
    }

    public ViHackerRuntimeException(IErrorCode errorCode) {
        super(String.valueOf(errorCode.getCode()));
        this.errorCode = errorCode.getCode();
        this.errorMsg = errorCode.getMessage();
    }

    public ViHackerRuntimeException(IErrorCode errorCode, Throwable cause) {
        super(String.valueOf(errorCode.getCode()), cause);
        this.errorCode = errorCode.getCode();
        this.errorMsg = errorCode.getMessage();
    }

    public ViHackerRuntimeException(String errorMsg) {
        super(errorMsg);
        this.errorMsg = errorMsg;
    }

    public ViHackerRuntimeException(Long errorCode, String errorMsg) {
        super(String.valueOf(errorCode));
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    public ViHackerRuntimeException(Long errorCode, String errorMsg, Throwable cause) {
        super(String.valueOf(errorCode), cause);
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }


    public Long getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Long errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    @Override
    public String getMessage() {
        return errorMsg;
    }

    @Override
    public Throwable fillInStackTrace() {
        return this;
    }
}
