package cc.vihackerframework.uaa.configure;

import cc.vihackerframework.core.auth.entity.AdminAuthUser;
import cc.vihackerframework.core.constant.Oauth2Constant;
import cc.vihackerframework.redis.starter.service.RedisService;
import cc.vihackerframework.uaa.exception.ViHackerAuthWebResponseExceptionTranslator;
import cc.vihackerframework.uaa.granter.CaptchaTokenGranter;
import cc.vihackerframework.uaa.granter.SmsCodeTokenGranter;
import cc.vihackerframework.uaa.granter.SocialTokenGranter;
import cc.vihackerframework.uaa.properties.ViHackerAuthProperties;
import cc.vihackerframework.uaa.service.RedisAuthenticationCodeService;
import cc.vihackerframework.uaa.service.RedisClientDetailsService;
import cc.vihackerframework.uaa.service.impl.SingleLoginTokenServices;
import com.xkcoding.justauth.AuthRequestFactory;
import lombok.RequiredArgsConstructor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.core.userdetails.UserDetailsByNameServiceWrapper;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.CompositeTokenGranter;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.TokenGranter;
import org.springframework.security.oauth2.provider.approval.ApprovalStore;
import org.springframework.security.oauth2.provider.approval.JdbcApprovalStore;
import org.springframework.security.oauth2.provider.client.ClientCredentialsTokenGranter;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeTokenGranter;
import org.springframework.security.oauth2.provider.code.JdbcAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.implicit.ImplicitTokenGranter;
import org.springframework.security.oauth2.provider.password.ResourceOwnerPasswordTokenGranter;
import org.springframework.security.oauth2.provider.refresh.RefreshTokenGranter;
import org.springframework.security.oauth2.provider.token.*;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationProvider;

