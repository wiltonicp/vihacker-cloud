package cc.vihackerframework.schedule.mapper;

import cc.vihackerframework.schedule.entity.pojos.Taskinfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * @author jiangshanchen
 * @title: TaskinfoMapper
 * @projectName vihacker-cloud-master
 * @description: TODO
 * @date 2022/3/2上午9:36
 */
@Mapper
public interface TaskinfoMapper extends BaseMapper<Taskinfo> {

    public List<Taskinfo> queryFutureTime(@Param("taskType")int type, @Param("priority")int priority, @Param("future")Date future);
}
