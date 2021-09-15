package cc.vihackerframework.resource.starter.configure;

import cc.vihackerframework.resource.starter.handler.ViHackerAccessDeniedHandler;
import cc.vihackerframework.resource.starter.handler.ViHackerAuthExceptionEntryPoint;
import cc.vihackerframework.resource.starter.properties.ViHackerSecurityProperties;
import lombok.RequiredArgsConstructor;
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
//@Order(5)
@EnableResourceServer
@RequiredArgsConstructor
@EnableAutoConfiguration(exclude = UserDetailsServiceAutoConfiguration.class)
public class ViHackerResourceServerConfigure extends ResourceServerConfigurerAdapter {

    private final ViHackerSecurityProperties properties;
    private final ViHackerAccessDeniedHandler accessDeniedHandler;
    private final ViHackerAuthExceptionEntryPoint authExceptionEntryPoint;

    @Override
    public void configure(HttpSecurity http) throws Exception {

        String[] anonUrls = properties.getIgnoreUrls().toArray(new String[properties.getIgnoreUrls().size()]);
        ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry config
                = http.csrf().disable().requestMatchers().anyRequest()
                .and()
                .authorizeRequests();
        config
            .antMatchers(anonUrls).permitAll()
            .antMatchers(properties.getAuthUri()).authenticated()
            //任何请求
            .anyRequest()
            //都需要身份认证
            .authenticated()
            .and()
            .httpBasic();
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
