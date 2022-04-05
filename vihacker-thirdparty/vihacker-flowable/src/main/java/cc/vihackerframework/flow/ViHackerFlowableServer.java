package cc.vihackerframework.flow;

import cc.vihackerframework.feign.starter.annotation.EnableViHackerFeign;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Created By Ranger on 2022/3/30
 */
@EnableViHackerFeign
@SpringBootApplication
public class ViHackerFlowableServer {

    public static void main(String[] args) {
        SpringApplication.run(ViHackerFlowableServer.class);
    }
}
