package cc.vihackerframework.uaa.entity.enums;

import cc.vihackerframework.core.entity.enums.EnumMessage;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Created by Ranger on 2022/5/27.
 */
@Getter
@AllArgsConstructor
public enum LoginType implements EnumMessage {

    /**
     * 用户名
     */
    USERNAME("1","username"),

    /**
     * 手机号码
     */
    MOBILE("2","mobile");

    /**
     * 类型
     */
    final String type;

    /**
     * 名称
     */
    final String desc;

    @Override
    public String getCode() {
        return type;
    }

    @Override
    public String getDesc() {
        return desc;
    }
}
