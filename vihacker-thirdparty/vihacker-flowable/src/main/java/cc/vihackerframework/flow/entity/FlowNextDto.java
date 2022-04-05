package cc.vihackerframework.flow.entity;

import cc.vihackerframework.core.entity.system.Role;
import cc.vihackerframework.core.entity.system.SysUser;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 动态人员、组
 * Created by Ranger on 2022/04/05.
 */
@Data
public class FlowNextDto implements Serializable {

    private String type;

    private String vars;

    private List<SysUser> userList;

    private List<Role> roleList;
}
