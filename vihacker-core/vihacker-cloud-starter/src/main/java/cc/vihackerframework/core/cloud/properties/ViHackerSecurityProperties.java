package cc.vihackerframework.core.cloud.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * <p>
 *
 * @author Ranger
 * @email wilton.icp@gmail.com
 * @since 2021/8/11
 */
@Data
@RefreshScope
@ConfigurationProperties(ViHackerSecurityProperties.PREFIX)
public class ViHackerSecurityProperties {

    /**
     * 前缀
     */
    public static final String PREFIX = "vihacker.security";

    /**
     * 是否启用网关鉴权模式
     */
    private Boolean enable = true;

    /**
     * 免认证资源路径，忽略URL，List列表形式
     */
    private List<String> ignoreUrls = new ArrayList<>();

    /**
     * 监控中心和swagger需要访问的url
     */
    private static final String[] ENDPOINTS = {
            "/oauth/**",
            "/actuator/**",
            "/v2/api-docs/**",
            "/swagger/api-docs",
            "/swagger-ui.html",
            "/doc.html",
            "/swagger-resources/**",
            "/robots.txt",
            "/manifest.json",
            "/index.html",
            "/webjars/**",
            "**/favicon.ico",
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
    public void initIgnoreUrl() {
        Collections.addAll(ignoreUrls, ENDPOINTS);
    }

}
