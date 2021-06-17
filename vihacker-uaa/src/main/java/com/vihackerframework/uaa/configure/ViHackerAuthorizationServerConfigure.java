package com.vihackerframework.uaa.configure;

import com.vihackerframework.uaa.exception.ViHackerAuthWebResponseExceptionTranslator;
import com.vihackerframework.uaa.properties.ViHackerAuthProperties;
import com.vihackerframework.uaa.service.RedisAuthenticationCodeService;
import com.vihackerframework.uaa.service.RedisClientDetailsService;
import com.vihackerframework.uaa.service.impl.SingleLoginTokenServices;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.OAuth2RequestFactory;
import org.springframework.security.oauth2.provider.password.ResourceOwnerPasswordTokenGranter;
import org.springframework.security.oauth2.provider.request.DefaultOAuth2RequestFactory;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.DefaultUserAuthenticationConverter;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

/**
 * <p>
 * 认证服务器
 * @author Ranger
 * @email wilton.icp@gmail.com
 * @since 2021/6/5
 */
@Configuration
@RequiredArgsConstructor
@EnableAuthorizationServer
public class ViHackerAuthorizationServerConfigure extends AuthorizationServerConfigurerAdapter {

    private final ViHackerAuthProperties properties;
    private final UserDetailsService userDetailService;
    private final AuthenticationManager authenticationManager;
    private final RedisClientDetailsService redisClientDetailsService;
    private final RedisAuthenticationCodeService redisAuthenticationCodeService;
    private final ViHackerAuthWebResponseExceptionTranslator exceptionTranslator;


    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.withClientDetails(redisClientDetailsService);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
        endpoints.tokenStore(tokenStore())
                .userDetailsService(userDetailService)
                .authorizationCodeServices(redisAuthenticationCodeService)
                .authenticationManager(authenticationManager)
                .exceptionTranslator(exceptionTranslator)
                .accessTokenConverter(jwtAccessTokenConverter());
    }

    /**
     * 配置token状态查询
     *
     * @param security
     * @throws Exception
     */
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        //开启支持通过表单方式提交client_id和client_secret,否则请求时以basic auth方式,头信息传递Authorization发送请求
        security.allowFormAuthenticationForClients();
        security.checkTokenAccess("isAuthenticated()");
        security.tokenKeyAccess("isAuthenticated()");
    }

    @Bean
    public TokenStore tokenStore() {
        return new JwtTokenStore(jwtAccessTokenConverter());
    }

    @Bean
    @Primary
    public DefaultTokenServices defaultTokenServices() {
        DefaultTokenServices tokenServices = new SingleLoginTokenServices(properties.isSingleLogin());

        tokenServices.setTokenStore(tokenStore());
        tokenServices.setSupportRefreshToken(true);
        tokenServices.setClientDetailsService(redisClientDetailsService);
        tokenServices.setAccessTokenValiditySeconds(properties.getAccessTokenValiditySeconds());
        tokenServices.setRefreshTokenValiditySeconds(properties.getRefreshTokenValiditySeconds());
        return tokenServices;
    }

    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter() {
        JwtAccessTokenConverter accessTokenConverter = new JwtAccessTokenConverter();
        DefaultAccessTokenConverter defaultAccessTokenConverter = (DefaultAccessTokenConverter) accessTokenConverter.getAccessTokenConverter();
        DefaultUserAuthenticationConverter userAuthenticationConverter = new DefaultUserAuthenticationConverter();
        userAuthenticationConverter.setUserDetailsService(userDetailService);
        defaultAccessTokenConverter.setUserTokenConverter(userAuthenticationConverter);
        accessTokenConverter.setSigningKey(properties.getJwtAccessKey());
        return accessTokenConverter;
    }

    @Bean
    public ResourceOwnerPasswordTokenGranter resourceOwnerPasswordTokenGranter(AuthenticationManager authenticationManager, OAuth2RequestFactory oAuth2RequestFactory) {
        DefaultTokenServices defaultTokenServices = defaultTokenServices();
        defaultTokenServices.setTokenEnhancer(jwtAccessTokenConverter());
        return new ResourceOwnerPasswordTokenGranter(authenticationManager, defaultTokenServices, redisClientDetailsService, oAuth2RequestFactory);
    }

    @Bean
    public DefaultOAuth2RequestFactory oAuth2RequestFactory() {
        return new DefaultOAuth2RequestFactory(redisClientDetailsService);
    }
}
