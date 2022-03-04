package cc.vihackerframework.file;

import cc.vihackerframework.resource.starter.annotation.EnableViHackerResourceServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.MultipartAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author jiangshanchen
 * @title: ZnjxFileApplication
 * @projectName vihacker-cloud-master
 * @description: TODO
 * @date 2022/2/28下午4:48
 */

@SpringBootApplication
@EnableViHackerResourceServer
@EnableAutoConfiguration(exclude = { MultipartAutoConfiguration.class })
@EnableFeignClients
public class VihackerFileApplication extends SpringBootServletInitializer {




    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(VihackerFileApplication.class);
    }

    public static void main(String []args)  {

        SpringApplication.run(VihackerFileApplication.class);

    }
}
