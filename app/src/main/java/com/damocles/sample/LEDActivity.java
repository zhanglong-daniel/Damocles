package com.damocles.sample;

import java.util.Date;

import com.damocles.R;
import com.damocles.sample.util.Utils;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class LEDActivity extends AppCompatActivity {

    private static final String DATE_FORMAT = "%02d:%02d:%02d";
    private static final int REFRESH_DELAY = 500;

    private TextView mTextView;

    private final Handler mHandler = new Handler();
    private final Runnable mTimeRefresher = new Runnable() {
        @Override
        public void run() {
            final Date d = new Date();
            mTextView.setText(String.format(DATE_FORMAT, d.getHours(),
                    d.getMinutes(), d.getSeconds()));
            mHandler.postDelayed(this, REFRESH_DELAY);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_led);
        Utils.initToolbar(this, R.id.led_toolbar);
        initViews();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mHandler.post(mTimeRefresher);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mHandler.removeCallbacks(mTimeRefresher);
    }

    private void initViews() {
        mTextView = (TextView) findViewById(R.id.led_txt_time);
    }

}
