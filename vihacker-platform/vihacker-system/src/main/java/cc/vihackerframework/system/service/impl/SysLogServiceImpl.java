package cc.vihackerframework.system.service.impl;

import cc.vihackerframework.core.datasource.starter.util.PageUtil;
import cc.vihackerframework.core.entity.system.SysLog;
import cc.vihackerframework.core.util.StringUtil;
import cc.vihackerframework.system.mapper.ISysLogMapper;
import cc.vihackerframework.system.service.ISysLogService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cc.vihackerframework.core.datasource.starter.entity.Search;
import org.springframework.stereotype.Service;

/**
 * Created by Ranger on 2022/2/20
 */
@Service
public class SysLogServiceImpl extends ServiceImpl<ISysLogMapper, SysLog> implements ISysLogService {

    @Override
    public IPage<SysLog> listPage(Search search) {
        boolean isKeyword = StringUtil.isNotBlank(search.getKeyword());
        LambdaQueryWrapper<SysLog> queryWrapper = Wrappers.lambdaQuery();
        // 查询开始日期和结束日期
        queryWrapper.between(StringUtil.isNotBlank(search.getStartDate()), SysLog::getCreatedTime, search.getStartDate(), search.getEndDate());
        // 关键词查询
        queryWrapper.like(isKeyword, SysLog::getOperation, search.getKeyword()).or(isKeyword).like(isKeyword, SysLog::getTraceId, search.getKeyword());
        //　字段排序
        queryWrapper.orderByDesc(SysLog::getCreatedTime);
        return this.baseMapper.selectPage(PageUtil.getPage(search), queryWrapper);
    }
}
