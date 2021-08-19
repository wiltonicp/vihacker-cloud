package cc.vihackerframework.core.elasticsearch.base;

import lombok.Data;

import java.util.List;

/**
 * 分页结果
 * @author Ranger
 * @since 2021/5/2
 * @email wilton.icp@gmail.com
 */
@Data
public class ESPageResult<T> {

    private final long total;
    private final int pageNo;
    private final int pageSize;
    private List<T> results;

    public ESPageResult(long total, int pageNo, int pageSize, List<T> results) {
        this.total = total;
        this.pageNo = pageNo;
        this.pageSize = pageSize;
        this.results = results;
    }
}
