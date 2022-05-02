package cc.vihackerframework.system.controller;

import cc.vihackerframework.core.api.ViHackerApiResult;
import cc.vihackerframework.core.auth.util.SecurityUtil;
import cc.vihackerframework.core.entity.QueryRequest;
import cc.vihackerframework.core.entity.system.LoginLog;
import cc.vihackerframework.core.entity.system.SysUser;
import cc.vihackerframework.core.exception.ViHackerException;
import cc.vihackerframework.core.util.ExcelUtil;
import cc.vihackerframework.core.util.StringPool;
import cc.vihackerframework.core.util.ViHackerUtil;
import cc.vihackerframework.core.log.annotation.LogEndpoint;
import cc.vihackerframework.system.service.ILoginLogService;
import cc.vihackerframework.system.service.IUserDataPermissionService;
import cc.vihackerframework.system.service.IUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Map;

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
    private final ILoginLogService loginLogService;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("success")
    @ApiOperation(value = "登录成功调用,保存登录日志", notes = "新增")
    public void loginSuccess(HttpServletRequest request) {
        String currentUsername = SecurityUtil.getCurrentUsername();
        // update last login time
        this.userService.updateLoginTime(currentUsername);
        // save login log
        LoginLog loginLog = new LoginLog();
        loginLog.setUsername(currentUsername);
        loginLog.setSystemBrowserInfo(request.getHeader("user-agent"));
        this.loginLogService.saveLoginLog(loginLog);
    }


    @GetMapping
    @PreAuthorize("hasAuthority('user:view')")
    @ApiOperation(value = "分页用户列表", notes = "用户列表")
    public ViHackerApiResult userList(QueryRequest queryRequest, SysUser user) {
        Map<String, Object> dataTable = ViHackerUtil.getDataTable(userService.findUserDetailList(user, queryRequest));
        return ViHackerApiResult.data(dataTable);
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
    public void addUser(@Valid SysUser user) {
        this.userService.createUser(user);
    }

    @PutMapping
    @PreAuthorize("hasAuthority('user:update')")
    @ApiOperation(value = "修改用户", notes = "修改")
    @LogEndpoint(value = "修改用户", exception = "修改用户失败")
    public void updateUser(@Valid SysUser user) {
        this.userService.updateUser(user);
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
    public void deleteUsers(@NotBlank(message = "{required}") @PathVariable String userIds) {
        String[] ids = userIds.split(StringPool.COMMA);
        this.userService.deleteUsers(ids);
    }

    @PutMapping("profile")
    @ApiOperation(value = "修改个人信息", notes = "修改")
    @LogEndpoint(value = "修改个人信息",exception = "修改个人信息失败")
    public void updateProfile(@Valid SysUser user) throws ViHackerException {
        this.userService.updateProfile(user);
    }

    @PutMapping("avatar")
    @ApiOperation(value = "修改头像", notes = "修改")
    @LogEndpoint(value = "修改头像",exception = "修改头像失败")
    public void updateAvatar(@NotBlank(message = "{required}") String avatar) {
        this.userService.updateAvatar(avatar);
    }

    @GetMapping("password/check")
    @ApiOperation(value = "校验密码", notes = "校验密码")
    @LogEndpoint(value = "修改密码",exception = "修改密码失败")
    public boolean checkPassword(@NotBlank(message = "{required}") String password) {
        String currentUsername = SecurityUtil.getCurrentUsername();
        SysUser user = userService.findByName(currentUsername);
        return user != null && passwordEncoder.matches(password, user.getPassword());
    }

    @PutMapping("password")
    @ApiOperation(value = "修改密码", notes = "修改密码")
    @LogEndpoint(value = "修改密码",exception = "修改密码失败")
    public void updatePassword(@NotBlank(message = "{required}") String password) {
        userService.updatePassword(password);
    }

    @PutMapping("password/reset")
    @PreAuthorize("hasAuthority('user:reset')")
    @ApiOperation(value = "重置用户密码", notes = "重置")
    @LogEndpoint(value = "重置用户密码", exception = "重置用户密码失败")
    public void resetPassword(@NotBlank(message = "{required}") String usernames) {
        String[] usernameArr = usernames.split(StringPool.COMMA);
        this.userService.resetPassword(usernameArr);
    }

    @PostMapping("excel")
    @PreAuthorize("hasAuthority('user:export')")
    @ApiOperation(value = "导出用户数据", notes = "导出")
    @LogEndpoint(value = "导出用户数据", exception = "导出Excel失败")
    public void export(QueryRequest queryRequest, SysUser user, HttpServletResponse response) {
        List<SysUser> users = this.userService.findUserDetailList(user, queryRequest).getRecords();
        //使用工具类导出excel
        ExcelUtil.exportExcel(users, null, "用户数据", SysUser.class, "user", response);
    }
}
