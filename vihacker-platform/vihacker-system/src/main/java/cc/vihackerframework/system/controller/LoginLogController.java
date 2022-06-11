package cc.vihackerframework.system.controller;

import cc.vihackerframework.core.api.ViHackerApiResult;
import cc.vihackerframework.core.datasource.entity.QuerySearch;
import cc.vihackerframework.core.util.SecurityUtil;
import cc.vihackerframework.core.entity.system.LoginLog;
import cc.vihackerframework.core.util.ExcelUtil;
import cc.vihackerframework.core.util.StringPool;
import cc.vihackerframework.core.log.annotation.LogEndpoint;
import cc.vihackerframework.system.service.ILoginLogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Map;

/**
 * 当前用户日志管理
 * Created by Ranger on 2022/02/24
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("loginLog")
@Api(tags = "当前用户日志管理")
public class LoginLogController {

    private final ILoginLogService loginLogService;

    @GetMapping
    @ApiOperation("获取登录日志分页信息")
    public ViHackerApiResult loginLogList(QuerySearch search) {
        return ViHackerApiResult.data(this.loginLogService.findLoginLogs(search));
    }

    @GetMapping("currentUser")
    @ApiOperation("获取用户最近7次登录日志")
    public ViHackerApiResult getUserLastSevenLoginLogs(HttpServletRequest request) {
        String currentUsername = SecurityUtil.getCurrentUsername(request);
        List<LoginLog> userLastSevenLoginLogs = this.loginLogService.findUserLastSevenLoginLogs(currentUsername);
        return ViHackerApiResult.data(userLastSevenLoginLogs);
    }

    @DeleteMapping("{ids}")
    @PreAuthorize("hasAuthority('loginlog:delete')")
    @ApiOperation("删除登录日志")
    @LogEndpoint(value = "删除登录日志", exception = "删除登录日志失败")
    public void deleteLogs(@NotBlank(message = "{required}") @PathVariable String ids) {
        String[] loginLogIds = ids.split(StringPool.COMMA);
        this.loginLogService.deleteLoginLogs(loginLogIds);
    }

    @PostMapping("excel")
    @PreAuthorize("hasAuthority('loginlog:export')")
    @ApiOperation("导出登录日志数据")
    @LogEndpoint(value = "导出登录日志数据", exception = "导出Excel失败")
    public void export(QuerySearch search, HttpServletResponse response) {
        List<LoginLog> loginLogs = this.loginLogService.findLoginLogs(search).getRecords();
        //使用工具类导出excel
        ExcelUtil.exportExcel(loginLogs, null, "用户数据", LoginLog.class, "user", response);
    }
}
