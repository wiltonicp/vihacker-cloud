package cc.vihackerframework.file.service.impl;

import cc.vihackerframework.core.auth.util.SecurityUtil;
import cc.vihackerframework.core.entity.system.SysUser;

import cc.vihackerframework.file.entity.FileEntity;
import cc.vihackerframework.file.feign.UserFeign;
import cc.vihackerframework.file.mapper.FileMapper;
import cc.vihackerframework.file.service.IFileService;


import cc.vihackerframework.file.starter.util.FileUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author jiangshanchen
 * @title: FileServiceImpl
 * @projectName vihacker-cloud-master
 * @description: TODO
 * @date 2022/3/1下午3:30
 */
@Service
@RequiredArgsConstructor
public class FileServiceImpl  extends ServiceImpl<FileMapper, FileEntity> implements IFileService {

    public final UserFeign userFeign;



    @Override
    public List<FileEntity> listPage(Long folderId) {
        List<FileEntity> list = this.list(new QueryWrapper<FileEntity>()
                .eq(folderId != null, "folder_id", folderId)
                .eq("deleted", 0)
        );
        list.forEach(file ->{
            SysUser user = userFeign.getUserById(file.getCreatedBy());
            file.setCreatedByName(user.getUsername());
            file.setFileSizeVal(FileUtil.getSize(file.getFileSize()));
        });
        return list;
    }

    @Override
    public FileEntity getByFileMd5(String fileMd5) {
        return this.getOne(new QueryWrapper<FileEntity>()
                .eq(fileMd5 != null, "file_md5", fileMd5)
                .eq("deleted",0)
        );
    }

    @Override
    public FileEntity getByFileId(long id) {
        return this.baseMapper.getByFileId(id);
    }

    @Override
    public List<FileEntity> deletedList() {
        List<FileEntity> list = this.baseMapper.deletedList(SecurityUtil.getLoginUser().getUserId());
        list.forEach(file ->{
            file.setFileSizeVal(FileUtil.getSize(file.getFileSize()));
        });
        return list;
    }

    @Override
    public boolean restoreById(long id) {
        return false;
    }

    @Override
    public boolean deletePermanentlyById(long id) {
        return false;
    }

}
