package cc.vihackerframework.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * <p>
 * @author Ranger
 * @email wilton.icp@gmail.com
 * @since 2021/6/8
 */
@EnableDiscoveryClient
@SpringBootApplication
public class ViHackerGatewayApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(ViHackerGatewayApplication.class)
                .web(WebApplicationType.REACTIVE)
                .run(args);
    }
}
