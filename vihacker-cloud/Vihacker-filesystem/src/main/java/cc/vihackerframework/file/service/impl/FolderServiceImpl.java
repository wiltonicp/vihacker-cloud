package cc.vihackerframework.file.service.impl;

import cc.vihackerframework.core.auth.util.SecurityUtil;

import cc.vihackerframework.file.entity.FolderEntity;
import cc.vihackerframework.file.mapper.FolderMapper;
import cc.vihackerframework.file.service.IFolderService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author jiangshanchen
 * @title: FolderServiceImpl
 * @projectName vihacker-cloud-master
 * @description: TODO
 * @date 2022/3/1下午3:42
 */
@Service
@RequiredArgsConstructor
public class FolderServiceImpl  extends ServiceImpl<FolderMapper, FolderEntity> implements IFolderService {


    @Override
    public List<FolderEntity> list(Long parentId) {
        return null;
    }

    @Override
    public FolderEntity findByUserId(Long userId) {
        return this.getOne(new QueryWrapper<FolderEntity>()
                .eq("created_by", userId)
                .eq("parent_id", 0L)
        );
    }

    @Override
    public List<FolderEntity> findParentById(Long id) {
        return this.baseMapper.findParentById(id);
    }

    @Override
    public List<FolderEntity> deletedList() {
        return this.baseMapper.deletedList(SecurityUtil.getLoginUser().getUserId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean restoreById(long id) {
        return this.baseMapper.restoreById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deletePermanentlyById(long id) {
        return this.baseMapper.deletePermanentlyById(id);
    }
}
