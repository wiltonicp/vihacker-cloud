package cc.vihackerframework.core.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 菜单类型
 * Created by Ranger on 2022/02/28.
 */
@Getter
@AllArgsConstructor
public enum MenuTypeEnum implements EnumMessage{

    /**
     * 目录
     */
    LIB("0", "目录"),
    /**
     * 菜单
     */
    MENU("1", "菜单"),
    /**
     * 按钮
     */
    BUTTON("2", "按钮");

    private final String code;

    private final String message;


    @Override
    public String getDesc() {
        return message;
    }
}
