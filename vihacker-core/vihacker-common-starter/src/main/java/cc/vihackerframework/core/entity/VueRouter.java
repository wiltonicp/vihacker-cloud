package cc.vihackerframework.core.entity;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 构建 Vue路由
 * Created by Ranger on 2022/03/13
 */
@Data
public class VueRouter<T> implements Serializable {

    private static final long serialVersionUID = -3327478146308500708L;

    private String id;
    private String parentId;
    private String path;
    private String permission;
    private String name;
    private String component;
    private String redirect;
    private String type;
    private String typeName;
    private String icon;
    private Integer orderNum;
    private String status;
    private RouterMeta meta;
    private Boolean hidden = false;
    private Boolean alwaysShow = false;
    private LocalDateTime createTime;
    private List<VueRouter<T>> children;

    private Boolean hasParent = false;
    private Boolean hasChildren = false;

    public void initChildren() {
        this.children = new ArrayList<>();
    }

}
