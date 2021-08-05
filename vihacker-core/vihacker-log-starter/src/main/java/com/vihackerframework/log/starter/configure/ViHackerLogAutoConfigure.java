package com.vihackerframework.log.starter.configure;

import com.vihackerframework.log.starter.aspect.LogEndpointAspect;
import com.vihackerframework.log.starter.event.LogListener;
import com.vihackerframework.log.starter.properties.LogProperties;
import com.vihackerframework.log.starter.properties.LogType;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * <p>
 *
 * @author Ranger
 * @email wilton.icp@gmail.com
 * @since 2021/6/23
 */
@EnableAsync
@Configuration
@RequiredArgsConstructor
@ConditionalOnWebApplication
@EnableConfigurationProperties(value = LogProperties.class)
public class ViHackerLogAutoConfigure {

    private final LogProperties properties;

    @Bean
    public LogListener sysLogListener() {
        if (properties.getLogType().equals(LogType.KAFKA)) {
            return new LogListener();
        }
        return new LogListener();
    }

    @Bean
    public LogEndpointAspect logAspect(ApplicationContext applicationContext){
        return new LogEndpointAspect(applicationContext);
    }
}
