package com.damocles.sample;

import com.baidu.navisdk.adapter.BNRoutePlanNode;
import com.damocles.common.log.Log;
import com.damocles.navi.callback.NaviCallback;
import com.damocles.navi.NaviSdk;
import com.damocles.tts.TTSPlayer;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by zhanglong02 on 16/8/24.
 */
public class NaviActivity extends Activity {

    private NaviSdk mNaviSdk;

    private BNRoutePlanNode mBNRoutePlanNode = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mNaviSdk = NaviSdk.getInstance();
        mNaviSdk.initNaviCommonModule(this, new NaviCallback() {
            @Override
            public void onNaviGuideEnd() {
                Log.e("导航结束");
                NaviActivity.this.finish();
            }
        });
        mNaviSdk.onCreate();
        View view = mNaviSdk.getView();
        if (view != null) {
            setContentView(view);
        } else {
            Log.e("view is null");
        }
        Intent intent = getIntent();
        if (intent != null) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                mBNRoutePlanNode = (BNRoutePlanNode) bundle.getSerializable("routePlanNode");
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mNaviSdk.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mNaviSdk.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();
        mNaviSdk.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mNaviSdk.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mNaviSdk.onDestroy();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mNaviSdk.onConfigurationChanged(newConfig);
    }

    @Override
    public void onBackPressed() {
        mNaviSdk.onBackPressed(false);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return super.onKeyDown(keyCode, event);
    }
}
