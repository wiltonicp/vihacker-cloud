package cc.vihackerframework.system.demo.controller;

import cc.vihackerframework.core.api.ViHackerResult;
import cc.vihackerframework.core.auth.entity.AdminAuthUser;
import cc.vihackerframework.core.auth.util.ViHackerAuthUser;
import cc.vihackerframework.core.util.SecurityUtil;
import cc.vihackerframework.core.annotation.log.LogEndpoint;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Ranger on 2022/2/22
 */
@RestController
public class DemoController {

    @PostMapping("test1")
    @LogEndpoint(value = "测试日志保存",exception = "获取用户信息异常")
    public ViHackerResult getTest(String id){
        return ViHackerResult.success();
    }

    @PostMapping("test")
    public ViHackerResult getUser(HttpServletRequest request){
        AdminAuthUser loginUser = ViHackerAuthUser.getUser();

        return ViHackerResult.data(loginUser);
    }
}
