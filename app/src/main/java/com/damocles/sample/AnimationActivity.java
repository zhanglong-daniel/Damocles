package com.damocles.sample;

import com.damocles.R;
import com.damocles.common.widget.WaveformView;
import com.damocles.sample.util.Utils;

import android.animation.ObjectAnimator;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class AnimationActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView view;

    private WaveformView waveformView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animation);
        Utils.initToolbar(this, R.id.animation_toolbar);
        initViews();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.animation_btn_start:
                startAnimation();
                break;
            case R.id.animation_view:
                Toast.makeText(this, "hello", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }

    private void initViews() {
        findViewById(R.id.animation_btn_start).setOnClickListener(this);
        view = (TextView) findViewById(R.id.animation_view);
        view.setOnClickListener(this);
        view.setPivotX(0.0f);
        waveformView = (WaveformView) findViewById(R.id.animation_waveform);
        waveformView.setBackgroundColor(Color.BLACK);
    }

    boolean flag = false;

    private void startAnimation() {
        view.setVisibility(View.VISIBLE);
        if (flag) {

            ObjectAnimator.ofFloat(view, "scaleY", 0.0f, 1.0f).setDuration(1000).start();
        } else {

            ObjectAnimator.ofFloat(view, "scaleY", 1.0f, 0.0f).setDuration(1000).start();
        }
        flag = !flag;
    }

}
