package com.damocles.navi;

import com.baidu.navisdk.adapter.BNRoutePlanNode;
import com.baidu.navisdk.adapter.BaiduNaviManager;
import com.damocles.common.log.Log;
import com.damocles.navi.callback.RoutePlanCallback;

/**
 * Created by zhanglong02 on 16/8/24.
 */
class RoutePlanListenerImpl implements BaiduNaviManager.RoutePlanListener {

    private final static String LOG_TAG = "navi";

    private BNRoutePlanNode mBNRoutePlanNode = null;
    private RoutePlanCallback mCallback;

    public RoutePlanListenerImpl(BNRoutePlanNode node, RoutePlanCallback callback) {
        mBNRoutePlanNode = node;
        mCallback = callback;
    }

    @Override
    public void onJumpToNavigator() {
        Log.i(LOG_TAG, "算路成功");
        if (mCallback != null) {
            mCallback.onRoutePlanSuccess(mBNRoutePlanNode);
        }
    }

    @Override
    public void onRoutePlanFailed() {
        Log.e(LOG_TAG, "算路失败");
        if (mCallback != null) {
            mCallback.onRoutePlanFailed();
        }
    }
}
