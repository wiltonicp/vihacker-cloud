package cc.vihackerframework.system;

import cc.vihackerframework.core.feign.annotation.EnableViHackerFeign;
import cc.vihackerframework.core.security.annotation.EnableViHackerResourceServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 系统服务启动器
 *
 * @author Ranger
 * @email wilton.icp@gmail.com
 * @since 2021/6/17
 */
@EnableViHackerFeign
@SpringBootApplication
@EnableViHackerResourceServer
public class ViHackerSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(ViHackerSystemApplication.class,args);
    }
}
