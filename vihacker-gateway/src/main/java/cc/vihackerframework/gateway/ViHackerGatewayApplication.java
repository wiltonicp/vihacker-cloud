package cc.vihackerframework.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
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
        SpringApplication.run(ViHackerGatewayApplication.class, args);
    }
}
