package com.damocles.sample;

import com.damocles.R;
import com.damocles.sample.util.Utils;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

public class AnimationActivity extends AppCompatActivity implements View.OnClickListener {

    private View view;

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
        view = findViewById(R.id.animation_view);
        view.setOnClickListener(this);
        view.setPivotX(0.0f);
    }

    boolean flag = false;

    private void startAnimation() {
        if (flag) {

            ObjectAnimator.ofFloat(view, "scaleX", 56.0f / 256.0f, 1.0f).setDuration(1000).start();
        } else {

            ObjectAnimator.ofFloat(view, "scaleX", 1.0f, 56.0f / 256.0f).setDuration(1000).start();
        }
        flag = !flag;
    }

}
