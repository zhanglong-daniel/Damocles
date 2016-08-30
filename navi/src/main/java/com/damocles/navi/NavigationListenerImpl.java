package com.damocles.navi;

import com.baidu.navisdk.adapter.BNRouteGuideManager;
import com.baidu.navisdk.adapter.BNRoutePlanNode;
import com.damocles.common.log.Log;

/**
 * Created by zhanglong02 on 16/8/24.
 */
public class NavigationListenerImpl implements BNRouteGuideManager.OnNavigationListener {

    private final static String LOG_TAG = NaviUtils.LOG_TAG;

    private NaviCallback mCallback;

    public NavigationListenerImpl(NaviCallback callback) {
        mCallback = callback;
    }

    @Override
    public void notifyOtherAction(int actionType, int arg1, int arg2, Object obj) {
        if (actionType == 0) {
            //导航到达目的地 自动退出
            Log.i(LOG_TAG, "notifyOtherAction actionType = " + actionType + ",导航到达目的地！");
        }

        Log.i(LOG_TAG, "actionType:" + actionType + "arg1:" + arg1 + "arg2:" + arg2 + "obj:" + obj);

    }

    @Override
    public void onNaviGuideEnd() {
        Log.i(LOG_TAG, "导航结束");
        if (mCallback != null) {
            mCallback.onNaviGuideEnd();
        }
    }
}
