package com.damocles.sample;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import com.damocles.R;
import com.damocles.common.log.Log;
import com.damocles.common.security.MD5Util;
import com.damocles.common.util.CommonUtils;
import com.damocles.sample.util.Utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by zhanglong02 on 16/9/2.
 */
public class TTSActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText mEditText;

    private ProgressDialog mProgressDialog;

    private MediaPlayer mMediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tts);
        Utils.initToolbar(this, R.id.tts_toolbar);
        initViews();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tts_btn:
                String text = mEditText.getText().toString();
                if (TextUtils.isEmpty(text)) {
                    Toast.makeText(this, "请输入要转换的文字", Toast.LENGTH_SHORT).show();
                } else {
                    speak(text);
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mMediaPlayer != null) {
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    private void initViews() {
        mEditText = (EditText) findViewById(R.id.tts_edittext);
        findViewById(R.id.tts_btn).setOnClickListener(this);
    }

    private void speak(String text) {
        String url = "http://tts.baidu.com/text2audio?lan=zh&ie=UTF-8&text=" + text;
        // 下载mp3文件
        loadMp3(url, text);
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage("语音合成中。。。");
        }
        mProgressDialog.show();
        if (mMediaPlayer == null) {
            mMediaPlayer = new MediaPlayer();
        }
        try {
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mMediaPlayer.setDataSource(url);

            mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    if (mProgressDialog != null) {
                        mProgressDialog.cancel();
                    }
                    mMediaPlayer.start();
                }
            });
            mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    if (mMediaPlayer != null) {
                        mMediaPlayer.reset();
                    }
                }
            });
            mMediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {
                    if (mProgressDialog != null) {
                        mProgressDialog.cancel();
                    }
                    return false;
                }
            });
            mMediaPlayer.prepareAsync();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadMp3(final String urlString, final String fileName) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(urlString);
                    URLConnection connection = url.openConnection();
                    InputStream inputStream = connection.getInputStream();
                    final String filePath = getFilePath(fileName);
                    OutputStream outputStream = new FileOutputStream(filePath);
                    byte[] buffer = new byte[1024];
                    int len;
                    while ((len = inputStream.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, len);
                    }
                    outputStream.close();
                    inputStream.close();
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(TTSActivity.this, "下载成功，path=" + filePath, Toast.LENGTH_SHORT).show();
                            Log.e("filePath = " + filePath);
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private String getFilePath(String fileName)  {
        return CommonUtils.getExternalCacheDir(this).getAbsolutePath() + "/" + fileName + ".mp3";
    }
}
