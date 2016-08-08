package com.damocles.android.base;

import android.app.Application;
import android.content.Context;
import android.util.Log;

/**
 * Created by zhanglong02 on 16/3/3.
 */
public class DamoclesApplication extends Application {

    private static DamoclesApplication INSTANCE;

    public static DamoclesApplication getInstance() {
        return INSTANCE;
    }

    public boolean isNightMode = false;

    @Override
    protected void attachBaseContext(Context base) {
        Log.e("fuck", "attachBaseContext()");
        super.attachBaseContext(base);
    }

    @Override
    public void onCreate() {
        Log.e("fuck", "onCreate()");
        super.onCreate();
        INSTANCE = this;
    }
}
