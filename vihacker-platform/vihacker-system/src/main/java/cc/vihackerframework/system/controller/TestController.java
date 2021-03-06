package cc.vihackerframework.system.controller;

import cc.vihackerframework.core.api.ViHackerApiResult;
import cc.vihackerframework.core.api.ViHackerResult;
import cc.vihackerframework.core.auth.context.UserContext;
import cc.vihackerframework.core.auth.entity.AdminAuthUser;
import cc.vihackerframework.log.starter.annotation.LogEndpoint;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Ranger on 2022/2 */
@RestController
public class TestController {

    @PostMapping("test1")
    //@PreAuthorize("hasAuthority('test:view')")
    @LogEndpoint(value = "测试日志保存",exception = "获取用户信息异常")
    public ViHackerResult getTest(String id){
        return ViHackerResult.success();
    }

    @PostMapping("test12")
    //@PreAuthorize("hasAuthority('test:view')")
    public ViHackerApiResult getTest1(){

        AdminAuthUser current = UserContext.current();
        return ViHackerApiResult.data(current);
    }
}
