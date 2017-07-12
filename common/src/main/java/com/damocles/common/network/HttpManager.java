package com.damocles.common.network;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import com.damocles.common.log.Log;
import com.damocles.common.network.callback.DownloadCallback;
import com.damocles.common.network.callback.HttpCallback;
import com.damocles.common.util.CommonUtils;

import android.os.Handler;
import android.os.Looper;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * http请求管理类，需在主线程调用
 * Created by zhanglong02 on 16/12/2.
 */
public final class HttpManager {

    private static final String TAG = "network";

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
        X509TrustManager trustManager = createTrustManager();
        SSLSocketFactory sslSocketFactory = createSSLSocketFactory(trustManager);
        HostnameVerifier hostnameVerifier = createHostnameVerifier();
        mCookieJarImpl = new CookieJarImpl();
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(HttpConfig.CONNECT_TIMEOUT_SECONDS, TimeUnit.SECONDS)
                .writeTimeout(HttpConfig.WRITE_TIMEOUT_SECONDS, TimeUnit.SECONDS)
                .readTimeout(HttpConfig.READ_TIMEOUT_SECONDS, TimeUnit.SECONDS)
                .cookieJar(mCookieJarImpl);
        if (trustManager != null && sslSocketFactory != null) {
            builder.sslSocketFactory(sslSocketFactory, trustManager)
                    .hostnameVerifier(hostnameVerifier);
        } else {
            Log.e("trustManager==null || sslSocketFactory==null");
        }
        mOkHttpClient = builder.build();
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

    public static void downloadFile(String url, String destFileDir, DownloadCallback callback) {
        getInstance().download(url, destFileDir, callback);
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
                    e.printStackTrace();
                    errorCallback(callback, url, e.toString());
                } finally {
                    response.close();
                }
            }

            @Override
            public void onFailure(Call call, final IOException e) {
                e.printStackTrace();
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

    /**
     * 下载文件
     *
     * @param url              下载链接
     * @param destFileDir      存储目录
     * @param downloadCallback
     */
    private void download(final String url, final String destFileDir, final DownloadCallback downloadCallback) {
        final Request request = HttpManagerUtil.buildGetRequest(url);
        mOkHttpClient.newCall(request).enqueue(new Callback() {

            @Override
            public void onResponse(Call call, Response response) {
                String url = call.request().url().toString();
                InputStream inputStream = null;
                FileOutputStream fileOutputStream = null;
                try {
                    inputStream = response.body().byteStream();
                    String fileName = CommonUtils.getFileNameFromUrl(url);
                    if (fileName == null) {
                        Log.e("get file name from url failed! url=" + url);
                        fileName = "temp" + System.currentTimeMillis();
                    }
                    File file = new File(destFileDir, fileName);
                    if (!file.exists()) {
                        file.createNewFile();
                    }
                    Log.i("cacheFile = " + file.getAbsolutePath());
                    downloadStartCallback(downloadCallback, url, file.getAbsolutePath());
                    long contentLength = response.body().contentLength();
                    long downloadLength = 0L;
                    byte[] buffer = new byte[2048];
                    int len;
                    int index = 0; // index用于降低加载进度的回调频率
                    fileOutputStream = new FileOutputStream(file);
                    while ((len = inputStream.read(buffer)) != -1) {
                        fileOutputStream.write(buffer, 0, len);
                        downloadLength += len;
                        if (index % 10 == 0) {
                            downloadProgressCallback(downloadCallback, url, downloadLength, contentLength);
                        }
                        index++;
                    }
                    fileOutputStream.flush();
                    downloadCompleteCallback(downloadCallback, url, file.getAbsolutePath());
                } catch (IOException e) {
                    e.printStackTrace();
                    downloadErrorCallback(downloadCallback, url, e.toString());
                } finally {
                    try {
                        if (inputStream != null) {
                            inputStream.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        if (fileOutputStream != null) {
                            fileOutputStream.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    response.close();
                }
            }

            @Override
            public void onFailure(Call call, final IOException e) {
                e.printStackTrace();
                downloadErrorCallback(downloadCallback, call.request().url().toString(), e.toString());
            }

        });
    }

    private void downloadStartCallback(final DownloadCallback downloadCallback, final String url,
                                       final String filePath) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (downloadCallback != null) {
                    downloadCallback.onDownloadStart(url, filePath);
                }
            }
        });
    }

    private void downloadProgressCallback(final DownloadCallback callback, final String url, final long size, final
    long downloadSize) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (callback != null) {
                    callback.onDownloadProgress(url, downloadSize, size);
                }
            }
        });
    }

    private void downloadCompleteCallback(final DownloadCallback downloadCallback, final String url,
                                          final String filePath) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (downloadCallback != null) {
                    downloadCallback.onDownloadComplete(url, filePath);
                }
            }
        });
    }

    private void downloadErrorCallback(final DownloadCallback callback, final String url, final String errMsg) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (callback != null) {
                    callback.onDownloadError(url, errMsg);
                }
            }
        });
    }

    private SSLSocketFactory createSSLSocketFactory(TrustManager trustManager) {
        try {
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, new TrustManager[] {trustManager}, null);
            return sslContext.getSocketFactory();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
        return null;
    }

    private X509TrustManager createTrustManager() {
        return new X509TrustManager() {
            @Override
            public void checkClientTrusted(X509Certificate[] chain, String authType) throws
                    CertificateException {

            }

            @Override
            public void checkServerTrusted(X509Certificate[] chain, String authType) throws
                    CertificateException {

            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[] {};
            }
        };
    }

    private HostnameVerifier createHostnameVerifier() {
        return new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                Log.e("hostname=" + hostname);
                return true;
            }
        };
    }

}
