package cc.vihackerframework.uaa.controller;

import cc.vihackerframework.core.api.ViHackerApiResult;
import cc.vihackerframework.core.exception.ValidateCodeException;
import cc.vihackerframework.core.annotation.log.LogEndpoint;
import cc.vihackerframework.core.util.RequestUtil;
import cc.vihackerframework.uaa.properties.ValidateCodeProperties;
import cc.vihackerframework.uaa.service.ValidateCodeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.endpoint.TokenEndpoint;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by Ranger on 2022/5/26.
 */
@RestController
@RequestMapping("/oauth")
@AllArgsConstructor
@Api(tags = "Oauth2接口管理")
public class OauthController {

    private final TokenEndpoint tokenEndpoint;
    private final ValidateCodeProperties properties;
    private final ValidateCodeService validateCodeService;

    @LogEndpoint(value = "用户登录", exception = "用户登录请求异常")
    @GetMapping("/token")
    @ApiOperation(value = "用户登录Get", notes = "用户登录Get")
    public ViHackerApiResult getAccessToken(Principal principal, @RequestParam Map<String, String> parameters) throws HttpRequestMethodNotSupportedException, ValidateCodeException {
        if(properties.getEnable() && StringUtils.equalsIgnoreCase(parameters.get("grant_type"),"password")){
            validateCode(RequestUtil.getHttpServletRequest());
        }
        return custom(tokenEndpoint.getAccessToken(principal, parameters).getBody());
    }

    @LogEndpoint(value = "用户登录", exception = "用户登录请求异常")
    @PostMapping("/token")
    @ApiOperation(value = "用户登录Post", notes = "用户登录Post")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "grant_type", required = true, value = "授权类型", paramType = "query"),
            @ApiImplicitParam(name = "username", required = false, value = "用户名", paramType = "query"),
            @ApiImplicitParam(name = "password", required = false, value = "密码", paramType = "query"),
            @ApiImplicitParam(name = "scope", required = true, value = "使用范围", paramType = "query"),
    })
    public ViHackerApiResult postAccessToken(Principal principal, @RequestBody Map<String, String> parameters) throws HttpRequestMethodNotSupportedException, ValidateCodeException {
        if(properties.getEnable() && StringUtils.equalsIgnoreCase(parameters.get("grant_type"),"password")){
            validateCode(RequestUtil.getHttpServletRequest());
        }
        return custom(tokenEndpoint.postAccessToken(principal, parameters).getBody());
    }

    private void validateCode(HttpServletRequest httpServletRequest) throws ValidateCodeException {
        String code = httpServletRequest.getHeader("code");
        String key = httpServletRequest.getHeader("key");
        validateCodeService.check(key, code);
    }

    /**
     * 自定义返回格式
     *
     * @param accessToken 　Token
     * @return Result
     */
    private ViHackerApiResult custom(OAuth2AccessToken accessToken) {
        DefaultOAuth2AccessToken token = (DefaultOAuth2AccessToken) accessToken;
        Map<String, Object> data = new LinkedHashMap<>(token.getAdditionalInformation());
        data.put("accessToken", token.getValue());
        if (token.getRefreshToken() != null) {
            data.put("refreshToken", token.getRefreshToken().getValue());
        }
        return ViHackerApiResult.data(data);
    }
}
