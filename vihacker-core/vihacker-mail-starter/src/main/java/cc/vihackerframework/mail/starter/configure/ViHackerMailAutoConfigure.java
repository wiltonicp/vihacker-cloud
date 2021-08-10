package cc.vihackerframework.mail.starter.configure;

import cc.vihackerframework.mail.starter.core.IMailTemplate;
import cc.vihackerframework.mail.starter.core.VihackerMailTemplate;
import cc.vihackerframework.mail.starter.properties.VihackerMailProperties;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.mail.MailSenderAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;

import javax.annotation.Resource;

/**
 * 邮件配置
 *
 * @author Ranger
 * @email wilton.icp@gmail.com
 * @since 2021/8/10
 */
@Configuration
@AutoConfigureAfter(MailSenderAutoConfiguration.class)
public class ViHackerMailAutoConfigure {

    @Resource
    private JavaMailSender mailSender;

    @Resource
    private VihackerMailProperties mailProperties;

    @Bean
    @ConditionalOnBean({VihackerMailProperties.class, JavaMailSender.class})
    public IMailTemplate mailTemplate() {
        return new VihackerMailTemplate(mailSender,mailProperties);
    }
}
