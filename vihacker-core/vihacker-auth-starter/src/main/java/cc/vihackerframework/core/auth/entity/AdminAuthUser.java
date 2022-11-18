package cc.vihackerframework.core.auth.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.time.LocalDateTime;
import java.util.Collection;

/**
 * @Description
 * @Author: Ranger
 * @Date: 2021/1/25 15:06
 * @Email: wilton.icp@gmail.com
 */
@Getter
@EqualsAndHashCode(callSuper = true)
public class AdminAuthUser extends User {

    private static final long serialVersionUID = -6411066541689297219L;

    private Long userId;

    private String avatar;

    private String email;

    private String mobile;

    private Long sex;

    /**
     * 登录类型
     */
    private String type;

    /**
     * 租户ID
     */
    private String tenantId;

    private Long deptId;

    private String deptName;

    private Long roleId;

    private String roleName;

    private LocalDateTime lastLoginTime;

    private Long status;

//    public AdminAuthUser(String username, String password, Collection<? extends GrantedAuthority> authorities) {
//        super(username, password, authorities);
//    }
//
//    public AdminAuthUser(String username, String password, boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities) {
//        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
//    }


    public AdminAuthUser(String username, String password, Collection<? extends GrantedAuthority> authorities, Long userId, String avatar, String tenantId, Long roleId) {
        super(username, password, authorities);
        this.userId = userId;
        this.avatar = avatar;
        this.tenantId = tenantId;
        this.roleId = roleId;
    }

    public AdminAuthUser(String username, String password, boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked,
                         Collection<? extends GrantedAuthority> authorities, Long userId, String avatar, String email, String mobile, Long sex, String type, String tenantId,
                         Long deptId, String deptName, Long roleId, String roleName, LocalDateTime lastLoginTime, Long status) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
        this.userId = userId;
        this.avatar = avatar;
        this.email = email;
        this.mobile = mobile;
        this.sex = sex;
        this.type = type;
        this.tenantId = tenantId;
        this.deptId = deptId;
        this.deptName = deptName;
        this.roleId = roleId;
        this.roleName = roleName;
        this.lastLoginTime = lastLoginTime;
        this.status = status;
    }
}
