package cc.vihackerframework.system.controller;

import cc.vihackerframework.core.api.ViHackerApiResult;
import cc.vihackerframework.core.datasource.entity.QuerySearch;
import cc.vihackerframework.core.entity.system.SysDict;
import cc.vihackerframework.core.annotation.log.LogEndpoint;
import cc.vihackerframework.core.util.CollectionUtil;
import cc.vihackerframework.system.service.IDictService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

/**
 * 字典管理
 * Created by Ranger on 2022/6/5.
 */
@Slf4j
@RestController
@Api(tags = "字典管理")
@RequestMapping("dict")
@RequiredArgsConstructor
public class DictController {

    private final IDictService dictService;

    @GetMapping
    @PreAuthorize("hasAuthority('dict:view')")
    @ApiOperation(value = "字典分页", notes = "字典分页")
    @LogEndpoint(value = "查询字典分页", exception = "字典分页查询失败")
    public ViHackerApiResult<?> page(QuerySearch search) {
        return ViHackerApiResult.data(dictService.listPage(search));
    }

    @GetMapping("list-value")
    @PreAuthorize("hasAuthority('dict:view')")
    @LogEndpoint(value = "字典项列表", exception = "字典项列表异常")
    @ApiOperation(value = "字典项列表", notes = "字典项列表")
    public ViHackerApiResult<?> listValue(@RequestParam String code) {
        return ViHackerApiResult.data(
                new LambdaQueryChainWrapper<>(dictService.getBaseMapper())
                .eq(SysDict::getCode, code)
                .ne(SysDict::getParentId, 0)
                .orderByAsc(SysDict::getOrderNum).list());
    }

    @PostMapping
    @PreAuthorize("hasAuthority('dict:add')")
    @LogEndpoint(value = "字典新增", exception = "字典新增请求异常")
    @ApiOperation(value = "字典新增", notes = "字典新增,支持新增")
    public ViHackerApiResult addDict(@Valid @RequestBody SysDict dict) {
        return ViHackerApiResult.success(dictService.saveOrUpdate(dict));
    }

    @PutMapping
    @PreAuthorize("hasAuthority('dict:update')")
    @LogEndpoint(value = "字典修改", exception = "字典修改请求异常")
    @ApiOperation(value = "字典修改", notes = "字典修改,支持修改")
    public ViHackerApiResult updateDict(@Valid @RequestBody SysDict dict) {
        return ViHackerApiResult.success(dictService.saveOrUpdate(dict));
    }

    @DeleteMapping("/{ids}")
    @PreAuthorize("hasAuthority('dict:delete')")
    @ApiOperation(value = "删除字典")
    @LogEndpoint(value = "删除字典", exception = "删除字典失败")
    public ViHackerApiResult deleteDicts(@NotBlank(message = "{required}") @PathVariable String ids) {
        return ViHackerApiResult.success(dictService.removeByIds(CollectionUtil.stringToCollection(ids)));
    }

}