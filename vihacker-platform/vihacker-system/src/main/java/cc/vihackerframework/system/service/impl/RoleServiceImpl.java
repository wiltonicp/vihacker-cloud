package cc.vihackerframework.system.service.impl;

import cc.vihackerframework.core.datasource.entity.QuerySearch;
import cc.vihackerframework.core.entity.system.Role;
import cc.vihackerframework.core.entity.system.RoleMenu;
import cc.vihackerframework.core.util.CollectionUtil;
import cc.vihackerframework.system.mapper.RoleMapper;
import cc.vihackerframework.system.service.IRoleMenuService;
import cc.vihackerframework.system.service.IRoleService;
import cc.vihackerframework.system.service.IUserRoleService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Ranger on 2022/03/13
 */
@Slf4j
@Service("roleService")
@RequiredArgsConstructor
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements IRoleService {

    private final IRoleMenuService roleMenuService;
    private final IUserRoleService userRoleService;

    @Override
    public IPage<Role> findRoles(QuerySearch querySearch) {
        Page<Role> page = new Page<>(querySearch.getCurrent(), querySearch.getSize());
        Page<Role> pageList = new LambdaQueryChainWrapper<>(baseMapper)
                .between(StringUtils.isNotBlank(querySearch.getStartDate()),Role::getCreatedTime, querySearch.getStartDate(), querySearch.getEndDate())
                .or()
                .like(StringUtils.isNotBlank(querySearch.getKeyword()),Role::getRoleName, querySearch.getKeyword())
                .or()
                .like(StringUtils.isNotBlank(querySearch.getKeyword()),Role::getId, querySearch.getKeyword())
                .page(page);

        pageList.getRecords().forEach(role ->{
            List<RoleMenu> roleMenus = roleMenuService.getRoleMenusByRoleId(role.getId().toString());
            role.setMenu(roleMenus.stream().map(m -> m.getMenuId().toString()).collect(Collectors.toList()));
        });

        return pageList;
    }

    @Override
    public List<Role> findUserRole(String userName) {
        return baseMapper.findUserRole(userName);
    }

    @Override
    public List<Role> findAllRoles() {
        return new LambdaQueryChainWrapper<>(baseMapper)
                .eq(Role::getStatus,0)
                .orderByAsc(Role::getId).list();
    }

    @Override
    public Role findByName(String roleName) {
        return baseMapper.selectOne(new LambdaQueryWrapper<Role>().eq(Role::getRoleName, roleName));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean createRole(Role role) {
        role.setCreatedTime(LocalDateTime.now());
        this.save(role);

        if (CollectionUtil.isNotEmpty(role.getMenu())) {
            setRoleMenus(role, role.getMenu());
        }
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteRoles(String[] roleIds) {
        List<String> list = Arrays.asList(roleIds);
        baseMapper.deleteBatchIds(list);

        this.roleMenuService.deleteRoleMenusByRoleId(roleIds);
        this.userRoleService.deleteUserRolesByRoleId(roleIds);
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateRole(Role role) {
        role.setRoleName(null);
        role.setModifyTime(LocalDateTime.now());
        baseMapper.updateById(role);

        roleMenuService.remove(new LambdaQueryWrapper<RoleMenu>().eq(RoleMenu::getRoleId, role.getId()));
        if (CollectionUtil.isNotEmpty(role.getMenu())) {
            setRoleMenus(role, role.getMenu());
        }
        return true;
    }

    @Override
    public boolean setStatus(Role role) {
        return new LambdaUpdateChainWrapper<>(baseMapper)
                .set(Role::getStatus, role.getStatus())
                .eq(Role::getId, role.getId())
                .update();
    }

    private void setRoleMenus(Role role, List<String> menuIds) {
        List<RoleMenu> roleMenus = new ArrayList<>();
        menuIds.forEach(menuId -> {
            RoleMenu roleMenu = new RoleMenu();
            if (StringUtils.isNotBlank(menuId)) {
                roleMenu.setMenuId(Long.valueOf(menuId));
            }
            roleMenu.setRoleId(role.getId());
            roleMenus.add(roleMenu);
        });
        this.roleMenuService.saveBatch(roleMenus);
    }

}
