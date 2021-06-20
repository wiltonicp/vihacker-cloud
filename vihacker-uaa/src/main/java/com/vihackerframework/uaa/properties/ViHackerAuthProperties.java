package com.vihackerframework.uaa.properties;

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
@ConfigurationProperties(prefix = "vihacker.uaa")
public class ViHackerAuthProperties {

    /**
     * 开关：同应用账号互踢
     */
    private boolean isSingleLogin = false;

    /**
     * JWT加签密钥
     */
    private String jwtAccessKey;

    /**
     * 社交登录所使用的 Client
     */
    private String socialLoginClientId;

    /**
     * token有效期自定义设置，默认12小时
     */
    private Integer accessTokenValiditySeconds = 60 * 60 * 12;

    /**
     * 刷新token 有效期默认30天
     */
    private Integer refreshTokenValiditySeconds = 60 * 60 * 24 * 7;

}