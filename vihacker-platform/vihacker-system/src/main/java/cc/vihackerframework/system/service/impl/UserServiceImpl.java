package cc.vihackerframework.system.service.impl;

import cc.vihackerframework.core.api.IErrorCode;
import cc.vihackerframework.core.api.ResultCode;
import cc.vihackerframework.core.datasource.entity.QuerySearch;
import cc.vihackerframework.core.entity.CurrentUser;
import cc.vihackerframework.core.entity.enums.StatusEnum;
import cc.vihackerframework.core.entity.system.Role;
import cc.vihackerframework.core.exception.Asserts;
import cc.vihackerframework.core.util.CollectionUtil;
import cc.vihackerframework.core.util.EnumUtil;
import cc.vihackerframework.core.util.SecurityUtil;
import cc.vihackerframework.core.constant.ViHackerConstant;
import cc.vihackerframework.core.entity.system.SysUser;
import cc.vihackerframework.core.entity.system.UserDataPermission;
import cc.vihackerframework.core.entity.system.UserRole;
import cc.vihackerframework.core.exception.ViHackerException;
import cc.vihackerframework.core.util.StringPool;
import cc.vihackerframework.system.mapper.UserMapper;
import cc.vihackerframework.system.service.*;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
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
import java.util.stream.Collectors;

/**
 * Created by Ranger on 2022/02/24
 */
@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class UserServiceImpl extends ServiceImpl<UserMapper, SysUser> implements IUserService {

    private final IUserRoleService userRoleService;
    private final IDeptService deptService;

    private final IRoleService roleService;

    private final IUserDataPermissionService userDataPermissionService;

    @Override
    public SysUser findByName(String username) {
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUser::getUsername, username);
        return this.baseMapper.selectOne(queryWrapper);
    }

    @Override
    public IPage<SysUser> findUserDetailList(QuerySearch querySearch, String deptId) {
        Page<SysUser> page = new Page<>(querySearch.getCurrent(), querySearch.getSize());
        Page<SysUser> sysUserPage = new LambdaQueryChainWrapper<>(baseMapper)
                .eq(StringUtils.isNotBlank(deptId),SysUser::getDeptId,deptId)
                .between(StrUtil.isNotBlank(querySearch.getStartDate()), SysUser::getCreatedTime, querySearch.getStartDate(), querySearch.getEndDate())
                .like(StringUtils.isNotBlank(querySearch.getKeyword()), SysUser::getNickName, querySearch.getKeyword())
                .or(StringUtils.isNotBlank(querySearch.getKeyword()))
                .like(StringUtils.isNotBlank(querySearch.getKeyword()), SysUser::getRealName, querySearch.getKeyword())
                .or(StringUtils.isNotBlank(querySearch.getKeyword()))
                .like(StringUtils.isNotBlank(querySearch.getKeyword()), SysUser::getUsername, querySearch.getKeyword())
                .or(StringUtils.isNotBlank(querySearch.getKeyword()))
                .like(StringUtils.isNotBlank(querySearch.getKeyword()), SysUser::getId, querySearch.getKeyword())
                .orderByDesc(SysUser::getId)
                .page(page);
        sysUserPage.getRecords().stream().forEach(sysUser -> {
            sysUser.setStatusName(EnumUtil.getEnumByCode(StatusEnum.class,sysUser.getStatus().toString()));
            sysUser.setDeptName(deptService.getById(sysUser.getDeptId()).getName());
            List<Role> userRole = roleService.findUserRole(sysUser.getUsername());
            sysUser.setRoleId(userRole.stream().map(role -> role.getId().toString()).collect(Collectors.joining(StringPool.COMMA)));
            sysUser.setRoleName(userRole.stream().map(role -> role.getRoleName()).collect(Collectors.joining(StringPool.COMMA)));
        });

        return sysUserPage;
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
    public boolean createUser(SysUser user) {
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
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateUser(SysUser user) {
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
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteUsers(String userIds) {
        removeByIds(CollectionUtil.stringToCollection(userIds));
        // 删除用户角色
        String[] userIdArr = userIds.split(StringPool.COMMA);
        this.userRoleService.deleteUserRolesByUserId(userIdArr);
        this.userDataPermissionService.deleteByUserIds(userIdArr);
        return true;
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
    public boolean resetPassword(SysUser sysUser) {
        if (StringUtils.isBlank(sysUser.getPassword()) || ObjectUtils.isEmpty(sysUser.getId())) {
            Asserts.fail(ResultCode.GLOBAL_PARAM_ERROR);
        }
        String pwd = new BCryptPasswordEncoder().encode(sysUser.getPassword());
        SysUser asysUser = new SysUser();
        asysUser.setId(sysUser.getId());
        asysUser.setPassword(pwd);
        return updateById(asysUser);
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
