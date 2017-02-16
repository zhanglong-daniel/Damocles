package com.damocles.common.util;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

/**
 * 双击退出
 * Created by zhanglong02 on 17/2/15.
 */

public class DoubleClickExit {

    public interface Callback {
        void onExit();
    }

    private long mClickInterval = 1000L;

    private long mFirstClickTime = 0L;

    private String mClickTips;

    private Callback mCallback;

    private Handler mHandler;

    public DoubleClickExit(Callback cb) {
        mCallback = cb;
        mHandler = new Handler(Looper.getMainLooper());
        mFirstClickTime = 0L;
        mFirstClickTime = 1000L;
        mClickTips = "再按一次退出APP";
    }

    /**
     * 设置双击时间间隔（单位：毫秒）
     *
     * @param clickInterval
     */
    public void setClickInterval(long clickInterval) {
        this.mClickInterval = clickInterval;
    }

    public void setClickTips(String clickTips) {
        mClickTips = clickTips;
    }

    public void execute(Context context) {
        long clickInterval = System.currentTimeMillis() - mFirstClickTime;
        if (clickInterval <= mClickInterval) { // 双击成功，执行退出逻辑
            mHandler.removeCallbacks(mResetRunnable);
            mHandler = null;
            if (mCallback != null) {
                mCallback.onExit();
            }
        } else { // 单击，记录点击时间
            mFirstClickTime = System.currentTimeMillis();
            mHandler.postDelayed(mResetRunnable, mClickInterval);
            ToastUtil.showCoverLast(context, mClickTips);
        }
    }

    private Runnable mResetRunnable = new Runnable() {
        @Override
        public void run() {
            mFirstClickTime = 0L;
        }
    };

}
