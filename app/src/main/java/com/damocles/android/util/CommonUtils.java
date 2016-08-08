package com.damocles.android.util;

import android.os.Handler;
import android.os.Looper;

/**
 * Created by zhanglong02 on 16/3/9.
 */
public class CommonUtils {

    /**
     * 在主线程中执行任务
     *
     * @param r
     */
    public void runOnUiThread(Runnable r) {
        if (Thread.currentThread() == Looper.getMainLooper().getThread()) {
            r.run();
        } else {
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(r);
        }

    }
}
