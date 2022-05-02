package cc.vihackerframework.core.log.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * <p>
 *
 * @author Ranger
 * @email wilton.icp@gmail.com
 * @since 2021/6/23
 */
@Getter
@Setter
@ConfigurationProperties(LogProperties.PREFIX)
public class LogProperties {

    /**
     * 前缀
     */
    public static final String PREFIX = "vihacker.log";

    /**
     * 是否启用
     */
    private Boolean enable = false;

    /**
     * 默认记录日志类型
     */
    private LogType logType = LogType.DB;
}
