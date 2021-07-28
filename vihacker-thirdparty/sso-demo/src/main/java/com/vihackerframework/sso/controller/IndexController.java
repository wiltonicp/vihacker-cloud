package com.vihackerframework.sso.controller;

import com.vihackerframework.core.api.ViHackerResult;
import com.vihackerframework.core.entity.system.AdminAuthUser;
import com.vihackerframework.core.util.SecurityUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * @author Ranger
 * @email wilton.icp@gmail.com
 * @since 2021/6/17
 */
@RestController
public class IndexController {

    @GetMapping("test")
    public ViHackerResult test(){
        AdminAuthUser loginUser = SecurityUtil.getLoginUser();
        return ViHackerResult.data(loginUser);
    }
}
