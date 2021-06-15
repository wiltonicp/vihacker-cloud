package com.vihackerframework.resource.starter.configure;

import com.vihackerframework.core.constant.ViHackerConstant;
import com.vihackerframework.core.util.ViHackerUtil;
import com.vihackerframework.resource.starter.handler.ViHackerAccessDeniedHandler;
import com.vihackerframework.resource.starter.handler.ViHackerAuthExceptionEntryPoint;
import com.vihackerframework.resource.starter.properties.ViHackerSecurityProperties;
import feign.RequestInterceptor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties;
import org.springframework.boot.autoconfigure.security.oauth2.resource.UserInfoTokenServices;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpHeaders;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.expression.OAuth2MethodSecurityExpressionHandler;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.util.Base64Utils;

/**
 * <p>
 * @author Ranger
 * @email wilton.icp@gmail.com
 * @since 2021/6/15
 */
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
    @Primary
    @ConditionalOnMissingBean(DefaultTokenServices.class)
    public ViHackerUserInfoTokenServices userInfoTokenServices(ResourceServerProperties properties) {
        return new ViHackerUserInfoTokenServices(properties.getUserInfoUri(), properties.getClientId());
    }

    @Bean
    public RequestInterceptor oauth2FeignRequestInterceptor() {
        return requestTemplate -> {
            String gatewayToken = new String(Base64Utils.encode(ViHackerConstant.GATEWAY_TOKEN_VALUE.getBytes()));
            requestTemplate.header(ViHackerConstant.GATEWAY_TOKEN_HEADER, gatewayToken);
            String authorizationToken = ViHackerUtil.getCurrentTokenValue();
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
