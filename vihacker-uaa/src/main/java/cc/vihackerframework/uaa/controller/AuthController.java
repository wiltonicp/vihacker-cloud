package cc.vihackerframework.uaa.controller;

import cc.vihackerframework.core.api.ViHackerApiResult;
import cc.vihackerframework.core.auth.util.ViHackerAuthUser;
import cc.vihackerframework.core.exception.ValidateCodeException;
import cc.vihackerframework.core.log.annotation.LogEndpoint;
import cc.vihackerframework.uaa.service.ValidateCodeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.oauth2.provider.token.ConsumerTokenServices;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import java.io.IOException;

/**
 * 认证控制类
 * Created by Ranger on 2022/3/19
 */
@Slf4j
@Validated
@RestController
@AllArgsConstructor
@RequestMapping("/auth")
@Api(tags = "认证管理")
public class AuthController {

    private final ValidateCodeService validateCodeService;
    @Qualifier("consumerTokenServices")
    private final ConsumerTokenServices consumerTokenServices;


    @GetMapping("/user")
    @LogEndpoint(value = "用户信息",exception = "用户信息请求异常")
    @ApiOperation(value = "用户信息", notes = "用户信息")
    public ViHackerApiResult currentUser() {
        return ViHackerApiResult.data(ViHackerAuthUser.getUser());
    }

    /**
     * 验证码获取
     * @param request
     * @param response
     * @throws IOException
     * @throws ValidateCodeException
     */
    @GetMapping("/captcha")
    @LogEndpoint(value = "验证码获取",exception = "验证码获取请求异常")
    @ApiOperation(value = "验证码获取", notes = "验证码获取")
    public void captcha(HttpServletRequest request, HttpServletResponse response) throws IOException, ValidateCodeException {
        validateCodeService.getCode(request, response);
    }

    /**
     * 手机验证码下发
     * @param mobile 手机号码
     * @return
     */
    @GetMapping("/sms-code")
    @LogEndpoint(value = "手机验证码下发",exception = "手机验证码下发请求异常")
    @ApiOperation(value = "手机验证码下发", notes = "手机验证码下发")
    public ViHackerApiResult smsCode(@NotBlank(message = "{required}") String mobile){
        return validateCodeService.getSmsCode(mobile);
    }


    @LogEndpoint(value = "退出登录", exception = "退出登录请求异常")
    @ApiOperation(value = "退出登录", notes = "退出登录")
    @DeleteMapping("/logout")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", required = true, value = "授权类型", paramType = "header")
    })
    public ViHackerApiResult signout(@RequestHeader("Authorization") String token) {
        token = StringUtils.replace(token, "bearer ", "");
        consumerTokenServices.revokeToken(token);
        return ViHackerApiResult.success("退出登录成功");
    }


}
