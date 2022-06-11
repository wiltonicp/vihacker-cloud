package cc.vihackerframework.uaa.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.poi.ss.formula.functions.T;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 *
 * @author Ranger
 * @email wilton.icp@gmail.com
 * @since 2021/6/8
 */
@Data
@TableName("oauth_client_details")
public class OauthClientDetails implements Serializable {

    private static final long serialVersionUID = 421783821058285802L;

    /**
     * 客户端id
     */
    @TableId(value = "client_id")
    @NotBlank(message = "{required}")
    @Size(max = 255, message = "{noMoreThan}")
    @ApiModelProperty(value = "客户端标识")
    private String clientId;

    /**
     * 客户端密钥
     */
    @TableField("resource_ids")
    @Size(max = 255, message = "{noMoreThan}")
    @ApiModelProperty(value = "客户端密钥")
    private String resourceIds;

    /**
     * 资源集合
     */
    @TableField("client_secret")
    @NotBlank(message = "{required}")
    @Size(max = 255, message = "{noMoreThan}")
    @ApiModelProperty(value = "资源集合")
    private String clientSecret;

    /**
     * 授权范围
     */
    @TableField("scope")
    @NotBlank(message = "{required}")
    @Size(max = 255, message = "{noMoreThan}")
    @ApiModelProperty(value = "授权范围")
    private String scope;

    /**
     * 授权类型
     */
    @TableField("authorized_grant_types")
    @NotBlank(message = "{required}")
    @Size(max = 255, message = "{noMoreThan}")
    @ApiModelProperty(value = "授权类型")
    private String authorizedGrantTypes;

    /**
     * 回调地址
     */
    @TableField("web_server_redirect_uri")
    @Size(max = 255, message = "{noMoreThan}")
    @ApiModelProperty(value = "回调地址")
    private String webServerRedirectUri;

    /**
     * 访问资源所需权限
     */
    @TableField("authorities")
    @Size(max = 255, message = "{noMoreThan}")
    @ApiModelProperty(value = "权限")
    private String authorities;

    /**
     * 令牌过期秒数
     */
    @TableField("access_token_validity")
    @NotNull(message = "{required}")
    @ApiModelProperty(value = "令牌过期秒数")
    private Integer accessTokenValidity;

    /**
     * 刷新令牌过期秒数
     */
    @TableField("refresh_token_validity")
    @ApiModelProperty(value = "刷新令牌过期秒数")
    private Integer refreshTokenValidity;

    /**
     * 附加说明
     */
    @TableField("additional_information")
    @ApiModelProperty(value = "附加说明")
    private String additionalInformation;


    /**
     * 自动授权 是否登录时跳过授权
     */
    @TableField("autoapprove")
    @ApiModelProperty(value = "自动授权")
    private String autoapprove;

    /**
     * 终端明文安全码
     */
    @TableField("origin_secret")
    @ApiModelProperty(value = "终端明文安全码")
    private String originSecret;


    /**
     * 创建者
     */
    @ApiModelProperty(value = "创建者")
    @TableField(value = "CREATED_BY", fill = FieldFill.INSERT)
    private T createdBy;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    @TableField(value = "CREATED_TIME", fill = FieldFill.INSERT)
    private LocalDateTime createdTime;

    /**
     * 修改者
     */
    @ApiModelProperty(value = "修改者")
    @TableField(value = "MODIFY_BY", fill = FieldFill.INSERT_UPDATE)
    private T modifyBy;

    /**
     * 修改时间
     */
    @ApiModelProperty(value = "修改时间")
    @TableField(value = "MODIFY_TIME", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime modifyTime;

    /**
     * 版本信息
     */
    @Version
    @TableField(value = "VERSION")
    private Long version;

    /**
     * 数据逻辑删除标识字段
     */
    @TableLogic
    @TableField(fill = FieldFill.INSERT)
    private Integer deleted;

}
