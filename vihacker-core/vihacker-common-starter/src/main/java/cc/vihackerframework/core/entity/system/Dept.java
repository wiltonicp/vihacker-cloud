package cc.vihackerframework.core.entity.system;

import cc.vihackerframework.core.entity.ViHackerEntity;
import cn.afterturn.easypoi.excel.annotation.Excel;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
@ApiModel(value = "Depart对象", description = "组织机构表")
public class Dept extends ViHackerEntity implements Serializable {
    private static final long serialVersionUID = -7790334862410409053L;
    public static final Long TOP_DEPT_ID = 0L;


    @TableId(value = "ID", type = IdType.AUTO)
    private Long id;

    @TableField(value = "PARENT_ID")
    private Long parentId;

    @NotBlank(message = "部门名称不能为空")
    @Size(max = 20, message = "部门名称长度不能超过20个字符")
    @Excel(name = "部门名称", orderNum = "1", height = 20, width = 30, isImportField = "true_st")
    private String name;

    /**
     * 状态 0启用 1停用
     */
    @TableField("STATUS")
    private String status;

    @TableField(value = "ORDER_NUM")
    private Integer orderNum;

    /**
     * 租户ID
     */
    @TableField(value = "TENANT_ID")
    @ApiModelProperty(value = "租户ID")
    private String tenantId;
}