package cc.vihackerframework.core.entity.enums;

import com.baomidou.mybatisplus.annotation.IEnum;

/**
 * <p> 通用枚举
 *
 * @author Ranger
 * @email wilton.icp@gmail.com
 * @since 2021/3/12
 */
public interface EnumMessage extends IEnum<String> {

    /**
     * 编码
     * @return
     */
    default String getCode(){return toString();};

    /**
     * 描述
     * @return
     */
    String getDesc();

    /**
     * 枚举值
     * @return 数据库中的值
     */
    @Override
    default String getValue() {
        return getCode();
    }
}
