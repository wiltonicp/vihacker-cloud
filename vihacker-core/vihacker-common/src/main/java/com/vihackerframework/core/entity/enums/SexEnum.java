package com.vihackerframework.core.entity.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

import java.util.Objects;

/**
 * <p>性别
 *
 * @author Ranger
 * @email wilton.icp@gmail.com
 * @since 2021/3/12
 */
@Getter
public enum SexEnum implements EnumMessage {

    SEX_MALE(0L, "男"),
    SEX_FEMALE(1L, "女"),
    SEX_UNKNOW(1L, "保密");


    /**
     * 标记数据库存的值是code
     */
    @EnumValue
    private final Long code;
    @JsonValue
    private final String title;

    SexEnum(Long code, String title) {
        this.code = code;
        this.title = title;
    }

    @JsonCreator
    public static SexEnum getByCode(long code) {
        for (SexEnum value : SexEnum.values()) {
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
