package cc.vihackerframework.system.service;

import cc.vihackerframework.core.datasource.entity.QuerySearch;
import cc.vihackerframework.core.entity.system.SysDict;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * Created by Ranger on 2022/6/5.
 */
public interface IDictService extends IService<SysDict> {

    /**
     * 分页查询
     * @param search
     * @return
     */
    IPage<SysDict> listPage(QuerySearch search);
}
