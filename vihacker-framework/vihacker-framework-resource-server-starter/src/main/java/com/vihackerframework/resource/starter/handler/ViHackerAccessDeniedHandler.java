package com.vihackerframework.resource.starter.handler;

import com.vihackerframework.core.api.ViHackerResult;
import com.vihackerframework.core.util.ViHackerUtil;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * <p>
 * @author Ranger
 * @since 2021/6/15
 * @email wilton.icp@gmail.com
 */
public class ViHackerAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException {
        ViHackerUtil.response(response, MediaType.APPLICATION_JSON_VALUE,HttpServletResponse.SC_FORBIDDEN,ViHackerResult.failed("没有权限访问该资源"));
    }
}
