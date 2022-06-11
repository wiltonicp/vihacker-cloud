package cc.vihackerframework.core.entity.system;

import cc.vihackerframework.core.entity.ViHackerEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Ranger
 * @date: 2021/3/6 17:01
 * @email: wilton.icp@gmail.com
 */
@Data
@TableName("t_role")
@ApiModel(value = "Role对象", description = "角色表")
public class Role extends ViHackerEntity<Long> implements Serializable {

    private static final long serialVersionUID = -1714476694755654924L;

    /**
     * 角色名称
     */
    @TableField(value = "ROLE_NAME")
    @NotBlank(message = "角色名称不能为空")
    @Size(max = 10, message = "角色名称长度不能超过10个字符")
    @ApiModelProperty("角色名称")
    private String roleName;

    @TableField(value = "ROLE_CODE")
    @ApiModelProperty("角色编码")
    private String roleCode;

    /**
     * 排序
     */
    @TableField(value = "ORDER_NUM")
    @ApiModelProperty("排序")
    private int orderNum;

    /**
     * 状态 0 启用 1 停用
     */
    @TableField(value = "STATUS")
    @ApiModelProperty("状态 0 启用 1 停用")
    private String status;

    /**
     * 描述
     */
    @TableField(value = "REMARK")
    @Size(max = 50, message = "角色描述长度不能超过50个字符")
    @ApiModelProperty("描述")
    private String remark;

    /**
     * 租户id
     */
    @TableField(value = "TENANT_ID")
    @ApiModelProperty("租户id")
    private String tenantId;

    /**
     * 菜单
     */
    private transient List<String> menu;

}