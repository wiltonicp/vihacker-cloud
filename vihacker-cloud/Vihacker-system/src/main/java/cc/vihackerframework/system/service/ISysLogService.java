package cc.vihackerframework.system.service;


import cc.vihackerframework.core.datasource.starter.entity.Search;
import cc.vihackerframework.core.entity.system.SysLog;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;


/**
 * @author jiangshanchen
 * @title: FeignConfig
 * @projectName vihacker-cloud-master
 * @description: TODO
 * @date 2022/3/1下午5:26
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
