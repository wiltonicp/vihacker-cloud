package cc.vihackerframework.gateway.configure;

import cc.vihackerframework.core.cloud.properties.ViHackerSecurityProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * <p>
 *
 * @author Ranger
 * @email wilton.icp@gmail.com
 * @since 2021/8/11
 */
@Configuration
@EnableConfigurationProperties(ViHackerSecurityProperties.class)
public class ViHackerRequestConfigure {
}
