package cc.vihackerframework.gateway.filter;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;


import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.GATEWAY_REQUEST_URL_ATTR;
import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.addOriginalRequestUrl;

/**
 * 重写url解决多斜线问题
 * Created By Ranger on 2022/5/31.
 */
@Configuration
public class RewriteDoubleSlashPathWebFilter implements WebFilter {

    private static final String DOUBLE_SLASH = "//";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        ServerHttpRequest req = exchange.getRequest();
        String path = req.getURI().getRawPath();

        if (!path.contains(DOUBLE_SLASH)) {
            return chain.filter(exchange);
        }

        addOriginalRequestUrl(exchange, req.getURI());
        String newPath = path.replaceAll("[/]{2,}", "/");
        ServerHttpRequest request = req.mutate().path(newPath).build();
        exchange.getAttributes().put(GATEWAY_REQUEST_URL_ATTR, request.getURI());
        return chain.filter(exchange.mutate().request(request).build());
    }
}
