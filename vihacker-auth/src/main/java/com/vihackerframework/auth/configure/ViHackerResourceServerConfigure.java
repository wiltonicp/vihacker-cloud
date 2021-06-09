package com.vihackerframework.auth.configure;

import com.vihackerframework.auth.config.IgnoreUrlsConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

/**
 * <p> 资源服务器
 * @author Ranger
 * @email wilton.icp@gmail.com
 * @since 2021/6/7
 */
@Configuration
@EnableResourceServer
@RequiredArgsConstructor
public class ViHackerResourceServerConfigure extends ResourceServerConfigurerAdapter {

    private final IgnoreUrlsConfig ignoreUrlsConfig;

    @Override
    public void configure(HttpSecurity http) throws Exception {
        String[] anonUrls = ignoreUrlsConfig.getUrls().toArray(new String[ignoreUrlsConfig.getUrls().size()]);
        http.csrf().disable()
                .requestMatchers().antMatchers("/**")
                .and()
                .authorizeRequests()
                .antMatchers(anonUrls).permitAll()
                .antMatchers("/**").authenticated()
                .and()
                .httpBasic();
    }
}
