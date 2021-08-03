package com.vihackerframework.uaa.configure;

import com.vihackerframework.uaa.constant.SecurityConstant;
import com.vihackerframework.uaa.filter.ValidateCodeFilter;
import com.vihackerframework.uaa.handler.LoginFailureHandler;
import com.vihackerframework.uaa.handler.LoginSuccessHandler;
import com.vihackerframework.uaa.handler.ViHackerAuthenticationFailureHandler;
import com.vihackerframework.uaa.handler.ViHackerAuthenticationSuccessHandler;
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
//@Order(3)
@EnableWebSecurity
@RequiredArgsConstructor
public class ViHackerSecurityConfigure extends WebSecurityConfigurerAdapter {

    private final ValidateCodeFilter validateCodeFilter;
    private final UserDetailsService userDetailService;

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public AuthenticationSuccessHandler vihackerAuthenticationSuccessHandler() {
        return new ViHackerAuthenticationSuccessHandler();
    }

    @Bean
    public AuthenticationFailureHandler vihackerAuthenticationFailureHandler() {
        return new ViHackerAuthenticationFailureHandler();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.addFilterBefore(validateCodeFilter, UsernamePasswordAuthenticationFilter.class);
        http.requestMatchers()
            .antMatchers(SecurityConstant.AUTH_PATH, SecurityConstant.SOCIAL_PATH)
            .and()
            .authorizeRequests()
            .antMatchers(SecurityConstant.AUTH_PATH).authenticated()
            .and()
            .formLogin()
            //.loginPage("/login")
            //.loginProcessingUrl("/login")
            //.successHandler(loginSuccessHandler)
            //.failureHandler(loginFailureHandler)
            .permitAll()
            .and().csrf().disable()
            .httpBasic().disable();
        //http.headers().frameOptions().sameOrigin();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailService).passwordEncoder(passwordEncoder());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
