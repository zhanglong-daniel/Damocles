package com.damocles.navi;

import com.baidu.navisdk.adapter.BNOuterTTSPlayerCallback;
import com.damocles.common.log.Log;
import com.damocles.tts.TTSPlayer;

import android.text.TextUtils;

/**
 * Created by zhanglong02 on 16/9/8.
 */
class BNOuterTTSPlayerCallbackImpl implements BNOuterTTSPlayerCallback {

    @Override
    public int getTTSState() {
        return BNOuterTTSPlayerCallback.PLAYER_STATE_IDLE;
    }

    @Override
    public int playTTSText(String s, int i) {
        s = s.replaceAll("<usraud>|</usraud>", "");
        if (TextUtils.equals(s, "导航结束。")) {
            TTSPlayer.getInstance().stop();
        }
        TTSPlayer.getInstance().play(s);
        return 0;
    }

    @Override
    public void phoneCalling() {

    }

    @Override
    public void phoneHangUp() {

    }

    @Override
    public void initTTSPlayer() {

    }

    @Override
    public void releaseTTSPlayer() {

    }

    @Override
    public void stopTTS() {
        TTSPlayer.getInstance().stop();
    }

    @Override
    public void resumeTTS() {

    }

    @Override
    public void pauseTTS() {

    }
}
