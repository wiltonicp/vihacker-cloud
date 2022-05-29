package cc.vihackerframework.system.service;

import cc.vihackerframework.core.datasource.entity.QuerySearch;
import cc.vihackerframework.core.entity.system.SysUser;
import cc.vihackerframework.core.exception.ViHackerException;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Ranger on 2022/02/24
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
     * @param querySearch
     * @param deptId
     * @return
     */
    IPage<SysUser> findUserDetailList(QuerySearch querySearch, String deptId);

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
    boolean createUser(SysUser user);

    /**
     * 修改用户
     *
     * @param user user
     */
    boolean updateUser(SysUser user);

    /**
     * 删除用户
     *
     * @param userIds 用户 id数组
     */
    boolean deleteUsers(String userIds);

    /**
     * 更新个人信息
     *
     * @param user 个人信息
     * @throws ViHackerException 异常
     */
    void updateProfile(HttpServletRequest request,SysUser user) throws ViHackerException;

    /**
     * 更新用户头像
     *
     * @param avatar 用户头像
     */
    void updateAvatar(HttpServletRequest request,String avatar);

    /**
     * 更新用户密码
     *
     * @param password 新密码
     */
    void updatePassword(HttpServletRequest request,String password);

    /**
     * 重置密码
     * @param sysUser
     * @return
     */
    boolean resetPassword(SysUser sysUser);

}