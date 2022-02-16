package cc.vihackerframework.uaa.controller;

import cc.vihackerframework.core.api.ViHackerResult;
import cc.vihackerframework.core.exception.ValidateCodeException;
import cc.vihackerframework.uaa.service.ValidateCodeService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.oauth2.provider.token.ConsumerTokenServices;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
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

    private final ValidateCodeService validateCodeService;
    private final ConsumerTokenServices consumerTokenServices;

    /**
     * 生成验证码
     * @param request
     * @param response
     * @throws IOException
     * @throws ValidateCodeException
     */
    @GetMapping("captcha")
    @ApiOperation(value = "生成验证码")
    public void captcha(HttpServletRequest request, HttpServletResponse response) throws IOException, ValidateCodeException {
        validateCodeService.create(request, response);
    }

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
