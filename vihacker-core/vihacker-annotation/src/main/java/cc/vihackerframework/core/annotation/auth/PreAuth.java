package cc.vihackerframework.core.annotation.auth;

import java.lang.annotation.*;

/**
 * 权限注解
 * Created by Ranger on 2022/6/11.
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface PreAuth {

    /**
     * 是否启用权限拦截
     *
     * @return 是否启用
     */
    boolean enabled() default true;

    /**
     * url权限
     * @return
     */
    String hasAuthority() default "";

    /**
     * 角色编码
     * @return
     */
    String hasRole() default "";
}
