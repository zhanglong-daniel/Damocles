package com.damocles;

import com.baidu.naviauto.R;
import com.damocles.android.util.DeviceInfoUtils;
import com.damocles.sample.AnimationActivity;
import com.damocles.sample.BlueToothActivity;
import com.damocles.sample.BroadcastActivity;
import com.damocles.sample.CanvasAnimationActivity;
import com.damocles.sample.CascadeLayoutActivity;
import com.damocles.sample.LEDActivity;
import com.damocles.sample.ListViewChoiceModeActivity;
import com.damocles.sample.ListViewSectionActivity;
import com.damocles.sample.MultiIntentActivity;
import com.damocles.sample.NightModeActivity;
import com.damocles.sample.PreferenceActivitySample;
import com.damocles.sample.RecyclerViewActivity;
import com.damocles.sample.SwitcherActivity;
import com.damocles.sample.ToastActivity;
import com.damocles.sample.ViewGroupAnimationActivity;
import com.damocles.sample.util.Utils;

import android.content.ComponentName;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "damocles";

    private Toolbar mToolbar;
    private LinearLayout mLinearLayout;
    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "onCreate()");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initToolbar();
        initViews();
    }

    @Override
    protected void onStart() {
        Log.i(TAG, "onStart()");
        super.onStart();
    }

    @Override
    protected void onRestart() {
        Log.i(TAG, "onRestart()");
        super.onRestart();
    }

    @Override
    protected void onResume() {
        Log.i(TAG, "onResume()");
        super.onResume();
    }

    @Override
    protected void onPause() {
        Log.i(TAG, "onPause()");
        super.onPause();
    }

    @Override
    protected void onStop() {
        Log.i(TAG, "onStop()");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Log.i(TAG, "onDestroy()");
        super.onDestroy();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        Log.i(TAG, "onWindowFocusChanged()");
        super.onWindowFocusChanged(hasFocus);
        Log.e(TAG, "statusBarHeight = " + DeviceInfoUtils.getStatusBarHeightOnWindowFocusChanged(this));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        Log.e("fuck", DeviceInfoUtils.getScreenWidth() + " ; " + DeviceInfoUtils.getScreenHeight());
        super.onConfigurationChanged(newConfig);
    }

    private void initToolbar() {
        mToolbar = Utils.initToolbar(this, R.id.main_toolbar);
        mToolbar.setLogo(R.mipmap.ic_launcher);
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Toast.makeText(MainActivity.this, item.getTitle(), Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }

    private void initViews() {
        mTextView = (TextView) findViewById(R.id.main_txt);
        mLinearLayout = (LinearLayout) findViewById(R.id.main_linearlayout);
    }

    public void onWechatClick(View view) {
        //        startActivity(new Intent(this, WechatActivity.class));
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.setComponent(new ComponentName("com.baidu.wechathelper", "com.baidu.wechathelper.MainActivity"));
        startActivity(intent);
    }

    public void onNightModeClick(View view) {
        startActivity(new Intent(this, NightModeActivity.class));
    }

    public void onRecyclerViewClick(View view) {
        startActivity(new Intent(this, RecyclerViewActivity.class));
    }

    public void onAnimationClick(View view) {
        startActivity(new Intent(this, AnimationActivity.class));
    }

    public void onBluetoothClick(View view) {
        startActivity(new Intent(this, BlueToothActivity.class));
    }

    public void onMultiIntentClick(View view) {
        startActivity(new Intent(this, MultiIntentActivity.class));
    }

    public void onListViewChoiceModeClick(View view) {
        startActivity(new Intent(this, ListViewChoiceModeActivity.class));
    }

    public void onListViewSectionClick(View view) {
        startActivity(new Intent(this, ListViewSectionActivity.class));
    }

    public void onBroadcastClick(View view) {
        startActivity(new Intent(this, BroadcastActivity.class));
    }

    public void onToastClick(View view) {
        startActivity(new Intent(this, ToastActivity.class));
    }

    public void onLEDClick(View view) {
        startActivity(new Intent(this, LEDActivity.class));
    }

    public void onCanvasAnimationClick(View view) {
        startActivity(new Intent(this, CanvasAnimationActivity.class));
    }

    public void onViewGroupAnimationClick(View view) {
        startActivity(new Intent(this, ViewGroupAnimationActivity.class));
    }

    public void onSwitcherClick(View view) {
        startActivity(new Intent(this, SwitcherActivity.class));
    }

    public void onCascadeClick(View view) {
        startActivity(new Intent(this, CascadeLayoutActivity.class));
    }

    public void onPreferenceClick(View view) {
        startActivity(new Intent(this, PreferenceActivitySample.class));
    }

}
