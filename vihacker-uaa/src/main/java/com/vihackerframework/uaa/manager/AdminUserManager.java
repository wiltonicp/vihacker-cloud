package com.vihackerframework.uaa.manager;

import com.vihackerframework.uaa.mapper.IAdminMenuMapper;
import com.vihackerframework.uaa.mapper.IAdminUserMapper;
import com.vihackerframework.core.entity.system.Menu;
import com.vihackerframework.core.entity.system.SysUser;
import lombok.RequiredArgsConstructor;
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
    public SysUser findByName(String username) {
        SysUser user = adminUserMapper.findByName(username);
        if (user != null) {
        }
        return user;
    }

    /**
     * 通过用户名查询用户权限串
     *
     * @param username 用户名
     * @return 权限
     */
    public String findUserPermission(String username) {
        List<Menu> userPermissions = adminMenuMapper.findUserPermission(username);
        return userPermissions.stream().map(Menu::getPerms).collect(Collectors.joining(","));
    }
}
