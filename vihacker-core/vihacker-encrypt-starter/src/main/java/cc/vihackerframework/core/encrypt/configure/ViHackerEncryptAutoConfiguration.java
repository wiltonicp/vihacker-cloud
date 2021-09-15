package cc.vihackerframework.core.encrypt.configure;

import cc.vihackerframework.core.encrypt.properties.ViHackerEncryptProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Created by Ranger on 2021/9/15
 */
@Configuration
@RequiredArgsConstructor
@ComponentScan("cc.vihackerframework.core.encrypt")
@EnableConfigurationProperties(ViHackerEncryptProperties.class)
public class ViHackerEncryptAutoConfiguration {
}
