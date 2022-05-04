package cc.vihackerframework.core.security.configure;

import cc.vihackerframework.core.security.handler.ViHackerAuthExceptionEntryPoint;
import cc.vihackerframework.core.security.handler.ViHackerAccessDeniedHandler;
import cc.vihackerframework.core.security.properties.ViHackerSecurityProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Primary;
import org.springframework.core.annotation.Order;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.expression.OAuth2MethodSecurityExpressionHandler;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;

/**
 * <p>
 * @author Ranger
 * @email wilton.icp@gmail.com
 * @since 2021/6/15
 */
@Order(6)
@ComponentScan("cc.vihackerframework.core.auth.*")
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableConfigurationProperties(ViHackerSecurityProperties.class)
@ConditionalOnProperty(value = "vihacker.security.enable", havingValue = "true", matchIfMissing = true)
public class ViHackerCloudSecurityAutoConfigure extends GlobalMethodSecurityConfiguration {

    @Bean
    @ConditionalOnMissingBean(name = "accessDeniedHandler")
    public ViHackerAccessDeniedHandler accessDeniedHandler() {
        return new ViHackerAccessDeniedHandler();
    }

    @Bean
    @ConditionalOnMissingBean(name = "authenticationEntryPoint")
    public ViHackerAuthExceptionEntryPoint authenticationEntryPoint() {
        return new ViHackerAuthExceptionEntryPoint();
    }

    @Bean
    @ConditionalOnMissingBean(value = PasswordEncoder.class)
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public ViHackerCloudSecurityInteceptorConfigure cloudSecurityInteceptorConfigure() {
        return new ViHackerCloudSecurityInteceptorConfigure();
    }

    @Bean
    @Primary
    @ConditionalOnMissingBean(DefaultTokenServices.class)
    public ViHackerUserInfoTokenServices viHackerUserInfoTokenServices(ResourceServerProperties properties) {
        return new ViHackerUserInfoTokenServices(properties.getUserInfoUri(), properties.getClientId());
    }

    @Override
    protected MethodSecurityExpressionHandler createExpressionHandler() {
        return new OAuth2MethodSecurityExpressionHandler();
    }
}
