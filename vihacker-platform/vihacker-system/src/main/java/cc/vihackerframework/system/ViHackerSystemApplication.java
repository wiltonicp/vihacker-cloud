package cc.vihackerframework.system;

import cc.vihackerframework.resource.starter.annotation.EnableViHackerResourceServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 系统服务启动器
 *
 * @author Ranger
 * @email wilton.icp@gmail.com
 * @since 2021/6/17
 */
@EnableViHackerResourceServer
@SpringBootApplication
public class ViHackerSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(ViHackerSystemApplication.class,args);
    }
}
