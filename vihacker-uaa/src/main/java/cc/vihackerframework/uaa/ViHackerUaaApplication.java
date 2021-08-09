package cc.vihackerframework.uaa;

import cc.vihackerframework.resource.starter.annotation.EnableViHackerResourceServer;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 启动器
 * @author Ranger
 * @email wilton.icp@gmail.com
 * @since 2021/6/5
 */
@SpringBootApplication
@EnableViHackerResourceServer
@MapperScan("cc.vihackerframework.uaa.mapper")
public class ViHackerUaaApplication {

    public static void main(String[] args) {
        SpringApplication.run(ViHackerUaaApplication.class, args);
    }

}
