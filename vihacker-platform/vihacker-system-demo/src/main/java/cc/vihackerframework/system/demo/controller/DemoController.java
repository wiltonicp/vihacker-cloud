package cc.vihackerframework.system.demo.controller;

import cc.vihackerframework.core.api.ViHackerResult;
import cc.vihackerframework.log.starter.annotation.LogEndpoint;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
