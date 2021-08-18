package cc.vihackerframework.core.elasticsearch.annotation;

import cc.vihackerframework.core.elasticsearch.enums.ESFieldType;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * @author Ranger
 * @since 2021/5/2
 * @email wilton.icp@gmail.com
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Documented
@Inherited
public @interface ESField {

    @AliasFor("name")
    String value() default "";

    @AliasFor("value")
    String name() default "";

    ESFieldType type();

    String analyzer() default "";
}
