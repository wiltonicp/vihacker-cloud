package com.vihackerframework.auth.controller;

import com.vihackerframework.core.api.ViHackerResult;
import com.vihackerframework.core.util.ViHackerSecurityUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *
 * @author Ranger
 * @email wilton.icp@gmail.com
 * @since 2021/6/7
 */
@RestController
public class TestController {

    @GetMapping("auth/user")
    public ViHackerResult user() {
        return ViHackerResult.data(ViHackerSecurityUtil.getLoginUser());
    }


    @GetMapping("test")
    public ViHackerResult test() {
        return ViHackerResult.success();
    }
}
