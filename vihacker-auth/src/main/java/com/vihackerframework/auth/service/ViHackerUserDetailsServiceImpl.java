package com.vihackerframework.auth.service;

import com.vihackerframework.auth.manager.AdminUserManager;
import com.vihackerframework.core.entity.enums.StatusEnum;
import com.vihackerframework.core.entity.system.AdminAuthUser;
import com.vihackerframework.core.entity.system.SysUser;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
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
public class ViHackerUserDetailsServiceImpl implements UserDetailsService {

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
            return authUser;
        }
        throw new UsernameNotFoundException("用户名或密码错误");
    }
}
