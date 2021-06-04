package com.vihackerframework.core.entity.system;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wuwenze.poi.annotation.Excel;
import com.wuwenze.poi.annotation.ExcelField;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author Ranger
 * @date: 2021/3/6 17:01
 * @email: wilton.icp@gmail.com
 */
@Data
@TableName("t_role")
@Excel("角色信息表")
public class Role implements Serializable {

    private static final long serialVersionUID = -1714476694755654924L;

    @TableId(value = "ROLE_ID", type = IdType.AUTO)
    private Long roleId;

    @TableField(value = "ROLE_NAME")
    @NotBlank(message = "角色名称不能为空")
    @Size(max = 10, message = "角色名称长度不能超过10个字符")
    @ExcelField(value = "角色名称")
    private String roleName;

    @TableField(value = "REMARK")
    @Size(max = 50, message = "角色描述长度不能超过50个字符")
    @ExcelField(value = "角色描述")
    private String remark;

    @TableField(value = "CREATED_TIME")
    @ExcelField(value = "创建时间")
    private LocalDateTime createdTime;

    @TableField(value = "MODIFY_TIME")
    @ExcelField(value = "修改时间")
    private LocalDateTime modifyTime;

    private transient String menuIds;

}