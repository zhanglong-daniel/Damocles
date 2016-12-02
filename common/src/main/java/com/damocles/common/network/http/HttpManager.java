package com.damocles.common.network.http;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.damocles.common.log.Log;

import android.os.Handler;
import android.os.Looper;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
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

    public static void cancelAll() {
        Log.i(TAG, "cancel all http request");
        getInstance().mOkHttpClient.dispatcher().cancelAll();
    }

    public static void clearCookies() {
        Log.i(TAG, "clear cookies");
        getInstance().mCookieJarImpl.clear();
    }

    // ******************************************

    public static void get(String url, HttpCallback httpCallback) {
        Log.i(TAG, "url=" + url);
        Request request = HttpUtils.buildGetRequest(url);
        getInstance()._execute(request, httpCallback);
    }

    public static void post(String url, Map<String, String> postParams, HttpCallback httpCallback) {
        Log.i(TAG, "url=" + url);
        Request request = HttpUtils.buildPostRequest(url, postParams);
        getInstance()._execute(request, httpCallback);
    }

    public static void post(String url, String postJson, HttpCallback httpCallback) {
        Log.i(TAG, "url=" + url);
        Request request = HttpUtils.buildPostRequest(url, postJson);
        getInstance()._execute(request, httpCallback);
    }

    // ******************************************

    private void _execute(Request request, final HttpCallback httpCallback) {

        Callback callback = new Callback() {

            @Override
            public void onFailure(Call call, final IOException e) {
                _handleErrorCallback(httpCallback, e.toString());
            }

            @Override
            public void onResponse(Call call, final Response response) {
                try {
                    // cookies
                    _handleCookieCallback(httpCallback, HttpUtils.getCookies(response));
                    // response
                    int statusCode = response.code();
                    String body = response.body().string();
                    _handleSuccessCallback(httpCallback, statusCode, body);
                } catch (Exception e) {
                    _handleErrorCallback(httpCallback, e.toString());
                } finally {
                    response.close();
                }
            }
        };

        mOkHttpClient.newCall(request).enqueue(callback);
    }

    // ******************************************

    private void _handleErrorCallback(final HttpCallback httpCallback, final String error) {
        Log.e(TAG, "error=" + error);
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (httpCallback != null) {
                    httpCallback.onError(error);
                }
            }
        });
    }

    private void _handleSuccessCallback(final HttpCallback httpCallback, final int statusCode, final String response) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (httpCallback != null) {
                    httpCallback.onSuccess(statusCode, response);
                }
            }
        });
    }

    private void _handleCookieCallback(final HttpCallback httpCallback, final Map<String, String> cookies) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (httpCallback != null) {
                    httpCallback.onCookies(cookies);
                }
            }
        });
    }

}
