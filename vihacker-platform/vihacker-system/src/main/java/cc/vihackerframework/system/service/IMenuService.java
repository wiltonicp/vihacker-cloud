package cc.vihackerframework.system.service;

import cc.vihackerframework.core.entity.VueRouter;
import cc.vihackerframework.core.entity.system.Menu;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * Created by Ranger on 2022/03/13
 */
public interface IMenuService extends IService<Menu> {

    /**
     * 获取用户权限
     *
     * @param username 用户名
     * @return 用户权限
     */
    String findUserPermissions(HttpServletRequest request, String username);

    /**
     * 获取用户菜单
     *
     * @param username 用户名
     * @return 用户菜单
     */
    List<Menu> findUserMenus(HttpServletRequest request,String username);

    /**
     * 获取用户菜单
     *
     * @param menu menu
     * @return 用户菜单
     */
    Map<String, Object> findMenus(Menu menu);

    /**
     * 获取用户路由
     *
     * @param username 用户名
     * @return 用户路由
     */
    List<VueRouter<Menu>> getUserRouters(HttpServletRequest request,String username);

    /**
     * 获取菜单列表
     *
     * @param menu menu
     * @return 菜单列表
     */
    List<Menu> findMenuList(Menu menu);

    /**
     * 创建菜单
     *
     * @param menu menu
     */
    void createMenu(Menu menu);

    /**
     * 更新菜单
     *
     * @param menu menu
     */
    void updateMenu(Menu menu);

    /**
     * 递归删除菜单/按钮
     *
     * @param menuIds menuIds
     */
    void deleteMeuns(String[] menuIds);

}
