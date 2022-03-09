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
            this.setFieldValByName("createdBy", adminAuthUser.getUserId(), metaObject);
            this.setFieldValByName("modifyBy", adminAuthUser.getUserId(), metaObject);
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
        AdminAuthUser adminAuthUser = SecurityUtil.getLoginUser();
        if(ObjectUtils.isNotEmpty(adminAuthUser)){
            this.setFieldValByName("createdBy", adminAuthUser.getUserId(), metaObject);
            this.setFieldValByName("modifyBy", adminAuthUser.getUserId(), metaObject);
        }else{
            this.setFieldValByName("createdBy", 0L, metaObject);
            this.setFieldValByName("modifyBy", 0L, metaObject);
        }
    }
}
