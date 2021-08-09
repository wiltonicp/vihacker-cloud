package cc.vihackerframework.gateway.controller;

import cc.vihackerframework.core.api.ViHackerResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * <p>
 * @author Ranger
 * @email wilton.icp@gmail.com
 * @since 2021/6/15
 */
@Slf4j
@RestController
public class FallbackController {

    @RequestMapping("fallback/{name}")
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Mono<ViHackerResult> systemFallback(@PathVariable String name) {
        String response = "服务访问超时，请稍后再试";
        log.error("{}，目标微服务：{}", response, name);
        return Mono.just(ViHackerResult.failed(response));
    }
}
