package cc.vihackerframework.system.service;

import cc.vihackerframework.core.entity.QueryRequest;
import cc.vihackerframework.core.entity.system.SysUser;
import cc.vihackerframework.core.exception.ViHackerException;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;


/**
 * @author jiangshanchen
 * @title: FeignConfig
 * @projectName vihacker-cloud-master
 * @description: TODO
 * @date 2022/3/1下午5:26
 */
public interface IUserService extends IService<SysUser> {

    /**
     * 通过用户名查找用户
     *
     * @param username 用户名
     * @return 用户
     */
    SysUser findByName(String username);

    /**
     * 查找用户详细信息
     *
     * @param request request
     * @param user    用户对象，用于传递查询条件
     * @return IPage
     */
    IPage<SysUser> findUserDetailList(SysUser user, QueryRequest request);

    /**
     * 通过用户名查找用户详细信息
     *
     * @param username 用户名
     * @return 用户信息
     */
    SysUser findUserDetail(String username);

    /**
     * 更新用户登录时间
     *
     * @param username username
     */
    void updateLoginTime(String username);

    /**
     * 新增用户
     *
     * @param user user
     */
    void createUser(SysUser user);

    /**
     * 修改用户
     *
     * @param user user
     */
    void updateUser(SysUser user);

    /**
     * 删除用户
     *
     * @param userIds 用户 id数组
     */
    void deleteUsers(String[] userIds);

    /**
     * 更新个人信息
     *
     * @param user 个人信息
     * @throws ViHackerException 异常
     */
    void updateProfile(SysUser user) throws ViHackerException;

    /**
     * 更新用户头像
     *
     * @param avatar 用户头像
     */
    void updateAvatar(String avatar);

    /**
     * 更新用户密码
     *
     * @param password 新密码
     */
    void updatePassword(String password);

    /**
     * 重置密码
     *
     * @param usernames 用户集合
     */
    void resetPassword(String[] usernames);

}