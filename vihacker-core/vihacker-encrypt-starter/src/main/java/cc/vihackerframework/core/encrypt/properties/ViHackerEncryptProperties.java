package cc.vihackerframework.core.encrypt.properties;


import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created by Ranger on 2021/9/14
 */
@Getter
@Setter
@ConfigurationProperties(ViHackerEncryptProperties.PREFIX)
public class ViHackerEncryptProperties {

    public static final String PREFIX = "vihacker.encrypt";

    /**
     * 解密密钥
     */
    public String key;
}
