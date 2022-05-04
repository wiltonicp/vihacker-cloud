package cc.vihackerframework.uaa.controller;

import cc.vihackerframework.core.api.ViHackerResult;
import cc.vihackerframework.core.auth.entity.AdminAuthUser;
import cc.vihackerframework.core.auth.util.ViHackerAuthUser;
import cc.vihackerframework.core.util.SecurityUtil;
import cc.vihackerframework.core.log.annotation.LogEndpoint;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * @author Ranger
 * @email wilton.icp@gmail.com
 * @since 2021/6/7
 */
@RestController
public class TestController {

    //@SentinelResource("resource")
    @GetMapping("user1")
    @LogEndpoint(value = "获取用户信息",exception = "获取用户信息异常")
    public ViHackerResult user() {
        AdminAuthUser loginUser = ViHackerAuthUser.getUser();
        return ViHackerResult.data(loginUser);
    }


    @GetMapping("test")
    @PreAuthorize("hasAuthority('role:add')")
    public ViHackerResult test() {
        return ViHackerResult.success();
    }
}
