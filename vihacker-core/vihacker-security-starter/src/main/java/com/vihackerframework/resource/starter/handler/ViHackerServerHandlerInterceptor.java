package com.vihackerframework.resource.starter.handler;

import com.vihackerframework.core.api.ViHackerResult;
import com.vihackerframework.core.constant.ViHackerConstant;
import com.vihackerframework.core.util.ViHackerUtil;
import com.vihackerframework.resource.starter.properties.ViHackerSecurityProperties;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.util.Base64Utils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * <p>
 *
 * @author Ranger
 * @email wilton.icp@gmail.com
 * @since 2021/6/15
 */
public class ViHackerServerHandlerInterceptor implements HandlerInterceptor {

    private ViHackerSecurityProperties properties;

    public void setProperties(ViHackerSecurityProperties properties) {
        this.properties = properties;
    }

    @Override
    public boolean preHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) throws IOException {
        if (!properties.getOnlyFetchByGateway()) {
            return true;
        }
        String token = request.getHeader(ViHackerConstant.GATEWAY_TOKEN_HEADER);
        String gatewayToken = new String(Base64Utils.encode(ViHackerConstant.GATEWAY_TOKEN_VALUE.getBytes()));
        if (StringUtils.equals(gatewayToken, token)) {
            return true;
        } else {
            ViHackerUtil.response(response, MediaType.APPLICATION_JSON_VALUE,HttpServletResponse.SC_FORBIDDEN, ViHackerResult.failed("请通过网关获取资源"));
            return false;
        }
    }


}
