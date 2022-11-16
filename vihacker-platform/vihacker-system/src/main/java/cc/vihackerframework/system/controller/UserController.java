package cc.vihackerframework.system.controller;

import cc.vihackerframework.core.api.ViHackerApiResult;
import cc.vihackerframework.core.datasource.entity.QuerySearch;
import cc.vihackerframework.core.util.SecurityUtil;
import cc.vihackerframework.core.entity.system.SysUser;
import cc.vihackerframework.core.exception.ViHackerException;
import cc.vihackerframework.core.util.ExcelUtil;
import cc.vihackerframework.core.util.StringPool;
import cc.vihackerframework.core.annotation.log.LogEndpoint;
import cc.vihackerframework.system.service.IUserDataPermissionService;
import cc.vihackerframework.system.service.IUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sound.midi.Soundbank;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * Created by Ranger on 2022/02/24
 */
@Slf4j
@Validated
@RestController
@Api(tags = "用户管理")
@RequiredArgsConstructor
@RequestMapping("user")
public class UserController {

    private final IUserService userService;
    private final IUserDataPermissionService userDataPermissionService;

    @GetMapping
    @PreAuthorize("hasAuthority('user:view')")
    @ApiOperation(value = "分页用户列表", notes = "用户列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "current", required = true, value = "当前页", paramType = "form"),
            @ApiImplicitParam(name = "size", required = true, value = "每页显示数据", paramType = "form"),
            @ApiImplicitParam(name = "keyword", required = false, value = "模糊查询关键词", paramType = "form"),
            @ApiImplicitParam(name = "startDate", required = false, value = "创建开始日期", paramType = "form"),
            @ApiImplicitParam(name = "endDate", required = false, value = "创建结束日期", paramType = "form"),
            @ApiImplicitParam(name = "prop", required = false, value = "排序属性", paramType = "form"),
            @ApiImplicitParam(name = "order", required = false, value = "排序方式", paramType = "form"),
            @ApiImplicitParam(name = "deptId", required = false, value = "部门id", paramType = "form"),
    })
    public ViHackerApiResult userList(QuerySearch querySearch, String deptId) {
        return ViHackerApiResult.data(userService.findUserDetailList(querySearch,deptId));
    }

    @GetMapping("check/{username}")
    @ApiOperation(value = "校验用户", notes = "查询")
    public boolean checkUserName(@NotBlank(message = "{required}") @PathVariable String username) {
        return this.userService.findByName(username) == null;
    }

    @PostMapping
    @PreAuthorize("hasAuthority('user:add')")
    @ApiOperation(value = "新增用户", notes = "新增")
    @LogEndpoint(value = "新增用户", exception = "新增用户失败")
    public ViHackerApiResult addUser(@Valid @RequestBody SysUser user) {
        return ViHackerApiResult.success(this.userService.createUser(user));
    }

    @PutMapping
    @PreAuthorize("hasAuthority('user:update')")
    @ApiOperation(value = "修改用户", notes = "修改")
    @LogEndpoint(value = "修改用户", exception = "修改用户失败")
    public ViHackerApiResult updateUser(@Valid @RequestBody SysUser user) {
        return ViHackerApiResult.success(this.userService.updateUser(user));
    }

    @GetMapping("/{userId}")
    @PreAuthorize("hasAuthority('user:update')")
    @ApiOperation(value = "查询用户权限", notes = "查询")
    @LogEndpoint(value = "查询用户权限", exception = "查询用户权限失败")
    public ViHackerApiResult findUserDataPermissions(@NotBlank(message = "{required}") @PathVariable String userId) {
        String dataPermissions = this.userDataPermissionService.findByUserId(userId);
        return ViHackerApiResult.data(dataPermissions);
    }

    @DeleteMapping("/{userIds}")
    @PreAuthorize("hasAuthority('user:delete')")
    @ApiOperation(value = "删除用户", notes = "删除")
    @LogEndpoint(value = "删除用户", exception = "删除用户失败")
    public ViHackerApiResult deleteUsers(@NotBlank(message = "{required}") @PathVariable String userIds) {
        return ViHackerApiResult.success(this.userService.deleteUsers(userIds));
    }

    @PutMapping("profile")
    @ApiOperation(value = "修改个人信息", notes = "修改")
    @LogEndpoint(value = "修改个人信息",exception = "修改个人信息失败")
    public void updateProfile(HttpServletRequest request, @Valid SysUser user) throws ViHackerException {
        this.userService.updateProfile(request,user);
    }

    @PutMapping("avatar")
    @ApiOperation(value = "修改头像", notes = "修改")
    @LogEndpoint(value = "修改头像",exception = "修改头像失败")
    public void updateAvatar(HttpServletRequest request,@NotBlank(message = "{required}") String avatar) {
        this.userService.updateAvatar(request,avatar);
    }

    @GetMapping("password/check")
    @ApiOperation(value = "校验密码", notes = "校验密码")
    @LogEndpoint(value = "修改密码",exception = "修改密码失败")
    public boolean checkPassword(HttpServletRequest request, @NotBlank(message = "{required}") String password) {
        String currentUsername = SecurityUtil.getCurrentUsername(request);
        SysUser user = userService.findByName(currentUsername);
        return user != null && new BCryptPasswordEncoder().matches(password, user.getPassword());
    }

    @PutMapping("password")
    @ApiOperation(value = "修改密码", notes = "修改密码")
    @LogEndpoint(value = "修改密码",exception = "修改密码失败")
    public void updatePassword(HttpServletRequest request,@NotBlank(message = "{required}") String password) {
        userService.updatePassword(request,password);
    }

    @PutMapping("password/reset")
    @PreAuthorize("hasAuthority('user:reset')")
    @ApiOperation(value = "管理员重置用户密码", notes = "重置")
    @LogEndpoint(value = "管理员重置用户密码", exception = "重置用户密码失败")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", required = true, value = "用户ID", paramType = "form"),
            @ApiImplicitParam(name = "password", required = true, value = "密码", paramType = "form")
    })
    public ViHackerApiResult resetPassword(@RequestBody SysUser sysUser) {
        return ViHackerApiResult.success(this.userService.resetPassword(sysUser));
    }

    @PostMapping("excel")
    @PreAuthorize("hasAuthority('user:export')")
    @ApiOperation(value = "导出用户数据", notes = "导出")
    @LogEndpoint(value = "导出用户数据", exception = "导出Excel失败")
    public void export(QuerySearch querySearch,String deptId, HttpServletResponse response) {
        List<SysUser> users = this.userService.findUserDetailList(querySearch,deptId).getRecords();
        //使用工具类导出excel
        ExcelUtil.exportExcel(users, null, "用户数据", SysUser.class, "user", response);
    }
}
