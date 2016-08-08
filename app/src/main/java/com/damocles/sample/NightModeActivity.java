package com.damocles.sample;

import com.baidu.naviauto.R;
import com.damocles.android.base.DamoclesApplication;
import com.damocles.sample.util.Utils;

import android.app.ActivityManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class NightModeActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_night_mode);
        Utils.initToolbar(this, R.id.night_mode_toolbar);
        initViews();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    private void initViews() {
        mTextView = (TextView) findViewById(R.id.night_mode_text);
        if (DamoclesApplication.getInstance().isNightMode) {
            mTextView.setText("现在是夜间模式");
        } else {
            mTextView.setText("现在是日间模式");
        }
        findViewById(R.id.night_mode_btn).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.night_mode_btn) {
            DamoclesApplication.getInstance().isNightMode = !DamoclesApplication.getInstance().isNightMode;
            changeNightMode(DamoclesApplication.getInstance().isNightMode);
        }
    }

    private void changeNightMode(boolean on) {
        Resources resources = getResources();
        DisplayMetrics displayMetrics = resources.getDisplayMetrics();
        Configuration configuration = resources.getConfiguration();
        Log.e("fuck", "uimode = " + Integer.toHexString(configuration.uiMode));
        configuration.uiMode &= ~Configuration.UI_MODE_NIGHT_MASK;
        configuration.uiMode |= on ? Configuration.UI_MODE_NIGHT_YES : Configuration.UI_MODE_NIGHT_NO;
        resources.updateConfiguration(configuration, displayMetrics);
        Log.e("fuck", "new  uimode = " + Integer.toHexString(configuration.uiMode));
        recreate();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Log.e("fuck", "!!!!!!!!!!  uimode = " + Integer.toHexString(newConfig.uiMode));
        setContentView(R.layout.activity_night_mode);
        Utils.initToolbar(this, R.id.night_mode_toolbar);
        initViews();
    }
}
