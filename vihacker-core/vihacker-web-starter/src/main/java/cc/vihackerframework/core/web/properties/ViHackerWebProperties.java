package cc.vihackerframework.core.web.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created by Ranger on 2022/5/4.
 */
@Data
@ConfigurationProperties(prefix = "vihacker.web")
public class ViHackerWebProperties {

    /**
     * 是否只能通过网关获取资源
     */
    private Boolean onlyFetchByGateway = Boolean.TRUE;
}
