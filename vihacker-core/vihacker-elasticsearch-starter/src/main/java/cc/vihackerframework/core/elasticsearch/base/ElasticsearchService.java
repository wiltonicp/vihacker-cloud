package cc.vihackerframework.core.elasticsearch.base;

import cc.vihackerframework.core.elasticsearch.annotation.ESField;
import cc.vihackerframework.core.elasticsearch.annotation.ESId;
import cc.vihackerframework.core.elasticsearch.enums.ESFieldType;
import cc.vihackerframework.core.elasticsearch.props.ViHackerElasticSearchProperties;
import cc.vihackerframework.core.exception.Asserts;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.HttpAsyncResponseConsumerFactory;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.client.indices.PutMappingRequest;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.io.IOException;
import java.lang.reflect.Field;

/**
 * Elasticsearch 查询工具
 * @author Ranger
 * @since 2021/5/2
 * @email wilton.icp@gmail.com
 */
@Slf4j
public class ElasticsearchService {

    @Resource
    public RestHighLevelClient client;
    @Resource
    private ViHackerElasticSearchProperties elasticsearchProperties;

    public static final RequestOptions COMMON_OPTIONS;

    static {
        RequestOptions.Builder builder = RequestOptions.DEFAULT.toBuilder();

        // 默认缓冲限制为100MB，此处修改为30MB。
        builder.setHttpAsyncResponseConsumerFactory(new HttpAsyncResponseConsumerFactory.HeapBufferedResponseConsumerFactory(30 * 1024 * 1024));
        COMMON_OPTIONS = builder.build();
    }

