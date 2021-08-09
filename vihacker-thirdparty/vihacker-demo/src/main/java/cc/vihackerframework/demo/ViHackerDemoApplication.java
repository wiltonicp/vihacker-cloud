package cc.vihackerframework.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * <p>
 *
 * @author Ranger
 * @email wilton.icp@gmail.com
 * @since 2021/6/21
 */
@EnableScheduling
@SpringBootApplication
public class ViHackerDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(ViHackerDemoApplication.class,args);
        System.out.println();
    }
}
