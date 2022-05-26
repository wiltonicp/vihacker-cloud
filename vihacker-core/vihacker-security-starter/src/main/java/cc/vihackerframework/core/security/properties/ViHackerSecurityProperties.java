package cc.vihackerframework.core.security.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * <p>
 *
 * @author Ranger
 * @email wilton.icp@gmail.com
 * @since 2021/6/15
 */
@Data
@ConfigurationProperties(prefix = "vihacker.security")
public class ViHackerSecurityProperties {

    /**
     * 是否开启安全配置
     */
    private Boolean enable;
    /**
     * 配置需要认证的uri，默认为所有/**
     */
    private String authUri = "/**";
    /**
     * 免认证资源路径，支持通配符
     * 多个值时使用逗号分隔
     */
    private List<String> ignoreUrls = new ArrayList<>();

    /**
     * 认证中心默认忽略验证地址
     */
    private static final String[] SECURITY_ENDPOINTS = {
            "/oauth/**",
            "/actuator/**",
            "/v2/api-docs/**",
            "/swagger/api-docs",
            "/swagger-ui.html",
            "/doc.html",
            "/login",
            "/js/**",
            "/css/**",
            "/images/**",
            "/resource/**",
            "/static/**",
            "/swagger-resources/**",
            "/webjars/**",
            "**/favicon.ico",
            "/favicon.ico",
            "/druid/**",
            "/error/**",
            "/assets/**",
            "/auth/logout",
            "/auth/sms-code",
            "/auth/captcha",
            "/auth/code"
    };

    /**
     * 首次加载合并ENDPOINTS
     */
    @PostConstruct
    public void initIgnoreSecurity() {
        Collections.addAll(ignoreUrls, SECURITY_ENDPOINTS);
    }
}
