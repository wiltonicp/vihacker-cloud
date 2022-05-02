package cc.vihackerframework.system.demo;

import cc.vihackerframework.core.feign.annotation.EnableViHackerFeign;
import cc.vihackerframework.core.security.annotation.EnableViHackerResourceServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 启动器
 *
 * Created by Ranger on 2022/2/22
 */
@EnableViHackerFeign
//@EnableFeignClients(basePackages ={"cc.vihackerframework.*"})
@SpringBootApplication
@EnableViHackerResourceServer
public class ViHackerSystemDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(ViHackerSystemDemoApplication.class);
    }
}
