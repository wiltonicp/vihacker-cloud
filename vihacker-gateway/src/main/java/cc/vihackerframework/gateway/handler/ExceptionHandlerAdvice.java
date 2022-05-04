package cc.vihackerframework.gateway.handler;

import cn.hutool.core.util.StrUtil;
import cc.vihackerframework.core.api.ViHackerApiResult;
import cc.vihackerframework.core.api.ResultCode;
import cc.vihackerframework.core.handler.BaseExceptionHandler;
import io.netty.channel.ConnectTimeoutException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.support.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ResponseStatusException;

/**
 * 异常处理通知
 *
 * @author Ranger
 * @email wilton.icp@gmail.com
 * @since 2021/7/29
 */
@Slf4j
@Component
public class ExceptionHandlerAdvice extends BaseExceptionHandler {

    @ExceptionHandler(value = {ResponseStatusException.class})
    public ViHackerApiResult<?> handle(ResponseStatusException ex) {
        log.error("response status exception:{}", ex.getMessage());
        if (StrUtil.contains(ex.getMessage(), HttpStatus.NOT_FOUND.toString())) {
            return ViHackerApiResult.failed(ResultCode.VALIDATE_FAILED, ex.getMessage());
        } else {
            return ViHackerApiResult.failed(ResultCode.FAILED);
        }
    }

    @ExceptionHandler(value = {ConnectTimeoutException.class})
    public ViHackerApiResult<?> handle(ConnectTimeoutException ex) {
        log.error("connect timeout exception:{}", ex.getMessage());
        return ViHackerApiResult.failed(ResultCode.FAILED);
    }

    @ExceptionHandler(value = {NotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ViHackerApiResult<?> handle(NotFoundException ex) {
        log.error("not found exception:{}", ex.getMessage());
        return ViHackerApiResult.failed(ResultCode.VALIDATE_FAILED);
    }

    @ExceptionHandler(value = {RuntimeException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ViHackerApiResult<?> handle(RuntimeException ex) {
        log.error("runtime exception:{}", ex.getMessage());
        return ViHackerApiResult.failed(ex.getMessage());
    }

    @ExceptionHandler(value = {Exception.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ViHackerApiResult<?> handle(Exception ex) {
        log.error("exception:{}", ex.getMessage());
        return ViHackerApiResult.failed();
    }

    @ExceptionHandler(value = {Throwable.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ViHackerApiResult<?> handle(Throwable throwable) {
        ViHackerApiResult failed = ViHackerApiResult.failed();
        if (throwable instanceof ResponseStatusException) {
            failed = handle((ResponseStatusException) throwable);
        } else if (throwable instanceof ConnectTimeoutException) {
            failed = handle((ConnectTimeoutException) throwable);
        } else if (throwable instanceof NotFoundException) {
            failed = handle((NotFoundException) throwable);
        } else if (throwable instanceof RuntimeException) {
            failed = handle((RuntimeException) throwable);
        } else if (throwable instanceof Exception) {
            failed = handle((Exception) throwable);
        }
        return failed;
    }
}
