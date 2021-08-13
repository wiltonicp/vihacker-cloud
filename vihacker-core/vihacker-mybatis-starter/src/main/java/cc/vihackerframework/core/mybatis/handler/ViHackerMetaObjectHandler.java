package cc.vihackerframework.core.mybatis.handler;

import cc.vihackerframework.core.auth.entity.AdminAuthUser;
import cc.vihackerframework.core.auth.util.SecurityUtil;
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
        AdminAuthUser adminAuthUser = SecurityUtil.getLoginUser();
        if(ObjectUtils.isNotEmpty(adminAuthUser)){
            this.strictInsertFill(metaObject, "createdBy", Long.class, adminAuthUser.getUserId());
            this.strictInsertFill(metaObject, "modifyBy", Long.class, adminAuthUser.getUserId());
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
        AdminAuthUser adminAuthUser = SecurityUtil.getLoginUser();
        if(ObjectUtils.isNotEmpty(adminAuthUser)){
            this.strictInsertFill(metaObject, "createdBy", Long.class, adminAuthUser.getUserId());
            this.strictInsertFill(metaObject, "modifyBy", Long.class, adminAuthUser.getUserId());
        }else{
            this.strictInsertFill(metaObject, "createdBy", Long.class, 0L);
            this.strictInsertFill(metaObject, "modifyBy", Long.class, 0L);
        }
    }
}
