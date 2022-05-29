package cc.vihackerframework.uaa.service;

import cc.vihackerframework.core.datasource.entity.QuerySearch;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import cc.vihackerframework.uaa.entity.OauthClientDetails;

/**
 * <p>
 *
 * @author Ranger
 * @email wilton.icp@gmail.com
 * @since 2021/6/8
 */
public interface OauthClientDetailsService extends IService<OauthClientDetails> {

    /**
     * 查询（分页）
     * @param search
     * @return
     */
    IPage<OauthClientDetails> findOauthClientDetails(QuerySearch search);

    /**
     * 根据主键查询
     *
     * @param clientId clientId
     * @return OauthClientDetails
     */
    OauthClientDetails findById(String clientId);

    /**
     * 新增
     *
     * @param oauthClientDetails oauthClientDetails
     */
    void createOauthClientDetails(OauthClientDetails oauthClientDetails);

    /**
     * 修改
     *
     * @param oauthClientDetails oauthClientDetails
     */
    void updateOauthClientDetails(OauthClientDetails oauthClientDetails);

    /**
     * 删除
     *
     * @param clientIds clientIds
     */
    void deleteOauthClientDetails(String clientIds);
}
