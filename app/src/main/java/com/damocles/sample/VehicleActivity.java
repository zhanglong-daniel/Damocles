package com.damocles.sample;

import java.util.Date;

import com.damocles.R;
import com.damocles.sample.util.Utils;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.TextView;

public class VehicleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle);
        Utils.initToolbar(this, R.id.vehicle_toolbar);
        initViews();
    }

    private void initViews() {
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.e("fuck", "keycode = " + keyCode);
        Log.e("fuck", "KeyEvent = " + event.toString());
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        Log.e("fuck", "keycode = " + keyCode);
        Log.e("fuck", "KeyEvent = " + event.toString());
        return onKeyUp(keyCode, event);
    }

    @Override
    public boolean onKeyLongPress(int keyCode, KeyEvent event) {
        Log.e("fuck", "keycode = " + keyCode);
        Log.e("fuck", "KeyEvent = " + event.toString());
        return onKeyLongPress(keyCode, event);
    }
}
