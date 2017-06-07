package com.damocles.common.util;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

/**
 * 双击退出
 * Created by danielzhang on 17/2/15.
 */

public class DoubleClickExit {

    public interface Callback {
        void onExit();
    }

    private Context context;
    /**
     * 双击时间间隔（单位：毫秒）
     */
    private long clickInterval = 1000L;

    private long firstClickTime = 0L;

    private String clickTips;

    private Callback callback;

    private Handler handler;

    private DoubleClickExit() {
        handler = new Handler(Looper.getMainLooper());
        firstClickTime = 0L;
    }

    public void execute() {
        long clickInterval = System.currentTimeMillis() - firstClickTime;
        if (clickInterval <= clickInterval) { // 双击成功，执行退出逻辑
            handler.removeCallbacks(mResetRunnable);
            handler = null;
            if (callback != null) {
                callback.onExit();
            }
        } else { // 单击，记录点击时间
            firstClickTime = System.currentTimeMillis();
            handler.postDelayed(mResetRunnable, clickInterval);
            Toast.makeText(context, clickTips, Toast.LENGTH_SHORT).show();
        }
    }

    private Runnable mResetRunnable = new Runnable() {
        @Override
        public void run() {
            firstClickTime = 0L;
        }
    };

    public static class Builder {

        private Context mContext;
        private long mClickInterval = 1000L;
        private String mClickTips;
        private Callback mCallback;

        public Builder(Context context) {
            mContext = context;
            mClickInterval = 1000L;
            mClickTips = "再按一次退出APP";
            mCallback = null;
        }

        public Builder setClickInterval(long clickInterval) {
            mClickInterval = clickInterval;
            return this;
        }

        public Builder setClickTips(String clickTips) {
            mClickTips = clickTips;
            return this;
        }

        public Builder setCallback(Callback callback) {
            mCallback = callback;
            return this;
        }

        public DoubleClickExit build() {
            DoubleClickExit doubleClickExit = new DoubleClickExit();
            doubleClickExit.context = mContext;
            doubleClickExit.clickInterval = mClickInterval;
            doubleClickExit.clickTips = mClickTips;
            doubleClickExit.callback = mCallback;
            return doubleClickExit;
        }
    }

}
