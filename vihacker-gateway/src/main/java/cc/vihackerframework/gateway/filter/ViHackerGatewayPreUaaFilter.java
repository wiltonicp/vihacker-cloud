package cc.vihackerframework.gateway.filter;

import cc.vihackerframework.core.constant.Oauth2Constant;
import cc.vihackerframework.core.constant.ViHackerConstant;
import cc.vihackerframework.core.util.ResponseUtil;
import cc.vihackerframework.core.util.SecurityUtil;
import cc.vihackerframework.core.util.TokenUtil;
import cc.vihackerframework.gateway.properties.ViHackerGatewayProperties;
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

import java.util.regex.Pattern;

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

    private final ViHackerGatewayProperties properties;

    private static final String PATTERN_OAUTH = "^.*/oauth/.*";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        //　如果在忽略的url里，则跳过
        String path = exchange.getRequest().getURI().getPath();
        String requestUrl = exchange.getRequest().getURI().getRawPath();
        if (ignore(path) || ignore(requestUrl) || ignoreAuth(path)) {
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

        // 判断token是否存在于redis,对于只允许一台设备场景适用。
        // 如只允许一台设备登录，需要在登录成功后，查询key是否存在，如存在，则删除此key，提供思路。
//        boolean hasKey = redisService.hasKey("auth:" + token);
//        log.debug("查询token是否存在: " + hasKey);
//        if (!hasKey) {
//            return unauthorized(resp, "登录超时，请重新登录");
//        }
        return chain.filter(exchange);
    }


    /**
     * 检查是否忽略url
     * @param path 路径
     * @return boolean
     */
    private boolean ignore(String path) {
        return properties.getForbidRequestUri().stream()
                .map(url -> url.replace("/**", ""))
                .anyMatch(path::startsWith);
    }

    private boolean ignoreAuth(String path) {
        if(Pattern.matches(PATTERN_OAUTH, path)){
            return true;
        }
        return false;
    }


    private Mono<Void> unauthorized(ServerHttpResponse resp, String msg) {
        return ResponseUtil.webFluxResponseWriter(resp, ViHackerConstant.JSON_UTF8, HttpStatus.UNAUTHORIZED, msg);
    }

    @Override
    public int getOrder() {
        return -1000;
    }
}
