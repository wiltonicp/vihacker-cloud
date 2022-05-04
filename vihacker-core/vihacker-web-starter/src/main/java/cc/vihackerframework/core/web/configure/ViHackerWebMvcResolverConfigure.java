package cc.vihackerframework.core.web.configure;

import cc.vihackerframework.core.web.handler.ViHackerServerHandlerInterceptor;
import cc.vihackerframework.core.web.properties.ViHackerWebProperties;
import cc.vihackerframework.core.web.support.LoginUserArgumentResolver;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * Created by Ranger on 2022/5/4.
 */
@Slf4j
@Configuration
@EnableConfigurationProperties(ViHackerWebProperties.class)
public class ViHackerWebMvcResolverConfigure implements WebMvcConfigurer {

    private ViHackerWebProperties properties;

    @Autowired
    public void setProperties(ViHackerWebProperties properties){
        this.properties = properties;
    }

    /**
     *  Token参数解析
     * @param argumentResolvers
     */
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        //注入用户信息
        argumentResolvers.add(new LoginUserArgumentResolver());
    }


    @Bean
    public HandlerInterceptor serverProtectInterceptor() {
        ViHackerServerHandlerInterceptor serverHandlerInterceptor = new ViHackerServerHandlerInterceptor();
        serverHandlerInterceptor.setProperties(properties);
        return serverHandlerInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(serverProtectInterceptor());
    }
}
