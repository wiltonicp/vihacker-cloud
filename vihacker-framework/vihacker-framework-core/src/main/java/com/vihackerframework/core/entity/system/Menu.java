package com.vihackerframework.core.entity.system;

import com.vihackerframework.common.entity.ViHackerEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @Description
 * @Author: Ranger
 * @Date: 2021/1/25 16:33
 * @Email: wilton.icp@gmail.com
 */
@Data
@TableName("t_menu")
public class Menu extends ViHackerEntity implements Serializable {
    private static final long serialVersionUID = 7187628714679791771L;

    // 菜单
    public static final String TYPE_MENU = "0";
    // 按钮
    public static final String TYPE_BUTTON = "1";
    public static final Long TOP_MENU_ID = 0L;

    /**
     * 菜单/按钮ID
     */
    @TableId(value = "MENU_ID", type = IdType.AUTO)
    private Long menuId;

    /**
     * 上级菜单ID
     */
    @TableField("PARENT_ID")
    private Long parentId;

    /**
     * 菜单/按钮名称
     */
    @TableField("MENU_NAME")
    private String menuName;

    /**
     * 菜单URL
     */
    @TableField("PATH")
    private String path;

    /**
     * 对应 Vue组件
     */
    @TableField("COMPONENT")
    private String component;

    /**
     * 权限标识
     */
    @TableField("PERMS")
    private String perms;

    /**
     * 图标
     */
    @TableField("ICON")
    private String icon;

    /**
     * 类型 0菜单 1按钮
     */
    @TableField("TYPE")
    private String type;

    /**
     * 排序
     */
    @TableField("ORDER_NUM")
    private Integer orderNum;
}
