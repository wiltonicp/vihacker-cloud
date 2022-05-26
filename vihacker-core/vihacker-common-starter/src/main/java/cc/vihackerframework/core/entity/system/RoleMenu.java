package cc.vihackerframework.core.entity.system;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @author Ranger
 * @email wilton.icp@gmail.com
 * @since 2021/3/10
 */
@TableName("t_role_menu")
@Data
public class RoleMenu implements Serializable {

    private static final long serialVersionUID = -7573904024872252113L;

    @TableId(value = "ID")
    private Long id;
    @TableField(value = "ROLE_ID")
    private Long roleId;
    @TableField(value = "MENU_ID")
    private Long menuId;
}