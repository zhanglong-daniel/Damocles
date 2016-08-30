package com.damocles.sample;

import com.damocles.R;
import com.damocles.sample.service.MyService;
import com.damocles.sample.util.Utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class BroadcastActivity extends AppCompatActivity {

    private TextView mTextView;

    private BroadcastReceiver mBroadcastReceiver;
    private IntentFilter mIntentFilter;

    private int countDownSeconds = 5;

    private Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_broadcast);
        Utils.initToolbar(this, R.id.broadcast_toolbar);
        initViews();
        mBroadcastReceiver = new MyBroadcastReceiver();
        mIntentFilter = new IntentFilter(MyService.ACTION);
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(mBroadcastReceiver, mIntentFilter);
        BroadcastActivity.this.startService(new Intent(BroadcastActivity.this, MyService.class));
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mBroadcastReceiver);
    }

    private void initViews() {
        mTextView = (TextView) findViewById(R.id.broadcast_txt);
        countDown();
    }

    private void countDown() {
        mTextView.setText(countDownSeconds + "秒后接收Service发出的广播消息...");
        countDownSeconds--;
        if (countDownSeconds <= 0) {
            return;
        }
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                countDown();
            }
        }, 1000);
    }

    private void updateTextView(Intent intent) {
        mTextView.setText("收到广播消息：" + intent.getExtras().getString("test"));
    }

    private class MyBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            updateTextView(intent);
        }
    }

}
