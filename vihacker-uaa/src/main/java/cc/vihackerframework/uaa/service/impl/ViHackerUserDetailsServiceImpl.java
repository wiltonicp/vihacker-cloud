package cc.vihackerframework.uaa.service.impl;

import cc.vihackerframework.core.auth.entity.AdminAuthUser;
import cc.vihackerframework.uaa.manager.AdminUserManager;
import cc.vihackerframework.core.entity.enums.StatusEnum;
import cc.vihackerframework.core.entity.system.SysUser;
import cc.vihackerframework.uaa.service.ViHackerUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * <p>
 *
 * @author Ranger
 * @email wilton.icp@gmail.com
 * @since 2021/6/5
 */
@Service
@RequiredArgsConstructor
public class ViHackerUserDetailsServiceImpl implements ViHackerUserDetailsService {

    private final AdminUserManager manager;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //获取用户信息
        SysUser sysUser = manager.findByName(username);
        if (sysUser != null) {
            String permissions = manager.findUserPermission(username);
            boolean notLocked = false;
            if (StatusEnum.STATUS_VALID.getCode().equals(sysUser.getStatus())) {
                notLocked = true;
            }
            AdminAuthUser authUser = new AdminAuthUser(sysUser.getUsername(), sysUser.getPassword(), true, true, true, notLocked,
                    AuthorityUtils.commaSeparatedStringToAuthorityList(permissions));

            BeanUtils.copyProperties(sysUser, authUser);
            Authentication authentication= new UsernamePasswordAuthenticationToken(authUser, null, authUser.getAuthorities()) ;
            SecurityContextHolder.getContext().setAuthentication(authentication);
            return authUser;
        }
        throw new UsernameNotFoundException("用户名或密码错误");
    }

    @Override
    public UserDetails loadUserByMobile(String mobile) throws UsernameNotFoundException {
        return null;
    }

    @Override
    public UserDetails loadUserBySocial(String openId) throws UsernameNotFoundException {
        return null;
    }
}
