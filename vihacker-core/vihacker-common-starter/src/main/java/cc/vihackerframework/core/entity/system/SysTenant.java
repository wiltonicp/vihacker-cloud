package cc.vihackerframework.core.entity.system;

import cc.vihackerframework.core.entity.ViHackerEntity;
import cc.vihackerframework.core.entity.enums.TenantStatusEnum;
import cc.vihackerframework.core.entity.enums.TenantTypeEnum;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;

import static com.baomidou.mybatisplus.annotation.SqlCondition.LIKE;

/**
 * 租户表
 * Created by Ranger on 2022/6/11.
 */
@Data
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("t_tenant")
@ApiModel(value = "Tenant", description = "企业")
public class SysTenant extends ViHackerEntity<Long> implements Serializable {

    private static final long serialVersionUID = 2688500458790276737L;

    /**
     * 企业编码
     */
    @ApiModelProperty(value = "企业编码")
    @NotEmpty(message = "企业编码不能为空")
    @Size(max = 20, message = "企业编码长度不能超过20")
    @TableField(value = "code", condition = LIKE)
    private String code;

    /**
     * 企业名称
     */
    @ApiModelProperty(value = "企业名称")
    @Size(max = 255, message = "企业名称长度不能超过255")
    @TableField(value = "name", condition = LIKE)
    private String name;

    /**
     * 类型
     * #{CREATE:创建;REGISTER:注册}
     */
    @ApiModelProperty(value = "类型")
    @TableField("type")
    private TenantTypeEnum type;

    /**
     * 状态
     * #{NORMAL:正常;WAIT_INIT:待初始化;FORBIDDEN:禁用;WAITING:待审核;REFUSE:拒绝;DELETE:已删除}
     */
    @ApiModelProperty(value = "状态")
    @TableField("status")
    private TenantStatusEnum status;

    /**
     * 内置
     */
    @ApiModelProperty(value = "内置")
    @TableField("readonly")
    private Boolean readonly;

    /**
     * 责任人
     */
    @ApiModelProperty(value = "责任人")
    @Size(max = 50, message = "责任人长度不能超过50")
    @TableField(value = "director", condition = LIKE)
    private String director;

    /**
     * 有效期
     * 为空表示永久
     */
    @ApiModelProperty(value = "有效期")
    @TableField(value = "exp_time", updateStrategy = FieldStrategy.IGNORED)
    private LocalDateTime expTime;

    /**
     * logo地址
     */
    @ApiModelProperty(value = "logo地址")
    @Size(max = 255, message = "logo地址长度不能超过255")
    @TableField(value = "logo_url", condition = LIKE)
    private String logoUrl;

    /**
     * 企业简介
     */
    @ApiModelProperty(value = "企业简介")
    @Size(max = 255, message = "企业简介长度不能超过255")
    @TableField(value = "description", condition = LIKE)
    private String description;
}
