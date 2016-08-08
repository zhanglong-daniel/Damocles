package com.damocles.android.network.loader;

import java.util.ArrayDeque;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.http.HttpEntity;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.protocol.HTTP;
import org.json.JSONObject;

import com.damocles.android.network.HttpEntityParams;
import com.damocles.android.network.HttpRequestHelper;
import com.damocles.android.network.HttpResult;
import com.damocles.android.network.NetworkUtils;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

public final class JsonLoader {

    enum RequestMethod {
        GET, POST
    }

    public interface Callback {
        void onSuccess(JSONObject json);

        void onException(Exception e);

        void onHttpError(int statusCode, String response);
    }

    private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
    private static final int CORE_POOL_SIZE = CPU_COUNT + 1;
    private static final int MAXIMUM_POOL_SIZE = CPU_COUNT * 2 + 1;
    private static final int KEEP_ALIVE = 1;

    private static final ThreadFactory sThreadFactory = new ThreadFactory() {
        private final AtomicInteger mCount = new AtomicInteger(1);

        public Thread newThread(Runnable r) {
            return new Thread(r, "JsonLoader #" + mCount.getAndIncrement());
        }
    };

    private static final BlockingQueue<Runnable> sPoolWorkQueue = new LinkedBlockingQueue<Runnable>(128);

    /**
     * An {@link Executor} that can be used to execute tasks in parallel.
     */
    public static final Executor THREAD_POOL_EXECUTOR = new ThreadPoolExecutor(CORE_POOL_SIZE, MAXIMUM_POOL_SIZE,
            KEEP_ALIVE, TimeUnit.SECONDS, sPoolWorkQueue, sThreadFactory);

    public static final Executor SERIAL_EXECUTOR = new SerialExecutor();

    private static final int MESSAGE_POST_SUCCESS = 0x1;
    private static final int MESSAGE_POST_EXCEPTION = 0x2;
    private static final int MESSAGE_POST_ERROR = 0x3;


    private static volatile Executor sDefaultExecutor = SERIAL_EXECUTOR;

    private static class SerialExecutor implements Executor {
        final ArrayDeque<Runnable> mTasks = new ArrayDeque<Runnable>();
        Runnable mActive;

        public synchronized void execute(final Runnable r) {
            mTasks.offer(new Runnable() {
                public void run() {
                    try {
                        r.run();
                    } finally {
                        scheduleNext();
                    }
                }
            });
            if (mActive == null) {
                scheduleNext();
            }
        }

        protected synchronized void scheduleNext() {
            if ((mActive = mTasks.poll()) != null) {
                THREAD_POOL_EXECUTOR.execute(mActive);
            }
        }
    }


    // *********************************************************

    public static void post(String url, HttpEntityParams params, Callback callback) {
        post(url, null, params, callback);
    }

    public static void post(String url, String cookie, HttpEntityParams params, Callback callback) {
        UrlEncodedFormEntity entity = null;
        if (params != null) {
            try {
                entity = new UrlEncodedFormEntity(
                        params.getHttpEntityParams(), HTTP.UTF_8);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        execute(url, cookie, entity, callback, RequestMethod.POST);
    }

    public static void post(String url, String payload, Callback callback) {
        post(url, payload, "", callback);
    }

    public static void post(String url, String payload, String cookie, Callback callback) {
        StringEntity entity = null;
        if (payload != null) {
            try {
                entity = new StringEntity(payload, HTTP.UTF_8);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        execute(url, cookie, entity, callback, RequestMethod.POST);
    }

    public static void get(String url, Callback callback) {
        get(url, null, callback);
    }

    public static void get(String url, String cookie, Callback callback) {
        execute(url, cookie, null, callback, RequestMethod.GET);
    }

    private static void execute(final String url, final String
            cookie, final HttpEntity entity, final Callback callback, final RequestMethod method) {

        if (Looper.myLooper() != Looper.getMainLooper()) {
            throw new IllegalStateException("JsonLoader must be invoked from the main thread.");
        }
        final Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if (callback == null) {
                    return;
                }
                switch (msg.what) {
                    case MESSAGE_POST_SUCCESS:
                        callback.onSuccess((JSONObject) msg.obj);
                        break;
                    case MESSAGE_POST_ERROR:
                        callback.onHttpError(msg.arg1, (String) msg.obj);
                        break;
                    case MESSAGE_POST_EXCEPTION:
                        callback.onException((Exception) msg.obj);
                        break;
                }
            }
        };

        Runnable runnable = new Runnable() {

            @Override
            public void run() {
                try {
                    HttpResult httpResult = null;
                    switch (method) {
                        case GET:
                            httpResult = HttpRequestHelper.get(url, cookie);
                            break;
                        case POST:
                            httpResult = HttpRequestHelper.post(url, entity, cookie);
                            break;
                        default:
                            httpResult = null;
                            break;
                    }
                    if (httpResult != null && !httpResult.isOk()) {
                        Message msg = handler.obtainMessage(MESSAGE_POST_ERROR, httpResult.getResponse());
                        msg.arg1 = httpResult.getStatusCode();
                        handler.sendMessage(msg);
                        return;
                    }
                    JSONObject jsonObject = new JSONObject(httpResult.getResponse());
                    Message msg = handler.obtainMessage(MESSAGE_POST_SUCCESS, jsonObject);
                    handler.sendMessage(msg);
                } catch (Exception e) {
                    Message msg = handler.obtainMessage(MESSAGE_POST_EXCEPTION, e);
                    handler.sendMessage(msg);
                }
            }
        };
        sDefaultExecutor.execute(runnable);
    }

}
