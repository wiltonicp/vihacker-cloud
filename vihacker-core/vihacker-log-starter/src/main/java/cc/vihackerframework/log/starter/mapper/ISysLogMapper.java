package cc.vihackerframework.log.starter.mapper;

import cc.vihackerframework.core.entity.system.SysLog;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * Created by Ranger on 2022/2/20
 */
@Mapper
public interface ISysLogMapper extends BaseMapper<SysLog> {
}
