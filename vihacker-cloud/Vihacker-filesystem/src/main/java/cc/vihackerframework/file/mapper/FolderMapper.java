package cc.vihackerframework.file.mapper;


import cc.vihackerframework.file.entity.FolderEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author jiangshanchen
 * @title: FolderMapper
 * @projectName vihacker-cloud-master
 * @description: TODO
 * @date 2022/3/1下午3:36
 */

@Repository
@Mapper
public interface FolderMapper  extends BaseMapper<FolderEntity> {


    /**
     * 根据子id查询所有父节点
     * @param id
     * @return
     */
    //@SqlParser(filter = true)
    List<FolderEntity> findParentById(Long id);

    /**
     * 查询已经被删除的列表
     * @param createdBy
     * @return
     */
    List<FolderEntity> deletedList(long createdBy);

    /**
     * 还原文件
     * @param id
     * @return
     */
    boolean restoreById(long id);

    /**
     * 永久删除
     * @param id
     * @return
     */
    boolean deletePermanentlyById(long id);
}
