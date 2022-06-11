package cc.vihackerframework.core.entity.enums;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 企业类型-枚举
 * Created by Ranger on 2022/6/11.
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "TenantTypeEnum", description = "类型-枚举")
public enum TenantTypeEnum implements EnumMessage{
    /**
     * CREATE="创建"
     */
    CREATE("创建"),
    /**
     * REGISTER="注册"
     */
    REGISTER("注册");

    @ApiModelProperty(value = "描述")
    private String desc;

    @Override
    public String getDesc() {
        return this.desc;
    }

    @Override
    @ApiModelProperty(value = "编码", allowableValues = "CREATE,REGISTER", example = "CREATE")
    public String getValue() {
        return this.name();
    }
}
