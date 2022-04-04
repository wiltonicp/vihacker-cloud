package cc.vihackerframework.uaa.granter;

import cc.vihackerframework.core.auth.entity.AdminAuthUser;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.common.exceptions.InvalidGrantException;
import org.springframework.security.oauth2.provider.*;
import org.springframework.security.oauth2.provider.token.AbstractTokenGranter;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;

import java.util.Map;

/**
 * 自定义token授予抽象实现
 * 继承抽象类AbstractTokenGranter，我们也是抽象的，后面好扩展
 *
 * Created by Ranger on 2022/3/20
 */
public abstract class CustomAbstractTokenGranter extends AbstractTokenGranter {

    protected CustomAbstractTokenGranter(AuthorizationServerTokenServices tokenServices, ClientDetailsService clientDetailsService, OAuth2RequestFactory requestFactory, String grantType) {
        super(tokenServices, clientDetailsService, requestFactory, grantType);
    }

    @Override
    protected OAuth2Authentication getOAuth2Authentication(ClientDetails client, TokenRequest tokenRequest) {
        Map<String, String> parameters = tokenRequest.getRequestParameters();
        AdminAuthUser adminAuthUser = (AdminAuthUser) getUserDetails(parameters);
        if (null == adminAuthUser) {
            throw new InvalidGrantException("账户未找到");
        }

        Authentication userAuth = new UsernamePasswordAuthenticationToken(adminAuthUser,
                adminAuthUser.getPassword(), adminAuthUser.getAuthorities());

        OAuth2Request storedOAuth2Request = getRequestFactory().createOAuth2Request(client, tokenRequest);
        return new OAuth2Authentication(storedOAuth2Request, userAuth);
    }

    /**
     * 自定义获取用户信息
     */
    protected abstract UserDetails getUserDetails(Map<String, String> parameters);
}
