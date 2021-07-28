package com.vihackerframework.gateway.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * <p>
 * @author Ranger
 * @email wilton.icp@gmail.com
 * @since 2021/6/15
 */
@Getter
@Setter
@SpringBootConfiguration
@ConfigurationProperties(ViHackerGatewayProperties.PREFIX)
public class ViHackerGatewayProperties {

    public final static String PREFIX = "vihacker.gateway";

    /**
     * 禁止外部访问的 URI，多个值的话以逗号分隔
     */
    private String forbidRequestUri;
}
