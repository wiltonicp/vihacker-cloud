package cc.vihackerframework.system.controller;

import cc.vihackerframework.core.api.ViHackerApiResult;
import cc.vihackerframework.core.auth.context.UserContext;
import cc.vihackerframework.core.auth.entity.AdminAuthUser;
import cc.vihackerframework.core.auth.util.SecurityUtil;
import cc.vihackerframework.core.log.annotation.LogEndpoint;
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

        AdminAuthUser current = UserContext.current();
        AdminAuthUser loginUser = SecurityUtil.getLoginUser();
        return ViHackerApiResult.data(current);
    }
}
