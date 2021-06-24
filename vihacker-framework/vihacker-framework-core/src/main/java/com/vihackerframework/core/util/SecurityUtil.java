package com.vihackerframework.core.util;

import com.vihackerframework.core.entity.system.AdminAuthUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *
 * @author Ranger
 * @email wilton.icp@gmail.com
 * @since 2021/6/7
 */
public class SecurityUtil {

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
}
