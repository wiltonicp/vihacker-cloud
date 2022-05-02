package cc.vihackerframework.uaa.controller;

import cc.vihackerframework.core.log.annotation.LogEndpoint;
import com.xkcoding.justauth.AuthRequestFactory;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.zhyd.oauth.model.AuthCallback;
import me.zhyd.oauth.model.AuthResponse;
import me.zhyd.oauth.request.AuthRequest;
import me.zhyd.oauth.utils.AuthStateUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 第三方认证
 * Created by Ranger on 2022/03/19
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("social")
@Api("第三方认证")
public class SocialLoginController {

    private final AuthRequestFactory factory;

    /**
     * 登录类型
     */
    @LogEndpoint(value = "登录类型", exception = "登录类型请求异常")
    @ApiOperation(value = "登录类型", notes = "登录类型")
    @GetMapping("/list")
    public Map<String, String> loginType() {
        List<String> oauthList = factory.oauthList();
        return oauthList.stream().collect(Collectors.toMap(oauth -> oauth.toLowerCase() + "登录", oauth -> "http://localhost:8301/vihacker-uaa/auth/login/" + oauth.toLowerCase()));
    }

    /**
     * 第三方登录
     * @param oauthType 登录类型
     * @param response  response
     * @throws IOException IO异常
     */
    @LogEndpoint(value = "第三方登录", exception = "第三方登录请求异常")
    @ApiOperation(value = "第三方登录", notes = "第三方登录")
    @GetMapping("/login/{oauthType}")
    public void login(@PathVariable String oauthType, HttpServletResponse response) throws IOException {
        AuthRequest authRequest = factory.get(oauthType);
        response.sendRedirect(authRequest.authorize(oauthType + "::" + AuthStateUtils.createState()));
    }

    /**
     * 登录成功后的回调
     * @param oauthType 第三方登录类型
     * @param callback  携带返回的信息
     * @return
     */
    @LogEndpoint(value = "第三方登录回调", exception = "第三方登录回调请求异常")
    @ApiOperation(value = "第三方登录回调", notes = "第三方登录回调")
    @GetMapping("/{oauthType}/callback")
    public AuthResponse callback(@PathVariable String oauthType, AuthCallback callback) {
        AuthRequest authRequest = factory.get(oauthType);
        AuthResponse response = authRequest.login(callback);
        log.info("【response】= {}", response);
        return response;
    }
}
