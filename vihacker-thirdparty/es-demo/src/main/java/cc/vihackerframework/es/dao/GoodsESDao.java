package cc.vihackerframework.es.dao;

import cc.vihackerframework.core.elasticsearch.base.BaseElasticsearchDao;
import cc.vihackerframework.core.elasticsearch.base.ESPageRequest;
import cc.vihackerframework.core.elasticsearch.base.ESPageResult;
import cc.vihackerframework.core.elasticsearch.base.ESSort;
import cc.vihackerframework.es.model.GoodsESEntity;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <p>
 * @author Ranger
 * @since 2021/5/2
 * @email wilton.icp@gmail.com
 */
@Component
public class GoodsESDao<T extends GoodsESEntity> extends BaseElasticsearchDao<GoodsESEntity> {


    /**
     * 全文搜索查询
     * @param queryName
     * @return
     */
    public List<GoodsESEntity> findListByAnalysisForGroupData(String queryName) {
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.matchQuery("groupData",queryName));
        List<GoodsESEntity> search = search(searchSourceBuilder);
        return search;
    }

    /**
     * 多条件等值查询查询
     * @return
     */
    public List<GoodsESEntity> findListByEq() {
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.filter(QueryBuilders.matchQuery("goodsName","保时捷跑车V10"));
        boolQueryBuilder.filter(QueryBuilders.matchQuery("goodBrand","国际1"));
        searchSourceBuilder.query(boolQueryBuilder);
        List<GoodsESEntity> search = search(searchSourceBuilder);
        return search;
    }

    /**
     * 多条件like查询查询
     * @return
     */
    public List<GoodsESEntity> findListByLike() {
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.filter(QueryBuilders.wildcardQuery("goodsName","?V10"));
        boolQueryBuilder.filter(QueryBuilders.wildcardQuery("goodBrand","国际1"));
        searchSourceBuilder.query(boolQueryBuilder);
        List<GoodsESEntity> search = search(searchSourceBuilder);
        return search;
    }

    /**
     * 全文搜索查询分页排序
     * @param queryName
     * @return
     * QueryBuilders.boolQuery()
     */
    public ESPageResult findList(String queryName) {
        // 搜索条件
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.matchQuery("groupData",queryName));

        // 分页
        ESPageRequest esPageRequest = new ESPageRequest(1, 2);

        // 排序
        ESSort esSort = new ESSort(SortOrder.ASC,"goodsName");

        ESPageResult<GoodsESEntity> search = search(searchSourceBuilder, esPageRequest, esSort);
        return search;
    }
}


