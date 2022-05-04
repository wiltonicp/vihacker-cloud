package cc.vihackerframework.core.feign.config;

import cc.vihackerframework.core.constant.TenantConstant;
import cc.vihackerframework.core.constant.ViHackerConstant;
import cc.vihackerframework.core.context.TenantContextHolder;
import cc.vihackerframework.core.util.SecurityUtil;
import cc.vihackerframework.core.util.StringUtil;
import feign.RequestInterceptor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.util.Base64Utils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * feign拦截器
 * Created by Ranger on 2022/5/4.
 */
public class FeignInterceptorConfiguration {

    /**
     * 使用feign client发送请求时，传递tenantId和traceId
     * @return
     */
    @Bean
    public RequestInterceptor requestInterceptor() {
        return requestTemplate -> {
            //传递tenantId
            String tenantId = TenantContextHolder.getTenantId();
            if (StringUtil.isNotBlank(tenantId)) {
                requestTemplate.header(TenantConstant.VIHACKER_TENANT_ID, tenantId);
            }
            String gatewayToken = new String(Base64Utils.encode(ViHackerConstant.GATEWAY_TOKEN_VALUE.getBytes()));
            requestTemplate.header(ViHackerConstant.GATEWAY_TOKEN_HEADER, gatewayToken);
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attributes != null) {
                HttpServletRequest request = attributes.getRequest();
                String headerToken = SecurityUtil.getHeaderToken(request);
                if (StringUtils.isNotBlank(headerToken)) {
                    requestTemplate.header(HttpHeaders.AUTHORIZATION, ViHackerConstant.OAUTH2_TOKEN_TYPE + headerToken);
                }
            }
        };
    }
}
