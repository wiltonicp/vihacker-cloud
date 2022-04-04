package cc.vihackerframework.core.sharding;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Created by Ranger on 2021/9/23
 */
@SpringBootApplication
public class ShardingServer {

    public static void main(String[] args) {
        SpringApplication.run(ShardingServer.class,args);
    }
}
