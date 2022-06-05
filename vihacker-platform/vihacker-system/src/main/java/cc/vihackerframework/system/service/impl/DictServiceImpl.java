package cc.vihackerframework.system.service.impl;

import cc.vihackerframework.core.datasource.entity.QuerySearch;
import cc.vihackerframework.core.datasource.util.PageUtil;
import cc.vihackerframework.core.entity.system.SysDict;
import cc.vihackerframework.core.util.StringUtil;
import cc.vihackerframework.system.mapper.DictMapper;
import cc.vihackerframework.system.service.IDictService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * Created by Ranger on 2022/6/5.
 */
@Service("dictService")
public class DictServiceImpl extends ServiceImpl<DictMapper, SysDict> implements IDictService {

    @Override
    public IPage<SysDict> listPage(QuerySearch search) {
        return new LambdaQueryChainWrapper<>(baseMapper)
                .between(StringUtil.isNotBlank(search.getStartDate()),SysDict::getCreatedTime,search.getStartDate(),search.getEndDate())
                .or().like(StringUtil.isNotBlank(search.getKeyword()),SysDict::getId, search.getKeyword())
                .or().like(StringUtil.isNotBlank(search.getKeyword()),SysDict::getCode, search.getKeyword())
                .eq(SysDict::getParentId,0)
                .page(PageUtil.getPage(search));
    }


}
