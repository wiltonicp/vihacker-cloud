package cc.vihackerframework.core.security.configure;

import cc.vihackerframework.core.security.properties.ViHackerSecurityProperties;
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


    @Override
    @SuppressWarnings("all")
    public void addInterceptors(InterceptorRegistry registry) {
        /**
         * 注册用户信息拦截器
         */
        //registry.addInterceptor(new UserInfoInterceptor()).addPathPatterns("/**");
    }
}
