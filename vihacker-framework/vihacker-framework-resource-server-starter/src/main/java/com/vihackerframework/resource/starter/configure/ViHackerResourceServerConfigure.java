package com.vihackerframework.resource.starter.configure;

import com.sun.javafx.binding.StringConstant;
import com.vihackerframework.resource.starter.handler.ViHackerAccessDeniedHandler;
import com.vihackerframework.resource.starter.handler.ViHackerAuthExceptionEntryPoint;
import com.vihackerframework.resource.starter.properties.ViHackerSecurityProperties;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer.ExpressionInterceptUrlRegistry;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

/**
 * <p> 资源服务器配置
 * @author Ranger
 * @email wilton.icp@gmail.com
 * @since 2021/6/15
 */
@Order(5)
@EnableResourceServer
@RequiredArgsConstructor
@EnableAutoConfiguration(exclude = UserDetailsServiceAutoConfiguration.class)
public class ViHackerResourceServerConfigure extends ResourceServerConfigurerAdapter {

    private final ViHackerSecurityProperties properties;
    private final ViHackerAccessDeniedHandler accessDeniedHandler;
    private final ViHackerAuthExceptionEntryPoint authExceptionEntryPoint;

    @Override
    public void configure(HttpSecurity http) throws Exception {

        String[] anonUrls = properties.getAnonUris().toArray(new String[properties.getAnonUris().size()]);
        ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry config
                = http.requestMatchers().anyRequest()
                .and()
                .authorizeRequests();
        config
            .antMatchers(anonUrls).permitAll()
            .antMatchers(properties.getAuthUri()).permitAll()
            //任何请求
            .anyRequest()
            //都需要身份认证
            .authenticated()
            //csrf跨站请求
            .and()
            .csrf().disable();
    }

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) {
        if (authExceptionEntryPoint != null) {
            resources.authenticationEntryPoint(authExceptionEntryPoint);
        }
        if (accessDeniedHandler != null) {
            resources.accessDeniedHandler(accessDeniedHandler);
        }
    }
}
