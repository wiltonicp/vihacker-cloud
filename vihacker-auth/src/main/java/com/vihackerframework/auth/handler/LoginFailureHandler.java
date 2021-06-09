package com.vihackerframework.auth.handler;

import com.vihackerframework.core.api.ViHackerResult;
import com.vihackerframework.core.util.ViHackerUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
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
        String message;
        if (e instanceof BadCredentialsException) {
            message = "用户名或密码错误！";
        } else if (e instanceof LockedException) {
            message = "用户已被锁定！";
        } else {
            message = "认证失败，请联系网站管理员！";
        }
        ViHackerUtil.response(httpServletResponse, MediaType.APPLICATION_JSON_VALUE, HttpServletResponse.SC_OK, ViHackerResult.failed(message));
    }
}
