package cc.vihackerframework.es.enums;

/**
 * @author Ranger
 * @since 2021/5/2
 * @email wilton.icp@gmail.com
 */
public enum ElasticsearchIndexEnum {
    /** 商品 */
    GOODS("goods");

    ElasticsearchIndexEnum(String indexName) {
        this.indexName = indexName;
    }

    private String indexName;

    public String getIndexName() {
        return indexName;
    }

}
