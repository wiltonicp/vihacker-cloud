package cc.vihackerframework.core.entity.system;

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


    @TableId("USER_ID")
    private Long userId;
    @TableId("DEPT_ID")
    private Long deptId;

}