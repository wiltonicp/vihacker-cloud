package cc.vihackerframework.uaa.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

/**
 * <p>
 * @author Ranger
 * @email wilton.icp@gmail.com
 * @since 2021/6/8
 */
@Controller
@RequiredArgsConstructor
public class SecurityController {

    @GetMapping("user")
    public@ResponseBody Principal currentUser(Principal principal) {
        return principal;
    }

    @GetMapping("login")
    public String login() {
        return "login";
    }


}
