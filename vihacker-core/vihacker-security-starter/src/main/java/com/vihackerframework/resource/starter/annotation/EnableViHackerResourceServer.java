package com.vihackerframework.resource.starter.annotation;

import com.vihackerframework.resource.starter.configure.ViHackerResourceServerConfigure;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 资源服务注解
 *
 * @author Ranger
 * @email wilton.icp@gmail.com
 * @since 2021/6/15
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(ViHackerResourceServerConfigure.class)
public @interface EnableViHackerResourceServer {
}
