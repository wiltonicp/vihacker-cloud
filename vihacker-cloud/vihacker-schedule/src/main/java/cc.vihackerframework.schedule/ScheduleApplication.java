package cc.vihackerframework.schedule;

import cc.vihackerframework.feign.starter.annotation.EnableViHackerFeign;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;





@SpringBootApplication
//@MapperScan("com.vihackerframework.schedule.mapper")
@EnableScheduling  //开启调度任务
@EnableViHackerFeign
public class ScheduleApplication {


    public static void main(String[] args) {
        SpringApplication.run(ScheduleApplication.class,args);
    }


}
