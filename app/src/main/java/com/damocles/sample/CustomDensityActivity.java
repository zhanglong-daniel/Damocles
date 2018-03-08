package com.damocles.sample;

import com.damocles.R;
import com.damocles.sample.util.Utils;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;

public class CustomDensityActivity extends AppCompatActivity {

    private Button mButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        modifyDensity(240);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_density);
        Utils.initToolbar(this, R.id.custom_density_toolbar);
        initViews();
    }

    private void initViews() {
        mButton = (Button) findViewById(R.id.custom_density_btn);
        mButton.setText("density=" + getDensity());
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mButton.setText("density=" + getDensity());
            }
        });
    }

    private int getDensity() {
        return getResources().getDisplayMetrics().densityDpi;
    }

    private void modifyDensity(int density) {
        Configuration configuration = getResources().getConfiguration();
        configuration.densityDpi = density;
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        displayMetrics.densityDpi = density;
        displayMetrics.density = density / 160.0f;
        getResources().updateConfiguration(configuration, displayMetrics);
    }
}
