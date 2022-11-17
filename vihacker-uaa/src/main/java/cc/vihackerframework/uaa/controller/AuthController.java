package cc.vihackerframework.uaa.controller;

import cc.vihackerframework.core.api.ViHackerApiResult;
import cc.vihackerframework.core.auth.entity.UserInfo;
import cc.vihackerframework.core.entity.CurrentUser;
import cc.vihackerframework.core.exception.ValidateCodeException;
import cc.vihackerframework.core.annotation.log.LogEndpoint;
import cc.vihackerframework.core.util.SecurityUtil;
import cc.vihackerframework.uaa.entity.enums.LoginType;
import cc.vihackerframework.uaa.manager.AdminUserManager;
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
import java.util.HashMap;
import java.util.Map;

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

    private final AdminUserManager adminUserManager;
    private final ValidateCodeService validateCodeService;
    @Qualifier("consumerTokenServices")
    private final ConsumerTokenServices consumerTokenServices;

    @GetMapping("/user")
    @LogEndpoint(value = "用户信息",exception = "用户信息请求异常")
    @ApiOperation(value = "用户信息", notes = "用户信息")
    public ViHackerApiResult currentUser(HttpServletRequest request) {
        CurrentUser currentUser = SecurityUtil.getCurrentUser(request);
        UserInfo userInfo = null;

        if(currentUser.getType().equals(LoginType.USERNAME.getType())){
            userInfo = adminUserManager.findByName(currentUser.getAccount());
        }else {
            userInfo = adminUserManager.findByMobile(currentUser.getAccount());
        }

        Map<String, Object> data = new HashMap<>(8);
        data.put("userName", currentUser.getAccount());
        data.put("nickName", userInfo.getSysUser().getNickName());
        data.put("realName", userInfo.getSysUser().getRealName());
        data.put("avatar", userInfo.getSysUser().getAvatar());
        data.put("roleId", userInfo.getSysUser().getRoleId());
        data.put("departId", userInfo.getSysUser().getDeptId());
        data.put("tenantId", userInfo.getSysUser().getTenantId());
        data.put("permissions", userInfo.getPermissions());
        return ViHackerApiResult.data(data);
    }


    @LogEndpoint(value = "验证码获取", exception = "验证码获取请求异常")
    @GetMapping("/code")
    @ApiOperation(value = "验证码获取", notes = "验证码获取")
    public ViHackerApiResult authCode() {
        return validateCodeService.getCode();
    }

    /**
     * 验证码获取 流模式
     * @param request
     * @param response
     * @throws IOException
     * @throws ValidateCodeException
     */
    @GetMapping("/captcha")
    @LogEndpoint(value = "验证码获取 流模式",exception = "验证码获取请求异常")
    @ApiOperation(value = "验证码获取 流模式", notes = "验证码获取")
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
