package cc.vihackerframework.file.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

/**
 * @author jiangshanchen
 * @title: UploadConfig
 * @projectName vihacker-cloud-master
 * @description: TODO
 * @date 2022/3/3下午2:50
 */

@Configuration
public class UploadConfig {





        @Bean(name="multipartResolver")
        public MultipartResolver multipartResolver(){
            return new CommonsMultipartResolver();
        }




}

