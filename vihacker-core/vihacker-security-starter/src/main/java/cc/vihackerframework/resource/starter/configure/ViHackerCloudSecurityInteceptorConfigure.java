package cc.vihackerframework.resource.starter.configure;

import cc.vihackerframework.resource.starter.handler.ViHackerServerHandlerInterceptor;
import cc.vihackerframework.resource.starter.interceptor.UserInfoInterceptor;
import cc.vihackerframework.resource.starter.properties.ViHackerSecurityProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * <p>
 * @author Ranger
 * @email wilton.icp@gmail.com
 * @since 2021/6/15
 */
public class ViHackerCloudSecurityInteceptorConfigure implements WebMvcConfigurer {

    private ViHackerSecurityProperties properties;

    @Autowired
    public void setProperties(ViHackerSecurityProperties properties) {
        this.properties = properties;
    }

    @Bean
    public HandlerInterceptor serverProtectInterceptor() {
        ViHackerServerHandlerInterceptor serverHandlerInterceptor = new ViHackerServerHandlerInterceptor();
        serverHandlerInterceptor.setProperties(properties);
        return serverHandlerInterceptor;
    }



    @Override
    @SuppressWarnings("all")
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(serverProtectInterceptor());
        /**
         * 注册用户信息拦截器
         */
        registry.addInterceptor(new UserInfoInterceptor()).addPathPatterns("/**");
    }
}
