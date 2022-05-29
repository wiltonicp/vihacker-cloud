package cc.vihackerframework.core.entity;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Ranger
 * @Description
 * @Date 2021/3/6 10:38
 * @Email wilton.icp@gmail.com
 */
@Data
public class Tree<T> {

    private String id;

    private String name;

    private String parentId;

    private List<Tree<T>> children;

    private Integer orderNum;

    private String status;

    private String tenantId;

    private boolean hasParent = false;

    private boolean hasChildren = false;

    private LocalDateTime createTime;

    public void initChildren() {
        this.children = new ArrayList<>();
    }
}
