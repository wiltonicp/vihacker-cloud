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
            this.strictInsertFill(metaObject, "createdBy", Long.class, current.getUserId());
            this.strictInsertFill(metaObject, "modifyBy", Long.class, current.getUserId());
        }else{
            this.strictInsertFill(metaObject, "createdBy", Long.class, 0L);
            this.strictInsertFill(metaObject, "modifyBy", Long.class, 0L);
        }
        this.strictInsertFill(metaObject, "version", Long.class, 0L);
        this.strictInsertFill(metaObject, "deleted", Integer.class, 0);
        this.strictInsertFill(metaObject, "createdTime", LocalDateTime.class, LocalDateTime.now());
        this.strictInsertFill(metaObject, "modifyTime", LocalDateTime.class, LocalDateTime.now());
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        CurrentUser current = UserContext.current();
        if(ObjectUtils.isNotEmpty(current)){
            this.strictInsertFill(metaObject, "modifyBy", Long.class, current.getUserId());
        }else{
            this.strictInsertFill(metaObject, "modifyBy", Long.class, 0L);
        }
        this.strictInsertFill(metaObject, "modifyTime", LocalDateTime.class, LocalDateTime.now());
    }
}
