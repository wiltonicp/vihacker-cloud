package cc.vihackerframework.core.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import java.io.Serializable;
import java.util.Set;

/**
 * @author Ranger
 * @email wilton.icp@gmail.com
 * @since 2021/3/6
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CurrentUser implements Serializable {

    private static long serialVersionUID = 761748087824726463L;

    /**
     * 用户id
     */
    @ApiModelProperty(hidden = true)
    private Long userId;
    /**
     * 账号
     */
    @ApiModelProperty(hidden = true)
    private String account;
    /**
     * 昵称
     */
    @ApiModelProperty(hidden = true)
    private String nickName;
    /**
     * 租户ID
     */
    @ApiModelProperty(hidden = true)
    private String tenantId;
    /**
     * 部门id
     */
    @ApiModelProperty(hidden = true)
    private String deptId;
    /**
     * 岗位id
     */
    @ApiModelProperty(hidden = true)
    private String postId;
    /**
     * 角色id
     */
    @ApiModelProperty(hidden = true)
    private String roleId;

    /**
     * 角色编码
     */
    @ApiModelProperty(hidden = true)
    private String roleCode;

    /**
     * 角色名
     */
    @ApiModelProperty(hidden = true)
    private String roleName;

    /**
     * 登录类型
     */
    @ApiModelProperty(hidden = true)
    private String type;

    /**
     * 权限集合
     */
    @JsonIgnore
    @ApiModelProperty(hidden = true)
    private Set<GrantedAuthority> authorities;
}
