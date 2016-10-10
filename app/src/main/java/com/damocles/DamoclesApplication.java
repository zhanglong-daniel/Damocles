package com.damocles;

import android.app.Application;

/**
 * Created by zhanglong02 on 16/9/8.
 */
public class DamoclesApplication extends Application {

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
