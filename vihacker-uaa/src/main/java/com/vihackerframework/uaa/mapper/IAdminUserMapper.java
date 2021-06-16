package com.vihackerframework.uaa.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.vihackerframework.core.entity.system.SysUser;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @Description
 * @Author: Ranger
 * @Date: 2021/1/19 10:36
 * @Email: wilton.icp@gmail.com
 */
@Mapper
@Repository
public interface IAdminUserMapper extends BaseMapper<SysUser> {

    /**
     * 获取用户
     *
     * @param username 用户名
     * @return 用户
     */
    SysUser findByName(String username);
}
