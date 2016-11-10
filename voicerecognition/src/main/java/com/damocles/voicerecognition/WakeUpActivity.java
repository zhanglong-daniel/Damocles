package com.damocles.voicerecognition;

import java.lang.reflect.Field;
import java.util.ArrayList;

import com.damocles.common.log.Log;
import com.damocles.tts.TTSListener;
import com.damocles.tts.TTSPlayer;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.LinearLayout;
import android.widget.Toast;

/**
 * Created by zhanglong02 on 16/10/11.
 */
public class WakeUpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wakeup);
        initToolbar();
        initViews();
        WakeUp.getInstance().registerListener(new WakeUpListener() {
            @Override
            public void onStart() {
                Log.e("wakeup", "唤醒词监听已启动");
            }

            @Override
            public void onStop() {
                Log.e("wakeup", "唤醒词监听已停止");
                Toast.makeText(WakeUpActivity.this, "唤醒词监听已停止", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onWakeup(String word) {
                Log.i("wakeup", "唤醒成功, 唤醒词: " + word);
                Toast.makeText(WakeUpActivity.this, "唤醒成功, 唤醒词: " + word, Toast.LENGTH_SHORT).show();
                TTSPlayer.getInstance().setTTSListener(new TTSListener() {
                    @Override
                    public void onBuffer() {

                    }

                    @Override
                    public void onPlayBegin() {

                    }

                    @Override
                    public void onCancel() {

                    }

                    @Override
                    public void onError(String error) {

                    }

                    @Override
                    public void onPlayEnd() {
                        WakeUp.getInstance().stop();
                        startRecognizer();
                    }

                    @Override
                    public void onInitFinish() {

                    }
                });
                TTSPlayer.getInstance().play("您好，请问需要什么帮助？");
            }

            @Override
            public void onException(Exception e) {
                Log.e("wakeup", "exception :\n" + e);
                Toast.makeText(WakeUpActivity.this, "exception:" + e, Toast.LENGTH_SHORT).show();

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        WakeUp.getInstance().start(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        WakeUp.getInstance().stop();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Bundle results = data.getExtras();
            ArrayList<String> results_recognition = results.getStringArrayList("results_recognition");
            TTSPlayer.getInstance().setTTSListener(new TTSListener() {
                @Override
                public void onBuffer() {

                }

                @Override
                public void onPlayBegin() {

                }

                @Override
                public void onCancel() {

                }

                @Override
                public void onError(String error) {

                }

                @Override
                public void onPlayEnd() {

                }

                @Override
                public void onInitFinish() {

                }
            });
            TTSPlayer.getInstance().play("识别结果：" + results_recognition.get(0));
            Toast.makeText(WakeUpActivity.this, "识别结果(数组形式): " + results_recognition + "\n", Toast.LENGTH_SHORT).show();
        }
    }

    private void initViews() {
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.wakeup_toolbar);
        int statusBarHeight = getStatusBarHeight();
        int w = LinearLayout.LayoutParams.MATCH_PARENT;
        int h = getResources().getDimensionPixelSize(R.dimen.tool_bar_height);
        if (Build.VERSION.SDK_INT > 19) {
            toolbar.setPadding(0, statusBarHeight, 0, 0);
            h += statusBarHeight;
        }
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(w, h);
        toolbar.setLayoutParams(params);
        setSupportActionBar(toolbar);
    }

    private int getStatusBarHeight() {
        try {
            Class<?> c = Class.forName("com.android.internal.R$dimen");
            Object obj = c.newInstance();
            Field field = c.getField("status_bar_height");
            int id = Integer.parseInt(field.get(obj).toString());
            return getResources().getDimensionPixelSize(id);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    private void startRecognizer() {
        Intent intent = new Intent("com.baidu.action.RECOGNIZE_SPEECH");
        // 设置离线的授权文件(离线模块需要授权), 该语法可以用自定义语义工具生成, 链接http://yuyin.baidu.com/asr#m5
        intent.putExtra("grammar", "asset:///baidu_speech_grammar.bsg");
        intent.putExtra("sound_start", R.raw.bdspeech_recognition_start);
        intent.putExtra("sound_end", R.raw.bdspeech_speech_end);
        intent.putExtra("sound_success", R.raw.bdspeech_recognition_success);
        intent.putExtra("sound_error", R.raw.bdspeech_recognition_error);
        intent.putExtra("sound_cancel", R.raw.bdspeech_recognition_cancel);
        //intent.putExtra("slot-data", your slots); // 设置grammar中需要覆盖的词条,如联系人名
        startActivityForResult(intent, 1);
    }
}

