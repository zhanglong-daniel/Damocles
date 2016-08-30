package com.damocles.sample;

import java.util.Random;

import com.damocles.R;
import com.damocles.sample.util.Utils;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher;

public class SwitcherActivity extends AppCompatActivity {

    private TextSwitcher mTextSwitcher;
    private ImageSwitcher mImageSwitcher;
    private Animation mInAnimation;
    private Animation mOutAnimation;
    private Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_switcher);
        Utils.initToolbar(this, R.id.switcher_toolbar);
        initViews();
        mHandler.post(updateRunnable);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mHandler.removeCallbacks(updateRunnable);
    }

    private void initViews() {
        mTextSwitcher = (TextSwitcher) findViewById(R.id.switcher_text);
        mImageSwitcher = (ImageSwitcher) findViewById(R.id.switcher_image);
        mInAnimation = AnimationUtils.loadAnimation(this, android.R.anim.slide_in_left);
        mOutAnimation = AnimationUtils.loadAnimation(this, android.R.anim.slide_out_right);
        mTextSwitcher.setInAnimation(mInAnimation);
        mTextSwitcher.setOutAnimation(mOutAnimation);
        mImageSwitcher.setInAnimation(mInAnimation);
        mImageSwitcher.setOutAnimation(mOutAnimation);
        mTextSwitcher.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                TextView textView = new TextView(SwitcherActivity.this);
                textView.setText(String.valueOf(new Random().nextInt(100000)));
                return textView;
            }
        });
        mImageSwitcher.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                ImageView imageView = new ImageView(SwitcherActivity.this);
                imageView.setImageResource(R.mipmap.ic_launcher);
                return imageView;
            }
        });
    }

    private Runnable updateRunnable = new Runnable() {
        @Override
        public void run() {
            updateSwitcher();
            mHandler.postDelayed(this, 2000);
        }
    };

    private void updateSwitcher() {
        mTextSwitcher.setText(String.valueOf(new Random().nextInt(100000)));
        mImageSwitcher
                .setImageResource(new Random().nextInt(2) == 0 ? R.mipmap.ic_android : R.mipmap.ic_launcher);
    }
}
