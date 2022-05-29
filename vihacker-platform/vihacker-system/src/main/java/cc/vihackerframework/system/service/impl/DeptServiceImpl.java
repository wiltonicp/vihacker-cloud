package cc.vihackerframework.system.service.impl;

import cc.vihackerframework.core.context.UserContext;
import cc.vihackerframework.core.datasource.entity.QuerySearch;
import cc.vihackerframework.core.entity.*;
import cc.vihackerframework.core.entity.system.Dept;
import cc.vihackerframework.core.tree.TreeUtil;
import cc.vihackerframework.system.mapper.DeptMapper;
import cc.vihackerframework.system.service.IUserDataPermissionService;
import cc.vihackerframework.system.service.IDeptService;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

/**
 * Created by Ranger on 2022/02/24
 */
@Slf4j
@Service("deptService")
@RequiredArgsConstructor
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class DeptServiceImpl extends ServiceImpl<DeptMapper, Dept> implements IDeptService {

    private final IUserDataPermissionService userDataPermissionService;

    @Override
    public List<? extends Tree<?>> findDepts(QuerySearch querySearch) {
        List<Tree<Dept>> trees = new ArrayList<>();
        List<Dept> depts = new LambdaQueryChainWrapper<>(baseMapper)
                .between(StringUtils.isNotBlank(querySearch.getStartDate()),
                        Dept::getCreatedTime, querySearch.getStartDate(), querySearch.getEndDate())
                .like(StringUtils.isNotBlank(querySearch.getKeyword()), Dept::getName, querySearch.getKeyword())
                .orderByAsc(Dept::getOrderNum)
                .list();
        buildTrees(trees,depts);
        return TreeUtil.buildTree(trees);
    }

    @Override
    public List<Dept> export(QuerySearch querySearch) {
        List<Dept> deptList = new LambdaQueryChainWrapper<>(baseMapper)
                .between(StringUtils.isNotBlank(querySearch.getStartDate()),
                        Dept::getCreatedTime, querySearch.getStartDate(), querySearch.getEndDate())
                .like(StringUtils.isNotBlank(querySearch.getKeyword()), Dept::getName, querySearch.getKeyword())
                .orderByAsc(Dept::getOrderNum)
                .list();
        return deptList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean createDept(Dept dept) {
        if (dept.getParentId() == null) {
            dept.setParentId(Dept.TOP_DEPT_ID);
        }
        dept.setTenantId(UserContext.current().getTenantId());
        dept.setCreatedTime(LocalDateTime.now());
        return this.save(dept);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateDept(Dept dept) {
        if (dept.getParentId() == null) {
            dept.setParentId(Dept.TOP_DEPT_ID);
        }
        dept.setModifyTime(LocalDateTime.now());
        return this.updateById(dept);
    }

    private void buildTrees(List<Tree<Dept>> trees, List<Dept> depts) {
        depts.forEach(dept -> {
            DeptTree tree = new DeptTree();
            BeanUtils.copyProperties(dept,tree);
            tree.setId(dept.getId().toString());
            tree.setStatus(dept.getStatus());
            tree.setParentId(dept.getParentId().toString());
            tree.setName(dept.getName());
            tree.setOrderNum(dept.getOrderNum());
            tree.setCreateTime(dept.getCreatedTime());
            trees.add(tree);
        });
    }
}
