package cc.vihackerframework.core.entity.system;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import cc.vihackerframework.core.entity.ViHackerEntity;
import io.swagger.annotations.ApiModelProperty;
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

    public static final Long TOP_MENU_ID = 0L;

    /**
     * 菜单/按钮ID
     */
    @TableId(value = "ID", type = IdType.AUTO)
    private Long id;

    /**
     * 上级菜单ID
     */
    @TableField("PARENT_ID")
    private Long parentId;

    /**
     * 菜单/按钮名称
     */
    @TableField("NAME")
    private String name;

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
    @TableField("PERMISSION")
    private String permission;

    /**
     * 图标
     */
    @TableField("ICON")
    private String icon;

    /**
     * 类型 0目录 1菜单 2按钮
     */
    @TableField("TYPE")
    private String type;

    /**
     * 状态 0启用 1停用
     */
    @TableField("STATUS")
    private String status;

    /**
     * 排序
     */
    @TableField("ORDER_NUM")
    private Integer orderNum;
}
