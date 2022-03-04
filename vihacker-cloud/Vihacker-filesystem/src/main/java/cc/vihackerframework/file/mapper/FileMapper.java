package cc.vihackerframework.file.mapper;


import cc.vihackerframework.file.entity.FileEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author jiangshanchen
 * @title: FileMapper
 * @projectName vihacker-cloud-master
 * @description: TODO
 * @date 2022/3/1下午3:36
 */

@Repository
@Mapper
public interface FileMapper  extends BaseMapper<FileEntity> {


    /**
     * 根据 id 查询
     * @param id
     * @return
     */
    @Select("select * from f_file where id = #{id}")
    FileEntity getByFileId(@Param("id") long id);

    /**
     * 查询已经消耗空间
     * @param userId
     * @return
     */
    @Select("SELECT IFNULL( SUM( file_size ), 0 ) AS total FROM f_file WHERE created_by = #{userId}")
    long getUserFreeStorageSize(@Param("userId") long userId);

    /**
     * 查询已经被删除的列表
     * @param createdBy
     * @return
     */
    List<FileEntity> deletedList(long createdBy);

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
