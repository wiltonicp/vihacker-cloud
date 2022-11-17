package cc.vihackerframework.core.handler;

import cc.vihackerframework.core.api.ResultCode;
import cc.vihackerframework.core.api.ViHackerApiResult;
import cc.vihackerframework.core.exception.ValidateCodeException;
import cc.vihackerframework.core.exception.ViHackerAuthException;
import cc.vihackerframework.core.exception.ViHackerException;
import cc.vihackerframework.core.exception.ViHackerRuntimeException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Path;
import java.nio.file.AccessDeniedException;
import java.sql.SQLException;
import java.sql.SQLSyntaxErrorException;
import java.util.List;
import java.util.Set;

/**
 * 全局统用异常处理器
 * 所谓的全局异常处理指的是全局处理Controller层抛出来的异常。因为全局异常处理器在各个服务系统里都能用到
 * 对于通用的异常类型捕获可以在BaseExceptionHandler中定义
 *
 * @Description
 * @Author: Ranger
 * @Date: 2021/1/15 13:57
 * @Email: wilton.icp@gmail.com
 */
@Slf4j
public class BaseExceptionHandler {


    @ExceptionHandler(value = ViHackerException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ViHackerApiResult handleBaseException(ViHackerException e) {
        log.error("系统异常", e);
        return ViHackerApiResult.failed(e.getMessage());
    }

    /**
     * 处理自定义的业务异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler(value = ViHackerRuntimeException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ViHackerApiResult bizExceptionHandler(ViHackerRuntimeException e) {
        log.error("发生业务异常！原因是：{}", e.getErrorMsg());
        return ViHackerApiResult.failed(e.getMessage());
    }

    /**
     * 处理空指针的异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler(value = NullPointerException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ViHackerApiResult exceptionHandler(NullPointerException e) {
        log.error("发生空指针异常！原因是:", e);
        return ViHackerApiResult.failed(ResultCode.BODY_NOT_MATCH);
    }

    /**
     * 校验异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ViHackerApiResult exceptionHandler(MethodArgumentNotValidException e) {
        BindingResult bindingResult = e.getBindingResult();
        String errorMesssage = "";
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            errorMesssage += fieldError.getDefaultMessage() + "!";
        }
        log.error("参数异常:", e);
        return ViHackerApiResult.failed(errorMesssage);
    }

    /**
     * 统一处理请求参数校验(实体对象传参)
     *
     * @param e BindException
     * @return ViHackerApiResult
     */
    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ViHackerApiResult handleBindException(BindException e) {
        StringBuilder message = new StringBuilder();
        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
        for (FieldError error : fieldErrors) {
            message.append(error.getField()).append(error.getDefaultMessage()).append(",");
        }
        message = new StringBuilder(message.substring(0, message.length() - 1));
        log.error("参数校验错误：{}",e);
        return ViHackerApiResult.failed(message.toString());
    }

    /**
     * 统一处理请求参数校验(普通传参)
     *
     * @param e ConstraintViolationException
     * @return ViHackerApiResult
     */
    @ExceptionHandler(value = ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ViHackerApiResult handleConstraintViolationException(ConstraintViolationException e) {
        StringBuilder message = new StringBuilder();
        Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
        for (ConstraintViolation<?> violation : violations) {
            Path path = violation.getPropertyPath();
            String[] pathArr = StringUtils.splitByWholeSeparatorPreserveAllTokens(path.toString(), ".");
            message.append(pathArr[1]).append(violation.getMessage()).append(",");
        }
        message = new StringBuilder(message.substring(0, message.length() - 1));
        log.error("参数校验错误：{}",e);
        return ViHackerApiResult.failed(message.toString());
    }

    @ExceptionHandler(value = ViHackerAuthException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ViHackerApiResult handleWiltonException(ViHackerException e) {
        log.error("系统错误", e);
        return ViHackerApiResult.failed(e.getMessage());
    }

//    @ExceptionHandler(value = Exception.class)
//    @ResponseStatus(HttpStatus.OK)
//    public ViHackerApiResult handleException(Exception e) {
//        log.error("系统内部异常，异常信息", e);
//        return ViHackerApiResult.failed("系统内部异常");
//    }

    @ExceptionHandler(value = ValidateCodeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ViHackerApiResult handleValidateCodeException(ValidateCodeException e) {
        log.error("系统错误", e);
        return ViHackerApiResult.failed(e.getMessage());
    }


    @ExceptionHandler(value = AccessDeniedException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ViHackerApiResult handleAccessDeniedException() {
        return ViHackerApiResult.failed(ResultCode.FORBIDDEN);
    }

    @ExceptionHandler(value = SQLSyntaxErrorException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ViHackerApiResult sqlSyntaxErrorException() {
        return ViHackerApiResult.failed("SQL异常，请检查服务端!");
    }

    @ExceptionHandler(value= DataAccessException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ViHackerApiResult dataAccessErrorHandler(DataAccessException e){
        log.error("SQL错误：", e);
        return ViHackerApiResult.failed("SQL异常，请检查服务端!");
    }

    @ExceptionHandler(value= SQLException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ViHackerApiResult sqlErrorHandler(SQLException e){
        log.error("SQL错误：", e);
        return ViHackerApiResult.failed("SQL异常，请检查服务端!");
    }
}
