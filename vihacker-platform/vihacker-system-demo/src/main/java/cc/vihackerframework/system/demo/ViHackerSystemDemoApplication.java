package cc.vihackerframework.system.demo;

import cc.vihackerframework.feign.starter.annotation.EnableViHackerFeign;
import cc.vihackerframework.resource.starter.annotation.EnableViHackerResourceServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

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
