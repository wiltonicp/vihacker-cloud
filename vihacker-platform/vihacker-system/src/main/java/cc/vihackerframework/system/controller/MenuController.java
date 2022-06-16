package cc.vihackerframework.system.controller;

import cc.vihackerframework.core.api.ViHackerApiResult;
import cc.vihackerframework.core.datasource.entity.QuerySearch;
import cc.vihackerframework.core.entity.CurrentUser;
import cc.vihackerframework.core.entity.system.Menu;
import cc.vihackerframework.core.util.CollectionUtil;
import cc.vihackerframework.core.util.ExcelUtil;
import cc.vihackerframework.core.log.annotation.LogEndpoint;
import cc.vihackerframework.core.annotation.user.LoginAuth;
import cc.vihackerframework.system.service.IMenuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * 菜单相关接口
 * Created by Ranger on 2022/03/13
 */
@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@Api(tags = "菜单管理")
@RequestMapping("/menu")
public class MenuController {

    private final IMenuService menuService;

    @GetMapping("/tree")
    @ApiOperation(value = "根据用户获取菜单路由", notes = "菜单路由")
    public ViHackerApiResult<?> getUserRouters(@ApiIgnore @LoginAuth CurrentUser user) {
        return ViHackerApiResult.data(this.menuService.getUserRouters(user.getAccount()));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('menu:view')")
    @ApiOperation(value = "查询菜单列表", notes = "查询")
    @LogEndpoint(value = "查询菜单列表", exception = "查询菜单列表失败")
    public ViHackerApiResult<?> menuList(QuerySearch querySearch) {
        return ViHackerApiResult.data(this.menuService.findMenus(querySearch));
    }

    @GetMapping("/permissions")
    @ApiOperation(value = "获取用户权限菜单", notes = "用户权限菜单")
    public String findUserPermissions(HttpServletRequest request,String username) {
        return this.menuService.findUserPermissions(username);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('menu:add')")
    @ApiOperation(value = "新增菜单/按钮", notes = "新增")
    @LogEndpoint(value = "新增菜单/按钮", exception = "新增菜单/按钮失败")
    public ViHackerApiResult<?> addMenu(@Valid @RequestBody Menu menu) {
        return ViHackerApiResult.success(this.menuService.createMenu(menu));
    }

    @DeleteMapping("/{menuIds}")
    @PreAuthorize("hasAuthority('menu:delete')")
    @ApiOperation(value = "删除菜单/按钮", notes = "删除")
    @LogEndpoint(value = "删除菜单/按钮", exception = "删除菜单/按钮失败")
    public ViHackerApiResult<?> deleteMenus(@NotBlank(message = "{required}") @PathVariable String menuIds) {
        return ViHackerApiResult.success(this.menuService.removeByIds(CollectionUtil.stringToCollection(menuIds)));
    }

    @PutMapping
    @PreAuthorize("hasAuthority('menu:update')")
    @ApiOperation(value = "修改菜单/按钮", notes = "修改")
    @LogEndpoint(value = "修改菜单/按钮", exception = "修改菜单/按钮失败")
    public ViHackerApiResult<?> updateMenu(@Valid @RequestBody Menu menu) {
        return ViHackerApiResult.success(this.menuService.updateMenu(menu));
    }

    @PostMapping("excel")
    @PreAuthorize("hasAuthority('menu:export')")
    @ApiOperation(value = "导出菜单/按钮", notes = "导出")
    @LogEndpoint(value = "导出菜单数据", exception = "导出Excel失败")
    public void export(Menu menu, HttpServletResponse response) {
        List<Menu> menus = this.menuService.findMenuList(menu);
        //使用工具类导出excel
        ExcelUtil.exportExcel(menus, null, "菜单", Menu.class, "menus", response);
    }
}