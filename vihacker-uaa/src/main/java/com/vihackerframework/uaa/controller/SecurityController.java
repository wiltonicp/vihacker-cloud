package com.vihackerframework.uaa.controller;

import com.vihackerframework.core.api.ViHackerResult;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.oauth2.provider.token.ConsumerTokenServices;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

/**
 * <p>
 *
 * @author Ranger
 * @email wilton.icp@gmail.com
 * @since 2021/6/8
 */
@Controller
@RequiredArgsConstructor
public class SecurityController {

    private final ConsumerTokenServices consumerTokenServices;


    @GetMapping("user/info")
    public@ResponseBody Principal currentUser(Principal principal) {
        return principal;
    }

    @GetMapping("login")
    public String login() {
        return "login";
    }

    @DeleteMapping("signout")
    public @ResponseBody
    ViHackerResult signout(HttpServletRequest request, @RequestHeader("Authorization") String token) {
        token = StringUtils.replace(token, "bearer ", "");
        consumerTokenServices.revokeToken(token);
        return ViHackerResult.success("signout");
    }
}
