package com.damocles.common.network.callback;

import java.util.List;
import java.util.Map;

/**
 * Created by zhanglong02 on 16/12/2.
 */
public interface HttpCallback {

    void onSuccess(String url, int statusCode, String response);

    void onCookies(String url, List<String> cookies);

    void onError(String url, String errMsg);
}
