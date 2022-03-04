package cc.vihackerframework.system.controller;

import cc.vihackerframework.core.api.ViHackerResult;
import cc.vihackerframework.log.starter.annotation.LogEndpoint;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author jiangshanchen
 * @title: FeignConfig
 * @projectName vihacker-cloud-master
 * @description: TODO
 * @date 2022/3/1下午5:26
 */
@RestController
public class TestController {

    @PostMapping("test1")
    //@PreAuthorize("hasAuthority('test:view')")
    @LogEndpoint(value = "测试日志保存",exception = "获取用户信息异常")
    public ViHackerResult getTest(String id){
        return ViHackerResult.success();
    }
}
