package cc.vihackerframework.system.mapper;

import cc.vihackerframework.core.entity.system.SysUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by Ranger on 2022/02/24
 */
public interface UserMapper extends BaseMapper<SysUser> {

    /**
     * 查找用户详细信息
     *
     * @param page 分页对象
     * @param user 用户对象，用于传递查询条件
     * @param <T>  type
     * @return Ipage
     */
    <T> IPage<SysUser> findUserDetailPage(Page<T> page, @Param("user") SysUser user);

    /**
     * 查找用户详细信息
     *
     * @param user 用户对象，用于传递查询条件
     * @return List<User>
     */
    List<SysUser> findUserDetail(@Param("user") SysUser user);

}
