package cc.vihackerframework.system.controller;

import cc.vihackerframework.core.api.ViHackerApiResult;
import cc.vihackerframework.core.datasource.entity.QuerySearch;
import cc.vihackerframework.core.entity.system.Role;
import cc.vihackerframework.core.util.ExcelUtil;
import cc.vihackerframework.core.util.StringPool;
import cc.vihackerframework.core.log.annotation.LogEndpoint;
import cc.vihackerframework.system.service.IRoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * 角色相关
 * Created by Ranger on 2022/03/13
 */
@Slf4j
@Validated
@RestController
@Api(tags = "角色管理")
@RequiredArgsConstructor
@RequestMapping("role")
public class RoleController {

    private final IRoleService roleService;

    @GetMapping
    @PreAuthorize("hasAuthority('role:view')")
    @ApiOperation(value = "分页查询角色", notes = "查询角色")
    @LogEndpoint(value = "查询角色列表", exception = "查询角色列表失败")
    public ViHackerApiResult roleList(QuerySearch querySearch) {
        return ViHackerApiResult.data(roleService.findRoles(querySearch));
    }

    @GetMapping("options")
    @ApiOperation(value = "查询所有角色-下拉框使用", notes = "所有角色")
    public ViHackerApiResult roles() {
        return ViHackerApiResult.data(roleService.findAllRoles());
    }

    @GetMapping("check/{roleName}")
    @ApiOperation(value = "校验角色名称", notes = "校验角色")
    public ViHackerApiResult checkRoleName(@NotBlank(message = "{required}") @PathVariable String roleName) {
        Role result = this.roleService.findByName(roleName);
        return ViHackerApiResult.success(result == null);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('role:add')")
    @ApiOperation(value = "新增角色", notes = "新增")
    @LogEndpoint(value = "新增角色", exception = "新增角色失败")
    public ViHackerApiResult addRole(@Valid @RequestBody Role role) {
        return ViHackerApiResult.success(this.roleService.createRole(role));
    }

    @DeleteMapping("/{roleIds}")
    @PreAuthorize("hasAuthority('role:delete')")
    @ApiOperation(value = "角色删除", notes = "角色删除，支持批量操作")
    @LogEndpoint(value = "角色删除", exception = "角色删除失败")
    public ViHackerApiResult deleteRoles(@NotBlank(message = "{required}") @PathVariable String roleIds) {
        String[] ids = roleIds.split(StringPool.COMMA);
        return ViHackerApiResult.success(this.roleService.deleteRoles(ids));
    }

    @PutMapping
    @PreAuthorize("hasAuthority('role:update')")
    @ApiOperation(value = "角色修改", notes = "角色修改")
    @LogEndpoint(value = "角色修改", exception = "角色修改失败")
    public ViHackerApiResult updateRole(@Valid @RequestBody Role role) {
        return ViHackerApiResult.success(this.roleService.updateRole(role));
    }

    @PutMapping("/set-status")
    @PreAuthorize("hasAuthority('role:update')")
    @ApiOperation(value = "角色修改状态", notes = "修改")
    @LogEndpoint(value = "角色修改状态", exception = "角色修改状态失败")
    public ViHackerApiResult setStatus(@RequestBody Role role) {
        return ViHackerApiResult.success(this.roleService.setStatus(role));
    }

    @PostMapping("excel")
    @PreAuthorize("hasAuthority('role:export')")
    @ApiOperation(value = "导出角色", notes = "导出")
    @LogEndpoint(value = "导出角色数据", exception = "导出Excel失败")
    public void export(QuerySearch querySearch, HttpServletResponse response) {
        List<Role> roles = this.roleService.findRoles(querySearch).getRecords();
        //使用工具类导出excel
        ExcelUtil.exportExcel(roles, null, "角色", Role.class, "roles", response);
    }
}
