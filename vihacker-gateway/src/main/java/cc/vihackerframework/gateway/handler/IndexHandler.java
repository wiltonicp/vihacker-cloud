package cc.vihackerframework.gateway.handler;

import cc.vihackerframework.core.entity.enums.EnvType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * 网关默认首页
 *
 * @author Ranger
 * @since 2021/7/29
 * @email wilton.icp@gmail.com
 */
@RestController
public class IndexHandler {

	@Value("${spring.profiles.active}")
	private String env;

	@GetMapping("/")
	public Mono<String> index() {
		return Mono.just(desc());
	}

	private String desc() {
		StringBuilder sb = new StringBuilder(100);
		sb.append("<div style='color: blue;text-align: center;'>ViHackerCloud gateway has been started!</div>");
		if (!EnvType.PROD.getValue().equals(env)) {
			sb.append("<br/>");
			sb.append("<div style='text-align: center;'><ul style='list-style: none;'><li>文档地址：<a href='doc.html'>doc.html</a></li></ul></div>");
		}
		return sb.toString();
	}
}
