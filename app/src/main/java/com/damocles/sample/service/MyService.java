package com.damocles.sample.service;

import java.util.Date;

import android.app.IntentService;
import android.content.Intent;
import android.os.SystemClock;

/**
 * Created by zhanglong02 on 16/3/4.
 */
public class MyService extends IntentService {

    public static final String ACTION = "MyService.ACTION.TEST";

    public MyService() {
        super("MyService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        SystemClock.sleep(5000L);
        Intent broadcast = new Intent(ACTION);
        broadcast.putExtra("test", new Date().toGMTString());
        sendBroadcast(broadcast);
    }
}
