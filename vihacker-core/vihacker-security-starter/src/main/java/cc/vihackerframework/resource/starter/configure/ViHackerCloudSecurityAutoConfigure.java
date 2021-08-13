package cc.vihackerframework.resource.starter.configure;

import cc.vihackerframework.core.auth.util.SecurityUtil;
import cc.vihackerframework.core.constant.ViHackerConstant;
import cc.vihackerframework.resource.starter.handler.ViHackerAccessDeniedHandler;
import cc.vihackerframework.resource.starter.handler.ViHackerAuthExceptionEntryPoint;
import cc.vihackerframework.resource.starter.properties.ViHackerSecurityProperties;
import feign.RequestInterceptor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.expression.OAuth2MethodSecurityExpressionHandler;
import org.springframework.util.Base64Utils;

/**
 * <p>
 * @author Ranger
 * @email wilton.icp@gmail.com
 * @since 2021/6/15
 */
@Order(6)
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableConfigurationProperties(ViHackerSecurityProperties.class)
@ConditionalOnProperty(value = "vihacker.security.enable", havingValue = "true", matchIfMissing = true)
public class ViHackerCloudSecurityAutoConfigure extends GlobalMethodSecurityConfiguration {

    @Bean
    @ConditionalOnMissingBean(name = "accessDeniedHandler")
    public ViHackerAccessDeniedHandler accessDeniedHandler() {
        return new ViHackerAccessDeniedHandler();
    }

    @Bean
    @ConditionalOnMissingBean(name = "authenticationEntryPoint")
    public ViHackerAuthExceptionEntryPoint authenticationEntryPoint() {
        return new ViHackerAuthExceptionEntryPoint();
    }

    @Bean
    @ConditionalOnMissingBean(value = PasswordEncoder.class)
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public ViHackerCloudSecurityInteceptorConfigure cloudSecurityInteceptorConfigure() {
        return new ViHackerCloudSecurityInteceptorConfigure();
    }

    @Bean
    public RequestInterceptor oauth2FeignRequestInterceptor() {
        return requestTemplate -> {
            String gatewayToken = new String(Base64Utils.encode(ViHackerConstant.GATEWAY_TOKEN_VALUE.getBytes()));
            requestTemplate.header(ViHackerConstant.GATEWAY_TOKEN_HEADER, gatewayToken);
            String authorizationToken = SecurityUtil.getCurrentTokenValue();
            if (StringUtils.isNotBlank(authorizationToken)) {
                requestTemplate.header(HttpHeaders.AUTHORIZATION, ViHackerConstant.OAUTH2_TOKEN_TYPE + authorizationToken);
            }
        };
    }

    @Override
    protected MethodSecurityExpressionHandler createExpressionHandler() {
        return new OAuth2MethodSecurityExpressionHandler();
    }
}
