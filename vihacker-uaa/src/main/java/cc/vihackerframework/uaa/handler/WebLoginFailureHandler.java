package cc.vihackerframework.uaa.handler;

import cc.vihackerframework.core.api.ResultCode;
import cc.vihackerframework.core.api.ViHackerResult;
import cc.vihackerframework.core.util.ViHackerUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 登录失败处理
 *
 * @Author: Ranger
 * @Date: 2021/1/28 14:00
 * @Email: wilton.icp@gmail.com
 */
@Slf4j
@Configuration
public class WebLoginFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException exception) throws IOException, ServletException {
        ResultCode resultCode = null;
        String username = httpServletRequest.getParameter("username");
        if (exception instanceof AccountExpiredException) {
            // 账号过期
            log.info("[登录失败] - 用户[{}]账号过期", username);
            resultCode = ResultCode.USER_ACCOUNT_EXPIRED;
        } else if (exception instanceof BadCredentialsException) {
            // 密码错误
            log.info("[登录失败] - 用户[{}]密码错误", username);
            resultCode = ResultCode.USER_PASSWORD_ERROR;

        } else if (exception instanceof CredentialsExpiredException) {
            // 密码过期
            log.info("[登录失败] - 用户[{}]密码过期", username);
            resultCode = ResultCode.USER_PASSWORD_EXPIRED;

        } else if (exception instanceof DisabledException) {
            // 用户被禁用
            log.info("[登录失败] - 用户[{}]被禁用", username);
            resultCode = ResultCode.USER_DISABLED;

        } else if (exception instanceof LockedException) {
            // 用户被锁定
            log.info("[登录失败] - 用户[{}]被锁定", username);
            resultCode = ResultCode.USER_LOCKED;

        } else if (exception instanceof InternalAuthenticationServiceException) {
            // 内部错误
            log.error(String.format("[登录失败] - [%s]内部错误", username));
            resultCode = ResultCode.USER_LOGIN_FAIL;

        } else {
            // 其他错误
            log.error(String.format("[登录失败] - [%s]其他错误", username), exception);
            resultCode = ResultCode.USER_LOGIN_FAIL;
        }
        ViHackerUtil.response(httpServletResponse, MediaType.APPLICATION_JSON_VALUE, HttpServletResponse.SC_OK, ViHackerResult.failed(resultCode));
    }
}
