package cc.vihackerframework.system.service;

import cc.vihackerframework.core.datasource.entity.Search;
import cc.vihackerframework.core.entity.system.SysLog;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * Created by Ranger on 2022/2/20
 */
public interface ISysLogService extends IService<SysLog> {

    /**
     * 日志分页列表
     *
     * @param search 搜索和分页对象
     * @return 日志分页列表
     */
    IPage<SysLog> listPage(Search search);
}
