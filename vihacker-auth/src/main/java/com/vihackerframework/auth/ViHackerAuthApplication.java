package com.vihackerframework.auth;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 启动器
 * @author Ranger
 * @since 2021/6/5
 * @email wilton.icp@gmail.com
 */
@SpringBootApplication
@MapperScan("com.vihackerframework.auth.mapper")
public class ViHackerAuthApplication {

    public static void main(String[] args) {
        SpringApplication.run(ViHackerAuthApplication.class, args);
    }

}
