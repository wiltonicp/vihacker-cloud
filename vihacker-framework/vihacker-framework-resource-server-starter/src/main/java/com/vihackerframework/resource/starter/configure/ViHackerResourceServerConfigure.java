package com.vihackerframework.resource.starter.configure;

import com.sun.javafx.binding.StringConstant;
import com.vihackerframework.resource.starter.handler.ViHackerAccessDeniedHandler;
import com.vihackerframework.resource.starter.handler.ViHackerAuthExceptionEntryPoint;
import com.vihackerframework.resource.starter.properties.ViHackerSecurityProperties;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;

/**
 * <p> 资源服务器配置
 * @author Ranger
 * @email wilton.icp@gmail.com
 * @since 2021/6/15
 */
@EnableResourceServer
@EnableAutoConfiguration(exclude = UserDetailsServiceAutoConfiguration.class)
public class ViHackerResourceServerConfigure extends ResourceServerConfigurerAdapter {

    private ViHackerSecurityProperties properties;
    private ViHackerAccessDeniedHandler accessDeniedHandler;
    private ViHackerAuthExceptionEntryPoint authExceptionEntryPoint;


    @Autowired
    public ViHackerResourceServerConfigure(ViHackerSecurityProperties properties, ViHackerAccessDeniedHandler accessDeniedHandler, ViHackerAuthExceptionEntryPoint authExceptionEntryPoint) {
        this.properties = properties;
        this.accessDeniedHandler = accessDeniedHandler;
        this.authExceptionEntryPoint = authExceptionEntryPoint;
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {

        String[] anonUrls = properties.getAnonUris().toArray(new String[properties.getAnonUris().size()]);

        http.csrf().disable()
                .requestMatchers().antMatchers(properties.getAuthUri())
                .and()
                .authorizeRequests()
                .antMatchers(anonUrls).permitAll()
                .antMatchers(properties.getAuthUri()).authenticated()
                .and()
                .httpBasic();
    }

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) {
        resources.authenticationEntryPoint(authExceptionEntryPoint)
                .accessDeniedHandler(accessDeniedHandler);
    }
}
