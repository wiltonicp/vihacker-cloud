package com.vihackerframework.uaa.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.vihackerframework.core.entity.system.Menu;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Description
 * @Author: Ranger
 * @Date: 2021/1/25 16:32
 * @Email: wilton.icp@gmail.com
 */
@Mapper
@Repository
public interface IAdminMenuMapper extends BaseMapper<Menu> {
    /**
     * 通过用户名查找用户权限集合
     *
     * @param username
     * @return
     */
    List<Menu> findUserPermission(String username);
}
