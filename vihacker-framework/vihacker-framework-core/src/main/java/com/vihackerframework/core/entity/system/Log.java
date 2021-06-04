package com.vihackerframework.core.entity.system;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wuwenze.poi.annotation.Excel;
import com.wuwenze.poi.annotation.ExcelField;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author Ranger
 * @date: 2021/3/9 15:36
 * @email: wilton.icp@gmail.com
 */
@Data
@TableName("t_log")
@Excel("系统日志表")
public class Log implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 日志ID
     */
    @TableId(value = "ID", type = IdType.AUTO)
    private Long id;

    /**
     * 操作用户
     */
    @TableField("USERNAME")
    @ExcelField(value = "操作用户")
    private String username;

    /**
     * 操作内容
     */
    @TableField("OPERATION")
    @ExcelField(value = "操作内容")
    private String operation;

    /**
     * 耗时
     */
    @TableField("TIME")
    @ExcelField(value = "耗时（毫秒）")
    private Long time;

    /**
     * 操作方法
     */
    @TableField("METHOD")
    @ExcelField(value = "操作方法")
    private String method;

    /**
     * 方法参数
     */
    @TableField("PARAMS")
    @ExcelField(value = "方法参数")
    private String params;

    /**
     * 操作者IP
     */
    @TableField("IP")
    @ExcelField(value = "操作者IP")
    private String ip;

    /**
     * 创建时间
     */
    @TableField("CREATED_TIME")
    @ExcelField(value = "操作时间")
    private LocalDateTime createdTime;

    /**
     * 操作地点
     */
    @TableField("LOCATION")
    @ExcelField(value = "操作地点")
    private String location;

    /**
     * 详情
     */
    @TableField("RESULT")
    private Object result;

    private transient String createTimeFrom;
    private transient String createTimeTo;
}
