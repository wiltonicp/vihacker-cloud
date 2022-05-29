package cc.vihackerframework.core.entity.system;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import cc.vihackerframework.core.entity.ViHackerEntity;
import cc.vihackerframework.core.entity.enums.SexEnum;
import cc.vihackerframework.core.entity.enums.StatusEnum;
import cc.vihackerframework.core.util.EnumUtil;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author Ranger
 * @date: 2021/3/6 15:58
 * @email: wilton.icp@gmail.com
 */
@Data
@TableName("t_user")
public class SysUser extends ViHackerEntity implements Serializable {

    private static final long serialVersionUID = -4352868070794165001L;

    /**
     * 用户 ID
     */
    @TableId(value = "ID", type = IdType.AUTO)
    private Long id;

    /**
     * 租户ID
     */
    @TableField("TENANT_ID")
    private String tenantId;

    /**
     * 用户名
     */
    @TableField("USERNAME")
    @Size(min = 4, max = 11, message = "用户名长度在 4-11 之间")
    private String username;

    /**
     * 密码
     */
    @TableField("PASSWORD")
    private String password;

    /**
     * 部门 ID
     */
    @TableField("DEPT_ID")
    private Long deptId;

    /**
     * 昵称
     */
    @TableField("NICK_NAME")
    private String nickName;

    /**
     * 真实姓名
     */
    @TableField("REAL_NAME")
    private String realName;

    /**
     * 邮箱
     */
    @TableField("EMAIL")
    @Size(max = 50, message = "长度不能超过50个字符")
    @Email(message = "邮箱地址有误")
    private String email;

    /**
     * 联系电话
     */
    @TableField("MOBILE")
    private String mobile;

    /**
     * 状态 0锁定 1有效
     */
    @TableField("STATUS")
    @NotNull(message = "状态不能为空")
    private Integer status;

    /**
     * 状态，用于展示
     */
    @TableField(exist = false)
    private StatusEnum statusName;

    /**
     * 最近访问时间
     */
    @TableField("LAST_LOGIN_TIME")
    private LocalDateTime lastLoginTime;

    /**
     * 性别 0男 1女 2 保密
     */
    @TableField("SSEX")
    @NotNull(message = "性别不能为空")
    private Integer sex;

    /**
     * 性别,用于展示
     */
    @TableField(exist = false)
    private SexEnum sexName;

    /**
     * 头像
     */
    @TableField("AVATAR")
    private String avatar;

    /**
     * 描述
     */
    @TableField("DESCRIPTION")
    @Size(max = 100, message = "长度不能超过100个字符")
    private String description;

    /**
     * 部门名称
     */
    @TableField(exist = false)
    private String deptName;

    /**
     * 角色 ID
     */
    @TableField(exist = false)
    private String roleId;

    @TableField(exist = false)
    private String roleName;

    public void created(SysUser user) {
        this.setSexName(EnumUtil.getEnumByCode(SexEnum.class, user.getSex().toString()));
        this.setStatusName(EnumUtil.getEnumByCode(StatusEnum.class, user.getStatus().toString()));
    }

}
