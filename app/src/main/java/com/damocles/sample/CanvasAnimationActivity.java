package com.damocles.sample;

import com.damocles.android.util.DeviceInfoUtils;
import com.damocles.android.view.DrawView;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class CanvasAnimationActivity extends AppCompatActivity {

    private DrawView mDrawView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDrawView = new DrawView(this);
        mDrawView.width = DeviceInfoUtils.getScreenWidth();
        mDrawView.height = DeviceInfoUtils.getScreenHeight() - DeviceInfoUtils.getStatusBarHeight();
        mDrawView.setBackgroundColor(Color.WHITE);
        setContentView(mDrawView);
    }
}