import javax.sql.DataSource;
import java.util.*;

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

    private final DataSource dataSource;
    private final RedisService redisService;
    private final AuthRequestFactory factory;
    private final ViHackerAuthProperties properties;
    private final UserDetailsService userDetailService;
    private final AuthenticationManager authenticationManager;
    private final RedisConnectionFactory redisConnectionFactory;
    private final RedisClientDetailsService redisClientDetailsService;
    private final RedisAuthenticationCodeService redisAuthenticationCodeService;
    private final ViHackerAuthWebResponseExceptionTranslator exceptionTranslator;


    /**
     * 配置token存储到redis中
     */
    @Bean
    public RedisTokenStore redisTokenStore() {
        return new RedisTokenStore(redisConnectionFactory);
    }


    /**
     * 已授权客户端存储记录
     */
    @Bean
    public ApprovalStore approvalStore() {
        return new JdbcApprovalStore(dataSource);
    }

    /**
     * 授权码code的存储-mysql
     */
    @Bean
    public AuthorizationCodeServices authorizationCodeServices() {
        JdbcAuthorizationCodeServices service = new JdbcAuthorizationCodeServices(dataSource);
        return service;
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.withClientDetails(redisClientDetailsService);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
        DefaultTokenServices tokenServices = createDefaultTokenServices();

        // token增强链
        TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
        // 把jwt增强，与额外信息增强加入到增强链
        tokenEnhancerChain.setTokenEnhancers(Arrays.asList(tokenEnhancer(), jwtAccessTokenConverter()));
        tokenServices.setTokenEnhancer(tokenEnhancerChain);

        // 配置tokenServices参数
        addUserDetailsService(tokenServices);
        endpoints
                .tokenGranter(tokenGranter(endpoints, tokenServices))
                .tokenServices(tokenServices)
                .approvalStore(approvalStore())
                .authorizationCodeServices(authorizationCodeServices())
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
        security
                .checkTokenAccess("isAuthenticated()")
                .tokenKeyAccess("isAuthenticated()")
                .allowFormAuthenticationForClients();
    }

    /**
     * 创建默认的tokenServices
     * @return
     */
    private DefaultTokenServices createDefaultTokenServices() {
        DefaultTokenServices tokenServices = new SingleLoginTokenServices(properties.isSingleLogin());
        tokenServices.setTokenStore(redisTokenStore());
        // 支持刷新Token
        tokenServices.setSupportRefreshToken(Boolean.TRUE);
        tokenServices.setReuseRefreshToken(Boolean.FALSE);
        tokenServices.setClientDetailsService(redisClientDetailsService);
        addUserDetailsService(tokenServices);
        return tokenServices;
    }


    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter() {
        JwtAccessTokenConverter accessTokenConverter = new JwtAccessTokenConverter();
        accessTokenConverter.setSigningKey(properties.getJwtAccessKey());
        return accessTokenConverter;
    }

    private void addUserDetailsService(DefaultTokenServices tokenServices) {
        if (userDetailService != null) {
            PreAuthenticatedAuthenticationProvider provider = new PreAuthenticatedAuthenticationProvider();
            provider.setPreAuthenticatedUserDetailsService(new UserDetailsByNameServiceWrapper<>(userDetailService));
            tokenServices.setAuthenticationManager(new ProviderManager(Collections.singletonList(provider)));
        }
    }

    /**
     * 重点
     * 先获取已经有的五种授权，然后添加我们自己的进去
     *
     * @param endpoints AuthorizationServerEndpointsConfigurer
     * @return TokenGranter
     */
    private TokenGranter tokenGranter(final AuthorizationServerEndpointsConfigurer endpoints, DefaultTokenServices tokenServices) {
        List<TokenGranter> granters = new ArrayList<>(Collections.singletonList(endpoints.getTokenGranter()));

        // 短信验证码模式
        granters.add(new SmsCodeTokenGranter(authenticationManager, tokenServices, endpoints.getClientDetailsService(),
                endpoints.getOAuth2RequestFactory(), redisService));
        // 验证码模式
        granters.add(new CaptchaTokenGranter(authenticationManager, tokenServices, endpoints.getClientDetailsService(),
                endpoints.getOAuth2RequestFactory(), redisService));
        // 社交登录模式
        granters.add(new SocialTokenGranter(authenticationManager, tokenServices, endpoints.getClientDetailsService(),
                endpoints.getOAuth2RequestFactory(), redisService, factory));
        // 增加密码模式
        granters.add(new ResourceOwnerPasswordTokenGranter(authenticationManager, tokenServices, endpoints.getClientDetailsService(),
                endpoints.getOAuth2RequestFactory()));
        //授权码模式
        granters.add(new AuthorizationCodeTokenGranter(
                        tokenServices,
                        endpoints.getAuthorizationCodeServices(),
                        endpoints.getClientDetailsService(),
                        endpoints.getOAuth2RequestFactory()));
        //刷新token模式
        granters.add(new RefreshTokenGranter(
                        tokenServices,
                        endpoints.getClientDetailsService(),
                        endpoints.getOAuth2RequestFactory()));
        //、简化模式
        granters.add(new ImplicitTokenGranter(
                tokenServices,
                endpoints.getClientDetailsService(),
                endpoints.getOAuth2RequestFactory()));

        //客户端模式
        granters.add(new ClientCredentialsTokenGranter(
                tokenServices,
                endpoints.getClientDetailsService(),
                endpoints.getOAuth2RequestFactory()));

        return new CompositeTokenGranter(granters);
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
                    additionMessage.put(Oauth2Constant.MATE_USER_ID, String.valueOf(user.getUserId()));
                    additionMessage.put(Oauth2Constant.MATE_USER_NAME, user.getUsername());
                    additionMessage.put(Oauth2Constant.MATE_AVATAR, user.getAvatar());
                    additionMessage.put(Oauth2Constant.MATE_ROLE_ID, String.valueOf(user.getRoleId()));
                    additionMessage.put(Oauth2Constant.MATE_TYPE, user.getType());
                    additionMessage.put(Oauth2Constant.MATE_TENANT_ID, user.getTenantId());
                }
                ((DefaultOAuth2AccessToken) oAuth2AccessToken).setAdditionalInformation(additionMessage);
                return oAuth2AccessToken;
            }
        };
    }
}
