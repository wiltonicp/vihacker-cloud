package cc.vihackerframework.gateway.filter;

import cc.vihackerframework.core.cloud.properties.ViHackerSecurityProperties;
import cc.vihackerframework.core.constant.Oauth2Constant;
import cc.vihackerframework.core.constant.ViHackerConstant;
import cc.vihackerframework.core.util.ResponseUtil;
import cc.vihackerframework.core.util.SecurityUtil;
import cc.vihackerframework.core.util.StringPool;
import cc.vihackerframework.core.util.TokenUtil;
import io.jsonwebtoken.Claims;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;


/**
 * 全局拦截
 * 网关统一的token验证
 *
 * @author Ranger
 * @email wilton.icp@gmail.com
 * @since 2021/7/29
 */
@Slf4j
@Component
@AllArgsConstructor
public class ViHackerGatewayPreUaaFilter implements GlobalFilter, Ordered {

    private final ViHackerSecurityProperties properties;

    /**
     * 路径前缀以/vihacker开头，如vihacker-uaa
     */
    public static final String PATH_PREFIX = "/vihacker";

    /**
     * 索引自1开头检索，跳过第一个字符就是检索的字符的问题
     */
    public static final int FROM_INDEX = 1;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        //　如果在忽略的url里，则跳过
        String path = replacePrefix(exchange.getRequest().getURI().getPath());
        String requestUrl = exchange.getRequest().getURI().getRawPath();
        if (ignore(path) || ignore(requestUrl)) {
            return chain.filter(exchange);
        }

        // 验证token是否有效
        ServerHttpResponse resp = exchange.getResponse();
        String headerToken = exchange.getRequest().getHeaders().getFirst(Oauth2Constant.HEADER_TOKEN);
        if (headerToken == null) {
            return unauthorized(resp, "没有携带Token信息！");
        }
        String token = TokenUtil.getToken(headerToken);
        Claims claims = SecurityUtil.getClaims(token);
        if (claims == null) {
            return unauthorized(resp, "token已过期或验证不正确！");
        }
        return chain.filter(exchange);
    }


    /**
     * 检查是否忽略url
     * @param path 路径
     * @return boolean
     */
    private boolean ignore(String path) {
        return properties.getIgnoreUrls().stream()
                .map(url -> url.replace("/**", ""))
                .anyMatch(path::startsWith);
    }

    /**
     * 移除模块前缀
     * @param path 路径
     * @return String
     */
    private String replacePrefix(String path) {
        if (path.startsWith(PATH_PREFIX)) {
            return path.substring(path.indexOf(StringPool.SLASH, FROM_INDEX));
        }
        return path;
    }

    private Mono<Void> unauthorized(ServerHttpResponse resp, String msg) {
        return ResponseUtil.webFluxResponseWriter(resp, ViHackerConstant.JSON_UTF8, HttpStatus.UNAUTHORIZED, msg);
    }

    @Override
    public int getOrder() {
        return -1000;
    }
}
