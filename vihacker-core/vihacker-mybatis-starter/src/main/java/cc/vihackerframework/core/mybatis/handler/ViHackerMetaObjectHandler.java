package cc.vihackerframework.core.mybatis.handler;

import cc.vihackerframework.core.context.UserContext;
import cc.vihackerframework.core.entity.CurrentUser;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * <p>
 *
 * @author Ranger
 * @email wilton.icp@gmail.com
 * @since 2021/8/13
 */
@Component
public class ViHackerMetaObjectHandler implements MetaObjectHandler {


    @Override
    public void insertFill(MetaObject metaObject) {
        CurrentUser current = UserContext.current();
        if(ObjectUtils.isNotEmpty(current)){
            this.setFieldValByName("createdBy", current.getUserId(), metaObject);
            this.setFieldValByName("modifyBy", current.getUserId(), metaObject);
        }else{
            this.setFieldValByName("createdBy", 0L, metaObject);
            this.setFieldValByName("modifyBy", 0L, metaObject);
        }
        this.setFieldValByName("version", 0L, metaObject);
        this.setFieldValByName("deleted", 0, metaObject);
        this.setFieldValByName("createdTime", LocalDateTime.now(), metaObject);
        this.setFieldValByName("modifyTime", LocalDateTime.now(), metaObject);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        CurrentUser current = UserContext.current();
        if(ObjectUtils.isNotEmpty(current)){
            this.setFieldValByName("createdBy", current.getUserId(), metaObject);
            this.setFieldValByName("modifyBy", current.getUserId(), metaObject);
        }else{
            this.setFieldValByName("createdBy", 0L, metaObject);
            this.setFieldValByName("modifyBy", 0L, metaObject);
        }
    }
}
