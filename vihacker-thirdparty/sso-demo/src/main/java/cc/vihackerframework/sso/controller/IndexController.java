package cc.vihackerframework.sso.controller;

import cc.vihackerframework.core.auth.entity.AdminAuthUser;
import cc.vihackerframework.core.auth.util.SecurityUtil;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.ui.ModelMap;
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

    @GetMapping("/")
    public String test(ModelMap modelMap,Authentication authentication){
        AdminAuthUser loginUser = SecurityUtil.getLoginUser();
        modelMap.put("username", loginUser.getUsername());
        Authentication userAuthentication = SecurityUtil.getUserAuthentication();
        modelMap.put("authorities", userAuthentication.getAuthorities());
        OAuth2Authentication oauth2Authentication = (OAuth2Authentication)authentication;
        modelMap.put("clientId", oauth2Authentication.getOAuth2Request().getClientId());
        modelMap.put("token", SecurityUtil.getCurrentTokenValue());
        return "index";
    }

    @GetMapping("/user")
    public Authentication user(Authentication user){
        return user;
    }
}
