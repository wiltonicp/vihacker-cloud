package cc.vihackerframework.system.controller;

import cc.vihackerframework.core.api.ViHackerApiResult;
import cc.vihackerframework.core.entity.QueryRequest;
import cc.vihackerframework.core.entity.system.Dept;
import cc.vihackerframework.core.util.ExcelUtil;
import cc.vihackerframework.core.util.StringPool;
import cc.vihackerframework.log.starter.annotation.LogEndpoint;
import cc.vihackerframework.system.service.IDeptService;
import com.sun.javafx.binding.StringConstant;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.actuate.endpoint.web.annotation.ControllerEndpoint;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Map;

/**
 * 部门相关
 * Created by Ranger on 2022/2/24
 */
@Slf4j
@Validated
@RestController
@RequestMapping("dept")
@RequiredArgsConstructor
public class DeptController {

    private final IDeptService deptService;

    @GetMapping
    @ApiOperation(value = "查询部门")
    public ViHackerApiResult deptList(QueryRequest request, Dept dept) {
        Map<String, Object> depts = this.deptService.findDepts(request, dept);
        return ViHackerApiResult.data(depts);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('dept:add')")
    @ApiOperation(value = "新增部门")
    @LogEndpoint(value = "新增部门", exception = "新增部门失败")
    public void addDept(@Valid Dept dept) {
        this.deptService.createDept(dept);
    }

    @DeleteMapping("/{deptIds}")
    @PreAuthorize("hasAuthority('dept:delete')")
    @ApiOperation(value = "删除部门")
    @LogEndpoint(value = "删除部门", exception = "删除部门失败")
    public void deleteDepts(@NotBlank(message = "{required}") @PathVariable String deptIds) {
        String[] ids = deptIds.split(StringPool.COMMA);
        this.deptService.deleteDepts(ids);
    }

    @PutMapping
    @PreAuthorize("hasAuthority('dept:update')")
    @ApiOperation(value = "修改部门")
    @LogEndpoint(value = "修改部门", exception = "修改部门失败")
    public void updateDept(@Valid Dept dept) {
        this.deptService.updateDept(dept);
    }

    @PostMapping("excel")
    @PreAuthorize("hasAuthority('dept:export')")
    @ApiOperation(value = "导出部门数据")
    @LogEndpoint(value = "导出部门数据", exception = "导出Excel失败")
    public void export(Dept dept, QueryRequest request, HttpServletResponse response) {
        List<Dept> depts = this.deptService.findDepts(dept, request);
        //使用工具类导出excel
        ExcelUtil.exportExcel(depts, null, "部门", Dept.class, "depart", response);
    }
}
