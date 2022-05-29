package cc.vihackerframework.uaa.service.impl;

import cc.vihackerframework.core.auth.entity.AdminAuthUser;
import cc.vihackerframework.core.auth.entity.UserInfo;
import cc.vihackerframework.core.constant.Oauth2Constant;
import cc.vihackerframework.core.exception.ViHackerAuthException;
import cc.vihackerframework.uaa.manager.AdminUserManager;
import cc.vihackerframework.core.entity.enums.StatusEnum;
import cc.vihackerframework.core.entity.system.SysUser;
import cc.vihackerframework.uaa.service.ViHackerUserDetailsService;
import cn.hutool.core.convert.Convert;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

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
        UserInfo userInfo = manager.findByName(username);
        if (userInfo == null) {
            throw new ViHackerAuthException("该用户：" + username + "不存在");
        }
        userInfo.setType(Oauth2Constant.LOGIN_USERNAME_TYPE);
        userInfo.setUserName(username);
        return getUserDetails(userInfo);
    }

    @Override
    public UserDetails loadUserByMobile(String mobile) throws UsernameNotFoundException {
        //获取用户信息
        UserInfo userInfo = manager.findByMobile(mobile);
        if (userInfo == null) {
            throw new ViHackerAuthException("该用户：" + mobile + "不存在");
        }
        userInfo.setType(Oauth2Constant.LOGIN_MOBILE_TYPE);
        userInfo.setUserName(mobile);
        return getUserDetails(userInfo);
    }

    private UserDetails getUserDetails(UserInfo userInfo) {
        boolean notLocked = true;
        if (ObjectUtils.isEmpty(userInfo)) {
            log.info("该用户：{} 不存在！", userInfo.getUserName());
            throw new ViHackerAuthException("该用户：" + userInfo.getUserName() + "不存在");
        } else if (StatusEnum.STATUS_LOCK.getCode().equals(userInfo.getSysUser().getStatus())) {
            notLocked = false;
            log.info("该用户：{} 已被停用!", userInfo.getUserName());
            throw new ViHackerAuthException("对不起，您的账号：" + userInfo.getUserName() + " 已停用");
        }
        SysUser sysUser = userInfo.getSysUser();
        log.info("用户名：{}", sysUser.getUsername());
        Collection<? extends GrantedAuthority> authorities
                = AuthorityUtils.createAuthorityList(Convert.toStrArray(userInfo.getPermissions()));
        log.info("authorities: {}", authorities);
        return new AdminAuthUser(sysUser.getUsername(),sysUser.getPassword(),true,true,true, notLocked,authorities,
                sysUser.getId(),sysUser.getAvatar(),sysUser.getEmail(),sysUser.getMobile(),sysUser.getSex(), userInfo.getType(),sysUser.getTenantId(),
                sysUser.getDeptId(),sysUser.getDeptName(),sysUser.getRoleId(),sysUser.getRoleName(),sysUser.getLastLoginTime(),sysUser.getStatus().longValue());
    }

    @Override
    public UserDetails loadUserBySocial(String openId) throws UsernameNotFoundException {
        return null;
    }
}
