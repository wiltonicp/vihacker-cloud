package cc.vihackerframework.core.entity.system;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author Ranger
 * @email wilton.icp@gmail.com
 * @since 2021/3/10
 */
@Data
@TableName("t_dept")
public class Dept implements Serializable {

    public static final Long TOP_DEPT_ID = 0L;
    private static final long serialVersionUID = -7790334862410409053L;
    @TableId(value = "DEPT_ID", type = IdType.AUTO)
    private Long deptId;

    @TableField(value = "PARENT_ID")
    private Long parentId;

    @NotBlank(message = "部门名称不能为空")
    @Size(max = 20, message = "部门名称长度不能超过20个字符")
    @Excel(name = "部门名称", orderNum = "1", height = 20, width = 30, isImportField = "true_st")
    private String deptName;

    @TableField(value = "ORDER_NUM")
    private Integer orderNum;

    @TableField(value = "CREATED_TIME")
    @Excel(name = "创建时间", orderNum = "2", height = 20, width = 30, isImportField = "true_st")
    private LocalDateTime createdTime;

    @TableField(value = "MODIFY_TIME")
    @Excel(name = "修改时间", orderNum = "3", height = 20, width = 30, isImportField = "true_st")
    private LocalDateTime modifyTime;

    private transient String createTimeFrom;

    private transient String createTimeTo;

}