package cc.vihackerframework.core.entity.system;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author Ranger
 * @date: 2021/3/9 15:36
 * @email: wilton.icp@gmail.com
 */
@Data
@Accessors(chain = true)
@TableName("t_log")
public class SysLog implements Serializable {

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
    @Excel(name = "操作用户", orderNum = "1", height = 20, width = 30, isImportField = "true_st")
    private String username;

    /**
     * 日志标题
     */
    @ApiModelProperty(value = "日志标题")
    @TableField("TITLE")
    private String title;

    /**
     * 跟踪ID
     */
    @ApiModelProperty(value = "跟踪ID")
    @TableField("TRACE_ID")
    private String traceId;

    /**
     * 请求路径
     */
    @ApiModelProperty(value = "请求路径")
    @TableField("URL")
    private String url;

    /**
     * 操作内容
     */
    @TableField("OPERATION")
    @Excel(name = "操作内容", orderNum = "2", height = 20, width = 30, isImportField = "true_st")
    private String operation;

    /**
     * 耗时
     */
    @TableField("TIME")
    @Excel(name = "耗时（毫秒）", orderNum = "3", height = 20, width = 30, isImportField = "true_st")
    private Long time;

    /**
     * 操作方法
     */
    @TableField("METHOD")
    @Excel(name = "操作方法", orderNum = "4", height = 20, width = 30, isImportField = "true_st")
    private String method;

    /**
     * 方法参数
     */
    @TableField("PARAMS")
    @Excel(name = "方法参数", orderNum = "5", height = 20, width = 30, isImportField = "true_st")
    private String params;

    /**
     * 操作者IP
     */
    @TableField("IP")
    @Excel(name = "操作者IP", orderNum = "6", height = 20, width = 30, isImportField = "true_st")
    private String ip;

    /**
     * 创建时间
     */
    @TableField(value = "CREATED_TIME", fill = FieldFill.INSERT)
    @Excel(name = "操作时间", orderNum = "7", height = 20, width = 30, isImportField = "true_st")
    private LocalDateTime createdTime;

    /**
     * 操作地点
     */
    @TableField("LOCATION")
    @Excel(name = "操作地点", orderNum = "8", height = 20, width = 30, isImportField = "true_st")
    private String location;

    /**
     * 详情
     */
    @TableField("RESULT")
    private Object result;

    /**
     * 异常信息
     */
    @TableField("EXCEPTION")
    private String exception;

    private transient String createTimeFrom;
    private transient String createTimeTo;
}
