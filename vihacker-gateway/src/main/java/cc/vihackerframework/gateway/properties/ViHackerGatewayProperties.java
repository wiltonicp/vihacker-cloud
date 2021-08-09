package cc.vihackerframework.gateway.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

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
     * 忽略URL，List列表形式
     */
    private List<String> forbidRequestUri = new ArrayList<>();
}
