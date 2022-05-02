package cc.vihackerframework.uaa;

import cc.vihackerframework.core.feign.annotation.EnableViHackerFeign;
import cc.vihackerframework.core.security.annotation.EnableViHackerResourceServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 启动器
 * @author Ranger
 * @email wilton.icp@gmail.com
 * @since 2021/6/5
 */
@EnableViHackerFeign
@SpringBootApplication
@EnableViHackerResourceServer
public class ViHackerUaaApplication {

    public static void main(String[] args) {
        SpringApplication.run(ViHackerUaaApplication.class, args);
    }

}
