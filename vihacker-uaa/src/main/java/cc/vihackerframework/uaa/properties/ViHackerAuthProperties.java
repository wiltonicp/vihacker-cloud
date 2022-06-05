package cc.vihackerframework.uaa.properties;

import cc.vihackerframework.core.constant.Oauth2Constant;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * <p>
 *
 * @author Ranger
 * @email wilton.icp@gmail.com
 * @since 2021/6/5
 */
@Getter
@Setter
@Component
@ConfigurationProperties(ViHackerAuthProperties.PREFIX)
public class ViHackerAuthProperties {

    public static final String PREFIX = "vihacker.uaa";

    /**
     * 开关：同应用账号互踢
     */

    private boolean isSingleLogin = Boolean.FALSE;

    /**
     * JWT加签密钥
     */
    private String jwtAccessKey = Oauth2Constant.SIGN_KEY;

    /**
     * JWT加签密钥 过期时间
     */
    private String jwtExpiration = "3600";

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
