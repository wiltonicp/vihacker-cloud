package cc.vihackerframework.core.util;

import cc.vihackerframework.core.api.ResultCode;
import cc.vihackerframework.core.context.UserContext;
import cc.vihackerframework.core.entity.CurrentUser;
import cc.vihackerframework.core.constant.Oauth2Constant;
import cc.vihackerframework.core.exception.ViHackerAuthException;
import io.jsonwebtoken.Claims;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * <p>
 *
 * @author Ranger
 * @email wilton.icp@gmail.com
 * @since 2021/6/7
 */
@Slf4j
@UtilityClass
public class SecurityUtil {

    public static String BEARER = "bearer";
    public static Integer AUTH_LENGTH = 7;

    /**
     * 从HttpServletRequest里获取token
     *
     * @param request HttpServletRequest
     * @return token
     */
    public static String getHeaderToken(HttpServletRequest request) {
        return request.getHeader(Oauth2Constant.HEADER_TOKEN);
    }

    /**
     * 获取token串
     * @param request
     * @return
     */
    public static String getToken(HttpServletRequest request) {
        String headerToken = getHeaderToken(request);
        if (StringUtil.isBlank(headerToken)) {
            throw new ViHackerAuthException("没有携带Token信息！");
        }
        return StringUtil.isNotBlank(headerToken) ? TokenUtil.getToken(headerToken) : "";
    }

    /**
     * 从Token解析获取Claims对象
     *
     * @param token ViHacker-Auth获取的token
     * @return Claims
     */
    public static Claims getClaims(String token) {
        Claims claims = null;
        if (StringUtil.isNotBlank(token)) {
            try {
                claims = TokenUtil.getClaims(token);
            } catch (Exception e) {
                throw new ViHackerAuthException(ResultCode.UNAUTHORIZED.getMessage());
            }
        }
        return claims;
    }

    /**
     * 从HttpServletRequest获取CurrentUser信息
     *
     * @param request HttpServletRequest
     * @return CurrentUser
     */
    public static CurrentUser getCurrentUser(HttpServletRequest request) {
        CurrentUser currentUser = new CurrentUser();
        String token = getToken(request);
        Claims claims = getClaims(token);
        try {
            // 然后根据token获取用户登录信息，这里省略获取用户信息的过程
            currentUser.setUserId(Long.parseLong(claims.get(Oauth2Constant.VIHACKER_USER_ID,String.class)));
            currentUser.setAccount((String) claims.get(Oauth2Constant.VIHACKER_USER_NAME));
            currentUser.setRoleId(String.valueOf(claims.get(Oauth2Constant.VIHACKER_ROLE_ID)));
            currentUser.setTenantId(String.valueOf(claims.get(Oauth2Constant.VIHACKER_TENANT_ID)));
            currentUser.setType(claims.get(Oauth2Constant.VIHACKER_TYPE,Integer.class));
            Object permissions = claims.get(Oauth2Constant.VIHACKER_AUTHORITIES);
            List<GrantedAuthority> authorities = AuthorityUtils.commaSeparatedStringToAuthorityList(permissions.toString());
            currentUser.setAuthorities(new HashSet<GrantedAuthority>(authorities));
            UserContext.setUser(currentUser);
        }catch (Exception e){
            log.error("用户上下文信息获取失败：{}",e.getMessage());
        }
        return currentUser;
    }

    /**
     * 获取当前用户名称
     *
     * @return String 用户名
     */
    public static String getCurrentUsername(HttpServletRequest request) {

        return getCurrentUser(request).getAccount();
    }
}
