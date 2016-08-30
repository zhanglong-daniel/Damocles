package com.damocles.navi;

import com.baidu.navisdk.adapter.BNRoutePlanNode;

/**
 * Created by zhanglong02 on 16/8/24.
 */
public interface NaviCallback {

    void onRoutePlanSuccess(BNRoutePlanNode routePlanNode);

    void onRoutePlanFailed();

    void onTtsStart();

    void onTtsEnd();

    void onNaviGuideEnd();
}
