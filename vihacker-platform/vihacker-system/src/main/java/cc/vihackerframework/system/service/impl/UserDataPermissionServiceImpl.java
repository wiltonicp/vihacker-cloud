package cc.vihackerframework.system.service.impl;

import cc.vihackerframework.core.entity.system.UserDataPermission;
import cc.vihackerframework.core.util.StringPool;
import cc.vihackerframework.system.mapper.UserDataPermissionMapper;
import cc.vihackerframework.system.service.IUserDataPermissionService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sun.javafx.binding.StringConstant;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Ranger on 2022/02/24
 */
@Service("userDataPermissionService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class UserDataPermissionServiceImpl extends ServiceImpl<UserDataPermissionMapper, UserDataPermission> implements IUserDataPermissionService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByDeptIds(List<String> deptIds) {
        this.baseMapper.delete(new LambdaQueryWrapper<UserDataPermission>().in(UserDataPermission::getDeptId, deptIds));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByUserIds(String[] userIds) {
        List<String> list = Arrays.asList(userIds);
        this.baseMapper.delete(new LambdaQueryWrapper<UserDataPermission>().in(UserDataPermission::getUserId, list));
    }

    @Override
    public String findByUserId(String userId) {
        LambdaQueryWrapper<UserDataPermission> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserDataPermission::getUserId, userId);
        return this.baseMapper.selectList(wrapper).stream().map(permission -> String.valueOf(permission.getDeptId())).collect(Collectors.joining(StringPool.COMMA));
    }
}
