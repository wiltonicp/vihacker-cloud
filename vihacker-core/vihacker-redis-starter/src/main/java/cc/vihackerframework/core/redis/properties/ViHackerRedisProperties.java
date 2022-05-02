package cc.vihackerframework.core.redis.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * <p>
 *
 * @author Ranger
 * @email wilton.icp@gmail.com
 * @since 2021/6/4
 */
@ConfigurationProperties(prefix = ViHackerRedisProperties.PREFIX)
public class ViHackerRedisProperties {

    /**
     * 前缀
     */
    public static final String PREFIX = "vihacker.redis";

    /**
     * 是否开启Lettuce Redis
     */
    private Boolean enable = true;

    public Boolean getEnable() {
        return enable;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
    }

    @Override
    public String toString() {
        return "ViHackerLettuceRedisProperties{" +
                "enable=" + enable +
                '}';
    }
}
