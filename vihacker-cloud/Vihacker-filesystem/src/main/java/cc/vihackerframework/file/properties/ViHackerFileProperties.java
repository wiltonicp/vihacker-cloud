package cc.vihackerframework.file.properties;

import lombok.Data;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.io.File;

/**
 * @author jiangshanchen
 * @title: ViHackerFilePropertied
 * @projectName vihacker-cloud-master
 * @description: TODO
 * @date 2022/2/28下午4:29
 */

@Data
@SpringBootConfiguration
@ConfigurationProperties(prefix = "file.upload")
public class ViHackerFileProperties {



    public String path;


    public String getUserPath(){
        return this.path + File.separator ;
        //return this.path + File.separator + SecurityUtil.getLoginUser().getSpaceCode()  + File.separator;
    }


}