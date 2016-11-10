package com.damocles.voicerecognition;

import com.baidu.speech.VoiceRecognitionService;

import android.content.ComponentName;
import android.content.Context;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.SpeechRecognizer;

/**
 * Created by zhanglong02 on 16/10/13.
 */
public class VoiceRecognizer {

    private VoiceRecognizer() {

    }

    private static class InstanceHolder {
        private static final VoiceRecognizer INSTANCE = new VoiceRecognizer();
    }

    public static VoiceRecognizer getInstance() {
        return InstanceHolder.INSTANCE;
    }

    private SpeechRecognizer mSpeechRecognizer;
    private VoiceRecognizerListener mListener;

    public void registerListener(VoiceRecognizerListener listener) {
        mListener = listener;
    }

    public void start(Context context) {
        init(context);
//        mSpeechRecognizer.startListening();

    }

    public void stop() {
        if (mSpeechRecognizer != null) {
            mSpeechRecognizer.stopListening();
        }
    }

    public void cancel() {
        if (mSpeechRecognizer != null) {
            mSpeechRecognizer.cancel();
        }
    }

    public void destroy() {
        if (mSpeechRecognizer != null) {
            mSpeechRecognizer.destroy();
        }
    }

    private void init(Context context) {
        if (mSpeechRecognizer != null) {
            return;
        }
        mSpeechRecognizer =
                SpeechRecognizer
                        .createSpeechRecognizer(context, new ComponentName(context, VoiceRecognitionService.class));
        mSpeechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle params) {

            }

            @Override
            public void onBeginningOfSpeech() {

            }

            @Override
            public void onRmsChanged(float rmsdB) {

            }

            @Override
            public void onBufferReceived(byte[] buffer) {

            }

            @Override
            public void onEndOfSpeech() {

            }

            @Override
            public void onError(int error) {

            }

            @Override
            public void onResults(Bundle results) {

            }

            @Override
            public void onPartialResults(Bundle partialResults) {

            }

            @Override
            public void onEvent(int eventType, Bundle params) {

            }
        });
    }

}
