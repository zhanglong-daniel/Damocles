/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package com.damocles.tts;

import com.damocles.common.log.Log;

import android.content.Context;

/**
 * Created by zhanglong02 on 16/8/25.
 */
public class TTSPlayer implements ITTSPlayer {

    private enum TTSPlatform {
        Baidu
    }

    private static final TTSPlatform mTTSPlatform = TTSPlatform.Baidu;

    private ITTSPlayer mTTSPlayer = null;

    private TTSPlayer() {
        switch (mTTSPlatform) {
            case Baidu:
                mTTSPlayer = new BaiduTTSPlayer();
                break;
            default:
                break;
        }
    }

    private static final class InstanceHolder {
        static final TTSPlayer INSTANCE = new TTSPlayer();
    }

    public static TTSPlayer getInstance() {
        return InstanceHolder.INSTANCE;
    }

    @Override
    public void init(Context context) {
        Log.i("tts sdk init");
        mTTSPlayer.init(context);
    }

    @Override
    public void setTTSListener(TTSListener listener) {
        mTTSPlayer.setTTSListener(listener);
    }

    @Override
    public void play(String text) {
        Log.i("play tts : " + text);
        mTTSPlayer.play(text);
    }

    @Override
    public void stop() {
        Log.i("stop tts");
        mTTSPlayer.stop();
    }

    @Override
    public void release() {
        Log.i("release tts");
        mTTSPlayer.release();
    }
}
