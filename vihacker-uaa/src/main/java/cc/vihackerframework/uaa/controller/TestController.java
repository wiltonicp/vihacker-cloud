package cc.vihackerframework.uaa.controller;

import cc.vihackerframework.core.api.ViHackerResult;
import cc.vihackerframework.core.auth.entity.AdminAuthUser;
import cc.vihackerframework.core.auth.util.SecurityUtil;
import cc.vihackerframework.log.starter.annotation.LogEndpoint;
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
    @GetMapping("user")
    @LogEndpoint(value = "获取用户信息",exception = "获取用户信息异常")
    public ViHackerResult user() {
        AdminAuthUser loginUser = SecurityUtil.getLoginUser();
        return ViHackerResult.data(loginUser);
    }


    @GetMapping("test")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    public ViHackerResult test() {
        return ViHackerResult.success();
    }
}
