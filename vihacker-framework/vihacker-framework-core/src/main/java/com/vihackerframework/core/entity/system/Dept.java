package com.vihackerframework.core.entity.system;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wuwenze.poi.annotation.Excel;
import com.wuwenze.poi.annotation.ExcelField;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author Ranger
 * @since 2021/3/10
 * @email wilton.icp@gmail.com
 */
@Data
@TableName("t_dept")
@Excel("部门信息表")
public class Dept implements Serializable {

    public static final Long TOP_DEPT_ID = 0L;
    private static final long serialVersionUID = -7790334862410409053L;
    @TableId(value = "DEPT_ID", type = IdType.AUTO)
    private Long deptId;

    @TableField(value = "PARENT_ID")
    private Long parentId;

    @NotBlank(message = "部门名称不能为空")
    @Size(max = 20, message = "部门名称长度不能超过20个字符")
    @ExcelField(value = "部门名称")
    private String deptName;

    @TableField(value = "ORDER_NUM")
    private Integer orderNum;

    @TableField(value = "CREATED_TIME")
    @ExcelField(value = "创建时间")
    private LocalDateTime createdTime;

    @TableField(value = "MODIFY_TIME")
    @ExcelField(value = "修改时间")
    private LocalDateTime modifyTime;

    private transient String createTimeFrom;

    private transient String createTimeTo;

}