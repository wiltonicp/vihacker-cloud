package com.vihackerframework.core.entity;

import com.vihackerframework.core.entity.system.Menu;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Ranger
 * @date: 2021/3/8 16:29
 * @email: wilton.icp@gmail.com
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class MenuTree extends Tree<Menu> {

    private String path;
    private String component;
    private String perms;
    private String icon;
    private String type;
    private Integer orderNum;
}
