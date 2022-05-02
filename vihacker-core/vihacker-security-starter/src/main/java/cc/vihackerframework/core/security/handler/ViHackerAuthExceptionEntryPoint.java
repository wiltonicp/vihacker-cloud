package cc.vihackerframework.core.security.handler;

import cc.vihackerframework.core.api.ViHackerResult;
import cc.vihackerframework.core.util.ViHackerUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * <p>
 * @author Ranger
 * @since 2021/6/15
 * @email wilton.icp@gmail.com
 */
@Slf4j
public class ViHackerAuthExceptionEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        String requestUri = request.getRequestURI();
        int status = HttpServletResponse.SC_UNAUTHORIZED;
        String message = "访问令牌不合法";
        log.error("客户端访问:{},请求失败: {}", requestUri, message, authException);
        ViHackerUtil.response(response, MediaType.APPLICATION_JSON_VALUE,status, ViHackerResult.unauthorized(message));
    }
}
