package cc.vihackerframework.resource.starter.configure;

import cc.vihackerframework.resource.starter.handler.ViHackerAccessDeniedHandler;
import cc.vihackerframework.resource.starter.handler.ViHackerAuthExceptionEntryPoint;
import cc.vihackerframework.resource.starter.properties.ViHackerSecurityProperties;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ArrayUtils;
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
        if (properties == null) {
            permitAll(http);
            return;
        }
        String[] anonUrls = properties.getIgnoreUrls().toArray(new String[properties.getIgnoreUrls().size()]);
        if (ArrayUtils.isEmpty(anonUrls)) {
            anonUrls = new String[]{};
        }

        if (ArrayUtils.contains(anonUrls, properties.getAuthUri())) {
            permitAll(http);
            return;
        }
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
