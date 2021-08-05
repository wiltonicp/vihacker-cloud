package com.vihackerframework.core.entity.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

import java.util.Objects;

/**
 * <p>用户状态
 *
 * @author Ranger
 * @email wilton.icp@gmail.com
 * @since 2021/3/12
 */
@Getter
public enum StatusEnum implements EnumMessage {

    STATUS_LOCK(0L, "锁定"),
    STATUS_VALID(1L, "有效");

    /**
     * 标记数据库存的值是code
     */
    @EnumValue
    private final Long code;
    @JsonValue
    private final String title;

    StatusEnum(Long code, String title) {
        this.code = code;
        this.title = title;
    }

    @JsonCreator
    public static StatusEnum getByCode(long code) {
        for (StatusEnum value : StatusEnum.values()) {
            if (Objects.equals(code, value.getCode())) {
                return value;
            }
        }
        return null;
    }


    @Override
    public String getDesc() {
        return null;
    }
}
