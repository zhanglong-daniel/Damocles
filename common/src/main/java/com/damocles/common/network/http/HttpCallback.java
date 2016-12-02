package com.damocles.common.network.http;

import java.util.Map;

/**
 * Created by zhanglong02 on 16/12/2.
 */
public interface HttpCallback {

    void onError(String error);

    void onSuccess(int statusCode, String response);

    void onCookies(Map<String, String> cookies);
}
