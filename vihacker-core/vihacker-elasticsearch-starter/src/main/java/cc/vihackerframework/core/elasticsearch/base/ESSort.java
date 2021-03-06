package cc.vihackerframework.core.elasticsearch.base;

import lombok.Builder;
import lombok.Data;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.util.Assert;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 排序参数
 * @author Ranger
 * @since 2021/5/2
 * @email wilton.icp@gmail.com
 */
public class ESSort {

    public final List<ESOrder> orders;
    public ESSort() {
        orders = new ArrayList<>();
    }

    public ESSort(SortOrder direction, String property) {
        orders = new ArrayList<>();
        add(direction,property);
    }



    /**
     * 追加排序字段
     * @param direction  排序方向
     * @param property  排序字段
     * @return
     */
    public ESSort add(SortOrder direction, String property) {

        Assert.notNull(direction, "direction must not be null!");
        Assert.hasText(property, "fieldName must not be empty!");

        orders.add(ESOrder.builder().direction(direction).property(property).build());
        return this;
    }


    @Builder
    @Data
    public static class ESOrder implements Serializable {

        private final SortOrder direction;
        private final String property;

    }

}
