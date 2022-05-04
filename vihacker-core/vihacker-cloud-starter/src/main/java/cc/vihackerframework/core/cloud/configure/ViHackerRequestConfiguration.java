package cc.vihackerframework.core.cloud.configure;

import cc.vihackerframework.core.cloud.filter.TenantContextHolderFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by Ranger on 2022/5/4.
 */
@Configuration
public class ViHackerRequestConfiguration {

    @Bean
    public TenantContextHolderFilter tenantContextHolderFilter() {
        return new TenantContextHolderFilter();
    }
}
