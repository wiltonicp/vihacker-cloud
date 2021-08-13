package cc.vihackerframework.core.auth.util;

import cc.vihackerframework.core.api.ResultCode;
import cc.vihackerframework.core.auth.entity.AdminAuthUser;
import cc.vihackerframework.core.auth.entity.CurrentUser;
import cc.vihackerframework.core.exception.ViHackerAuthException;
import cc.vihackerframework.core.util.StringUtil;
import cc.vihackerframework.core.util.TokenUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * <p>
 *
 * @author Ranger
 * @email wilton.icp@gmail.com
 * @since 2021/6/7
 */
@Slf4j
public class SecurityUtil {

    public static String BEARER = "bearer";
    public static Integer AUTH_LENGTH = 7;

    /**
     * 获取token串
     *
     * @param auth token
     * @return String
     */
    public static String getToken(String auth) {
        if ((auth != null) && (auth.length() > AUTH_LENGTH)) {
            String headStr = auth.substring(0, 6).toLowerCase();
            if (headStr.compareTo(BEARER) == 0) {
                auth = auth.substring(7);
            }
            return auth;
        }
        return null;
    }

    /**
     * 从Token解析获取Claims对象
     *
     * @param token Mate-Auth获取的token
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
     * 从HttpServletRequest里获取token
     *
     * @param request HttpServletRequest
     * @return token
     */
    public static String getHeaderToken(HttpServletRequest request) {
        return request.getHeader("ViHacker-Uaa");
    }

    /**
     * 从ThreadLocal获取其自己的SecurityContext，从而获取在Security上下文中缓存的登录用户
     */
    public static AdminAuthUser getLoginUser() {
        AdminAuthUser user = null;
        SecurityContext ctx = SecurityContextHolder.getContext();
        Authentication auth = ctx.getAuthentication();
        if (auth.getPrincipal() instanceof UserDetails) user = (AdminAuthUser) auth.getPrincipal();
        assert user != null;
        return user;
    }

    /**
     * 获取在线用户信息
     *
     * @return CurrentUser 当前用户信息
     */
    public static CurrentUser getCurrentUser() {
        try {
            LinkedHashMap<String, Object> authenticationDetails = getAuthenticationDetails();
            Object principal = authenticationDetails.get("principal");
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(mapper.writeValueAsString(principal), CurrentUser.class);
        } catch (Exception e) {
            log.error("获取当前用户信息失败", e);
            return null;
        }
    }

    /**
     * 获取当前用户认证信息
     *
     * @return 认证对象
     */
    public static Authentication getUserAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    /**
     * 获取当前用户信息
     *
     * @return 用户对象
     */
    public static Object getCurrentPrincipal() {
        return getUserAuthentication().getPrincipal();
    }

    /**
     * 判断是否具有此权限
     *
     * @param roleName
     * @return
     */
    public Boolean hasRole(String roleName) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<String> roleCodes = new ArrayList<>();
        userDetails.getAuthorities().forEach(authority -> {
            roleCodes.add(authority.getAuthority());
        });
        return roleCodes.contains(roleName);
    }

    /**
     * 获取当前用户名称
     *
     * @return String 用户名
     */
    public static String getCurrentUsername() {
        Object principal = getOauth2Authentication().getPrincipal();
        if (principal instanceof AdminAuthUser) {
            return ((AdminAuthUser) principal).getUsername();
        }
        return (String) getOauth2Authentication().getPrincipal();
    }

    private static OAuth2Authentication getOauth2Authentication() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (OAuth2Authentication) authentication;
    }

    @SuppressWarnings("all")
    private static LinkedHashMap<String, Object> getAuthenticationDetails() {
        return (LinkedHashMap<String, Object>) getOauth2Authentication().getUserAuthentication().getDetails();
    }

    /**
     * 获取当前用户权限集
     *
     * @return Collection<GrantedAuthority>权限集
     */
    public static Collection<GrantedAuthority> getCurrentUserAuthority() {
        return getOauth2Authentication().getAuthorities();
    }

    /**
     * 获取当前令牌内容
     *
     * @return String 令牌内容
     */
    public static String getCurrentTokenValue() {
        try {
            OAuth2AuthenticationDetails details = (OAuth2AuthenticationDetails) getOauth2Authentication().getDetails();
            return details.getTokenValue();
        } catch (Exception ignore) {
            return null;
        }
    }
}
