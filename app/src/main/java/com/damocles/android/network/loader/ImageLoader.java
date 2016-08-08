package com.damocles.android.network.loader;

import java.util.ArrayDeque;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import com.damocles.android.network.HttpEntityParams;
import com.damocles.android.network.HttpRequestHelper;
import com.damocles.android.network.HttpResult;
import com.damocles.android.network.ImageHttpResult;
import com.damocles.android.network.NetworkUtils;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

public final class ImageLoader {

    public interface Callback {
        void onSuccess(String url, Bitmap result);

        void onException(Exception e);

        void onHttpError(int statusCode);
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

    public static void get(String url, Callback callback) {
        execute(url, null, callback);
    }

    public static void get(String url, String cookie, Callback callback) {
        execute(url, cookie, callback);
    }

    private static void execute(final String url, final String cookie, final Callback callback) {

        if (Looper.myLooper() != Looper.getMainLooper()) {
            throw new IllegalStateException("ImageLoader must be invoked from the main thread.");
        }

        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (callback == null) {
                    return;
                }
                switch (msg.what) {
                    case MESSAGE_POST_SUCCESS:
                        String url = msg.getData().getString("url");
                        callback.onSuccess(url, (Bitmap) msg.obj);
                        break;
                    case MESSAGE_POST_ERROR:
                        callback.onHttpError(msg.arg1);
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
                    ImageHttpResult httpResult = HttpRequestHelper.getImage(url, cookie);
                    if (!httpResult.isOk()) {
                        Message msg = handler.obtainMessage(MESSAGE_POST_ERROR, httpResult.getResponse());
                        msg.arg1 = httpResult.getStatusCode();
                        handler.sendMessage(msg);
                        return;
                    }
                    Message msg = handler.obtainMessage(MESSAGE_POST_SUCCESS, httpResult.getResponse());
                    Bundle data = new Bundle();
                    data.putString("url", url);
                    msg.setData(data);
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
