package cc.vihackerframework.core.auth.configure;

import cc.vihackerframework.core.auth.aspect.PreAuthAspect;
import cc.vihackerframework.core.auth.properties.ViHackerUaaProperties;
import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Ranger on 2022/6/12.
 */
@Configuration
@AllArgsConstructor
@EnableConfigurationProperties({ViHackerUaaProperties.class})
public class ViHackerAuthAutoConfigure {

    @Bean
    @ConditionalOnProperty(prefix = ViHackerUaaProperties.PREFIX, name = "enable", havingValue = "true", matchIfMissing = true)
    public PreAuthAspect uriSecurityPreAuthAspect(HttpServletRequest request) {
        return new PreAuthAspect(request);
    }
}
