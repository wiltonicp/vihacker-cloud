package cc.vihackerframework.system.controller;

import cc.vihackerframework.core.api.ViHackerApiResult;
import cc.vihackerframework.core.datasource.entity.QuerySearch;
import cc.vihackerframework.core.entity.system.Dept;
import cc.vihackerframework.core.util.CollectionUtil;
import cc.vihackerframework.core.util.ExcelUtil;
import cc.vihackerframework.core.log.annotation.LogEndpoint;
import cc.vihackerframework.system.service.IDeptService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * 部门相关
 * Created by Ranger on 2022/2/24
 */
@Slf4j
@Validated
@RestController
@Api(tags = "部门管理")
@RequestMapping("dept")
@RequiredArgsConstructor
public class DeptController {

    private final IDeptService deptService;

    @GetMapping
    @PreAuthorize("hasAuthority('dept:view')")
    @ApiOperation(value = "查询部门列表", notes = "部门列表")
    @LogEndpoint(value = "查询部门列表", exception = "查询部门列表失败")
    public ViHackerApiResult deptList(QuerySearch querySearch) {
        return ViHackerApiResult.data(this.deptService.findDepts(querySearch));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('dept:add')")
    @ApiOperation(value = "新增部门")
    @LogEndpoint(value = "新增部门", exception = "新增部门失败")
    public ViHackerApiResult addDept(@Valid @RequestBody Dept dept) {
        return ViHackerApiResult.success(this.deptService.createDept(dept));
    }

    @DeleteMapping("/{deptIds}")
    @PreAuthorize("hasAuthority('dept:delete')")
    @ApiOperation(value = "删除部门")
    @LogEndpoint(value = "删除部门", exception = "删除部门失败")
    public ViHackerApiResult deleteDepts(@NotBlank(message = "{required}") @PathVariable String deptIds) {
        return ViHackerApiResult.success(this.deptService.removeByIds(CollectionUtil.stringToCollection(deptIds)));
    }

    @PutMapping
    @PreAuthorize("hasAuthority('dept:update')")
    @ApiOperation(value = "修改部门")
    @LogEndpoint(value = "修改部门", exception = "修改部门失败")
    public ViHackerApiResult updateDept(@Valid @RequestBody Dept dept) {
        return ViHackerApiResult.success(this.deptService.updateDept(dept));
    }

    @PostMapping("excel")
    @PreAuthorize("hasAuthority('dept:export')")
    @ApiOperation(value = "导出部门数据")
    @LogEndpoint(value = "导出部门数据", exception = "导出Excel失败")
    public void export(QuerySearch querySearch, HttpServletResponse response) {
        List<Dept> depts = this.deptService.export(querySearch);
        //使用工具类导出excel
        ExcelUtil.exportExcel(depts, null, "部门", Dept.class, "depart", response);
    }
}
