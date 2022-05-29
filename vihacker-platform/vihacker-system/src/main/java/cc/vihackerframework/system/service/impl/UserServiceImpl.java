package cc.vihackerframework.system.service.impl;

import cc.vihackerframework.core.entity.CurrentUser;
import cc.vihackerframework.core.util.SecurityUtil;
import cc.vihackerframework.core.constant.ViHackerConstant;
import cc.vihackerframework.core.datasource.util.SortUtil;
import cc.vihackerframework.core.entity.QueryRequest;
import cc.vihackerframework.core.entity.system.SysUser;
import cc.vihackerframework.core.entity.system.UserDataPermission;
import cc.vihackerframework.core.entity.system.UserRole;
import cc.vihackerframework.core.exception.ViHackerException;
import cc.vihackerframework.core.util.StringPool;
import cc.vihackerframework.system.mapper.UserMapper;
import cc.vihackerframework.system.service.IUserDataPermissionService;
import cc.vihackerframework.system.service.IUserRoleService;
import cc.vihackerframework.system.service.IUserService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Ranger on 2022/02/24
 */
@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class UserServiceImpl extends ServiceImpl<UserMapper, SysUser> implements IUserService {

    private final IUserRoleService userRoleService;
    private final IUserDataPermissionService userDataPermissionService;

    @Override
    public SysUser findByName(String username) {
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUser::getUsername, username);
        return this.baseMapper.selectOne(queryWrapper);
    }

    @Override
    public IPage<SysUser> findUserDetailList(SysUser user, QueryRequest request) {
        Page<SysUser> page = new Page<>(request.getPageNum(), request.getPageSize());
        SortUtil.handlePageSort(request, page, "userId", ViHackerConstant.ORDER_ASC, false);
        return this.baseMapper.findUserDetailPage(page, user);
    }

    @Override
    public SysUser findUserDetail(String username) {
        SysUser param = new SysUser();
        param.setUsername(username);
        List<SysUser> users = this.baseMapper.findUserDetail(param);
        return CollectionUtils.isNotEmpty(users) ? users.get(0) : null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateLoginTime(String username) {
        SysUser user = new SysUser();
        user.setLastLoginTime(LocalDateTime.now());

        this.baseMapper.update(user, new LambdaQueryWrapper<SysUser>().eq(SysUser::getUsername, username));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createUser(SysUser user) {
        // 创建用户
        user.setCreatedTime(LocalDateTime.now());
        user.setAvatar(ViHackerConstant.DEFAULT_AVATAR);
        user.setPassword(new BCryptPasswordEncoder().encode(ViHackerConstant.DEFAULT_PASSWORD));
        save(user);
        // 保存用户角色
        String[] roles = StringUtils.splitByWholeSeparatorPreserveAllTokens(user.getRoleId().toString(), StringPool.COMMA);
        setUserRoles(user, roles);
        // 保存用户数据权限关联关系
        String[] deptIds = StringUtils.splitByWholeSeparatorPreserveAllTokens(user.getDeptId().toString(), StringPool.COMMA);
        setUserDataPermissions(user, deptIds);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateUser(SysUser user) {
        // 更新用户
        user.setPassword(null);
        user.setUsername(null);
        user.setCreatedTime(null);
        user.setModifyTime(LocalDateTime.now());
        updateById(user);

        String[] userIds = {String.valueOf(user.getId())};
        userRoleService.deleteUserRolesByUserId(userIds);
        String[] roles = StringUtils.splitByWholeSeparatorPreserveAllTokens(user.getRoleId().toString(), StringPool.COMMA);
        setUserRoles(user, roles);

        userDataPermissionService.deleteByUserIds(userIds);
        String[] deptIds = StringUtils.splitByWholeSeparatorPreserveAllTokens(user.getDeptId().toString(), StringPool.COMMA);
        setUserDataPermissions(user, deptIds);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteUsers(String[] userIds) {
        List<String> list = Arrays.asList(userIds);
        removeByIds(list);
        // 删除用户角色
        this.userRoleService.deleteUserRolesByUserId(userIds);
        this.userDataPermissionService.deleteByUserIds(userIds);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateProfile(HttpServletRequest request,SysUser user) throws ViHackerException {
        user.setPassword(null);
        user.setUsername(null);
        user.setStatus(null);
        if (isCurrentUser(user.getId(),request)) {
            updateById(user);
        } else {
            throw new ViHackerException("您无权修改别人的账号信息！");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateAvatar(HttpServletRequest request,String avatar) {
        SysUser user = new SysUser();
        user.setAvatar(avatar);
        String currentUsername = SecurityUtil.getCurrentUsername(request);
        this.baseMapper.update(user, new LambdaQueryWrapper<SysUser>().eq(SysUser::getUsername, currentUsername));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updatePassword(HttpServletRequest request,String password) {
        SysUser user = new SysUser();
        user.setPassword(new BCryptPasswordEncoder().encode(password));
        String currentUsername = SecurityUtil.getCurrentUsername(request);
        this.baseMapper.update(user, new LambdaQueryWrapper<SysUser>().eq(SysUser::getUsername, currentUsername));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void resetPassword(String[] usernames) {
        SysUser params = new SysUser();
        params.setPassword(new BCryptPasswordEncoder().encode(ViHackerConstant.DEFAULT_PASSWORD));

        List<String> list = Arrays.asList(usernames);
        this.baseMapper.update(params, new LambdaQueryWrapper<SysUser>().in(SysUser::getUsername, list));

    }

    private void setUserRoles(SysUser user, String[] roles) {
        List<UserRole> userRoles = new ArrayList<>();
        Arrays.stream(roles).forEach(roleId -> {
            UserRole userRole = new UserRole();
            userRole.setUserId(user.getId());
            userRole.setRoleId(Long.valueOf(roleId));
            userRoles.add(userRole);
        });
        userRoleService.saveBatch(userRoles);
    }

    private void setUserDataPermissions(SysUser user, String[] deptIds) {
        List<UserDataPermission> userDataPermissions = new ArrayList<>();
        Arrays.stream(deptIds).forEach(deptId -> {
            UserDataPermission permission = new UserDataPermission();
            permission.setDeptId(Long.valueOf(deptId));
            permission.setUserId(user.getId());
            userDataPermissions.add(permission);
        });
        userDataPermissionService.saveBatch(userDataPermissions);
    }

    private boolean isCurrentUser(Long id,HttpServletRequest request) {
        CurrentUser currentUser = SecurityUtil.getCurrentUser(request);
        return currentUser != null && id.equals(currentUser.getUserId());
    }
}
