package cc.vihackerframework.resource.starter.interceptor;

import cc.vihackerframework.core.auth.context.UserContext;
import cc.vihackerframework.core.auth.entity.AdminAuthUser;
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
        String authorization = request.getHeader("Authorization");
        authorization = StrUtil.removePrefix(authorization, "Bearer ");
        authorization = StrUtil.removePrefix(authorization, "bearer ");
        if (StrUtil.isNotBlank(authorization)) {
            Jwt decode = JwtHelper.decode(authorization);

            String claims = decode.getClaims();
            HashMap<String, Object> hashMap = objectMapper.readValue(claims, HashMap.class);
            System.out.println(hashMap);
            String userName = hashMap.get("username").toString();
            Long roleId = Long.parseLong(hashMap.get("roleId").toString());
            String tenantId = String.valueOf(hashMap.get("tenantId"));
            Long userId = Long.parseLong(hashMap.get("userId").toString());
            String avatar = hashMap.get("avatar").toString();
            Object permissions = hashMap.get("authorities");

            Collection<? extends GrantedAuthority> authorities
                    = AuthorityUtils.commaSeparatedStringToAuthorityList(permissions.toString());
            AdminAuthUser adminAuthUser = new AdminAuthUser(userName,"",authorities,userId,avatar,tenantId,roleId);
            UserContext.setUserInfo(adminAuthUser);
        }
        return true;
    }
}
