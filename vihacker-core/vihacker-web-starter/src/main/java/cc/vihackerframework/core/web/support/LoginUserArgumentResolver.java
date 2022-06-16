package cc.vihackerframework.core.web.support;

import cc.vihackerframework.core.annotation.user.LoginAuth;
import cc.vihackerframework.core.entity.CurrentUser;
import cc.vihackerframework.core.util.SecurityUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;

/**
 * 获取用户信息
 * Created by Ranger on 2022/5/4.
 * @link https://blog.csdn.net/aiyaya_/article/details/79221733
 */
@Slf4j
@AllArgsConstructor
public class LoginUserArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        boolean isHasEnableUserAnn = parameter.hasParameterAnnotation(LoginAuth.class);
        boolean isHasLoginUserParameter = parameter.getParameterType().isAssignableFrom(CurrentUser.class);
        return isHasEnableUserAnn && isHasLoginUserParameter;
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory) throws Exception {
        LoginAuth user = methodParameter.getParameterAnnotation(LoginAuth.class);
        HttpServletRequest request = nativeWebRequest.getNativeRequest(HttpServletRequest.class);
        CurrentUser currentUser = SecurityUtil.getCurrentUser(request);
        return currentUser;
    }
}
