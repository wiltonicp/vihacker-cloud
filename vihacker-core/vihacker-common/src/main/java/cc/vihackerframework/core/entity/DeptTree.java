package cc.vihackerframework.core.entity;

import cc.vihackerframework.core.entity.system.Dept;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Ranger
 * @email wilton.icp@gmail.com
 * @since 2021/3/10
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class DeptTree extends Tree<Dept> {

    private Integer orderNum;
}
