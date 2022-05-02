package cc.vihackerframework.core.mail.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 邮件配置文件
 *
 * @author Ranger
 * @email wilton.icp@gmail.com
 * @since 2021/8/10
 */
@Getter
@Setter
@ConfigurationProperties(VihackerMailProperties.PREFIX)
public class VihackerMailProperties extends MailProperties {

    /**
     * 前缀
     */
    public static final String PREFIX = "vihacker.mail";

    private String from;

    private String fromName;

}
