package cc.vihackerframework.uaa.service.impl;

import cc.vihackerframework.core.auth.entity.AdminAuthUser;
import cc.vihackerframework.core.constant.Oauth2Constant;
import cc.vihackerframework.uaa.manager.AdminUserManager;
import cc.vihackerframework.core.entity.enums.StatusEnum;
import cc.vihackerframework.core.entity.system.SysUser;
import cc.vihackerframework.uaa.service.ViHackerUserDetailsService;
import cn.hutool.core.convert.Convert;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collection;

/**
 * <p>
 *
 * @author Ranger
 * @email wilton.icp@gmail.com
 * @since 2021/6/5
 */
@Slf4j
@Service
public class ViHackerUserDetailsServiceImpl implements ViHackerUserDetailsService {

    @Resource
    private AdminUserManager manager;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //获取用户信息
        SysUser sysUser = manager.findByName(username);
        if (sysUser != null) {
            log.info("用户名：{}", sysUser.getUsername());
            String permissions = manager.findUserPermission(username);
            boolean notLocked = false;
            if (StatusEnum.STATUS_VALID.getCode().equals(sysUser.getStatus())) {
                notLocked = true;
            }
            Collection<? extends GrantedAuthority> authorities
                    = AuthorityUtils.commaSeparatedStringToAuthorityList(permissions);
            log.info("authorities: {}", authorities);
            return new AdminAuthUser(sysUser.getUsername(),sysUser.getPassword(),true,true,true, notLocked,authorities,
                    sysUser.getUserId(),sysUser.getAvatar(),sysUser.getEmail(),sysUser.getMobile(),sysUser.getSex(), Oauth2Constant.LOGIN_USERNAME_TYPE,sysUser.getTenantId(),
                    sysUser.getDeptId(),sysUser.getDeptName(),sysUser.getRoleId(),sysUser.getRoleName(),sysUser.getLastLoginTime(),sysUser.getStatus());
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
