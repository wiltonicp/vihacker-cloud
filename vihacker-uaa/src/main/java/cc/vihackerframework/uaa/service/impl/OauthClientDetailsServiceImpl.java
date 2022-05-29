package cc.vihackerframework.uaa.service.impl;

import cc.vihackerframework.core.util.StringPool;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cc.vihackerframework.uaa.entity.OauthClientDetails;
import cc.vihackerframework.uaa.mapper.OauthClientDetailsMapper;
import cc.vihackerframework.uaa.service.OauthClientDetailsService;
import cc.vihackerframework.uaa.service.RedisClientDetailsService;
import cc.vihackerframework.core.exception.Asserts;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * <p>
 *
 * @author Ranger
 * @email wilton.icp@gmail.com
 * @since 2021/6/8
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class OauthClientDetailsServiceImpl extends ServiceImpl<OauthClientDetailsMapper, OauthClientDetails> implements OauthClientDetailsService {


    private final PasswordEncoder passwordEncoder;
    private final RedisClientDetailsService redisClientDetailsService;

    @Override
    public IPage<OauthClientDetails> findOauthClientDetails(QuerySearch search, OauthClientDetails oauthClientDetails) {
        LambdaQueryWrapper<OauthClientDetails> queryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(oauthClientDetails.getClientId())) {
            queryWrapper.eq(OauthClientDetails::getClientId, oauthClientDetails.getClientId());
        }
        Page<OauthClientDetails> page = new Page<>(request.getPageNum(), request.getPageSize());
        IPage<OauthClientDetails> result = this.page(page, queryWrapper);

        List<OauthClientDetails> records = new ArrayList<>();
        result.getRecords().forEach(o -> {
            o.setOriginSecret(null);
            o.setClientSecret(null);
            records.add(o);
        });
        result.setRecords(records);
        return result;
    }

    @Override
    public OauthClientDetails findById(String clientId) {
        return this.baseMapper.selectById(clientId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createOauthClientDetails(OauthClientDetails oauthClientDetails) {
        OauthClientDetails byId = this.findById(oauthClientDetails.getClientId());
        if (byId != null) {
            Asserts.fail("该Client已存在");
        }
        oauthClientDetails.setOriginSecret(oauthClientDetails.getClientSecret());
        oauthClientDetails.setClientSecret(passwordEncoder.encode(oauthClientDetails.getClientSecret()));
        boolean saved = this.save(oauthClientDetails);
        if (saved) {
            log.info("缓存Client -> {}", oauthClientDetails);
            this.redisClientDetailsService.loadClientByClientId(oauthClientDetails.getClientId());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateOauthClientDetails(OauthClientDetails oauthClientDetails) {
        String clientId = oauthClientDetails.getClientId();

        LambdaQueryWrapper<OauthClientDetails> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(OauthClientDetails::getClientId, oauthClientDetails.getClientId());

        oauthClientDetails.setClientId(null);
        oauthClientDetails.setClientSecret(null);
        boolean updated = this.update(oauthClientDetails, queryWrapper);
        if (updated) {
            log.info("更新Client -> {}", oauthClientDetails);
            this.redisClientDetailsService.removeRedisCache(clientId);
            this.redisClientDetailsService.loadClientByClientId(clientId);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteOauthClientDetails(String clientIds) {
        Object[] clientIdArray = StringUtils.splitByWholeSeparatorPreserveAllTokens(clientIds, StringPool.COMMA);
        LambdaQueryWrapper<OauthClientDetails> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(OauthClientDetails::getClientId, clientIdArray);
        boolean removed = this.remove(queryWrapper);
        if (removed) {
            log.info("删除ClientId为({})的Client", clientIds);
            Arrays.stream(clientIdArray).forEach(c -> this.redisClientDetailsService.removeRedisCache(String.valueOf(c)));

        }
    }
}
