package cc.vihackerframework.flow.feign;

import cc.vihackerframework.core.api.ViHackerApiResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * Created by Ranger on 2022/4/5.
 */
@FeignClient("vihacker-system")
public interface ISysUserProvider {

    /**
     * 根据 id 查询用户
     * @param userId
     * @return
     */
    @PostMapping("provider/user/get")
    ViHackerApiResult selectUserById(Long userId);
}
