package com.vihackerframework.auth.properties;

import lombok.Data;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * <p>
 *
 * @author Ranger
 * @email wilton.icp@gmail.com
 * @since 2021/6/5
 */
@Data
@SpringBootConfiguration
@ConfigurationProperties(prefix = "vihacker.auth")
public class ViHackerAuthProperties {

    /**
     * JWT加签密钥
     */
    private String jwtAccessKey;

    /**
     * 社交登录所使用的 Client
     */
    private String socialLoginClientId;
}
