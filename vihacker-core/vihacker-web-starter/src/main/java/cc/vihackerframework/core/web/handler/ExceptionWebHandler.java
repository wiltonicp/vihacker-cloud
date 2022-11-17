package cc.vihackerframework.core.web.handler;

import cc.vihackerframework.core.api.ViHackerApiResult;
import cc.vihackerframework.core.handler.BaseExceptionHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常捕获
 * Created By Ranger on 2022/11/17.
 */
@Slf4j
@ResponseBody
@RestControllerAdvice
public class ExceptionWebHandler extends BaseExceptionHandler {

    /**
     * 通用Exception异常捕获
     *
     * @param ex 自定义Exception异常类型
     * @return Result
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ViHackerApiResult handleException(Exception ex) {
        log.error("程序异常：" + ex.toString());
        String message = ex.getMessage();
        if (StringUtils.contains(message, "Bad credentials")) {
            message = "您输入的密码不正确";
        } else if (StringUtils.contains(ex.toString(), "InternalAuthenticationServiceException")) {
            message = "您输入的用户名不存在";
        }
        return ViHackerApiResult.failed(message);
    }
}
