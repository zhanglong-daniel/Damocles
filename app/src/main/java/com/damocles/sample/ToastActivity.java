package com.damocles.sample;

import com.damocles.R;
import com.damocles.sample.util.Utils;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

public class ToastActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_toast);
        Utils.initToolbar(this, R.id.toast_toolbar);
        initViews();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.toast_btn_left_top:
                showToast("left top", Gravity.LEFT | Gravity.TOP);
                break;
            case R.id.toast_btn_left_bottom:
                showToast("left bottom", Gravity.LEFT | Gravity.BOTTOM);
                break;
            case R.id.toast_btn_right_top:
                showToast("right top", Gravity.RIGHT | Gravity.TOP);
                break;
            case R.id.toast_btn_right_bottom:
                showToast("right bottom", Gravity.RIGHT | Gravity.BOTTOM);
                break;
            case R.id.toast_btn_center:
                showToast("center", Gravity.CENTER);
                break;
            default:
                break;
        }
    }

    private void initViews() {
        findViewById(R.id.toast_btn_left_top).setOnClickListener(this);
        findViewById(R.id.toast_btn_left_bottom).setOnClickListener(this);
        findViewById(R.id.toast_btn_right_top).setOnClickListener(this);
        findViewById(R.id.toast_btn_right_bottom).setOnClickListener(this);
        findViewById(R.id.toast_btn_center).setOnClickListener(this);
    }

    private void showToast(String text, int gravity) {
        Toast toast = Toast.makeText(this, text, Toast.LENGTH_SHORT);
        toast.setGravity(gravity, 0, 0);
        toast.show();
    }

}
