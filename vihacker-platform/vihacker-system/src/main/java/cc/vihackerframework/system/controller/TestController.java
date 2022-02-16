package cc.vihackerframework.system.controller;

import cc.vihackerframework.core.api.ViHackerResult;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Ranger on 2022/2 */
@RestController
public class TestController {

    @PostMapping("test1")
    @PreAuthorize("hasAuthority('test:view')")
    public ViHackerResult getTest(){
        return ViHackerResult.success();
    }
}
