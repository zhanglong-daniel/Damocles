package com.damocles.navi.callback;

import com.baidu.navisdk.adapter.BNRoutePlanNode;

/**
 * Created by zhanglong02 on 16/9/8.
 */
public interface RoutePlanCallback {

    void onRoutePlanSuccess(BNRoutePlanNode routePlanNode);

    void onRoutePlanFailed();
}
