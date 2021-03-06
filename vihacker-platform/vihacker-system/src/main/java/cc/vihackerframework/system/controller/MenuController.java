package cc.vihackerframework.system.controller;

import cc.vihackerframework.core.api.ViHackerApiResult;
import cc.vihackerframework.core.entity.VueRouter;
import cc.vihackerframework.core.entity.system.Menu;
import cc.vihackerframework.core.util.ExcelUtil;
import cc.vihackerframework.core.util.StringPool;
import cc.vihackerframework.log.starter.annotation.LogEndpoint;
import cc.vihackerframework.system.service.IMenuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @GetMapping("/{username}")
    @ApiOperation(value = "根据用户获取菜单路由", notes = "菜单路由")
    public ViHackerApiResult getUserRouters(@NotBlank(message = "{required}") @PathVariable String username) {
        Map<String, Object> result = new HashMap<>(4);
        List<VueRouter<Menu>> userRouters = this.menuService.getUserRouters(username);
        String userPermissions = this.menuService.findUserPermissions(username);
        String[] permissionArray = new String[0];
        if (StringUtils.isNoneBlank(userPermissions)) {
            permissionArray = StringUtils.splitByWholeSeparatorPreserveAllTokens(userPermissions, StringPool.COMMA);
        }
        result.put("routes", userRouters);
        result.put("permissions", permissionArray);
        return ViHackerApiResult.data(result);
    }

    @GetMapping
    @ApiOperation(value = "获取菜单树", notes = "菜单树")
    public ViHackerApiResult menuList(Menu menu) {
        Map<String, Object> menus = this.menuService.findMenus(menu);
        return ViHackerApiResult.data(menus);
    }

    @GetMapping("/permissions")
    @ApiOperation(value = "获取用户权限菜单", notes = "用户权限菜单")
    public String findUserPermissions(String username) {
        return this.menuService.findUserPermissions(username);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('menu:add')")
    @ApiOperation(value = "新增菜单/按钮", notes = "新增")
    @LogEndpoint(value = "新增菜单/按钮", exception = "新增菜单/按钮失败")
    public void addMenu(@Valid Menu menu) {
        this.menuService.createMenu(menu);
    }

    @DeleteMapping("/{menuIds}")
    @PreAuthorize("hasAuthority('menu:delete')")
    @ApiOperation(value = "删除菜单/按钮", notes = "删除")
    @LogEndpoint(value = "删除菜单/按钮", exception = "删除菜单/按钮失败")
    public void deleteMenus(@NotBlank(message = "{required}") @PathVariable String menuIds) {
        String[] ids = menuIds.split(StringPool.COMMA);
        this.menuService.deleteMeuns(ids);
    }

    @PutMapping
    @PreAuthorize("hasAuthority('menu:update')")
    @ApiOperation(value = "修改菜单/按钮", notes = "修改")
    @LogEndpoint(value = "修改菜单/按钮", exception = "修改菜单/按钮失败")
    public void updateMenu(@Valid Menu menu) {
        this.menuService.updateMenu(menu);
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