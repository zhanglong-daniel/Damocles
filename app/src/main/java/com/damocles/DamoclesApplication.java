package com.damocles;

import com.damocles.common.base.CommonApplication;

/**
 * Created by zhanglong02 on 16/9/8.
 */
public class DamoclesApplication extends CommonApplication {

    private static DamoclesApplication INSTANCE;

    public static boolean isNightMode = false;

    public static DamoclesApplication getInstance() {
        return INSTANCE;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        INSTANCE = this;
    }

}
