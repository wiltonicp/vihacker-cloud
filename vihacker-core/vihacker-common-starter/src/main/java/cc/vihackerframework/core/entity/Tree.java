package cc.vihackerframework.core.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Ranger
 * @Description
 * @Date 2021/3/6 10:38
 * @Email wilton.icp@gmail.com
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Tree<T> {

    private Long id;

    private String label;

    private List<Tree<T>> children;

    private Long parentId;

    private boolean hasParent = false;

    private boolean hasChildren = false;

    public void initChildren() {
        this.children = new ArrayList<>();
    }
}