    /**
     * 判断索引是否存在
     * @param indexName
     * @return
     */
    public boolean existIndex(String indexName) {
        boolean exists = false;
        try {
            GetIndexRequest request = new GetIndexRequest(indexName);
            exists = client.indices().exists(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
            Asserts.fail("判断索引 {" + indexName + "} 是否存在失败");
        }
        return exists;
    }

    /**
     * 创建索引
     * @param indexName
     */
    public void createIndexRequest(String indexName) {
        createIndexRequest(indexName, elasticsearchProperties.getIndex().getNumberOfShards(), elasticsearchProperties.getIndex().getNumberOfReplicas());
    }

    /**
     * 创建索引
     * @param indexName
     * @param shards 分片
     * @param replicas 副本
     */
    public void createIndexRequest(String indexName, int shards, int replicas) {
        if (existIndex(indexName)) {
            return;
        }

        try {
            CreateIndexRequest request = new CreateIndexRequest(indexName);
            // Settings for this index
            request.settings(Settings.builder()
                    .put("index.number_of_shards", shards)
                    .put("index.number_of_replicas", replicas)
            );

            CreateIndexResponse createIndexResponse = client.indices().create(request, RequestOptions.DEFAULT);
            log.info(" acknowledged : {}", createIndexResponse.isAcknowledged());
            log.info(" shardsAcknowledged :{}", createIndexResponse.isShardsAcknowledged());
        } catch (IOException e) {
            Asserts.fail("创建索引 {" + indexName + "} 失败");
        }
    }

    /**
     * 创建索引
     * @param indexName
     * @param shards 分片
     * @param replicas 副本
     */
    public void createIndexRequest(String indexName, int shards, int replicas, String analyzer) {
        if (existIndex(indexName)) {
            return;
        }

        try {
            CreateIndexRequest request = new CreateIndexRequest(indexName);
            // Settings for this index
            request.settings(Settings.builder()
                    .put("index.number_of_shards", shards)
                    .put("index.number_of_replicas", replicas)
                    .put("analysis.analyzer.default.type", analyzer)
            );

            CreateIndexResponse createIndexResponse = client.indices().create(request, RequestOptions.DEFAULT);
            log.info(" acknowledged : {}", createIndexResponse.isAcknowledged());
            log.info(" shardsAcknowledged :{}", createIndexResponse.isShardsAcknowledged());
        } catch (IOException e) {
            Asserts.fail("创建索引 {" + indexName + "} 失败");
        }
    }

    /**
     * 保存文档
     * @param indexName
     * @param clazz
     */
    public void putMappingRequest(String indexName, Class clazz) {
        Field[] fields = clazz.getDeclaredFields();
        if (fields == null || fields.length == 0) {
            return;
        }

        try {
            PutMappingRequest request = new PutMappingRequest(indexName);
            XContentBuilder builder = XContentFactory.jsonBuilder();
            builder.startObject();
            {
                builder.startObject("properties");
                {
                    for (int i = 0; i < fields.length; i++) {
                        Field field = fields[i];
                        ESId esId = field.getAnnotation(ESId.class);
                        if (esId != null) {
                            continue;
                        } else {
                            AnnotationAttributes esField = AnnotatedElementUtils.getMergedAnnotationAttributes(field, ESField.class);
                            if (esField == null) {
                                continue;
                            }
                            String name = esField.getString("name");
                            if (StringUtils.isEmpty(name)) {
                                Asserts.fail("注解ESField的name属性未指定");
                            }
                            ESFieldType esFieldType = (ESFieldType) esField.get("type");
                            if (esFieldType == null) {
                                Asserts.fail("注解ESField的type属性未指定");
                            }
                            builder.startObject(name);
                            {
                                builder.field("type", esFieldType.typeName);
                                // 分词器
                                String analyzer = esField.getString("analyzer");
                                if (StringUtils.hasText(analyzer)) {
                                    builder.field("analyzer", analyzer);
                                }
                            }
                            builder.endObject();
                        }
                    }
                }
                builder.endObject();
            }
            builder.endObject();
            request.source(builder);

            AcknowledgedResponse putMappingResponse = client.indices().putMapping(request, RequestOptions.DEFAULT);
            log.info("acknowledged : :{}", putMappingResponse.isAcknowledged());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除索引
     * @param index
     */
    public void deleteIndexRequest(String index) {
        DeleteIndexRequest deleteIndexRequest = new DeleteIndexRequest(index);
        try {
            client.indices().delete(deleteIndexRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            Asserts.fail("删除索引 {" + index + "} 失败");
        }
    }

    public static IndexRequest buildIndexRequest(String index, String id, Object object) {
        return new IndexRequest(index).id(id).source(JSON.toJSONString(object), XContentType.JSON);
    }

    /**
     * 更新文档
     * @param index
     * @param id
     * @param object
     */
    public void updateRequest(String index, String id, Object object) {
        try {
            UpdateRequest updateRequest = new UpdateRequest(index, id).doc(JSON.toJSONString(object), XContentType.JSON);
            client.update(updateRequest, COMMON_OPTIONS);
        } catch (IOException e) {
            Asserts.fail("更新索引 {" + index + "} 数据 {" + object + "} 失败");
        }
    }

    /**
     * 删除文档
     * @param index
     * @param id
     */
    public void deleteRequest(String index, String id) {
        try {
            DeleteRequest deleteRequest = new DeleteRequest(index, id);
            client.delete(deleteRequest, COMMON_OPTIONS);
        } catch (IOException e) {
            Asserts.fail("删除索引 {" + index + "} 数据id {" + id + "} 失败");
        }
    }

    /**
     * 检索
     * @param index
     * @return
     */
    public SearchResponse search(String index) {
        SearchRequest searchRequest = new SearchRequest(index);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.matchAllQuery());
        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = null;
        try {
            searchResponse = client.search(searchRequest, COMMON_OPTIONS);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return searchResponse;
    }

    /**
     * 检索
     * @param index
     * @param searchSourceBuilder
     * @return
     */
    public SearchResponse search(String index, SearchSourceBuilder searchSourceBuilder) {
        SearchRequest searchRequest = new SearchRequest(index);
        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = null;
        try {
            searchResponse = client.search(searchRequest, COMMON_OPTIONS);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return searchResponse;
    }
}
