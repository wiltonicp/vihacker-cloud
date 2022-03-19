package cc.vihackerframework.uaa.configure;

import cc.vihackerframework.core.auth.entity.AdminAuthUser;
import cc.vihackerframework.core.constant.Oauth2Constant;
import cc.vihackerframework.uaa.exception.ViHackerAuthWebResponseExceptionTranslator;
import cc.vihackerframework.uaa.properties.ViHackerAuthProperties;
import cc.vihackerframework.uaa.service.RedisAuthenticationCodeService;
import cc.vihackerframework.uaa.service.RedisClientDetailsService;
import lombok.RequiredArgsConstructor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2RequestFactory;
import org.springframework.security.oauth2.provider.password.ResourceOwnerPasswordTokenGranter;
import org.springframework.security.oauth2.provider.request.DefaultOAuth2RequestFactory;
import org.springframework.security.oauth2.provider.token.*;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

import java.util.*;

/**
 * <p>
 * 认证服务器
 * @author Ranger
 * @email wilton.icp@gmail.com
 * @since 2021/6/5
 */
@Order(2)
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
        DefaultTokenServices tokenServices = defaultTokenServices();
        // token增强链
        TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
        // 把jwt增强，与额外信息增强加入到增强链
        tokenEnhancerChain.setTokenEnhancers(Arrays.asList(tokenEnhancer(), jwtAccessTokenConverter()));
        tokenServices.setTokenEnhancer(tokenEnhancerChain);

        endpoints.tokenStore(tokenStore())
                .userDetailsService(userDetailService)
                .authorizationCodeServices(redisAuthenticationCodeService)
                .authenticationManager(authenticationManager)
                .exceptionTranslator(exceptionTranslator)
                .accessTokenConverter(jwtAccessTokenConverter())
                .tokenEnhancer(tokenEnhancerChain);

    }

    @Bean
    public TokenStore tokenStore() {
        return new JwtTokenStore(jwtAccessTokenConverter());
    }

    @Bean
    @Primary
    public DefaultTokenServices defaultTokenServices() {
        DefaultTokenServices tokenServices = new DefaultTokenServices();

        tokenServices.setTokenStore(tokenStore());
        tokenServices.setSupportRefreshToken(true);
        tokenServices.setClientDetailsService(redisClientDetailsService);
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

    /**
     * jwt token增强，添加额外信息
     *
     * @return TokenEnhancer
     */
    @Bean
    public TokenEnhancer tokenEnhancer() {
        return new TokenEnhancer() {
            @Override
            public OAuth2AccessToken enhance(OAuth2AccessToken oAuth2AccessToken, OAuth2Authentication oAuth2Authentication) {

                // 添加额外信息的map
                final Map<String, Object> additionMessage = new HashMap<>(2);
                // 对于客户端鉴权模式，直接返回token
                if (oAuth2Authentication.getUserAuthentication() == null) {
                    return oAuth2AccessToken;
                }
                // 获取当前登录的用户
                AdminAuthUser user = (AdminAuthUser) oAuth2Authentication.getUserAuthentication().getPrincipal();

                // 如果用户不为空 则把id放入jwt token中
                if (user != null) {
                    additionMessage.put(Oauth2Constant.VIHACKER_USER_ID, String.valueOf(user.getUserId()));
                    additionMessage.put(Oauth2Constant.VIHACKER_USER_NAME, user.getUsername());
                    additionMessage.put(Oauth2Constant.VIHACKER_AVATAR, user.getAvatar());
                    additionMessage.put(Oauth2Constant.VIHACKER_ROLE_ID, String.valueOf(user.getRoleId()));
                    additionMessage.put(Oauth2Constant.VIHACKER_TYPE, user.getType());
                    additionMessage.put(Oauth2Constant.VIHACKER_TENANT_ID, user.getTenantId());
                }
                ((DefaultOAuth2AccessToken) oAuth2AccessToken).setAdditionalInformation(additionMessage);
                return oAuth2AccessToken;
            }
        };
    }
}
