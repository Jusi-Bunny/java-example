package com.example.mybatis.enums;

import com.baomidou.mybatisplus.annotation.IEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum GenderEnum implements IEnum<Integer> {

    UNKNOWN(0, "未知"),
    MALE(1, "男"),
    FEMALE(2, "女");

    // @EnumValue
    // @JsonValue
    private final Integer code;

    private final String desc;

    @Override
    public Integer getValue() {
        return this.code;
    }

    public static GenderEnum of(Integer code) {
        if (code == null) {
            return null;
        }

        for (GenderEnum item : values()) {
            if (item.code.equals(code)) {
                return item;
            }
        }

        throw new IllegalArgumentException("未知账号状态：" + code);
    }
}
