package cc.vihackerframework.system.controller;

import cc.vihackerframework.core.api.ViHackerApiResult;
import cc.vihackerframework.core.context.UserContext;
import cc.vihackerframework.core.entity.CurrentUser;
import cc.vihackerframework.core.annotation.log.LogEndpoint;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Ranger on 2022/2 */
@RestController
public class TestController {

    @PostMapping("test1")
    //@PreAuthorize("hasAuthority('test:view')")
    @LogEndpoint(value = "测试日志保存",exception = "获取用户信息异常")
    public ViHackerApiResult getTest(String id){
        return ViHackerApiResult.success();
    }

    @PostMapping("test12")
    //@PreAuthorize("hasAuthority('test:view')")
    public ViHackerApiResult getTest1(){

        CurrentUser current = UserContext.current();
        return ViHackerApiResult.data(current);
    }
}
