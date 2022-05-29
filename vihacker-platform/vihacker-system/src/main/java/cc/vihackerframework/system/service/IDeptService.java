package cc.vihackerframework.system.service;

import cc.vihackerframework.core.datasource.entity.Search;
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
     * @param search
     * @return
     */
    List<? extends Tree<?>> findDepts(Search search);

    /**
     * 获取部门列表
     * @param search
     * @return
     */
    List<Dept> export(Search search);

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
