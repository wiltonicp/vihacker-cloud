package cc.vihackerframework.core.entity.system;

import cc.vihackerframework.core.entity.ViHackerEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * Created by Ranger on 2022/6/5.
 */
@Data
@TableName("t_dict")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "Dict对象", description = "字典表")
public class SysDict extends ViHackerEntity implements Serializable {

    private static final long serialVersionUID = 8869180154879721773L;
    /**
     * 主键
     */
    @ApiModelProperty(value = "主键")
    private Long id;

    /**
     * 父主键
     */
    @ApiModelProperty(value = "父主键")
    private Long parentId;
    /**
     * 字典码
     */
    @ApiModelProperty(value = "字典码")
    private String code;
    /**
     * 字典值
     */
    @ApiModelProperty(value = "字典值")
    private String dictKey;
    /**
     * 字典名称
     */
    @ApiModelProperty(value = "字典名称")
    private String dictValue;
    /**
     * 排序
     */
    @ApiModelProperty(value = "排序")
    private Integer orderNum;
    /**
     * 字典备注
     */
    @ApiModelProperty(value = "字典备注")
    private String remark;
}
