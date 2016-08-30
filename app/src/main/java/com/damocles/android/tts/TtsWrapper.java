package com.damocles.android.tts;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import com.baidu.tts.answer.auth.AuthInfo;
import com.baidu.tts.client.SpeechSynthesizer;
import com.baidu.tts.client.SpeechSynthesizerListener;
import com.baidu.tts.client.TtsMode;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

/**
 * Created by zhanglong02 on 16/6/30.
 */
public class TtsWrapper {

//    private static final String SAMPLE_DIR_NAME = "baiduTTS";
//    private static final String SPEECH_FEMALE_MODEL_NAME = "bd_etts_speech_female.dat";
//    private static final String SPEECH_MALE_MODEL_NAME = "bd_etts_speech_male.dat";
//    private static final String TEXT_MODEL_NAME = "bd_etts_text.dat";
//    private static final String LICENSE_FILE_NAME = "temp_license";
//    private static final String ENGLISH_SPEECH_FEMALE_MODEL_NAME = "bd_etts_speech_female_en.dat";
//    private static final String ENGLISH_SPEECH_MALE_MODEL_NAME = "bd_etts_speech_male_en.dat";
//    private static final String ENGLISH_TEXT_MODEL_NAME = "bd_etts_text_en.dat";
//
//    private static TtsWrapper sInstance;
//
//    private SpeechSynthesizer mSpeechSynthesizer;
//    private String mSampleDirPath;
//
//    private TtsWrapper() {
//
//    }
//
//    public static TtsWrapper getInstance() {
//        if (sInstance == null) {
//            sInstance = new TtsWrapper();
//        }
//        return sInstance;
//    }
//
//    public void init(Context context, SpeechSynthesizerListener listener) {
//        initialEnv(context);
//        initialTts(context, listener);
//    }
//
//    public SpeechSynthesizer getSpeechSynthesizer() {
//        return mSpeechSynthesizer;
//    }
//
//    private void initialEnv(Context context) {
//        if (mSampleDirPath == null) {
//            String sdcardPath = Environment.getExternalStorageDirectory().toString();
//            mSampleDirPath = sdcardPath + "/" + SAMPLE_DIR_NAME;
//        }
//        makeDir(mSampleDirPath);
//        copyFromAssetsToSdcard(context, false, SPEECH_FEMALE_MODEL_NAME, mSampleDirPath + "/" +
//                SPEECH_FEMALE_MODEL_NAME);
//        copyFromAssetsToSdcard(context, false, SPEECH_MALE_MODEL_NAME, mSampleDirPath + "/" + SPEECH_MALE_MODEL_NAME);
//        copyFromAssetsToSdcard(context, false, TEXT_MODEL_NAME, mSampleDirPath + "/" + TEXT_MODEL_NAME);
//        copyFromAssetsToSdcard(context, false, LICENSE_FILE_NAME, mSampleDirPath + "/" + LICENSE_FILE_NAME);
//        copyFromAssetsToSdcard(context, false, "english/" + ENGLISH_SPEECH_FEMALE_MODEL_NAME, mSampleDirPath + "/"
//                + ENGLISH_SPEECH_FEMALE_MODEL_NAME);
//        copyFromAssetsToSdcard(context, false, "english/" + ENGLISH_SPEECH_MALE_MODEL_NAME, mSampleDirPath + "/"
//                + ENGLISH_SPEECH_MALE_MODEL_NAME);
//        copyFromAssetsToSdcard(context, false, "english/" + ENGLISH_TEXT_MODEL_NAME, mSampleDirPath + "/"
//                + ENGLISH_TEXT_MODEL_NAME);
//    }
//
//    private void makeDir(String dirPath) {
//        File file = new File(dirPath);
//        if (!file.exists()) {
//            file.mkdirs();
//        }
//    }
//
//    /**
//     * 将sample工程需要的资源文件拷贝到SD卡中使用（授权文件为临时授权文件，请注册正式授权）
//     *
//     * @param isCover 是否覆盖已存在的目标文件
//     * @param source
//     * @param dest
//     */
//    private void copyFromAssetsToSdcard(Context context, boolean isCover, String source, String dest) {
//        File file = new File(dest);
//        if (isCover || (!isCover && !file.exists())) {
//            InputStream is = null;
//            FileOutputStream fos = null;
//            try {
//                is = context.getResources().getAssets().open(source);
//                String path = dest;
//                fos = new FileOutputStream(path);
//                byte[] buffer = new byte[1024];
//                int size = 0;
//                while ((size = is.read(buffer, 0, 1024)) >= 0) {
//                    fos.write(buffer, 0, size);
//                }
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            } finally {
//                if (fos != null) {
//                    try {
//                        fos.close();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//                try {
//                    if (is != null) {
//                        is.close();
//                    }
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//    }
//
//    private void initialTts(Context context, SpeechSynthesizerListener listener) {
//        this.mSpeechSynthesizer = SpeechSynthesizer.getInstance();
//        this.mSpeechSynthesizer.setContext(context);
//        this.mSpeechSynthesizer.setSpeechSynthesizerListener(listener);
//        // 文本模型文件路径 (离线引擎使用)
//        this.mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_TTS_TEXT_MODEL_FILE, mSampleDirPath + "/"
//                + TEXT_MODEL_NAME);
//        // 声学模型文件路径 (离线引擎使用)
//        this.mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_TTS_SPEECH_MODEL_FILE, mSampleDirPath + "/"
//                + SPEECH_FEMALE_MODEL_NAME);
//        // 本地授权文件路径,如未设置将使用默认路径.设置临时授权文件路径，LICENCE_FILE_NAME请替换成临时授权文件的实际路径，仅在使用临时license
//        // 文件时需要进行设置，如果在[应用管理]中开通了离线授权，不需要设置该参数，建议将该行代码删除（离线引擎）
//        this.mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_TTS_LICENCE_FILE, mSampleDirPath + "/"
//                + LICENSE_FILE_NAME);
//        // 请替换为语音开发者平台上注册应用得到的App ID (离线授权)
//        this.mSpeechSynthesizer.setAppId("8049748");
//        // 请替换为语音开发者平台注册应用得到的apikey和secretkey (在线授权)
//        this.mSpeechSynthesizer.setApiKey("69G8ECRpSAGzSWp2nQ5bZVKu", "f62799bf52f2ccbbd335e7b3c0921a66");
//        // 授权检测接口
//        AuthInfo authInfo = this.mSpeechSynthesizer.auth(TtsMode.MIX);
//        if (authInfo.isSuccess()) {
//            Log.e("tts", "auth success");
//            this.mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_SPEAKER, SpeechSynthesizer.SPEAKER_FEMALE);
//            mSpeechSynthesizer.initTts(TtsMode.MIX);
//        } else {
//            String errorMsg = authInfo.getTtsError().getDetailMessage();
//            Log.e("tts", "auth failed errorMsg=" + errorMsg);
//        }
//    }

}
