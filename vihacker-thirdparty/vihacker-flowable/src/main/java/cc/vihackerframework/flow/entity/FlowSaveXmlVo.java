package cc.vihackerframework.flow.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by Ranger on 2022/04/05.
 */
@Data
public class FlowSaveXmlVo implements Serializable {

    /**
     * 流程名称
     */
    private String name;

    /**
     * 流程分类
     */
    private String category;

    /**
     * xml 文件
     */
    private String xml;
}
