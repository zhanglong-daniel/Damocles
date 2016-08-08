package com.damocles.java.util;

import java.util.Random;

/**
 * Created by zhanglong02 on 16/1/6.
 */
public class Enums {

    /**
     * 随机获取enum实例
     *
     * @param enumClass
     * @param <T>
     *
     * @return
     */
    public static <T extends Enum<T>> T random(Class<T> enumClass) {
        T[] values = enumClass.getEnumConstants();
        if (values.length < 1) {
            throw new IllegalArgumentException(enumClass.getSimpleName() + " has no constant");
        }
        return values[new Random().nextInt(values.length)];
    }

}
