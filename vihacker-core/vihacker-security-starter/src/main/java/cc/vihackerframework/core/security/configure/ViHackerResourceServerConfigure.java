package cc.vihackerframework.core.security.configure;

import cc.vihackerframework.core.security.handler.ViHackerAccessDeniedHandler;
import cc.vihackerframework.core.security.handler.ViHackerAuthExceptionEntryPoint;
import cc.vihackerframework.core.security.properties.ViHackerSecurityProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;

/**
 * <p> 资源服务器配置
 * @author Ranger
 * @email wilton.icp@gmail.com
 * @since 2021/6/15
 */
@Order(5)
@EnableResourceServer
@EnableAutoConfiguration(exclude = UserDetailsServiceAutoConfiguration.class)
public class ViHackerResourceServerConfigure extends ResourceServerConfigurerAdapter {

    private ViHackerSecurityProperties properties;
    private ViHackerAccessDeniedHandler accessDeniedHandler;
    private ViHackerAuthExceptionEntryPoint authExceptionEntryPoint;

    @Autowired(required = false)
    public void setProperties(ViHackerSecurityProperties properties) {
        this.properties = properties;
    }

    @Autowired(required = false)
    public void setAccessDeniedHandler(ViHackerAccessDeniedHandler accessDeniedHandler) {
        this.accessDeniedHandler = accessDeniedHandler;
    }

    @Autowired(required = false)
    public void setExceptionEntryPoint(ViHackerAuthExceptionEntryPoint authExceptionEntryPoint) {
        this.authExceptionEntryPoint = authExceptionEntryPoint;
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry config
                = http.requestMatchers().anyRequest()
                .and()
                .authorizeRequests();
        properties.getIgnoreUrls().forEach(url -> {
            config.antMatchers(url).permitAll();
        });
        config
            //任何请求
            .anyRequest()
            //都需要身份认证
            .authenticated()
            //csrf跨站请求
            .and()
            .csrf().disable()
            .httpBasic().disable();
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

    private void permitAll(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.authorizeRequests().anyRequest().permitAll();
    }
}
