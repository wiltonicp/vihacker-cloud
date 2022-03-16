package cc.vihackerframework.resource.starter.interceptor;

import cc.vihackerframework.core.auth.context.UserContext;
import cc.vihackerframework.core.auth.entity.AdminAuthUser;
import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;

/**
 * Created By Ranger on 2022/3/16
 */
@Slf4j
public class UserInfoInterceptor implements HandlerInterceptor {

    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String authorization = request.getHeader("Authorization");
        authorization = StrUtil.removePrefix(authorization, "bearer ");
        authorization = StrUtil.removePrefix(authorization, "Bearer ");
        if (StrUtil.isNotBlank(authorization)) {
            Jwt decode = JwtHelper.decode(authorization);
            //验签
//            Jwt secretKey = JwtHelper.decodeAndVerify(authorization, new MacSigner("secretKey"));
            String claims = decode.getClaims();
            HashMap<String, Object> hashMap = objectMapper.readValue(claims, HashMap.class);
            System.out.println(hashMap);
            //Object userName = hashMap.get("user_name");
            //Object authorities = hashMap.get("authorities");

            UserContext.setUserInfo(null);
        }
        return true;
    }
}
