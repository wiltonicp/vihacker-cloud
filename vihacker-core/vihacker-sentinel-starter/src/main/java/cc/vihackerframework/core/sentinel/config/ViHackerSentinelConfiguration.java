package cc.vihackerframework.core.sentinel.config;

import com.alibaba.cloud.sentinel.feign.SentinelFeignAutoConfiguration;
import com.alibaba.csp.sentinel.adapter.spring.webflux.callback.BlockRequestHandler;
import com.alibaba.csp.sentinel.adapter.spring.webmvc.callback.BlockExceptionHandler;
import com.alibaba.fastjson.JSONObject;
import cc.vihackerframework.core.api.ViHackerResult;
import cc.vihackerframework.core.sentinel.feign.ViHackerFeignSentinel;
import feign.Feign;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerResponse;

import javax.servlet.http.HttpServletRequest;

/**
 * Sentinel配置
 *
 * @author Ranger
 * @email wilton.icp@gmail.com
 * @since 2021/6/17
 */
@Configuration(proxyBeanMethods = false)
@AutoConfigureBefore(SentinelFeignAutoConfiguration.class)
public class ViHackerSentinelConfiguration {

    @Bean
    @Scope("prototype")
    @ConditionalOnMissingBean
    @ConditionalOnProperty(name = "vihacker.feign.sentinel.enabled")
    public Feign.Builder feignSentinelBuilder() {
        return ViHackerFeignSentinel.builder();
    }
    /**
     * 限流、熔断统一处理类
     */
    @Configuration
    @ConditionalOnClass(HttpServletRequest.class)
    public static class WebmvcHandler {
        @Bean
        public BlockExceptionHandler webmvcBlockExceptionHandler() {
            return (request, response, e) -> {
                response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
                ViHackerResult<?> result = ViHackerResult.failed("Too many request, please retry later.");
                response.getWriter().print(JSONObject.toJSONString(result));
            };
        }

    }

    /**
     * 限流、熔断统一处理类
     */
    @Configuration
    @ConditionalOnClass(ServerResponse.class)
    public static class WebfluxHandler {
        @Bean
        public BlockRequestHandler webfluxBlockExceptionHandler() {
            return (exchange, t) ->
                    ServerResponse.status(HttpStatus.TOO_MANY_REQUESTS)
                            .contentType(MediaType.APPLICATION_JSON)
                            .body(BodyInserters.fromValue(ViHackerResult.failed(t.getMessage())));
        }
    }
}
