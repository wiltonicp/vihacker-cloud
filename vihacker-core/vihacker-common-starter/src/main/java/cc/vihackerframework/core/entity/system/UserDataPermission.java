package cc.vihackerframework.core.entity.system;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author Ranger
 * @date: 2021/3/6 15:45
 * @email: wilton.icp@gmail.com
 */
@Data
@TableName("t_user_data_permission")
public class UserDataPermission {

    @TableId("ID")
    private Long id;
    @TableField("USER_ID")
    private Long userId;
    @TableField("DEPT_ID")
    private Long deptId;

}