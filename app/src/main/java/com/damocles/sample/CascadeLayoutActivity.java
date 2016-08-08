package com.damocles.sample;

import com.baidu.naviauto.R;
import com.damocles.sample.util.Utils;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

public class CascadeLayoutActivity extends AppCompatActivity {

    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cascade);
        initViews();
    }

    private void initViews() {
        mToolbar = Utils.initToolbar(this, R.id.cascade_toolbar);
    }
}
