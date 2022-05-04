package cc.vihackerframework.core.web.handler;

import cc.vihackerframework.core.api.ViHackerApiResult;
import cc.vihackerframework.core.constant.ViHackerConstant;
import cc.vihackerframework.core.util.ViHackerUtil;
import cc.vihackerframework.core.web.properties.ViHackerWebProperties;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.util.Base64Utils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 拦截器判断是否通过网关获取资源
 * Created by Ranger on 2022/05/04.
 */
public class ViHackerServerHandlerInterceptor implements HandlerInterceptor {

    private ViHackerWebProperties properties;

    @Autowired
    public void setProperties(ViHackerWebProperties properties){
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
            ViHackerUtil.response(response, MediaType.APPLICATION_JSON_VALUE,HttpServletResponse.SC_FORBIDDEN, ViHackerApiResult.failed("请通过网关获取资源"));
            return false;
        }
    }


}
