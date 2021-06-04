package com.vihackerframework.core.entity;

import com.vihackerframework.core.entity.system.Dept;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Ranger
 * @since 2021/3/10
 * @email wilton.icp@gmail.com
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class DeptTree extends Tree<Dept> {

    private Integer orderNum;
}
