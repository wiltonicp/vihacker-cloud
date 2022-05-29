package cc.vihackerframework.system.service;

import cc.vihackerframework.core.datasource.entity.QuerySearch;
import cc.vihackerframework.core.entity.CurrentUser;
import cc.vihackerframework.core.entity.system.LoginLog;
import cc.vihackerframework.core.entity.system.SysUser;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * Created by Ranger on 2022/02/24
 */
public interface ILoginLogService extends IService<LoginLog> {

    /**
     * 获取登录日志分页信息
     * @param search
     * @return
     */
    IPage<LoginLog> findLoginLogs(QuerySearch search);

    /**
     * 保存登录日志
     * @param request
     */
    void saveLoginLog(HttpServletRequest request, CurrentUser user);

    /**
     * 删除登录日志
     *
     * @param ids 日志 id集合
     */
    void deleteLoginLogs(String[] ids);

    /**
     * 获取系统总访问次数
     *
     * @return Long
     */
    Long findTotalVisitCount();

    /**
     * 获取系统今日访问次数
     *
     * @return Long
     */
    Long findTodayVisitCount();

    /**
     * 获取系统今日访问 IP数
     *
     * @return Long
     */
    Long findTodayIp();

    /**
     * 获取系统近十天来的访问记录
     *
     * @param user 用户
     * @return 系统近十天来的访问记录
     */
    List<Map<String, Object>> findLastTenDaysVisitCount(SysUser user);

    /**
     * 通过用户名获取用户最近7次登录日志
     *
     * @param username 用户名
     * @return 登录日志集合
     */
    List<LoginLog> findUserLastSevenLoginLogs(String username);
}
