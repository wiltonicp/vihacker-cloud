package cc.vihackerframework.gateway.handler;

import cn.hutool.core.util.StrUtil;
import cc.vihackerframework.core.api.ViHackerResult;
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
    public ViHackerResult<?> handle(ResponseStatusException ex) {
        log.error("response status exception:{}", ex.getMessage());
        if (StrUtil.contains(ex.getMessage(), HttpStatus.NOT_FOUND.toString())) {
            return ViHackerResult.failed(ResultCode.VALIDATE_FAILED, ex.getMessage());
        } else {
            return ViHackerResult.failed(ResultCode.FAILED);
        }
    }

    @ExceptionHandler(value = {ConnectTimeoutException.class})
    public ViHackerResult<?> handle(ConnectTimeoutException ex) {
        log.error("connect timeout exception:{}", ex.getMessage());
        return ViHackerResult.failed(ResultCode.FAILED);
    }

    @ExceptionHandler(value = {NotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ViHackerResult<?> handle(NotFoundException ex) {
        log.error("not found exception:{}", ex.getMessage());
        return ViHackerResult.failed(ResultCode.VALIDATE_FAILED);
    }

    @ExceptionHandler(value = {RuntimeException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ViHackerResult<?> handle(RuntimeException ex) {
        log.error("runtime exception:{}", ex.getMessage());
        return ViHackerResult.failed(ex.getMessage());
    }

    @ExceptionHandler(value = {Exception.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ViHackerResult<?> handle(Exception ex) {
        log.error("exception:{}", ex.getMessage());
        return ViHackerResult.failed();
    }

    @ExceptionHandler(value = {Throwable.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ViHackerResult<?> handle(Throwable throwable) {
        ViHackerResult viHackerResult = ViHackerResult.failed();
        if (throwable instanceof ResponseStatusException) {
            viHackerResult = handle((ResponseStatusException) throwable);
        } else if (throwable instanceof ConnectTimeoutException) {
            viHackerResult = handle((ConnectTimeoutException) throwable);
        } else if (throwable instanceof NotFoundException) {
            viHackerResult = handle((NotFoundException) throwable);
        } else if (throwable instanceof RuntimeException) {
            viHackerResult = handle((RuntimeException) throwable);
        } else if (throwable instanceof Exception) {
            viHackerResult = handle((Exception) throwable);
        }
        return viHackerResult;
    }
}
