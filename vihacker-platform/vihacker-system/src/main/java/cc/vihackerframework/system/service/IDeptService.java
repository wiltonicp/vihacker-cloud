package cc.vihackerframework.system.service;

import cc.vihackerframework.core.datasource.entity.QuerySearch;
import cc.vihackerframework.core.entity.Tree;
import cc.vihackerframework.core.entity.system.Dept;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * Created by Ranger on 2022/02/24
 */
public interface IDeptService extends IService<Dept> {

    /**
     * 部门列表
     * @param querySearch
     * @return
     */
    List<? extends Tree<?>> findDepts(QuerySearch querySearch);

    /**
     * 获取部门列表
     * @param querySearch
     * @return
     */
    List<Dept> export(QuerySearch querySearch);

    /**
     * 创建部门
     *
     * @param dept dept
     */
    boolean createDept(Dept dept);

    /**
     * 更新部门
     *
     * @param dept dept
     */
    boolean updateDept(Dept dept);
}
