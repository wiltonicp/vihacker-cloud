package cc.vihackerframework.es.model;

import cc.vihackerframework.core.elasticsearch.annotation.ESDocument;
import cc.vihackerframework.core.elasticsearch.annotation.ESField;
import cc.vihackerframework.core.elasticsearch.annotation.ESId;
import cc.vihackerframework.core.elasticsearch.enums.ESFieldType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>
 * @author Ranger
 * @since 2021/5/2
 * @email wilton.icp@gmail.com
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ESDocument(indexName = "goods")
public class GoodsESEntity {
    // 筛选条件包括：商品名称，品牌，规格，适用车型，商品编号，原厂编号

    /**
     * 主键,商品ID
     */
    @ESId
    @ESField(value = "goodsId",type = ESFieldType.Long)
    private Long goodsId;

    /**
     * 商品名称
     */
    @ESField(value = "goodsName",type = ESFieldType.Text, analyzer = "ik_max_word")
    private String goodsName;
    /**
     * 品牌
     */
    @ESField(value = "goodBrand",type = ESFieldType.Keyword)
    private String goodBrand;
    /**
     * 规格
     */
    @ESField(value = "goodsSpec",type = ESFieldType.Keyword)
    private String goodsSpec;
    /**
     * 商品编号
     */
    @ESField(value = "goodsAccessoriesCode",type = ESFieldType.Keyword)
    private String goodsAccessoriesCode;
    /**
     * 原厂编号
     */
    @ESField(value = "goodsOriginalFactoryCode",type = ESFieldType.Keyword)
    private String goodsOriginalFactoryCode;

    /**
     * 复合字段，会被分词后存储
     */
    @ESField(value = "groupData",type = ESFieldType.Text,analyzer = "ik_max_word")
    private String groupData;
}
