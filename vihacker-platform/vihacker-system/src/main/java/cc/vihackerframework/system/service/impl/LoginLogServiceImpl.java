package cc.vihackerframework.system.service.impl;

import cc.vihackerframework.core.constant.ViHackerConstant;
import cc.vihackerframework.core.datasource.entity.QuerySearch;
import cc.vihackerframework.core.datasource.util.SortUtil;
import cc.vihackerframework.core.entity.CurrentUser;
import cc.vihackerframework.core.entity.system.LoginLog;
import cc.vihackerframework.core.entity.system.SysUser;
import cc.vihackerframework.core.util.AddressUtil;
import cc.vihackerframework.core.util.ViHackerUtil;
import cc.vihackerframework.system.mapper.LoginLogMapper;
import cc.vihackerframework.system.service.ILoginLogService;
import cc.vihackerframework.system.service.IUserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Created by Ranger on 2022/02/24
 */
@Service("loginLogService")
@RequiredArgsConstructor
public class LoginLogServiceImpl extends ServiceImpl<LoginLogMapper, LoginLog> implements ILoginLogService {

    private final IUserService userService;
    @Override
    public IPage<LoginLog> findLoginLogs(QuerySearch search) {
        QueryWrapper<LoginLog> queryWrapper = new QueryWrapper<>();

        if (StringUtils.isNotBlank(search.getKeyword())) {
            queryWrapper.lambda().eq(LoginLog::getUsername, search.getKeyword().toLowerCase());
        }
        if (StringUtils.isNotBlank(search.getStartDate())) {
            queryWrapper.lambda()
                    .ge(LoginLog::getLoginTime, search.getStartDate())
                    .le(LoginLog::getLoginTime, search.getEndDate());
        }

        Page<LoginLog> page = new Page<>(search.getCurrent(), search.getSize());
        SortUtil.handlePageSort(search, page, "loginTime", ViHackerConstant.ORDER_DESC, true);

        return this.page(page, queryWrapper);
    }

    @Override
    public void saveLoginLog(HttpServletRequest request, CurrentUser user) {
        // update last login time
        this.userService.updateLoginTime(user.getAccount());
        // save login log
        LoginLog loginLog = new LoginLog();
        loginLog.setUsername(user.getAccount());
        loginLog.setSystemBrowserInfo(request.getHeader("user-agent"));
        loginLog.setLoginTime(LocalDateTime.now());
        String ip = ViHackerUtil.getHttpServletRequestIpAddress();
        loginLog.setIp(ip);
        loginLog.setLocation(AddressUtil.getCityInfo(ip));
        this.save(loginLog);
    }

    @Override
    public void deleteLoginLogs(String[] ids) {
        List<String> list = Arrays.asList(ids);
        baseMapper.deleteBatchIds(list);
    }

    @Override
    public Long findTotalVisitCount() {
        return this.baseMapper.findTotalVisitCount();
    }

    @Override
    public Long findTodayVisitCount() {
        return this.baseMapper.findTodayVisitCount();
    }

    @Override
    public Long findTodayIp() {
        return this.baseMapper.findTodayIp();
    }

    @Override
    public List<Map<String, Object>> findLastTenDaysVisitCount(SysUser user) {
        return this.baseMapper.findLastTenDaysVisitCount(user);
    }

    @Override
    public List<LoginLog> findUserLastSevenLoginLogs(String username) {
        LoginLog loginLog = new LoginLog();
        loginLog.setUsername(username);

        QuerySearch search = new QuerySearch();
        search.setCurrent(1);
        // 近7日记录
        search.setSize(7);

        IPage<LoginLog> loginLogs = this.findLoginLogs(search);
        return loginLogs.getRecords();
    }
}
