package cc.vihackerframework.uaa.handler;

import cc.vihackerframework.core.api.ResultCode;
import cc.vihackerframework.core.api.ViHackerResult;
import cc.vihackerframework.core.util.ViHackerUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
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
public class LoginFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
        ResultCode resultCode = null;
        String username = httpServletRequest.getParameter("username");
        if (e instanceof AccountExpiredException) {
            resultCode = ResultCode.USER_ACCOUNT_EXPIRED;
            log.info("[登录失败] - 用户[{}]账号过期", username);
        }else if (e instanceof BadCredentialsException) {
            resultCode = ResultCode.USER_PASSWORD_ERROR;
            log.info("[登录失败] - 用户[{}]密码错误", username);
        }else if (e instanceof DisabledException) {
            resultCode = ResultCode.USER_DISABLED;
            log.info("[登录失败] - 用户[{}]被禁用", username);
        } else if (e instanceof LockedException) {
            resultCode = ResultCode.USER_LOCKED;
            log.info("[登录失败] - 用户[{}]被锁定", username);
        } else {
            log.error(String.format("[登录失败] - [%s]其他错误", username), e);
            resultCode = ResultCode.USER_LOGIN_FAIL;
        }
        ViHackerUtil.response(httpServletResponse, MediaType.APPLICATION_JSON_VALUE, HttpServletResponse.SC_OK, ViHackerResult.failed(resultCode));
    }
}
