package cc.vihackerframework.uaa.manager;

import cc.vihackerframework.core.auth.entity.UserInfo;
import cc.vihackerframework.core.util.StringPool;
import cc.vihackerframework.uaa.mapper.IAdminMenuMapper;
import cc.vihackerframework.uaa.mapper.IAdminUserMapper;
import cc.vihackerframework.core.entity.system.Menu;
import cc.vihackerframework.core.entity.system.SysUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 *
 * @author Ranger
 * @email wilton.icp@gmail.com
 * @since 2021/6/5
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class AdminUserManager {

    private final IAdminUserMapper adminUserMapper;
    private final IAdminMenuMapper adminMenuMapper;

    /**
     * 通过用户名查询用户信息
     *
     * @param username 用户名
     * @return 用户
     */
    public UserInfo findByName(String username) {
        SysUser user = adminUserMapper.findByName(username);
        return getUserInfo(user);
    }

    /**
     * 通过手机号查询用户信息
     * @param mobile
     * @return
     */
    public UserInfo findByMobile(String mobile){
        SysUser user = adminUserMapper.findByMobile(mobile);
        return getUserInfo(user);
    }

    /**
     * 通过用户名查询用户权限串
     *
     * @param username 用户名
     * @return 权限
     */
    public String findUserPermission(String username) {
        List<Menu> userPermissions = adminMenuMapper.findUserPermission(username);
        return userPermissions.stream().map(Menu::getPerms).collect(Collectors.joining(StringPool.COMMA));
    }

    public UserInfo getUserInfo(SysUser sysUser) {
        if (sysUser == null) {
            return null;
        }
        UserInfo userInfo = new UserInfo();
        userInfo.setSysUser(sysUser);
        userInfo.setPermissions(adminMenuMapper.findUserPermission(sysUser.getUsername()).stream().map(Menu::getPerms).collect(Collectors.toList()));
        userInfo.setTenantId(sysUser.getTenantId());
        log.debug("userInfo:{}", userInfo);
        return userInfo;
    }
}
