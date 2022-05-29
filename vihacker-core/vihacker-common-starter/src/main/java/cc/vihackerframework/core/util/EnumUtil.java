package cc.vihackerframework.core.util;

import cc.vihackerframework.core.entity.enums.EnumMessage;

/**
 * 枚举工具类
 *
 * @author Ranger
 * @email wilton.icp@gmail.com
 * @since 2021/3/12
 */
public class EnumUtil {

    public static <T extends EnumMessage> T getEnumByCode(Class<T> clazz, String code) {
        for (T inner : clazz.getEnumConstants()) {
            if (code.equals(inner.getCode())) {
                return inner;
            }
        }
        return null;
    }

}
