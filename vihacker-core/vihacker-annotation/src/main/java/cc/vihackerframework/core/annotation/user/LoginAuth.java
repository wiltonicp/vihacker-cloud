package cc.vihackerframework.core.annotation.user;

import java.lang.annotation.*;

/**
 * 自动获取用户信息注解
 * Created by Ranger on 2022/5/4.
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LoginAuth {
}
