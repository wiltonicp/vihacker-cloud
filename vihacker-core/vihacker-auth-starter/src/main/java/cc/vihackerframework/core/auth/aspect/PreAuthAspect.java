package cc.vihackerframework.core.auth.aspect;

import cc.vihackerframework.core.annotation.auth.PreAuth;
import cc.vihackerframework.core.api.ResultCode;
import cc.vihackerframework.core.constant.Oauth2Constant;
import cc.vihackerframework.core.entity.CurrentUser;
import cc.vihackerframework.core.exception.Asserts;
import cc.vihackerframework.core.util.SecurityUtil;
import cc.vihackerframework.core.util.StringUtil;
import cn.hutool.core.bean.BeanUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.util.PatternMatchUtils;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Set;

/**
 * AOP 鉴权
 * Created by Ranger on 2022/6/11.
 */
@Slf4j
@Aspect
public class PreAuthAspect {

    private ApplicationContext ac;
    private final HttpServletRequest request;

    public PreAuthAspect(HttpServletRequest request) {
        this.request = request;
    }

    /**
     * 方法和类上的 @PreAuth 注解
     * @param point
     * @return
     * @throws Throwable
     */
    @Around("@annotation(cc.vihackerframework.core.annotation.auth.PreAuth)")
    public Object preAuth(ProceedingJoinPoint point) throws Throwable {
        hasAuth(point);
        return point.proceed();
    }

    private void hasAuth(ProceedingJoinPoint point){
        Environment env = ac.getEnvironment();
        Boolean property = env.getProperty("vihacker.security.enabled", Boolean.class, false);
        if (!property) {
            log.debug("全局校验权限已经关闭");
            return;
        }
        MethodSignature ms = (MethodSignature) point.getSignature();
        Method method = ms.getMethod();

        // 读取权限注解，优先方法上，没有则读取类
        PreAuth preAuth = null;
        if (point.getSignature() instanceof MethodSignature) {
            method = ((MethodSignature) point.getSignature()).getMethod();
            if (method != null) {
                preAuth = method.getAnnotation(PreAuth.class);
            }
        }
        String methodName = method != null ? method.getName() : "";

        // 读取目标类上的权限注解
        PreAuth targetClass = point.getTarget().getClass().getAnnotation(PreAuth.class);
        //方法和类上 均无注解
        if (preAuth == null && targetClass == null) {
            log.debug("执行方法[{}]无需校验权限", methodName);
            return;
        }

        // 方法上禁用
        if (preAuth != null && !preAuth.enabled()) {
            log.debug("执行方法[{}]无需校验权限", methodName);
            return;
        }

        // 类上禁用
        if (targetClass != null && !targetClass.enabled()) {
            log.debug("执行方法[{}]无需校验权限", methodName);
            return;
        }

        if(!hasAuthority(preAuth.hasAuthority())){
            Asserts.fail(ResultCode.FORBIDDEN.build("执行方法[%s]需要[%s]权限",methodName,preAuth.hasAuthority()));
        }

        if(!hasRole(preAuth.hasRole())){
            Asserts.fail(ResultCode.FORBIDDEN.build("执行方法[%s]需要[%s]角色",methodName,preAuth.hasRole()));
        }
        return;
    }

    private boolean hasRole(String role) {
        CurrentUser currentUser = SecurityUtil.getCurrentUser(request);
        if(BeanUtil.isEmpty(currentUser) || StringUtil.isBlank(role)){
            return true;
        }
        // 如果用户是超级管理员，则直接跳过权限验证
        if (currentUser.getAccount().equalsIgnoreCase(Oauth2Constant.ADMIN)) {
            return true;
        }

        if(StringUtil.equalsIgnoreCase(currentUser.getRoleCode(),role)){
            return true;
        }
        return false;
    }

    private boolean hasAuthority(String authority){
        CurrentUser currentUser = SecurityUtil.getCurrentUser(request);
        if(BeanUtil.isEmpty(currentUser) || StringUtil.isBlank(authority)){
            return true;
        }
        // 如果用户是超级管理员，则直接跳过权限验证
        if (currentUser.getAccount().equalsIgnoreCase(Oauth2Constant.ADMIN)) {
            return true;
        }
        Set<GrantedAuthority> authorities = currentUser.getAuthorities();
        return AuthorityUtils.authorityListToSet(authorities).stream().filter(StringUtils::hasText)
                .anyMatch(x -> PatternMatchUtils.simpleMatch(authority, x));
    }
}
