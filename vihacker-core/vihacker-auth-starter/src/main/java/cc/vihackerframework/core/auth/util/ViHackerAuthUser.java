package cc.vihackerframework.core.auth.util;

import cc.vihackerframework.core.auth.entity.AdminAuthUser;
import lombok.experimental.UtilityClass;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Created by Ranger on 2022/5/4.
 */
@UtilityClass
public class ViHackerAuthUser {

    /**
     * 获取Authentication
     */
    private Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    /**
     * 获取用户
     */
    public AdminAuthUser getUser() {
        Authentication authentication = getAuthentication();
        return getUser(authentication);
    }


    /**
     * 获取用户
     *
     * @param authentication 用户认证
     * @return 登录用户
     */
    public AdminAuthUser getUser(Authentication authentication) {
        Object principal = authentication.getPrincipal();
        if (principal instanceof AdminAuthUser) {
            return (AdminAuthUser) principal;
        }
        return null;
    }

    /**
     * 获取用户名称
     *
     * @return username
     */
    public String getUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return null;
        }
        return authentication.getName();
    }
}
