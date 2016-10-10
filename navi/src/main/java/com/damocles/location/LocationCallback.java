package com.damocles.location;

/**
 * Created by zhanglong02 on 16/9/9.
 */
public interface LocationCallback {

    void onSuccess(String address, double lng, double lat);

    void onFailed(int code, String msg);

}
