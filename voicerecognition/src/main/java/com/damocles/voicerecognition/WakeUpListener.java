package com.damocles.voicerecognition;

/**
 * Created by zhanglong02 on 16/10/13.
 */
public interface WakeUpListener {

    void onStart();

    void onStop();

    void onWakeup(String word);

    void onException(Exception e);
}
