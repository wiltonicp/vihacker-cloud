package cc.vihackerframework.system;

import cc.vihackerframework.feign.starter.annotation.EnableViHackerFeign;
import cc.vihackerframework.resource.starter.annotation.EnableViHackerResourceServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


/**
 * @author jiangshanchen
 * @title: ZnjxSystemApplication
 * @projectName vihacker-cloud-master
 * @description: TODO
 * @date 2022/3/1下午5:31
 */

@EnableViHackerFeign
@SpringBootApplication
@EnableViHackerResourceServer
public class ZnjxSystemApplication {


    public static void main(String[] args) {
        SpringApplication.run(ZnjxSystemApplication.class,args);
    }
}
