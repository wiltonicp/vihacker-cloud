package cc.vihackerframework.uaa.configure;

import cc.vihackerframework.core.security.properties.ViHackerSecurityProperties;
import cc.vihackerframework.uaa.filter.ValidateCodeFilter;
import cc.vihackerframework.uaa.handler.ViHackerAuthenticationFailureHandler;
import cc.vihackerframework.uaa.handler.ViHackerAuthenticationSuccessHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * <p> 安全配置
 * @author Ranger
 * @email wilton.icp@gmail.com
 * @since 2021/6/5
 */
@Order(4)
@EnableWebSecurity
@RequiredArgsConstructor
public class ViHackerSecurityConfigure extends WebSecurityConfigurerAdapter {

    private final ValidateCodeFilter validateCodeFilter;
    private final ViHackerSecurityProperties properties;
    private final UserDetailsService userDetailsService;

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler() {
        return new ViHackerAuthenticationSuccessHandler();
    }

    @Bean
    public AuthenticationFailureHandler webLoginFailureHandler() {
        return new ViHackerAuthenticationFailureHandler();
    }

//    @Override
//    @Bean
//    public UserDetailsService userDetailsService() {
//        return new ViHackerUserDetailsServiceImpl();
//    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.addFilterBefore(validateCodeFilter, UsernamePasswordAuthenticationFilter.class);
        ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry config
                = http.requestMatchers().anyRequest()
                .and()
                .formLogin()
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
            .csrf().disable();
            //.httpBasic().disable();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
