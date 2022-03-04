package cc.vihackerframework.system.service;


import cc.vihackerframework.core.entity.system.UserRole;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author jiangshanchen
 * @title: FeignConfig
 * @projectName vihacker-cloud-master
 * @description: TODO
 * @date 2022/3/1下午5:26
 */
public interface IUserRoleService extends IService<UserRole> {

    /**
     * 删除角色用户管理关系
     *
     * @param roleIds 角色id数组
     */
    void deleteUserRolesByRoleId(String[] roleIds);

    /**
     * 删除角色用户管理关系
     *
     * @param userIds 用户id数组
     */
    void deleteUserRolesByUserId(String[] userIds);

    /**
     * 通过角色id查找对应的用户id
     *
     * @param roleIds 角色id
     * @return 用户id集
     */
    List<String> findUserIdsByRoleId(String[] roleIds);
}
