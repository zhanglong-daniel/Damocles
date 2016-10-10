/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package com.damocles.tts;

/**
 * Created by zhanglong02 on 16/8/25.
 */
public interface TTSListener {

    void onBuffer();

    void onPlayBegin();

    void onCancel();

    void onError(String error);

    void onPlayEnd();

    void onInitFinish();
}
