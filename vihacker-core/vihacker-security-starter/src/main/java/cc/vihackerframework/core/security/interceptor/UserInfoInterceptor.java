package cc.vihackerframework.core.security.interceptor;

import cc.vihackerframework.core.auth.context.UserContext;
import cc.vihackerframework.core.auth.entity.AdminAuthUser;
import cc.vihackerframework.core.constant.Oauth2Constant;
import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collection;
import java.util.HashMap;

/**
 * Created By Ranger on 2022/3/16
 */
@Slf4j
public class UserInfoInterceptor implements HandlerInterceptor {

    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String authorization = request.getHeader(Oauth2Constant.HEADER_TOKEN);
        authorization = StrUtil.removePrefix(authorization, "Bearer ");
        authorization = StrUtil.removePrefix(authorization, "bearer ");

        if (StrUtil.isNotBlank(authorization)) {
            Jwt decode = JwtHelper.decode(authorization);

            String claims = decode.getClaims();
            HashMap<String, Object> hashMap = objectMapper.readValue(claims, HashMap.class);

            String userName = String.valueOf(hashMap.get(Oauth2Constant.VIHACKER_USER_NAME));
            Long roleId = Long.parseLong(String.valueOf(hashMap.get(Oauth2Constant.VIHACKER_ROLE_ID)));
            String tenantId = String.valueOf(hashMap.get(Oauth2Constant.VIHACKER_TENANT_ID));
            Long userId = Long.parseLong(String.valueOf(hashMap.get(Oauth2Constant.VIHACKER_USER_ID)));
            String avatar = String.valueOf(hashMap.get(Oauth2Constant.VIHACKER_AVATAR));
            Object permissions = hashMap.get(Oauth2Constant.VIHACKER_AUTHORITIES);

            Collection<? extends GrantedAuthority> authorities
                    = AuthorityUtils.commaSeparatedStringToAuthorityList(permissions.toString());
            AdminAuthUser adminAuthUser = new AdminAuthUser(userName,"",authorities,userId,avatar,tenantId,roleId);
            UserContext.setUserInfo(adminAuthUser);
        }
        return true;
    }
}
