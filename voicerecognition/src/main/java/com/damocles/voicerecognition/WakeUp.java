package com.damocles.voicerecognition;

import java.util.HashMap;

import org.json.JSONObject;

import com.baidu.speech.EventListener;
import com.baidu.speech.EventManager;
import com.baidu.speech.EventManagerFactory;
import com.damocles.common.log.Log;

import android.content.Context;
import android.text.TextUtils;

/**
 * Created by zhanglong02 on 16/10/13.
 */
public class WakeUp {

    private WakeUp() {

    }

    private static class InstanceHolder {
        private static final WakeUp INSTANCE = new WakeUp();
    }

    public static WakeUp getInstance() {
        return InstanceHolder.INSTANCE;
    }

    private EventManager mWpEventManager;
    private WakeUpListener mWakeUpListener;

    public void registerListener(WakeUpListener wakeUpListener) {
        mWakeUpListener = wakeUpListener;
    }

    public void start(Context context) {
        init(context);
        HashMap params = new HashMap();
        // 设置唤醒资源, 唤醒资源请到 http://yuyin.baidu.com/wake#m4 来评估和导出
        params.put("kws-file", "assets:///WakeUp.bin");
        mWpEventManager.send("wp.start", new JSONObject(params).toString(), null, 0, 0);
    }

    public void stop() {
        if (mWpEventManager != null) {
            mWpEventManager.send("wp.stop", null, null, 0, 0);
        }
    }

    private void init(Context context) {
        if (mWpEventManager != null) {
            return;
        }
        // 1) 创建唤醒事件管理器
        mWpEventManager = EventManagerFactory.create(context, "wp");
        // 2) 注册唤醒事件监听器
        mWpEventManager.registerListener(new EventListener() {
            @Override
            public void onEvent(String name, String params, byte[] data, int offset, int length) {
                Log.i("wakeup", String.format("event: name=%s, params=%s", name, params));
                try {
                    if (TextUtils.equals(name, "wp.enter")) {
                        if (mWakeUpListener != null) {
                            mWakeUpListener.onStart();
                        }
                    } else if ("wp.exit".equals(name)) {
                        if (mWakeUpListener != null) {
                            mWakeUpListener.onStop();
                        }
                    } else
                        // 每次唤醒成功, 将会回调name=wp.data的事件, 被激活的唤醒词在params的word字段
                        if ("wp.data".equals(name)) {
                            if (mWakeUpListener != null) {
                                mWakeUpListener.onWakeup(new JSONObject(params).getString("word"));
                            }
                        }
                } catch (Exception e) {
                    if (mWakeUpListener != null) {
                        mWakeUpListener.onException(e);
                    }
                }
            }
        });
    }
}
