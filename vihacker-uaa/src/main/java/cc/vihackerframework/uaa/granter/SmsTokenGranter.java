package cc.vihackerframework.uaa.granter;

import cc.vihackerframework.core.constant.Oauth2Constant;
import cc.vihackerframework.redis.starter.service.RedisService;
import cc.vihackerframework.uaa.service.ViHackerUserDetailsService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.common.exceptions.UserDeniedAuthorizationException;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.OAuth2RequestFactory;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;

import java.util.Map;

/**
 * 手机号验证码登录 TokenGranter
 * Created by Ranger on 2022/3/20
 */
public class SmsTokenGranter extends CustomAbstractTokenGranter{

    private static final String SMS = "sms";

    private RedisService redisService;
    private ViHackerUserDetailsService userDetailsService;

    public SmsTokenGranter(AuthorizationServerTokenServices tokenServices, ClientDetailsService clientDetailsService, OAuth2RequestFactory requestFactory, RedisService redisService, ViHackerUserDetailsService userDetailsService) {
        super(tokenServices, clientDetailsService, requestFactory, SMS);
        this.redisService = redisService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected UserDetails getUserDetails(Map<String, String> parameters) {
        String mobile = parameters.get("mobile");
        String smsCode = parameters.get("code");
        if(StringUtils.isBlank(smsCode)){
            throw new UserDeniedAuthorizationException("请输入短信验证码！");
        }

        String codeFromRedis = null;
        // 从Redis里读取存储的验证码信息
        try {
            codeFromRedis = redisService.get(Oauth2Constant.SMS_CODE_KEY + mobile).toString();
        } catch (Exception e) {
            throw new UserDeniedAuthorizationException("短信验证码不存在或已过期！");
        }

        // 比较输入的验证码是否正确
        if (!StringUtils.equalsIgnoreCase(smsCode, codeFromRedis)) {
            throw new UserDeniedAuthorizationException("短信验证码不正确！");
        }
        redisService.del(Oauth2Constant.SMS_CODE_KEY + mobile);

        return userDetailsService.loadUserByMobile(mobile);
    }
}
