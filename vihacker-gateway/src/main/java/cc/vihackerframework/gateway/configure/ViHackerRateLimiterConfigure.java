package cc.vihackerframework.gateway.configure;

import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Mono;

/**
 * 路由限流配置
 * @author Ranger
 * @email wilton.icp@gmail.com
 * @since 2021/6/15
 */
@Configuration(proxyBeanMethods = false)
public class ViHackerRateLimiterConfigure {

    @Bean(value = "remoteAddrKeyResolver")
    public KeyResolver remoteAddrKeyResolver() {
        return exchange -> Mono.just(exchange.getRequest().getRemoteAddress().getAddress().getHostAddress());
    }
}
