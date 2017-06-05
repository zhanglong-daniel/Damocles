package com.damocles.common.network.http;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.damocles.common.log.Log;

import android.os.Handler;
import android.os.Looper;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * http请求管理类，需在主线程调用
 * Created by zhanglong02 on 16/12/2.
 */
public final class HttpManager {

    private static final String TAG = "network_http";

    private OkHttpClient mOkHttpClient;
    private Handler mHandler;
    private CookieJarImpl mCookieJarImpl;

    // single instance start
    private static HttpManager getInstance() {
        return InstanceHolder.INSTANCE;
    }

    private static class InstanceHolder {
        public static final HttpManager INSTANCE = new HttpManager();
    }

    private HttpManager() {
        Log.i(TAG, "init http manager");
        mCookieJarImpl = new CookieJarImpl();
        mOkHttpClient = new OkHttpClient.Builder()
                .connectTimeout(HttpConfig.CONNECT_TIMEOUT_SECONDS, TimeUnit.SECONDS)
                .writeTimeout(HttpConfig.WRITE_TIMEOUT_SECONDS, TimeUnit.SECONDS)
                .readTimeout(HttpConfig.READ_TIMEOUT_SECONDS, TimeUnit.SECONDS)
                .cookieJar(mCookieJarImpl)
                .build();
        mHandler = new Handler(Looper.getMainLooper());
    }
    // single instance end

    public static OkHttpClient getOkHttpClient() {
        return getInstance().mOkHttpClient;
    }

    public static void cancelAllRequests() {
        Log.i(TAG, "cancel all requests");
        getInstance().mOkHttpClient.dispatcher().cancelAll();
    }

    public static void clearCookies() {
        Log.i(TAG, "clear cookies");
        getInstance().mCookieJarImpl.clear();
    }

    // ******************************************

    public static void get(String url, HttpCallback callback) {
        Request request = HttpManagerUtil.buildGetRequest(url);
        getInstance().execute(request, callback);
    }

    public static void post(String url, Map<String, String> params, HttpCallback callback) {
        Request request = HttpManagerUtil.buildPostRequest(url, params);
        getInstance().execute(request, callback);
    }

    public static void post(String url, String postJson, HttpCallback callback) {
        Request request = HttpManagerUtil.buildPostRequest(url, postJson);
        getInstance().execute(request, callback);
    }

    private void execute(Request request, final HttpCallback callback) {
        mOkHttpClient.newCall(request).enqueue(new Callback() {

            @Override
            public void onResponse(Call call, final Response response) {
                String url = call.request().url().toString();
                if (callback == null) {
                    Log.e(TAG, "HttpCallback is null");
                }
                try {
                    List<String> cookies = response.headers().values("Set-Cookie");
                    // cookies
                    cookieCallback(callback, url, cookies);
                    // response
                    int statusCode = response.code();
                    if (response.isSuccessful()) {  // code >= 200 && code < 300
                        String body = response.body().string();
                        successCallback(callback, url, statusCode, body);
                    } else {
                        errorCallback(callback, url, "statusCode=" + statusCode);
                    }
                } catch (Exception e) {
                    errorCallback(callback, url, e.toString());
                } finally {
                    response.close();
                }
            }

            @Override
            public void onFailure(Call call, final IOException e) {
                errorCallback(callback, call.request().url().toString(), e.toString());
            }
        });
    }

    private void successCallback(final HttpCallback callback, final String url, final int statusCode, final String
            response) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (callback != null) {
                    callback.onSuccess(url, statusCode, response);
                }
            }
        });
    }

    private void cookieCallback(final HttpCallback callback, final String url, final List<String> cookies) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (callback != null) {
                    callback.onCookies(url, cookies);
                }
            }
        });
    }

    private void errorCallback(final HttpCallback callback, final String url, final String errMsg) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (callback != null) {
                    callback.onError(url, errMsg);
                }
            }
        });
    }
}
