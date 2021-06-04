package com.vihackerframework.redis.starter.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * <p>
 *
 * @author Ranger
 * @email wilton.icp@gmail.com
 * @since 2021/6/4
 */
@ConfigurationProperties(prefix = "vihacker.redis")
public class ViHackerLettuceRedisProperties {

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
