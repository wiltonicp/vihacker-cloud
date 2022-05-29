package cc.vihackerframework.system.service.impl;

import cc.vihackerframework.core.context.UserContext;
import cc.vihackerframework.core.datasource.entity.Search;
import cc.vihackerframework.core.entity.MenuTree;
import cc.vihackerframework.core.entity.RouterMeta;
import cc.vihackerframework.core.entity.VueRouter;
import cc.vihackerframework.core.entity.enums.MenuTypeEnum;
import cc.vihackerframework.core.entity.system.Menu;
import cc.vihackerframework.core.util.StringPool;
import cc.vihackerframework.core.tree.TreeUtil;
import cc.vihackerframework.system.mapper.MenuMapper;
import cc.vihackerframework.system.service.IMenuService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Ranger on 2022/03/13
 */
@Slf4j
@Service("menuService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements IMenuService {

    @Override
    public String findUserPermissions(String username) {
        List<Menu> userPermissions = this.baseMapper.findUserPermissions(username);
        return userPermissions.stream().map(Menu::getPermission).collect(Collectors.joining(StringPool.COMMA));
    }

    @Override
    public List<Menu> findUserMenus(String username) {
        return this.baseMapper.findUserMenus(username);
    }

    @Override
    public List<VueRouter<Menu>> findMenus(Search search) {
        List<VueRouter<Menu>> routes = new ArrayList<>();
        try {
            List<Menu> menus = new LambdaQueryChainWrapper<>(baseMapper)
                    .between(StringUtils.isNotBlank(search.getStartDate()),
                            Menu::getCreatedTime, search.getStartDate(), search.getEndDate())
                    .like(StringUtils.isNotBlank(search.getKeyword()), Menu::getName, search.getKeyword())
                    .orderByAsc(Menu::getOrderNum)
                    .list();
            buildVueRouter(routes, menus);
        } catch (NumberFormatException e) {
            log.error("查询菜单失败", e);
        }
        return TreeUtil.buildVueRouter(routes);
    }

    @Override
    public List<VueRouter<Menu>> getUserRouters(String username) {
        List<VueRouter<Menu>> routes = new ArrayList<>();
        List<Menu> menus = this.findUserMenus(username);
        buildVueRouter(routes, menus);
        return TreeUtil.buildVueRouter(routes);
    }

    /**
     * 生成菜单树
     * @param routes
     * @param menus
     */
    private void buildVueRouter(List<VueRouter<Menu>> routes, List<Menu> menus) {
        menus.forEach(menu -> {
            VueRouter<Menu> route = new VueRouter<>();
            route.setId(menu.getId().toString());
            route.setParentId(menu.getParentId().toString());
            route.setPath(menu.getPath());
            route.setComponent(menu.getComponent());
            route.setName(menu.getName());
            route.setIcon(menu.getIcon());
            route.setType(menu.getType());
            route.setStatus(menu.getStatus());
            route.setOrderNum(menu.getOrderNum());
            route.setPermission(menu.getPermission());
            route.setCreateTime(menu.getCreatedTime());
            if (MenuTypeEnum.LIB.getCode().equals(menu.getType())) {
                route.setTypeName(MenuTypeEnum.LIB.getMessage());
            } else if (MenuTypeEnum.MENU.getCode().equals(menu.getType())) {
                route.setTypeName(MenuTypeEnum.MENU.getMessage());
            } else if (MenuTypeEnum.BUTTON.getCode().equals(menu.getType())) {
                route.setTypeName(MenuTypeEnum.BUTTON.getMessage());
            }
            route.setMeta(new RouterMeta(menu.getName(), menu.getIcon(), true));
            routes.add(route);
        });
    }


    @Override
    public List<Menu> findMenuList(Menu menu) {
        LambdaQueryWrapper<Menu> queryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(menu.getName())) {
            queryWrapper.like(Menu::getName, menu.getName());
        }
        queryWrapper.orderByAsc(Menu::getId);
        return this.baseMapper.selectList(queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean createMenu(Menu menu) {
        menu.setTenantId(UserContext.current().getTenantId());
        setMenu(menu);
        return this.save(menu);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateMenu(Menu menu) {
        setMenu(menu);
        return this.updateById(menu);
    }

    private void buildTrees(List<MenuTree> trees, List<Menu> menus) {
        menus.forEach(menu -> {
            MenuTree tree = new MenuTree();
            tree.setId(menu.getId().toString());
            tree.setParentId(menu.getParentId().toString());
            tree.setName(menu.getName());
            tree.setComponent(menu.getComponent());
            tree.setIcon(menu.getIcon());
            tree.setOrderNum(menu.getOrderNum());
            tree.setPath(menu.getPath());
            tree.setType(menu.getType());
            tree.setStatus(menu.getStatus());
            tree.setTenantId(menu.getTenantId());
            tree.setPermission(menu.getPermission());
            trees.add(tree);
        });
    }

    private void setMenu(Menu menu) {
        if (menu.getParentId() == null) {
            menu.setParentId(Menu.TOP_MENU_ID);
        }
        if (MenuTypeEnum.BUTTON.getCode().equals(menu.getType())) {
            menu.setPath(null);
            menu.setIcon(null);
            menu.setComponent(null);
            menu.setOrderNum(null);
        }
    }
}
