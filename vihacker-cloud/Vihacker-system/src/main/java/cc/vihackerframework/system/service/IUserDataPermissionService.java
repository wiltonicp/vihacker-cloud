package cc.vihackerframework.system.service;


import cc.vihackerframework.core.entity.system.UserDataPermission;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author jiangshanchen
 * @title: FeignConfig
 * @projectName vihacker-cloud-master
 * @description: TODO
 * @date 2022/3/1下午5:26
 */
public interface IUserDataPermissionService extends IService<UserDataPermission> {

    /**
     * 通过部门ID删除关联关系
     *
     * @param deptIds 部门id
     */
    void deleteByDeptIds(List<String> deptIds);

    /**
     * 通过用户ID删除关联关系
     *
     * @param userIds 用户id
     */
    void deleteByUserIds(String[] userIds);

    /**
     * 通过用户ID查找关联关系
     *
     * @param userId 用户id
     * @return 关联关系
     */
    String findByUserId(String userId);

}
