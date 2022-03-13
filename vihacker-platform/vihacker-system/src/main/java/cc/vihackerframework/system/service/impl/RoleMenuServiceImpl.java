package cc.vihackerframework.system.service.impl;

import cc.vihackerframework.core.entity.system.RoleMenu;
import cc.vihackerframework.system.mapper.RoleMenuMapper;
import cc.vihackerframework.system.service.IRoleMenuService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Ranger on 2022/03/13
 */
@Service("roleMenuService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class RoleMenuServiceImpl extends ServiceImpl<RoleMenuMapper, RoleMenu> implements IRoleMenuService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteRoleMenusByRoleId(String[] roleIds) {
        List<String> list = Arrays.asList(roleIds);
        baseMapper.delete(new LambdaQueryWrapper<RoleMenu>().in(RoleMenu::getRoleId, list));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteRoleMenusByMenuId(String[] menuIds) {
        List<String> list = Arrays.asList(menuIds);
        baseMapper.delete(new LambdaQueryWrapper<RoleMenu>().in(RoleMenu::getMenuId, list));
    }

    @Override
    public List<RoleMenu> getRoleMenusByRoleId(String roleId) {
        return baseMapper.selectList(new LambdaQueryWrapper<RoleMenu>().eq(RoleMenu::getRoleId, roleId));
    }

}
