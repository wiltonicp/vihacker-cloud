package cc.vihackerframework.uaa.configure;

import cc.vihackerframework.core.auth.entity.AdminAuthUser;
import cc.vihackerframework.core.constant.Oauth2Constant;
import cc.vihackerframework.core.redis.service.RedisService;
import cc.vihackerframework.uaa.exception.ViHackerAuthWebResponseExceptionTranslator;
import cc.vihackerframework.uaa.granter.SmsTokenGranter;
import cc.vihackerframework.uaa.properties.ViHackerAuthProperties;
import cc.vihackerframework.uaa.service.RedisAuthenticationCodeService;
import cc.vihackerframework.uaa.service.RedisClientDetailsService;
import cc.vihackerframework.uaa.service.ViHackerUserDetailsService;
import cc.vihackerframework.uaa.service.impl.SingleLoginTokenServices;
import lombok.RequiredArgsConstructor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.core.userdetails.UserDetailsByNameServiceWrapper;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.*;
import org.springframework.security.oauth2.provider.password.ResourceOwnerPasswordTokenGranter;
import org.springframework.security.oauth2.provider.request.DefaultOAuth2RequestFactory;
import org.springframework.security.oauth2.provider.token.*;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationProvider;

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

    private final RedisService redisService;
    private final ViHackerAuthProperties properties;
    private final ViHackerUserDetailsService userDetailService;
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
        // 配置tokenServices参数
        addUserDetailsService(tokenServices);

        List<TokenGranter> tokenGranters = getTokenGranters(endpoints.getTokenServices(), endpoints.getClientDetailsService(), endpoints.getOAuth2RequestFactory());
        // 增加原有的验证认证方式
        tokenGranters.add(endpoints.getTokenGranter());

        endpoints.tokenStore(tokenStore())
                .tokenGranter(new CompositeTokenGranter(tokenGranters))
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
        DefaultTokenServices tokenServices = new SingleLoginTokenServices(properties.isSingleLogin());

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

    private void addUserDetailsService(DefaultTokenServices tokenServices) {
        if (userDetailService != null) {
            PreAuthenticatedAuthenticationProvider provider = new PreAuthenticatedAuthenticationProvider();
            provider.setPreAuthenticatedUserDetailsService(new UserDetailsByNameServiceWrapper<>(userDetailService));
            tokenServices.setAuthenticationManager(new ProviderManager(Collections.singletonList(provider)));
        }
    }

    /**
     * 自定义TokenGranter集合
     */
    private List<TokenGranter> getTokenGranters(AuthorizationServerTokenServices tokenServices, ClientDetailsService clientDetailsService, OAuth2RequestFactory requestFactory) {
        return new ArrayList<>(Collections.singletonList(
                new SmsTokenGranter(tokenServices, clientDetailsService, requestFactory, redisService, userDetailService)
        ));
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
