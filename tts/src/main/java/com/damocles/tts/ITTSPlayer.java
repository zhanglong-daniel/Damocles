/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package com.damocles.tts;

import android.content.Context;

/**
 * Created by zhanglong02 on 16/8/25.
 */
public interface ITTSPlayer {

    void init(Context context);

    void setTTSListener(TTSListener listener);

    void play(String text);

    void stop();

    void release();
}
