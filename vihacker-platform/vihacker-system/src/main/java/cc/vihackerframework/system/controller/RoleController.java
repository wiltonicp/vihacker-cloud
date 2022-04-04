package cc.vihackerframework.system.controller;

import cc.vihackerframework.core.api.ViHackerApiResult;
import cc.vihackerframework.core.entity.QueryRequest;
import cc.vihackerframework.core.entity.system.Role;
import cc.vihackerframework.core.util.ExcelUtil;
import cc.vihackerframework.core.util.StringPool;
import cc.vihackerframework.core.util.ViHackerUtil;
import cc.vihackerframework.log.starter.annotation.LogEndpoint;
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
import java.util.Map;

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
    @ApiOperation(value = "分页查询角色", notes = "查询角色")
    public ViHackerApiResult roleList(QueryRequest queryRequest, Role role) {
        Map<String, Object> dataTable = ViHackerUtil.getDataTable(roleService.findRoles(role, queryRequest));
        return ViHackerApiResult.data(dataTable);
    }

    @GetMapping("options")
    @ApiOperation(value = "查询所有角色", notes = "所有角色")
    public ViHackerApiResult roles() {
        List<Role> allRoles = roleService.findAllRoles();
        return ViHackerApiResult.data(allRoles);
    }

    @GetMapping("check/{roleName}")
    @ApiOperation(value = "校验角色名称", notes = "校验角色")
    public boolean checkRoleName(@NotBlank(message = "{required}") @PathVariable String roleName) {
        Role result = this.roleService.findByName(roleName);
        return result == null;
    }

    @PostMapping
    @PreAuthorize("hasAuthority('role:add')")
    @ApiOperation(value = "新增角色", notes = "新增")
    @LogEndpoint(value = "新增角色", exception = "新增角色失败")
    public void addRole(@Valid Role role) {
        this.roleService.createRole(role);
    }

    @DeleteMapping("/{roleIds}")
    @PreAuthorize("hasAuthority('role:delete')")
    @ApiOperation(value = "删除角色", notes = "删除")
    @LogEndpoint(value = "删除角色", exception = "删除角色失败")
    public void deleteRoles(@NotBlank(message = "{required}") @PathVariable String roleIds) {
        String[] ids = roleIds.split(StringPool.COMMA);
        this.roleService.deleteRoles(ids);
    }

    @PutMapping
    @PreAuthorize("hasAuthority('role:update')")
    @ApiOperation(value = "修改角色", notes = "修改")
    @LogEndpoint(value = "修改角色", exception = "修改角色失败")
    public void updateRole(@Valid Role role) {
        this.roleService.updateRole(role);
    }

    @PostMapping("excel")
    @PreAuthorize("hasAuthority('role:export')")
    @ApiOperation(value = "导出角色", notes = "导出")
    @LogEndpoint(value = "导出角色数据", exception = "导出Excel失败")
    public void export(QueryRequest queryRequest, Role role, HttpServletResponse response) {
        List<Role> roles = this.roleService.findRoles(role, queryRequest).getRecords();
        //使用工具类导出excel
        ExcelUtil.exportExcel(roles, null, "角色", Role.class, "roles", response);
    }
}
