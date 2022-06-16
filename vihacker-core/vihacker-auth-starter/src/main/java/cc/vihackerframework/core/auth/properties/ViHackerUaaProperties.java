package cc.vihackerframework.core.auth.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * <p>
 *
 * @author Ranger
 * @email wilton.icp@gmail.com
 * @since 2021/8/11
 */
@Data
@RefreshScope
@ConfigurationProperties(ViHackerUaaProperties.PREFIX)
public class ViHackerUaaProperties {

    /**
     * 前缀
     */
    public static final String PREFIX = "vihacker.uaa";

    /**
     * 是否启用token 验证
     */
    private Boolean enable = false;
}
