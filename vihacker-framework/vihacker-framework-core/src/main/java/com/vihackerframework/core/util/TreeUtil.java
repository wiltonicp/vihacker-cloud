package com.vihackerframework.core.util;

import com.vihackerframework.common.entity.Tree;

import java.util.ArrayList;
import java.util.List;

/**
* @author Ranger
* @Description:
* @Date: 2021/3/6 10:48
* @Email: wilton.icp@gmail.com
*/
public abstract class TreeUtil {
    private final static Long TOP_NODE_ID = 0L;

    /**
     * 用于构建菜单或部门树
     *
     * @param nodes nodes
     * @return <T> List<? extends Tree>
     */
    public static <T> List<? extends Tree<?>> build(List<? extends Tree<T>> nodes) {
        if (nodes == null) {
            return null;
        }
        List<Tree<T>> topNodes = new ArrayList<>();
        nodes.forEach(node -> {
            Long pid = node.getParentId();
            if (pid == null || TOP_NODE_ID.equals(pid)) {
                topNodes.add(node);
                return;
            }
            for (Tree<T> n : nodes) {
                Long id = n.getId();
                if (id != null && id.equals(pid)) {
                    if (n.getChildren() == null) {
                        n.initChildren();
                    }
                    n.getChildren().add(node);
                    node.setHasParent(true);
                    n.setHasChildren(true);
                    n.setHasParent(true);
                    return;
                }
            }
            if (topNodes.isEmpty()) {
                topNodes.add(node);
            }
        });
        return topNodes;
    }
}
